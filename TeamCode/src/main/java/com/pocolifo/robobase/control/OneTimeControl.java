package com.pocolifo.robobase.control;


public class OneTimeControl {
    private final BoolSupplier currentState;
    private boolean lastState;
    private boolean state;
    private boolean done;
    private boolean toggledLastUpdate;

    /**
     * Creates a {@link Toggleable}.
     *
     * @param currentState A method to get the current state of something (like whether a gamepad button is pressed)
     */
    public OneTimeControl(BoolSupplier currentState) {
        this.currentState = currentState;
    }

    /**
     * Get the current toggled state.
     *
     * @return The toggled state of this {@link OneTimeControl}
     */
    public boolean get() {
        return this.state;
    }

    /**
     * Updates this Toggleable. <strong>Should be called ONCE as frequent as possible or in the main loop!</strong>
     *
     * @return This {@link OneTimeControl} instance.
     */
    public OneTimeControl processUpdates() {
        boolean current = this.currentState.get();
        this.toggledLastUpdate = current && !this.lastState;

        if (this.toggledLastUpdate && !this.state) {
            this.state = true;
        }

        this.lastState = current;

        return this;
    }

    /**
     * Run code when this {@link OneTimeControl} is toggled ON.
     *
     * @param runnable A {@link Runnable} that is executed when this toggleable is toggled ON.
     * @return This {@link OneTimeControl} instance.
     */
    public OneTimeControl onToggleOn(Runnable runnable) {
        if (this.toggledLastUpdate && this.state && !this.done) {
            runnable.run();
            this.done = true;
        }

        return this;
    }
}
