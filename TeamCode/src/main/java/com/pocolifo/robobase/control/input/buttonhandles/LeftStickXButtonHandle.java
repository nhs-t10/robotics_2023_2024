package com.pocolifo.robobase.control.input.buttonhandles;

import com.qualcomm.robotcore.hardware.Gamepad;

public class LeftStickXButtonHandle extends ButtonHandle {
    private Gamepad g;
    public LeftStickXButtonHandle(Gamepad g) {
        this.g = g;
    }
    public float get() {
        return g.left_stick_x;
    }
}