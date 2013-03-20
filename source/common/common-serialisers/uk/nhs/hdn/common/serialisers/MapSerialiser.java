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

package uk.nhs.hdn.common.serialisers;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MapSerialiser
{
	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final String value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final MapSerialisable value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final ValueSerialisable value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @Nullable final Object value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final int value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final long value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final boolean value) throws CouldNotWritePropertyException;

	void writePropertyNull(@FieldTokenName @NonNls @NotNull final String name) throws CouldNotWritePropertyException;

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	<S extends MapSerialisable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values) throws CouldNotWritePropertyException;

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	<S extends ValueSerialisable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final List<?> values) throws CouldNotWritePropertyException;
}