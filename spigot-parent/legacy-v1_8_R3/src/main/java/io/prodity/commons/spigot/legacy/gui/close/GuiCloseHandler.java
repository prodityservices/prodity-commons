package io.prodity.commons.spigot.legacy.gui.close;

public interface GuiCloseHandler {

    static GuiCloseHandler of(Runnable runnable, Iterable<GuiCloseReason> reasons) {
        return new SimpleGuiCloseHandler(runnable, reasons);
    }

    static GuiCloseHandler of(Runnable runnable, GuiCloseReason... reasons) {
        return new SimpleGuiCloseHandler(runnable, reasons);
    }

    static GuiCloseHandler of(Runnable runnable) {
        return new SimpleGuiCloseHandler(runnable);
    }

    void run();

    boolean shouldHandle(GuiCloseReason reason);

}