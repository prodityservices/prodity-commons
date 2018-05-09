package io.prodity.commons.spigot.inject;


/**
 * Common time units expressed in terms of ticks. This represents the units
 * that the Bukkit Scheduler can operate on.
 */
public enum TimeUnit {
    DAYS(1_728_000L),
    HOURS(72_000L),
    MINUTES(1_200L),
    SECONDS(20L),
    TICKS(1L),;
    private final long ticks;

    TimeUnit(long ticks) {
        this.ticks = ticks;
    }

    public long toTicks(long amount) {
        return this.ticks * amount;
    }
}
