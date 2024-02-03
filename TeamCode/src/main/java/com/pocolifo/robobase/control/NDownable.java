package com.pocolifo.robobase.control;

public class NDownable implements InputType<Boolean> {
    private Runnable onDown;
    private Runnable onRelease;
    private boolean lastState;

    @Override
    public Boolean get(Boolean current) {
        if (this.onDown != null && current) {
            this.onDown.run();
        } else if (this.onRelease != null && !current && this.lastState) {
            this.onRelease.run();
        }

        this.lastState = current;

        return current;
    }

    public NDownable onDown(Runnable runnable) {
        this.onDown = runnable;
        return this;
    }

    public NDownable onRelease(Runnable runnable) {
        this.onRelease = runnable;
        return this;
    }
}
