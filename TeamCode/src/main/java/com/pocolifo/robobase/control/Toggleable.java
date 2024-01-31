package com.pocolifo.robobase.control;


public class Toggleable {
    private final BoolSupplier currentState;
    private boolean lastState;
    private boolean state;
    private boolean toggledLastUpdate;

    /**
     * Creates a {@link Toggleable}.
     *
     * @param currentState A method to get the current state of something (like whether a gamepad button is pressed)
     */
    public Toggleable(BoolSupplier currentState) {
        this.currentState = currentState;
    }

    /**
     * Creates a {@link Toggleable}.
     *
     * @param currentState A method to get the current state of something (like whether a gamepad button is pressed)
     */
    public Toggleable(BoolSupplier currentState, boolean initialState) {
        this(currentState);
        this.state = initialState;
    }

    /**
     * Get the current toggled state.
     *
     * @return The toggled state of this {@link Toggleable}
     */
    public boolean get() {
        return this.state;
    }

    /**
     * Updates this Toggleable. <strong>Should be called ONCE as frequent as possible or in the main loop!</strong>
     *
     * @return This {@link Toggleable} instance.
     */
    public Toggleable processUpdates() {
        boolean current = this.currentState.get();
        this.toggledLastUpdate = current && !this.lastState;

        if (this.toggledLastUpdate) {
            this.state = !this.state;
        }

        this.lastState = current;

        return this;
    }

    /**
     * Run code when this {@link Toggleable} is toggled ON.
     *
     * @param runnable A {@link Runnable} that is executed when this toggleable is toggled ON.
     * @return This {@link Toggleable} instance.
     */
    public Toggleable onToggleOn(Runnable runnable) {
        if (this.toggledLastUpdate && this.state) {
            runnable.run();
        }

        return this;
    }

    /**
     * Run code when this {@link Toggleable} is toggled OFF.
     *
     * @param runnable A {@link Runnable} that is executed when this toggleable is toggled OFF.
     * @return This {@link Toggleable} instance.
     */
    public Toggleable onToggleOff(Runnable runnable) {
        if (this.toggledLastUpdate && !this.state) {
            runnable.run();
        }

        return this;
    }
}
