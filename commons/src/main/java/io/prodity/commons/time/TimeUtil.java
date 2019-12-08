package io.prodity.commons.time;

import com.google.common.base.Preconditions;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public enum TimeUtil {

    ;

    /**
     * Converts the specified {@link TimeUnit} to a {@link ChronoUnit}.
     *
     * @param timeUnit the TimeUnit to convert
     * @return the ChronoUnit equivalent of the specified TimeUnit
     */
    public static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
        Preconditions.checkNotNull(timeUnit, "timeUnit");
        switch (timeUnit) {
            case DAYS:
                return ChronoUnit.DAYS;
            case HOURS:
                return ChronoUnit.HOURS;
            case MINUTES:
                return ChronoUnit.MINUTES;
            case SECONDS:
                return ChronoUnit.SECONDS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case NANOSECONDS:
                return ChronoUnit.NANOS;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            default:
                throw new IllegalArgumentException("unknown TimeUnit=" + timeUnit);
        }
    }

}