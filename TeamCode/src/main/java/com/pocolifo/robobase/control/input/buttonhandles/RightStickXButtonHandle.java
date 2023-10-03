package com.pocolifo.robobase.control.input.buttonhandles;

import com.qualcomm.robotcore.hardware.Gamepad;

public class RightStickXButtonHandle extends ButtonHandle {
    private Gamepad g;
    public RightStickXButtonHandle(Gamepad g) {
        this.g = g;
    }
    public float get() {
        return g.right_stick_x;
    }
}