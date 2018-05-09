package io.prodity.commons.spigot.inject;


/**
 * Common time units expressed in terms of ticks. This represents the units
 * that the Bukkit Scheduler can operate on.
 */
public enum TimeUnit {
    DAYS(1728000),
    HOURS(72000),
    MINUTES(1200),
    SECONDS(20),
    TICKS(1),;
    private final long ticks;

    TimeUnit(long ticks) {
        this.ticks = ticks;
    }

    public long toTicks(long amount) {
        return this.ticks * amount;
    }
}
