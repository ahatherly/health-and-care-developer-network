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
import org.jetbrains.annotations.Nullable;
import uk.nhs.hcdn.common.reflection.toString.AbstractToString;

public final class TextValueCollector extends AbstractToString
{
	@Nullable
	private String value;

	public TextValueCollector()
	{
		value = null;
	}

	public void text(@NotNull final String text) throws XmlSchemaViolationException
	{
		if (value != null)
		{
			throw new XmlSchemaViolationException("only one text node is permitted");
		}
		value = text;
	}

	// null if text node not present
	@Nullable
	public String finish() throws XmlSchemaViolationException
	{
		return value;
	}
}