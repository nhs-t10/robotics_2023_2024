package com.pocolifo.robobase.control;

import org.firstinspires.ftc.teamcode.R;

import java.util.function.Consumer;

public class NToggleable implements InputType<Boolean> {
    private boolean lastState;
    private boolean state;
    private Consumer<Boolean> onToggle;
    private Runnable onToggleOn;
    private Runnable onToggleOff;

    @Override
    public Boolean get(Boolean current) {
        boolean toggledLastUpdate = current && !this.lastState;

        if (toggledLastUpdate) {
            this.state = !this.state;
        }

        this.lastState = current;

        // Process toggles
        if (toggledLastUpdate) {
            if (this.onToggle != null) this.onToggle.accept(this.state);
            if (this.onToggleOn != null && this.state) this.onToggleOn.run();
            if (this.onToggleOff != null && !this.state) this.onToggleOff.run();
        }

        return state;
    }

    public NToggleable withInitialState(boolean initialState) {
        this.state = initialState;
        return this;
    }

    public NToggleable onToggle(Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }

    public NToggleable onToggleOn(Runnable onToggleOn) {
        this.onToggleOn = onToggleOn;
        return this;
    }

    public NToggleable onToggleOff(Runnable onToggleOff) {
        this.onToggleOff = onToggleOff;
        return this;
    }
}
