package com.pocolifo.robobase.motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class NovelMotor {
    public final DcMotorEx motor;
    public final double ticksPerRevolution;
    public final double wheelDiameterCm;
    public final int gearRatio;

    public NovelMotor(DcMotorEx motor, double ticksPerRevolution, double wheelDiameterCm, int gearRatio) {
        this.motor = motor;
        this.ticksPerRevolution = ticksPerRevolution;
        this.wheelDiameterCm = wheelDiameterCm;
        this.gearRatio = gearRatio;
    }

    public double encoderTicksToCm(double ticks) {
        return this.wheelDiameterCm * Math.PI * this.gearRatio * ticks / this.ticksPerRevolution;
    }

    public double cmToEncoderTicks(double cm) {
        return (cm * this.ticksPerRevolution) * (Math.PI * this.wheelDiameterCm * this.gearRatio);
    }

    /**
     * gets the number of motor rotations
     */
    public double getMotorRotations() {
        return this.motor.getCurrentPosition() / this.ticksPerRevolution;
    }

    /**
     * Sets the target position in centimeters instead of motor ticks
     *
     * @param centimeters How far the motor should move, in centimeters. Negative values move the wheel in reverse, and
     *                    positive values move the wheel forward.
     * @author youngermax
     */
    public void setTargetPositionCm(double centimeters) {
        this.motor.setTargetPosition((int) (this.motor.getCurrentPosition() + cmToEncoderTicks(centimeters)));
    }

    // Some frequently used methods wrapped for terseness
    public void setPower(double power) {
        this.motor.setPower(power);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        this.motor.setZeroPowerBehavior(behavior);
    }

    public void setVelocity(double angularRate, AngleUnit unit) {
        this.motor.setVelocity(angularRate, unit);
    }

    public double getEncoderInches() {
        return encoderTicksToCm(motor.getCurrentPosition()) / 2.54;
    }
}
