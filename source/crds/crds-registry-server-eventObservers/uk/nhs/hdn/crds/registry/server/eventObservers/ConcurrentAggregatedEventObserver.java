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

package uk.nhs.hdn.crds.registry.server.eventObservers;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CopyOnWriteArraySet;

public final class ConcurrentAggregatedEventObserver<K> extends AbstractEventObserver<K>
{
	@NotNull private final CopyOnWriteArraySet<EventObserver<K>> set;

	public ConcurrentAggregatedEventObserver()
	{
		set = new CopyOnWriteArraySet<>();
	}

	public void add(@NotNull final EventObserver<K> eventObserver)
	{
		set.add(eventObserver);
	}

	@Override
	public void storeChanged(@NotNull final K key)
	{
		for (final EventObserver<K> eventObserver : set)
		{
			eventObserver.storeChanged(key);
		}
	}
}
