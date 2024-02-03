package com.pocolifo.robobase.control;

import java.util.function.Consumer;

public class NTrigger implements InputType<Float> {
    private Runnable onRelease;
    private Consumer<Float> duringPress;
    private boolean lastPressed;

    @Override
    public Float get(Float state) {
        if (state != 0 && this.duringPress != null) {
            this.duringPress.accept(state);
            this.lastPressed = true;
        } else if (this.lastPressed && this.onRelease != null) {
            this.onRelease.run();
            this.lastPressed = false;
        }

        return state;
    }

    public NTrigger duringPress(Consumer<Float> duringPress) {
        this.duringPress = duringPress;
        return this;
    }

    public NTrigger onRelease(Runnable runnable) {
        this.onRelease = runnable;
        return this;
    }
}
