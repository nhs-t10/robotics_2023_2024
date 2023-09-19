package com.pocolifo.robobase.control;

public class Pressable {
    private final BoolSupplier currentState;
    private boolean lastState;

    /**
     * Creates a {@link Pressable}.
     *
     * @param currentState A method to get the current state of something (like whether a gamepad button is pressed)
     */
    public Pressable(BoolSupplier currentState) {
        this.currentState = currentState;
    }

    /**
     * Get the current toggled state.
     *
     * @return The toggled state of this {@link Toggleable}
     */
    public boolean get() {
        boolean before = !this.lastState && this.currentState.get();

        this.lastState = this.currentState.get();

        return before;
    }
}
