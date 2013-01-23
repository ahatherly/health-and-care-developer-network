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

package uk.nhs.hcdn.dts.domain.statusRecords;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum Event
{
	COLLECT,
	TRANSFER,
	SEND,
	RECEIVE,
	;

	@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
	private static final class CompilerWorkaround
	{
		private static final Map<String, Event> Index = new HashMap<>(4);
	}

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	Event()
	{
		CompilerWorkaround.Index.put(name(), this);
	}

	@SuppressWarnings("MethodNamesDifferingOnlyByCase")
	@Nullable
	public static Event event(@NotNull final String text)
	{
		return CompilerWorkaround.Index.get(text);
	}
}
