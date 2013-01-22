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

package uk.nhs.hcdn.dts.domain.schema;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.nhs.hcdn.common.parsers.xml.xmlParseEventHandlers.xmlCollectors.TextXmlConstructor;
import uk.nhs.hcdn.common.parsers.xml.xmlParseEventHandlers.xmlCollectors.XmlSchemaViolationException;
import uk.nhs.hcdn.dts.domain.Version;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static uk.nhs.hcdn.dts.domain.Version.version;

public final class VersionTextXmlConstructor extends TextXmlConstructor<Version>
{
	@NotNull
	@Override
	protected Version convert(@NotNull final String text) throws XmlSchemaViolationException
	{
		@Nullable final Version result = version(text);
		if (result == null)
		{
			throw new XmlSchemaViolationException(format(ENGLISH, "text %1$s is not a valid Version", text));
		}
		return result;
	}

	@NotNull
	@Override
	public Class<Version> type()
	{
		return Version.class;
	}
}
