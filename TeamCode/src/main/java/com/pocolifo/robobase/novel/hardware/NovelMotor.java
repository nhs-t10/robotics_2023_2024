package com.pocolifo.robobase.novel.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class NovelMotor {
    public final DcMotorEx motor;
    public final double ticksPerRevolution;
    public final double wheelDiameterInches;
    public final int gearRatio;

    public NovelMotor(DcMotorEx motor, double ticksPerRevolution, double wheelDiameterInches, int gearRatio) {
        this.motor = motor;
        this.ticksPerRevolution = ticksPerRevolution;
        this.wheelDiameterInches = wheelDiameterInches;
        this.gearRatio = gearRatio;
    }

    public double encoderTicksToInches(double ticks) {
        return this.wheelDiameterInches * Math.PI * this.gearRatio * ticks / this.ticksPerRevolution;
    }

    public double inchesToEncoderTicks(double inches) {
        return (inches / this.wheelDiameterInches) * Math.PI * this.ticksPerRevolution;
        //return (inches * this.ticksPerRevolution) * (Math.PI * this.wheelDiameterInches * this.gearRatio);
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
    public void setTargetPositionInches(double centimeters) {
        this.motor.setTargetPosition((int) (this.motor.getCurrentPosition() + inchesToEncoderTicks(centimeters)));
    }

    // Some frequently used methods wrapped for terseness
    public void setPower(double power) {
        this.motor.setPower(power);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        this.motor.setZeroPowerBehavior(behavior);
    }

    public static double inchesPerSecondToEncoderTicksPerSecond(double diameterIn, double ticksPerRevolution, double inchesPerSecond) {
        return ((inchesPerSecond / Math.PI) * ticksPerRevolution) / diameterIn;
    }

    public static double encoderTicksPerSecondToInchesPerSecond(double diameterIn, double ticksPerRevolution, double encoderTicksPerSecond) {
        return (diameterIn * encoderTicksPerSecond * Math.PI) / ticksPerRevolution;
    }

    public void setVelocity(double inchesPerSecond) {
        this.motor.setVelocity(
                inchesPerSecondToEncoderTicksPerSecond(
                        this.wheelDiameterInches,
                        this.ticksPerRevolution,
                        inchesPerSecond
                )
        );
    }

    public double getEncoderInches() {
        return encoderTicksToInches(motor.getCurrentPosition());
    }

    /**
     * @return The linear velocity in inches/second.
     */
    public double getVelocity() {
        return encoderTicksPerSecondToInchesPerSecond(this.wheelDiameterInches, this.ticksPerRevolution, this.motor.getVelocity());
    }
}
