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

package uk.nhs.hdn.common.parsers.json.parseModes;

import org.jetbrains.annotations.NotNull;
import uk.nhs.hdn.common.parsers.json.InvalidJsonException;
import uk.nhs.hdn.common.parsers.charaterSets.CharacterSet;
import uk.nhs.hdn.common.parsers.json.jsonParseEventHandlers.JsonParseEventHandler;
import uk.nhs.hdn.common.parsers.json.jsonParseEventHandlers.schemaViolationInvalidJsonExceptions.SchemaViolationInvalidJsonException;
import uk.nhs.hdn.common.parsers.convenientReaders.EndOfFileException;
import uk.nhs.hdn.common.parsers.convenientReaders.PeekingConvenientReader;

import java.math.BigDecimal;

import static java.lang.Long.signum;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static uk.nhs.hdn.common.parsers.json.CharacterHelper.*;

public final class NumberParseMode extends AbstractParseMode
{
	@NotNull
	public static final ParseMode RootNumberParseModeInstance = new NumberParseMode(true, Whitespace);

	@NotNull
	public static final ParseMode ObjectNumberParseModeInstance = new NumberParseMode(false, WhitespaceCommaCloseObject);

	@NotNull
	public static final ParseMode ArrayNumberParseModeInstance = new NumberParseMode(false, WhitespaceCommaCloseArray);

	private static final long ScaleByTen = 10L;

	private NumberParseMode(final boolean isRootValue, @NotNull final CharacterSet validTerminatingCharacters)
	{
		super(isRootValue, validTerminatingCharacters);
	}

	@Override
	public void parse(@NotNull final JsonParseEventHandler jsonParseEventHandler, @NotNull final PeekingConvenientReader peekingConvenientReader) throws InvalidJsonException
	{
		final char character = readRequiredCharacter(peekingConvenientReader);
		long integerComponent;
		if (is(character, Minus))
		{
			integerComponent = -decodeIntegerCharacter(readRequiredCharacter(peekingConvenientReader));
		}
		else
		{
			integerComponent = decodeIntegerCharacter(character);
		}

		if (integerComponent == 0L)
		{
			final char nextCharacter;
			try
			{
				nextCharacter = peekingConvenientReader.readCharacter();
			}
			catch (EndOfFileException e)
			{
				if (isNotRootValue)
				{
					throw new InvalidJsonException(e);
				}
				jsonParseEventHandler.numberValue(0L);
				return;
			}

			if (isTerminatingCharacter(nextCharacter))
			{
				peekingConvenientReader.pushBackLastCharacter();
				jsonParseEventHandler.numberValue(0L);
				return;
			}

			switch(nextCharacter)
			{
				case DecimalPoint:
					parseFractionComponent(jsonParseEventHandler, peekingConvenientReader, 0L);
					return;

				case UpperCaseExponent:
				case LowerCaseExponent:
					parseExponentComponent(jsonParseEventHandler, peekingConvenientReader, new StringBuilder("0"));
					return;

				default:
					throw new InvalidJsonException("integer number 0 not followed by valid character");
			}
		}

		do
		{
			final char nextCharacter;
			try
			{
				nextCharacter = peekingConvenientReader.readCharacter();
			}
			catch (EndOfFileException e)
			{
				if (isNotRootValue)
				{
					throw new InvalidJsonException(e);
				}
				jsonParseEventHandler.numberValue(integerComponent);
				return;
			}

			if (isTerminatingCharacter(nextCharacter))
			{
				peekingConvenientReader.pushBackLastCharacter();
				jsonParseEventHandler.numberValue(integerComponent);
				return;
			}

			switch (nextCharacter)
			{
				case DecimalPoint:
					parseFractionComponent(jsonParseEventHandler, peekingConvenientReader, 0L);
					return;

				case UpperCaseExponent:
				case LowerCaseExponent:
					parseExponentComponent(jsonParseEventHandler, peekingConvenientReader, new StringBuilder(Long.toString(integerComponent)));
					return;

				default:
					integerComponent = scale(integerComponent, decodeIntegerCharacter(nextCharacter));
					break;
			}
		}
		while(true);
	}

