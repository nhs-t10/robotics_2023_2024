package com.pocolifo.robobase.gamepad.input.types;

import com.pocolifo.robobase.gamepad.GController;
import com.pocolifo.robobase.gamepad.input.GInput;
import com.pocolifo.robobase.gamepad.input.GOnPress;
import com.pocolifo.robobase.gamepad.input.GOnRelease;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GTrigger implements GInput, GOnPress<GTrigger>, GOnRelease<GTrigger> {
    private final Supplier<Float> valueGetter;
    private final GController controller;
    private Runnable onPress, onRelease;
    private Consumer<Float> whileDown;
    private float lastValue;

    public GTrigger(GController controller, Supplier<Float> valueGetter) {
        this.valueGetter = valueGetter;
        this.controller = controller;
    }

    @Override
    public void update() {
        float value = this.valueGetter.get();

        if (this.onPress != null && value != 0 && this.lastValue == 0) {
            this.onPress.run();
        }

        if (this.onRelease != null && value == 0 && this.lastValue != 0) {
            this.onRelease.run();
        }

        if (this.whileDown != null && value != 0) {
            this.whileDown.accept(value);
        }

        this.lastValue = value;
    }

    @Override
    public GTrigger onPress(Runnable runnable) {
        this.onPress = runnable;
        return this;
    }

    @Override
    public GTrigger onRelease(Runnable runnable) {
        this.onRelease = runnable;
        return this;
    }

    public GTrigger whileDown(Consumer<Float> runnable) {
        this.whileDown = runnable;
        return this;
    }

    @Override
    public GController ok() {
        return this.controller;
    }
}
