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

package uk.nhs.hcdn.common.parsers.json.jsonParseEventHandlers.constructors.arrayConstructors;

import org.jetbrains.annotations.NotNull;
import uk.nhs.hcdn.common.arrayCreators.ArrayCreator;
import uk.nhs.hcdn.common.parsers.json.jsonParseEventHandlers.constructors.objectConstructors.ObjectConstructor;

public class NonNullCollectToArrayObjectsOnlyForElementsArrayConstructor<X> extends CollectToArrayObjectsOnlyForElementsArrayConstructor<X>
{
	@NotNull
	public static  <X> ArrayConstructor<?> nonNullArrayOfObjects(@NotNull final ArrayCreator<X> arrayCreator, @NotNull final ObjectConstructor<?> objectConstructor)
	{
		return new NonNullCollectToArrayObjectsOnlyForElementsArrayConstructor<>(arrayCreator, objectConstructor);
	}

	public NonNullCollectToArrayObjectsOnlyForElementsArrayConstructor(@NotNull final ArrayCreator<X> arrayCreator, @NotNull final ObjectConstructor<?> objectConstructor)
	{
		super(false, arrayCreator, objectConstructor);
	}
}