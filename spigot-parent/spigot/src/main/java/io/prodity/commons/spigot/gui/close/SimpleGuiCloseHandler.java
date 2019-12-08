package io.prodity.commons.spigot.gui.close;

import com.google.common.collect.ImmutableSet;

public class SimpleGuiCloseHandler implements GuiCloseHandler {

    private final Runnable runnable;
    private final ImmutableSet<GuiCloseReason> reasons;

    public SimpleGuiCloseHandler(Runnable runnable, Iterable<GuiCloseReason> reasons) {
        this.runnable = runnable;
        this.reasons = ImmutableSet.copyOf(reasons);
    }

    public SimpleGuiCloseHandler(Runnable runnable, GuiCloseReason... reasons) {
        this.runnable = runnable;
        this.reasons = ImmutableSet.copyOf(reasons);
    }

    public SimpleGuiCloseHandler(Runnable runnable) {
        this(runnable, GuiCloseReason.values());
    }

    @Override
    public boolean shouldHandle(GuiCloseReason reason) {
        return this.reasons.contains(reason);
    }

    @Override
    public void run() {
        try {
            this.runnable.run();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}