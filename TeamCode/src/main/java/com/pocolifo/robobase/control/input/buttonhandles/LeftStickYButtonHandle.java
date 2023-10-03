package com.pocolifo.robobase.control.input.buttonhandles;

import com.qualcomm.robotcore.hardware.Gamepad;

public class LeftStickYButtonHandle extends ButtonHandle {
    private Gamepad g;
    public LeftStickYButtonHandle(Gamepad g) {
        this.g = g;
    }
    public float get() {
        return g.left_stick_y;
    }
}