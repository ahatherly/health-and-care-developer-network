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

package uk.nhs.hdn.dbs.response.parsing.fixedWidthLineUsers;

import org.jetbrains.annotations.NotNull;
import uk.nhs.hdn.common.MillisecondsSince1970;
import uk.nhs.hdn.common.parsers.fixedWidth.CouldNotConvertFieldsException;
import uk.nhs.hdn.common.parsers.fixedWidth.FieldSchema;
import uk.nhs.hdn.common.parsers.fixedWidth.fieldConverters.CouldNotConvertFieldValueException;
import uk.nhs.hdn.common.parsers.parseResultUsers.ParseResultUser;
import uk.nhs.hdn.common.parsers.separatedValueParsers.fieldParsers.CouldNotParseFieldException;
import uk.nhs.hdn.common.parsers.separatedValueParsers.lineParsers.CouldNotParseLineException;
import uk.nhs.hdn.common.parsers.separatedValueParsers.separatedValuesParseEventHandlers.AbstractSingleLineSeparatedValueParseEventHandler;
import uk.nhs.hdn.dbs.response.ResponseBody;

import static uk.nhs.hdn.dbs.response.parsing.parsers.ResponseBodySingleLineFixedWidthParser.ResponseBodyFields;

public final class ResponseBodySingleLineSeparatedValueParseEventHandler extends AbstractSingleLineSeparatedValueParseEventHandler<ResponseBodySingleLineSeparatedValueParseEventHandler.ResponseBodySingleLineSeparatedValueParseEventHandlerState>
{
	@NotNull private final ResponseBodyFixedWidthLineUser responseBodyFixedWidthLineUser;
	private final int zeroBasedLineIndex;
	@NotNull private final ParseResultUser<ResponseBody> parseResultUser;

	public ResponseBodySingleLineSeparatedValueParseEventHandler(@NotNull final ResponseBodyFixedWidthLineUser responseBodyFixedWidthLineUser, final int zeroBasedLineIndex, @NotNull final ParseResultUser<ResponseBody> parseResultUser)
	{
		this.responseBodyFixedWidthLineUser = responseBodyFixedWidthLineUser;
		this.zeroBasedLineIndex = zeroBasedLineIndex;
		this.parseResultUser = parseResultUser;
	}

	@NotNull
	@Override
	public ResponseBodySingleLineSeparatedValueParseEventHandlerState start(@MillisecondsSince1970 final long lastModified)
	{
		return new ResponseBodySingleLineSeparatedValueParseEventHandlerState();
	}

	@Override
	public void field(@NotNull final ResponseBodySingleLineSeparatedValueParseEventHandlerState state, @NotNull final String value) throws CouldNotParseFieldException
	{
		state.field(value);
	}

	@Override
	public void endOfLine(@NotNull final ResponseBodySingleLineSeparatedValueParseEventHandlerState state) throws CouldNotParseLineException
	{
		state.endOfLine();
	}

	public class ResponseBodySingleLineSeparatedValueParseEventHandlerState
	{
		@NotNull private final Object[] collectedFields;
		private int fieldIndex;

		public ResponseBodySingleLineSeparatedValueParseEventHandlerState()
		{
			collectedFields = new Object[ResponseBodyFields.length];
			fieldIndex = 0;
		}

		public void field(@NotNull final String value) throws CouldNotParseFieldException
		{
			final FieldSchema responseBodyField = ResponseBodyFields[fieldIndex];
			try
			{
				collectedFields[fieldIndex] = responseBodyField.convert(value);
			}
			catch (CouldNotConvertFieldValueException e)
			{
				throw new CouldNotParseFieldException(fieldIndex, value, e);
			}

			fieldIndex++;
		}

		public void endOfLine() throws CouldNotParseLineException
		{
			final ResponseBody responseBody;
			try
			{
				responseBody = responseBodyFixedWidthLineUser.use(zeroBasedLineIndex, collectedFields);
			}
			catch (CouldNotConvertFieldsException e)
			{
				throw new CouldNotParseLineException(zeroBasedLineIndex, e);
			}
			parseResultUser.use(responseBody);
		}
	}
}
