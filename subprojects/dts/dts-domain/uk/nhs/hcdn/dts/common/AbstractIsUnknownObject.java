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

package uk.nhs.hcdn.dts.common;

import org.jetbrains.annotations.Nullable;
import uk.nhs.hcdn.common.reflection.toString.AbstractToString;
import uk.nhs.hcdn.common.reflection.toString.ExcludeFromToString;

public class AbstractIsUnknownObject extends AbstractToString implements IsUnknownObject
{
	@ExcludeFromToString
	private final boolean isUnknown;

	protected AbstractIsUnknownObject(final boolean isUnknown)
	{
		this.isUnknown = isUnknown;
	}

	@Override
	public final boolean isUnknown()
	{
		return isUnknown;
	}

	@Override
	public final boolean isKnown()
	{
		return !isUnknown;
	}

	@Override
	public boolean equals(@Nullable final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}

		final AbstractIsUnknownObject that = (AbstractIsUnknownObject) obj;

		if (isUnknown != that.isUnknown)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		return isUnknown ? 1 : 0;
	}
}
