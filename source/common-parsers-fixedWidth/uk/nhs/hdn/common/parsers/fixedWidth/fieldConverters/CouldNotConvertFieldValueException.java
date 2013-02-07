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

package uk.nhs.hdn.common.parsers.fixedWidth.fieldConverters;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class CouldNotConvertFieldValueException extends Exception
{
	public CouldNotConvertFieldValueException(@NotNull final String fieldValue, @NotNull final Class<?> toType, @NonNls @NotNull final String because)
	{
		super(format(ENGLISH, "Could not convert field value '%1$s' to type %2$s because %3$s", fieldValue, toType.getSimpleName(), because));
	}

	public CouldNotConvertFieldValueException(@NotNull final String fieldValue, @NotNull final Class<?> toType, @NotNull final Exception cause)
	{
		super(format(ENGLISH, "Could not convert field value '%1$s' to type %2$s because of exception %3$s", fieldValue, toType.getSimpleName(), cause.getMessage()), cause);
	}
}
