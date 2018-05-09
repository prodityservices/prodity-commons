package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.DoNotRegister;

public class DefaultFilter implements ListenerFilter
{
	public boolean shouldRegister(Object object)
	{
		return object.getClass().getAnnotation(DoNotRegister.class) == null;
	}
}
