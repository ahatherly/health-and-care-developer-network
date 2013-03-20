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

package uk.nhs.hdn.ckan.domain.strings;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import uk.nhs.hdn.common.serialisers.*;

public abstract class AbstractSerialisableString extends AbstractValueSerialisableStringIsUnknown implements Serialisable
{
	protected AbstractSerialisableString(@NonNls @NotNull final String value)
	{
		super(value);
	}

	@Override
	public final void serialise(@NotNull final Serialiser serialiser) throws CouldNotSerialiseException
	{
		try
		{
			serialiseValue(serialiser);
		}
		catch (CouldNotSerialiseValueException e)
		{
			throw new CouldNotSerialiseException(this, e);
		}
	}
}