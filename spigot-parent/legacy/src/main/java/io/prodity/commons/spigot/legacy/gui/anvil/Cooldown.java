
package io.prodity.commons.spigot.legacy.gui.anvil;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * A simple cooldown class.
 * <p>
 * Created on Nov 3, 2016.
 *
 * @author FakeNeth
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.NONE)
public class Cooldown {

    /**
     * The last time this {@link Cooldown} was caled at.
     */
    @Setter(AccessLevel.NONE)
    private long lastCalled;

    /**
     * The time of the cooldown, in milliseconds.
     */
    private long cooldownMillis;

    public Cooldown(final Number ticks) {
        this(TimeUnit.MILLISECONDS, ticks.longValue() * 50);
    }

    /**
     * @param unit The {@link TimeUnit} of the specified time amount.
     * @param timeAmount The amount of time the {@link Cooldown} is set to.
     */
    public Cooldown(@NonNull final TimeUnit unit, @NonNull final Number timeAmount) {
        this.cooldownMillis = unit.toMillis(timeAmount.longValue());
        this.lastCalled = System.currentTimeMillis() - this.cooldownMillis;
    }

    /**
     * Tests this {@link Cooldown} and resets it if the test passes.
     *
     * @return False if the {@link Cooldown} is still active, true if it passed and the {@link Cooldown} was reset.
     */
    public boolean test() {
        if (!testNoReset()) {
            return false;
        }
        reset();
        return true;
    }

    /**
     * Tests this {@link Cooldown} without resetting it.
     *
     * @return False if the {@link Cooldown} is still active, true if it isnt.
     */
    public boolean testNoReset() {
        return System.currentTimeMillis() - this.lastCalled > this.cooldownMillis;
    }

    /**
     * Resets the {@link Cooldown} so that the last time it was called is the current time.
     */
    public void reset() {
        this.lastCalled = System.currentTimeMillis();
    }

    /**
     * Set sthe time until the {@link Cooldown} expires.
     *
     * @param unit The {@link TimeUnit} of the specified time.
     * @param time The time amount to be set.
     */
    public void setTimeLeft(@NonNull final TimeUnit unit, final int time) {
        this.lastCalled = (System.currentTimeMillis() - this.cooldownMillis) + unit.toMillis(time);
    }

    /**
     * Adds time until the {@link Cooldown} expires.
     *
     * @param unit The {@link TimeUnit} of the specified time.
     * @param time The time amount to be added.
     */
    public void addTimeLeft(@NonNull final TimeUnit unit, final int time) {
        if (testNoReset()) {
            setTimeLeft(unit, time);
        } else {
            this.lastCalled += unit.toMillis(time);
        }
    }

}
