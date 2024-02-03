package com.pocolifo.robobase.control;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadController {
    private final Gamepad gamepad;

    private InputType<Boolean> x, y, a, b, rightBumper, leftBumper, up, down, right, left;
    private InputType<Float> rightTrigger, leftTrigger;

    public GamepadController(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    // X
    public GamepadController x(InputType<Boolean> x) {
        this.x = x;
        return this;
    }

    public boolean x() {
        return this.x.get(this.gamepad.x);
    }

    // Y
    public GamepadController y(InputType<Boolean> y) {
        this.y = y;
        return this;
    }

    public boolean y() {
        return this.y.get(this.gamepad.y);
    }

    // A
    public GamepadController a(InputType<Boolean> a) {
        this.a = a;
        return this;
    }

    public boolean a() {
        return this.a.get(this.gamepad.a);
    }

    // B
    public GamepadController b(InputType<Boolean> b) {
        this.b = b;
        return this;
    }

    public boolean b() {
        return this.b.get(this.gamepad.b);
    }

    // RT
    public GamepadController rightTrigger(InputType<Float> rightTrigger) {
        this.rightTrigger = rightTrigger;
        return this;
    }

    public float rightTrigger() {
        return this.rightTrigger.get(this.gamepad.right_trigger);
    }

    // LT
    public GamepadController leftTrigger(InputType<Float> leftTrigger) {
        this.leftTrigger = leftTrigger;
        return this;
    }

    public float leftTrigger() {
        return this.leftTrigger.get(this.gamepad.left_trigger);
    }

    // RB
    public GamepadController rightBumper(InputType<Boolean> rightBumper) {
        this.rightBumper = rightBumper;
        return this;
    }

    public boolean rightBumper() {
        return this.rightBumper.get(this.gamepad.right_bumper);
    }

    // LB
    public GamepadController leftBumper(InputType<Boolean> leftBumper) {
        this.leftBumper = leftBumper;
        return this;
    }

    public boolean leftBumper() {
        return this.leftBumper.get(this.gamepad.left_bumper);
    }

    // D-pad
    // Up
    public GamepadController up(InputType<Boolean> up) {
        this.up = up;
        return this;
    }

    public boolean up() {
        return this.up.get(this.gamepad.dpad_up);
    }

    // Down
    public GamepadController down(InputType<Boolean> down) {
        this.down = down;
        return this;
    }

    public boolean down() {
        return this.down.get(this.gamepad.dpad_down);
    }

    // Right
    public GamepadController right(InputType<Boolean> right) {
        this.right = right;
        return this;
    }

    public boolean right() {
        return this.right.get(this.gamepad.dpad_right);
    }

    // Left
    public GamepadController left(InputType<Boolean> left) {
        this.left = left;
        return this;
    }

    public boolean left() {
        return this.left.get(this.gamepad.dpad_left);
    }

    public void update() {
        if (this.x != null) this.x();
        if (this.y != null) this.y();
        if (this.a != null) this.a();
        if (this.b != null) this.b();
        if (this.rightTrigger != null) this.rightTrigger();
        if (this.leftTrigger != null) this.leftTrigger();
        if (this.rightBumper != null) this.rightBumper();
        if (this.leftBumper != null) this.leftBumper();
        if (this.up != null) this.up();
        if (this.down != null) this.down();
        if (this.right != null) this.right();
        if (this.left != null) this.left();
    }
}
