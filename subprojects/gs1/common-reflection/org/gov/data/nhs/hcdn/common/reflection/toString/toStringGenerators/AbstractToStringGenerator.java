package org.gov.data.nhs.hcdn.common.reflection.toString.toStringGenerators;

import org.gov.data.nhs.hcdn.common.reflection.toString.AbstractToString;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static org.gov.data.nhs.hcdn.common.VariableArgumentsHelper.copyOf;

public abstract class AbstractToStringGenerator<T> extends AbstractToString implements ToStringGenerator<T>
{
	@NotNull
	private final Class<? extends T>[] registerFor;

	@SafeVarargs
	protected AbstractToStringGenerator(@NotNull final Class<? extends T>... registerFor)
	{
		this.registerFor = copyOf(registerFor);
	}

	@Override
	public final void register(@NotNull final Map<Class<?>, ToStringGenerator<?>> register)
	{
		for (final Class<? extends T> aClass : registerFor)
		{
			if (register.put(aClass, this) != null)
			{
				throw new IllegalStateException(format(ENGLISH, "Already registered for %1$s", aClass.getSimpleName()));
			}
		}
	}
}
