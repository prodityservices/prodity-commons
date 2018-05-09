package io.prodity.commons.spigot.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with Task that are non-static, have 0 parameters, and return void
 * will be scheduled to run according to their {@link #period()}. {@link #async()}
 * can be specified to run the task asynchronously.  There is no mechanism for
 * disabling the task, this is designed for simple activities such as auto-saving.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {
    /**
     * How frequently to run the task, specified in {@link #unit()}.
     * @return task period
     */
    long period();
    TimeUnit unit() default TimeUnit.TICKS;
    boolean async() default false;
    boolean cancelOnError() default true;
}
