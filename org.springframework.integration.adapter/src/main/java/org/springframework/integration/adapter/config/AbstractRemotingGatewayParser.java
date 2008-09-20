/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.adapter.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.ConfigurationException;
import org.springframework.util.StringUtils;

/**
 * Base class for remoting gateway parsers.
 * 
 * @author Mark Fisher
 */
public abstract class AbstractRemotingGatewayParser extends AbstractSimpleBeanDefinitionParser {

	protected abstract Class<?> getBeanClass(Element element);


	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {
		String id = super.resolveId(element, definition, parserContext);
		if (!StringUtils.hasText(id)) {
			id = element.getAttribute("name");
		}
		if (!StringUtils.hasText(id)) {
			id = parserContext.getReaderContext().generateBeanName(definition);
		}
		return id;
	}

	@Override
	protected boolean isEligibleAttribute(String attributeName) {
		return !attributeName.equals("name") && !attributeName.equals("request-channel")
				&& !attributeName.equals("reply-channel") && super.isEligibleAttribute(attributeName);
	}

	@Override
	protected final void postProcess(BeanDefinitionBuilder builder, Element element) {
		String requestChannelRef = element.getAttribute("request-channel");
		if (!StringUtils.hasText(requestChannelRef)) {
			throw new ConfigurationException("a 'request-channel' reference is required");
		}
		builder.addPropertyReference("requestChannel", requestChannelRef);
		String replyChannel = element.getAttribute("reply-channel");
		if (StringUtils.hasText(replyChannel)) {
			builder.addPropertyReference("replyChannel", replyChannel);
		}
		this.doPostProcess(builder, element);
	}

	/**
	 * Subclasses may add to the bean definition by overriding this method.
	 */
	protected void doPostProcess(BeanDefinitionBuilder builder, Element element) {
	}

}
