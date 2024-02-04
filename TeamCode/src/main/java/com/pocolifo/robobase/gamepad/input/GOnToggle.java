package com.pocolifo.robobase.gamepad.input;

import java.util.function.Consumer;

public interface GOnToggle<T> {
    T onToggleOn(Runnable runnable);

    T onToggleOff(Runnable runnable);

    T onToggle(Consumer<Boolean> withNewState);

    T initialToggleState(boolean toggled);

    boolean isToggled();
}
