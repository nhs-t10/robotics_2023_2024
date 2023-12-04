package com.pocolifo.robobase.movement.mecanum;

import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.motor.Wheel;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumWheels {
    public final Wheel frontRight;
    public final Wheel frontLeft;
    public final Wheel backLeft;
    public final Wheel backRight;

    public MecanumWheels(Wheel frontLeft, Wheel frontRight, Wheel backLeft, Wheel backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    public MecanumWheels(HardwareMap map, String frontLeft, String frontRight, String backLeft, String backRight, double tickCount, double wheelDiameterCm) {
        this(
                new Wheel(map.get(DcMotor.class, frontLeft), tickCount, wheelDiameterCm),
                new Wheel(map.get(DcMotor.class, frontRight), tickCount, wheelDiameterCm),
                new Wheel(map.get(DcMotor.class, backLeft), tickCount, wheelDiameterCm),
                new Wheel(map.get(DcMotor.class, backRight), tickCount, wheelDiameterCm)
        );
    }

    public void setPower(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower) {
        this.frontRight.drive(frontRightPower);
        this.frontLeft.drive(frontLeftPower);
        this.backRight.drive(backRightPower);
        this.backLeft.drive(backLeftPower);
    }

    public void setPower(OmniDriveCoefficients.CoefficientSet coefficientSet) {
        this.setPower(coefficientSet.frontLeft, coefficientSet.frontRight, coefficientSet.backLeft, coefficientSet.backRight);
    }

    public void setMode(DcMotor.RunMode runMode) {
        this.frontLeft.motor.setMode(runMode);
        this.frontRight.motor.setMode(runMode);
        this.backRight.motor.setMode(runMode);
        this.backLeft.motor.setMode(runMode);
    }

    public void stopMoving() {
        this.frontLeft.stopMoving();
        this.frontRight.stopMoving();
        this.backRight.stopMoving();
        this.backLeft.stopMoving();
    }

    public void setDriveTarget(double centimeters, OmniDriveCoefficients.CoefficientSet set) {
        this.frontLeft.setDriveTarget(centimeters * set.frontLeft);
        this.frontRight.setDriveTarget(centimeters * set.frontRight);
        this.backRight.setDriveTarget(centimeters * set.backRight);
        this.backLeft.setDriveTarget(centimeters * set.backLeft);
    }

    public boolean isCloseToTargetPosition(int tickThreshold) {
        // Is each wheel in the target position threshold?
        tickThreshold = Math.abs(tickThreshold);

        boolean fl = Math.abs(this.frontLeft.motor.getTargetPosition()  - this.frontLeft.motor.getCurrentPosition())  < tickThreshold;
        boolean fr = Math.abs(this.frontRight.motor.getTargetPosition() - this.frontRight.motor.getCurrentPosition()) < tickThreshold;
        boolean bl = Math.abs(this.backLeft.motor.getTargetPosition()   - this.backLeft.motor.getCurrentPosition())   < tickThreshold;
        boolean br = Math.abs(this.backRight.motor.getTargetPosition()  - this.backRight.motor.getCurrentPosition())  < tickThreshold;

        return fl && bl && fr && br;
    }
}
