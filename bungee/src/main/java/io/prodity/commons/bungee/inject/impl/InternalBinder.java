package io.prodity.commons.bungee.inject.impl;

import io.prodity.commons.inject.impl.CoreBinder;
import io.prodity.commons.plugin.ProdityPlugin;

public class InternalBinder extends CoreBinder {

	public InternalBinder(ProdityPlugin plugin) {
		super(plugin);
	}

	@Override
	protected void configure() {
		super.configure();
	}

}
