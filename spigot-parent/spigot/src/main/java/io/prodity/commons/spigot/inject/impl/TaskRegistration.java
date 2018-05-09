package io.prodity.commons.spigot.inject.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.prodity.commons.inject.InjectUtils;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import io.prodity.commons.spigot.inject.Task;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InstanceLifecycleEvent;
import org.glassfish.hk2.api.InstanceLifecycleEventType;
import org.glassfish.hk2.api.InstanceLifecycleListener;

public class TaskRegistration implements InstanceLifecycleListener, PluginLifecycleListener {

    private final Multimap<ActiveDescriptor<?>, DiscoveredTask> tasks = HashMultimap.create();
    private final SpigotInjectedPlugin plugin;

    @Inject
    public TaskRegistration(SpigotInjectedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Filter getFilter() {
        return InjectUtils.filterByPlugin(plugin);
    }

    @Override
    public void lifecycleEvent(InstanceLifecycleEvent lifecycleEvent) {
        if (lifecycleEvent.getEventType() == InstanceLifecycleEventType.POST_PRODUCTION && !tasks
            .containsKey(lifecycleEvent.getActiveDescriptor())) {
            addAllTasks(lifecycleEvent.getActiveDescriptor(), lifecycleEvent.getLifecycleObject());
        } else if (lifecycleEvent.getEventType() == InstanceLifecycleEventType.PRE_DESTRUCTION) {
            for (DiscoveredTask task : tasks.removeAll(lifecycleEvent.getActiveDescriptor())) {
                if (!task.isCancelled()) {
                    task.cancel();
                }
            }
        }
    }

    private void addAllTasks(ActiveDescriptor<?> descriptor, Object instance) {
        findTasks(instance).forEach(task -> {
            if (plugin.isEnabled()) {
                task.start();
            }
            tasks.put(descriptor, task);
        });
    }

    private List<DiscoveredTask> findTasks(Object object) {
        List<DiscoveredTask> tasks = new ArrayList<>();
        Class<?> clazz = object.getClass();
        do {
            addMethods(clazz, object, tasks);
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        return tasks;
    }

    private void addMethods(Class<?> clazz, Object instance, List<DiscoveredTask> to) {
        for (Method method : clazz.getDeclaredMethods()) {
            Task task = checkValid(method);
            if (task != null) {
                to.add(new DiscoveredTask(instance, method, task));
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
        for (DiscoveredTask task : this.tasks.values()) {
            if (!task.wasScheduled()) {
                task.start();
            }
        }
    }

    @Override
    public void onDisable(ProdityPlugin plugin) {
        tasks.clear();
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
            Validate.isTrue(task == null, "Attempted to start a running task.");
            long periodInTicks = taskInfo.unit().toTicks(taskInfo.period());
            if (taskInfo.async()) {
                task = Bukkit.getScheduler().runTaskTimer(plugin, this, periodInTicks, periodInTicks);
            } else {
                task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, periodInTicks, periodInTicks);
            }
            return task;
        }

        public void cancel() {
            if (task != null) {
                task.cancel();
            }
        }

        public boolean wasScheduled() {
            return task != null;
        }

        public boolean isCancelled() {
            return task != null && !task.isCancelled();
        }

        @Override
        public void run() {
            try {
                method.invoke(instance);
            } catch (Exception e) {
                e.printStackTrace();
                if (taskInfo.cancelOnError()) {
                    this.task.cancel();
                }
            }
        }

        @Override
        public String toString() {
            return "DiscoveredTask{" +
                "instance=" + instance +
                ", method=" + method +
                ", taskInfo=" + taskInfo +
                ", task=" + task +
                '}';
        }
    }
}
