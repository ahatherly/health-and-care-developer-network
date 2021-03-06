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

package uk.nhs.hdn.barcodes.gs1.server.parsing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.nhs.hdn.barcodes.gs1.companyPrefixes.Gs1CompanyPrefix;
import uk.nhs.hdn.barcodes.gs1.organisation.AdditionalInformation;
import uk.nhs.hdn.barcodes.gs1.organisation.AdditionalInformationKey;
import uk.nhs.hdn.barcodes.gs1.organisation.Tuple;
import uk.nhs.hdn.common.parsers.separatedValueParsers.lineParsers.CouldNotParseLineException;
import uk.nhs.hdn.common.parsers.separatedValueParsers.lineParsers.LineParser;

import java.util.EnumMap;
import java.util.List;

public final class TupleLineParser implements LineParser<Tuple, List<Tuple>>
{
	private static final int NumberOfInvariantConstructorFields = 3;
	@NotNull
	private static final AdditionalInformationKey[] AdditionalInformationKeys = AdditionalInformationKey.values();
	private static final int MaximumAdditionalInformationKeys = AdditionalInformationKeys.length;
	private static final int MaximumLength = NumberOfInvariantConstructorFields + MaximumAdditionalInformationKeys;

	@NotNull public static final LineParser<Tuple, List<Tuple>> TupleLineParserInstance = new TupleLineParser();

	private TupleLineParser()
	{
	}

	@SuppressWarnings({"ClassExtendsConcreteCollection", "MethodCanBeVariableArityMethod"})
	@Override
	public void parse(final int lineIndex, @NotNull final Object[] parsedFields, @NotNull final List<Tuple> parsedLines) throws CouldNotParseLineException
	{
		final int length = parsedFields.length;
		if (length < NumberOfInvariantConstructorFields)
		{
			throw new CouldNotParseLineException(lineIndex, "Not enough fields");
		}

		if (length > MaximumLength)
		{
			throw new CouldNotParseLineException(lineIndex, "Too many fields");
		}

		final int additionalFieldsLength = length - NumberOfInvariantConstructorFields;
		//noinspection serial
		parsedLines.add(new Tuple
		(
			(Gs1CompanyPrefix) parsedFields[0],
			(String) parsedFields[1],
			(String) parsedFields[2],
			new AdditionalInformation(new EnumMap<AdditionalInformationKey, Object>(AdditionalInformationKey.class)
			{{
				for(int index = 0; index < additionalFieldsLength; index++)
				{
					@Nullable final Object parsedField = parsedFields[index + NumberOfInvariantConstructorFields];
					if (parsedField == null)
					{
						continue;
					}
					final AdditionalInformationKey key = AdditionalInformationKeys[index];
					put(key, parsedField);
				}
			}})
		));
	}
}
