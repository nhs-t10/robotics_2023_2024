package com.pocolifo.robobase.gamepad;

import com.pocolifo.robobase.gamepad.input.types.GButton;
import com.pocolifo.robobase.gamepad.input.types.GTrigger;
import com.qualcomm.robotcore.hardware.Gamepad;

public class GController {
    public final GButton x, y, a, b, rightBumper, leftBumper, dpadUp, dpadDown, dpadRight, dpadLeft, leftJoystick, rightJoystick;
    public final GTrigger rightTrigger, leftTrigger;

    public GController(Gamepad gamepad) {
        this.x = new GButton(this, () -> gamepad.x);
        this.y = new GButton(this, () -> gamepad.y);
        this.a = new GButton(this, () -> gamepad.a);
        this.b = new GButton(this, () -> gamepad.b);

        this.rightBumper = new GButton(this, () -> gamepad.right_bumper);
        this.leftBumper = new GButton(this, () -> gamepad.left_bumper);

        this.dpadUp = new GButton(this, () -> gamepad.dpad_up);
        this.dpadDown = new GButton(this, () -> gamepad.dpad_down);
        this.dpadLeft = new GButton(this, () -> gamepad.dpad_left);
        this.dpadRight = new GButton(this, () -> gamepad.dpad_right);

        this.leftJoystick = new GButton(this, () -> gamepad.left_stick_button);
        this.rightJoystick = new GButton(this, () -> gamepad.right_stick_button);

        this.leftTrigger = new GTrigger(this, () -> gamepad.left_trigger);
        this.rightTrigger = new GTrigger(this, () -> gamepad.right_trigger);
    }

    public void update() {
        this.x.update();
        this.y.update();
        this.a.update();
        this.b.update();

        this.leftBumper.update();
        this.rightBumper.update();

        this.dpadUp.update();
        this.dpadDown.update();
        this.dpadLeft.update();
        this.dpadRight.update();

        this.leftJoystick.update();
        this.rightJoystick.update();

        this.leftTrigger.update();
        this.rightTrigger.update();
    }
}
