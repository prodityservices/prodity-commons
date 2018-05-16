package io.prodity.commons.spigot.inject.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import io.prodity.commons.spigot.inject.Task;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InstanceLifecycleEvent;
import org.glassfish.hk2.api.InstanceLifecycleEventType;
import org.glassfish.hk2.api.InstanceLifecycleListener;

public class TaskRegistration implements InstanceLifecycleListener, PluginLifecycleListener {

    private final Multimap<ActiveDescriptor<?>, TaskRegistration.DiscoveredTask> tasks = HashMultimap.create();
    private final SpigotInjectedPlugin plugin;

    @Inject
    public TaskRegistration(SpigotInjectedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Filter getFilter() {
        return InjectUtils.filterByPlugin(this.plugin);
    }

    @Override
    public void lifecycleEvent(InstanceLifecycleEvent lifecycleEvent) {
        if (lifecycleEvent.getEventType() == InstanceLifecycleEventType.POST_PRODUCTION && !this.tasks
            .containsKey(lifecycleEvent.getActiveDescriptor())) {
            this.addAllTasks(lifecycleEvent.getActiveDescriptor(), lifecycleEvent.getLifecycleObject());
        } else if (lifecycleEvent.getEventType() == InstanceLifecycleEventType.PRE_DESTRUCTION) {
            for (TaskRegistration.DiscoveredTask task : this.tasks.removeAll(lifecycleEvent.getActiveDescriptor())) {
                if (!task.isCancelled()) {
                    task.cancel();
                }
            }
        }
    }

    private void addAllTasks(ActiveDescriptor<?> descriptor, Object instance) {
        this.findTasks(instance).forEach(task -> {
            if (this.plugin.isEnabled()) {
                task.start();
            }
            this.tasks.put(descriptor, task);
        });
    }

    private List<TaskRegistration.DiscoveredTask> findTasks(Object object) {
        List<TaskRegistration.DiscoveredTask> tasks = new ArrayList<>();
        Class<?> clazz = object.getClass();
        do {
            this.addMethods(clazz, object, tasks);
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        return tasks;
    }

    private void addMethods(Class<?> clazz, Object instance, List<TaskRegistration.DiscoveredTask> to) {
        for (Method method : clazz.getDeclaredMethods()) {
            Task task = this.checkValid(method);
            if (task != null) {
                to.add(new TaskRegistration.DiscoveredTask(instance, method, task));
            }
        }
    }

    private Task checkValid(Method method) {
        if (method.getParameterCount() == 0
            && !Modifier.isStatic(method.getModifiers())
            && method.getReturnType() == Void.TYPE) {
            return method.getAnnotation(Task.class);
        }
        return null;
    }

    @Override
    public void onEnable(ProdityPlugin plugin) {
        for (TaskRegistration.DiscoveredTask task : this.tasks.values()) {
            if (!task.wasScheduled()) {
                task.start();
            }
        }
    }

    @Override
    public void onDisable(ProdityPlugin plugin) {
        this.tasks.clear();
    }

    private class DiscoveredTask implements Runnable {

        private final Object instance;
        private final Method method;
        private final Task taskInfo;
        private BukkitTask task;

        private DiscoveredTask(Object instance, Method method, Task task) {
            this.instance = instance;
            this.method = method;
            this.taskInfo = task;
        }

        public BukkitTask start() {
            Preconditions.checkState(this.task == null, "Attempted to start a running task.");
            long periodInTicks = this.taskInfo.unit().toTicks(this.taskInfo.period());
            if (this.taskInfo.async()) {
                this.task = Bukkit.getScheduler().runTaskTimer(TaskRegistration.this.plugin, this, periodInTicks, periodInTicks);
            } else {
                this.task = Bukkit.getScheduler()
                    .runTaskTimerAsynchronously(TaskRegistration.this.plugin, this, periodInTicks, periodInTicks);
            }
            return this.task;
        }

        public void cancel() {
            if (this.task != null) {
                this.task.cancel();
            }
        }

        public boolean wasScheduled() {
            return this.task != null;
        }

        public boolean isCancelled() {
            return this.task != null && !this.task.isCancelled();
        }

        @Override
        public void run() {
            try {
                this.method.invoke(this.instance);
            } catch (Exception e) {
                e.printStackTrace();
                if (this.taskInfo.cancelOnError()) {
                    this.task.cancel();
                }
            }
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("instance", this.instance)
                .add("method", this.method)
                .add("taskInfo", this.taskInfo)
                .add("task", this.task)
                .toString();
        }
    }
}
