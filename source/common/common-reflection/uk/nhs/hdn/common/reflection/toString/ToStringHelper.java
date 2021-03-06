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

package uk.nhs.hdn.common.reflection.toString;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.nhs.hdn.common.exceptions.ShouldNeverHappenException;
import uk.nhs.hdn.common.reflection.ClassInformation;
import uk.nhs.hdn.common.reflection.toString.toStringGenerators.*;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static uk.nhs.hdn.common.reflection.ClassInformation.classInformation;
import static uk.nhs.hdn.common.reflection.FieldInformation.fieldInformation;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.ByteToStringGenerator.ByteToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.CharacterToStringGenerator.CharacterToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.ClassToStringGenerator.ClassToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.GregorianCalendarToStringGenerator.GregorianToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.LockToStringGenerator.LockToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.ObjectArrayToStringGenerator.ObjectArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.ObjectToStringGenerator.ObjectToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveBooleanArrayToStringGenerator.PrimitiveBooleanArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveByteArrayToStringGenerator.PrimitiveByteArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveCharArrayToStringGenerator.PrimitiveCharArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveDoubleArrayToStringGenerator.PrimitiveDoubleArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveFloatArrayToStringGenerator.PrimitiveFloatArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveIntArrayToStringGenerator.PrimitiveIntArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveLongArrayToStringGenerator.PrimitiveLongArrayToStringGeneratorInstance;
import static uk.nhs.hdn.common.reflection.toString.toStringGenerators.PrimitiveShortArrayToStringGenerator.PrimitiveShortArrayToStringGeneratorInstance;

@SuppressWarnings("ClassNamePrefixedWithPackageName")
public final class ToStringHelper
{
	private static final Map<Class<?>, ToStringGenerator<?>> ToStringGeneratorsRegister = register
	(
		ByteToStringGeneratorInstance,
		CharacterToStringGeneratorInstance,
		GregorianToStringGeneratorInstance,
		LockToStringGeneratorInstance,
		ClassToStringGeneratorInstance,
		PrimitiveBooleanArrayToStringGeneratorInstance,
		PrimitiveByteArrayToStringGeneratorInstance,
		PrimitiveShortArrayToStringGeneratorInstance,
		PrimitiveCharArrayToStringGeneratorInstance,
		PrimitiveIntArrayToStringGeneratorInstance,
		PrimitiveLongArrayToStringGeneratorInstance,
		PrimitiveFloatArrayToStringGeneratorInstance,
		PrimitiveDoubleArrayToStringGeneratorInstance
	);

	private static Map<Class<?>, ToStringGenerator<?>> register(@NotNull final ToStringGenerator<?>... toStringGenerators)
	{
		final Map<Class<?>, ToStringGenerator<?>> register = new HashMap<>(toStringGenerators.length * 2);
		for (final ToStringGenerator<?> toStringGenerator : toStringGenerators)
		{
			toStringGenerator.register(register);
		}
		return register;
	}
	private ToStringHelper()
	{}

	@SuppressWarnings("FeatureEnvy")
	@NotNull
	public static String toString(@NotNull final Object instanceToString)
	{
		@NotNull final Class<?> aClass = instanceToString.getClass();

		final StringWriter stringWriter = new StringWriter();

		final ClassInformation classInformation = classInformation(aClass);
		stringWriter.write(classInformation.simpleName());
		stringWriter.write("(");

		boolean afterFirstField = false;
		for (final Field field : classInformation.allPrivateOrProtectedOrPublicFinalFields())
		{
			if (fieldInformation(field).hasAnnotation(ExcludeFromToString.class))
			{
				continue;
			}
			try
			{
				if (afterFirstField)
				{
					stringWriter.write(", ");
				}
				final Object value = field.get(instanceToString);
				writeValue(stringWriter, value);
			}
			catch (IllegalAccessException e)
			{
				throw new ShouldNeverHappenException(e);
			}
			afterFirstField = true;
		}

		stringWriter.write(")");
		return stringWriter.toString();
	}

	public static void writeValue(@NotNull final StringWriter stringWriter, @Nullable final Object value)
	{
		final String response;
		if (value == null)
		{
			response = "null";
		}
		else
		{
			final Class<?> aClass = value.getClass();
			@Nullable final ToStringGenerator<?> nullableToStringGenerator = ToStringGeneratorsRegister.get(aClass);
			@SuppressWarnings("rawtypes") @NotNull final ToStringGenerator notNullToStringGenerator;
			if (nullableToStringGenerator == null)
			{
				notNullToStringGenerator = aClass.isArray() ? ObjectArrayToStringGeneratorInstance : ObjectToStringGeneratorInstance;
			}
			else
			{
				notNullToStringGenerator = nullableToStringGenerator;
			}
			response = notNullToString(value, notNullToStringGenerator);
		}
		stringWriter.write(response);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static String notNullToString(final Object value, final ToStringGenerator notNullToStringGenerator)
	{
		return notNullToStringGenerator.toString(value);
	}

}
