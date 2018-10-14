package io.prodity.commons.bungee.threads;

import io.prodity.commons.bungee.inject.BungeeInjectedPlugin;
import io.prodity.commons.threads.Threads;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import javax.inject.Inject;

public class BungeeThreads implements Threads {

	private final BungeeInjectedPlugin plugin;
	private final TaskScheduler scheduler;

	@Inject
	public BungeeThreads(BungeeInjectedPlugin plugin, TaskScheduler scheduler) {
		this.plugin = plugin;
		this.scheduler = scheduler;
	}

	@Override
	public void main(Runnable runnable) {
		// TODO: Figureo out
		runnable.run();
	}

	@Override
	public void async(Runnable runnable) {
		this.scheduler.runAsync(this.plugin, runnable);
	}

}