	private static long scale(final long integerComponent, final long digit) throws InvalidJsonException
	{
		final long newIntegerComponent = (integerComponent * ScaleByTen) + digit;
		if (signum(integerComponent) != signum(integerComponent))
		{
			throw new InvalidJsonException("integer component of number exceeded range in Java");
		}
		return newIntegerComponent;
	}

	private void parseFractionComponent(final JsonParseEventHandler jsonParseEventHandler, final PeekingConvenientReader peekingConvenientReader, final long integerComponent) throws InvalidJsonException
	{
		do
		{
			final char nextCharacter;
			try
			{
				nextCharacter = peekingConvenientReader.readCharacter();
			}
			catch (EndOfFileException e)
			{
				if (isNotRootValue)
				{
					throw new InvalidJsonException(e);
				}
				jsonParseEventHandler.numberValue(integerComponent);
				return;
			}

			if (isTerminatingCharacter(nextCharacter))
			{
				peekingConvenientReader.pushBackLastCharacter();
				jsonParseEventHandler.numberValue(integerComponent);
				return;
			}

			// Not particularly efficient
			final StringBuilder fraction = new StringBuilder(Long.toString(integerComponent)).append(DecimalPoint);
			switch (nextCharacter)
			{
				case UpperCaseExponent:
				case LowerCaseExponent:
					parseExponentComponent(jsonParseEventHandler, peekingConvenientReader, fraction);
					return;

				default:
					if (Digits.doesNotContain(nextCharacter))
					{
						throw new InvalidJsonException("character in number fraction was not a digit");
					}
					fraction.append(nextCharacter);
					break;
			}
		}
		while(true);
	}

	@SuppressWarnings("FeatureEnvy")
	private void parseExponentComponent(final JsonParseEventHandler jsonParseEventHandler, final PeekingConvenientReader peekingConvenientReader, final StringBuilder fraction) throws InvalidJsonException
	{
		fraction.append(EUpperCaseHexadecimalDigit);

		final char character = readRequiredCharacter(peekingConvenientReader);
		if (is(character, Minus))
		{
			fraction.append(Minus).append(readRequiredCharacter(peekingConvenientReader));
		}
		else if (is(character, Plus))
		{
			fraction.append(readRequiredCharacter(peekingConvenientReader));
		}
		else if (Digits.contains(character))
		{
			fraction.append(character);
		}
		else
		{
			throw new InvalidJsonException("unrecognised first character of exponent");
		}

		do
		{
			final char nextCharacter;
			try
			{
				nextCharacter = peekingConvenientReader.readCharacter();
			}
			catch (EndOfFileException e)
			{
				if (isNotRootValue)
				{
					throw new InvalidJsonException(e);
				}
				numberValue(jsonParseEventHandler, fraction);
				return;
			}
			if (isTerminatingCharacter(nextCharacter))
			{
				numberValue(jsonParseEventHandler, fraction);
				return;
			}
			if (Digits.contains(nextCharacter))
			{
				fraction.append(character);
				continue;
			}
			throw new InvalidJsonException("unexpected character in number exponent");
		}
		while(true);
	}

	private static void numberValue(final JsonParseEventHandler jsonParseEventHandler, final StringBuilder fraction) throws SchemaViolationInvalidJsonException
	{
		jsonParseEventHandler.numberValue(new BigDecimal(fraction.toString()));
	}

	@SuppressWarnings({"MagicNumber", "SwitchStatementWithTooManyBranches", "OverlyComplexMethod", "OverlyLongMethod"})
	private static long decodeIntegerCharacter(final char decimalDigit) throws InvalidJsonException
	{
		switch (decimalDigit)
		{
			case ZeroDigit:
				return 0L;

			case OneDigit:
				return 1L;

			case TwoDigit:
				return 2L;

			case ThreeDigit:
				return 3L;

			case FourDigit:
				return 4L;

			case FiveDigit:
				return 5L;

			case SixDigit:
				return 6L;

			case SevenDigit:
				return 7L;

			case EightDigit:
				return 8L;

			case NineDigit:
				return 9L;

			default:
				throw new InvalidJsonException(format(ENGLISH, "unrecognised decimal escape sequence character '%1$s' in string", (int) decimalDigit));
		}
	}
}
