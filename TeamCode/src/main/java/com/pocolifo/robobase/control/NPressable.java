package com.pocolifo.robobase.control;

public class NPressable implements InputType<Boolean> {
    private boolean lastState;
    private Runnable onPress;

    @Override
    public Boolean get(Boolean current) {
        boolean state = !this.lastState && current;
        this.lastState = current;

        if (state && this.onPress != null) {
            this.onPress.run();
        }

        return state;
    }

    public NPressable withInitialState(boolean state) {
        this.lastState = state;
        return this;
    }

    public NPressable onPress(Runnable runnable) {
        this.onPress = runnable;
        return this;
    }
}
