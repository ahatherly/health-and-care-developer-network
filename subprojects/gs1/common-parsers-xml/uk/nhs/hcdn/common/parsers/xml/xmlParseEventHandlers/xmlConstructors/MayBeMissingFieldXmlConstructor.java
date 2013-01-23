/*
 * © Crown Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.nhs.hcdn.common.parsers.xml.xmlParseEventHandlers.xmlConstructors;

import org.jetbrains.annotations.NotNull;

public final class MayBeMissingFieldXmlConstructor<C, V> implements MissingFieldXmlConstructor<C, V>
{
	@NotNull
	private final XmlConstructor<C, V> underlyingXmlConstructor;

	public MayBeMissingFieldXmlConstructor(@NotNull final XmlConstructor<C, V> underlyingXmlConstructor)
	{
		this.underlyingXmlConstructor = underlyingXmlConstructor;
	}

	@Override
	public V missingFieldValue() throws XmlSchemaViolationException
	{
		throw new XmlSchemaViolationException("missing field is required");
	}

	@NotNull
	@Override
	public Class<V> type()
	{
		return underlyingXmlConstructor.type();
	}

	@NotNull
	@Override
	public C start()
	{
		return underlyingXmlConstructor.start();
	}

	@NotNull
	@Override
	public XmlConstructor<?, ?> node(@NotNull final String name) throws XmlSchemaViolationException
	{
		return underlyingXmlConstructor.node(name);
	}

	@Override
	public void attribute(@NotNull final C collector, @NotNull final String key, @NotNull final String value) throws XmlSchemaViolationException
	{
		underlyingXmlConstructor.attribute(collector, key, value);
	}

	@Override
	public void text(@NotNull final C collector, @NotNull final String text) throws XmlSchemaViolationException
	{
		underlyingXmlConstructor.text(collector, text);
	}

	@Override
	public void node(@NotNull final C collector, @NotNull final String name, @NotNull final Object value) throws XmlSchemaViolationException
	{
		underlyingXmlConstructor.node(collector, name, value);
	}

	@NotNull
	@Override
	public V finish(@NotNull final C collector) throws XmlSchemaViolationException
	{
		return underlyingXmlConstructor.finish(collector);
	}
}
