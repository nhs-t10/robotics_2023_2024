package com.pocolifo.robobase.novel.motion;

import com.pocolifo.robobase.novel.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.hardware.NovelMotor;
import com.pocolifo.robobase.novel.motion.profiling.TrapezoidalMotionProfile;
import com.pocolifo.robobase.reconstructor.LocalizationEngine;
import com.pocolifo.robobase.reconstructor.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class NovelMecanumDriver {
    private final IMU imu;
    private final NovelMotor frontLeft;
    private final NovelMotor frontRight;
    private final NovelMotor backLeft;
    private final NovelMotor backRight;
    private final OmniDriveCoefficients omniDriveCoefficients;

    public NovelMecanumDriver(
            NovelMotor frontLeft,
            NovelMotor frontRight,
            NovelMotor backLeft,
            NovelMotor backRight,
            IMU imu,
            OmniDriveCoefficients omniDriveCoefficients
    ) {
        this.imu = imu;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.omniDriveCoefficients = omniDriveCoefficients;
    }

    public void setVelocity(Vector3D velocity) {
        OmniDriveCoefficients.CoefficientSet coefficientSet = this.omniDriveCoefficients.calculateCoefficientsWithPower(
                velocity.getX(),
                velocity.getY(),
                velocity.getZ()
        );

        this.setIndividualVelocity(
                coefficientSet.frontLeft,
                coefficientSet.frontRight,
                coefficientSet.backLeft,
                coefficientSet.backRight
        );
    }

    public void setIndividualVelocity(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft.setVelocity(frontLeft);
        this.frontRight.setVelocity(frontRight);
        this.backLeft.setVelocity(backLeft);
        this.backRight.setVelocity(backRight);
    }

    public void stop() {
        this.setIndividualVelocity(0, 0, 0, 0);
    }

    public void useGamepad(Gamepad gamepad, double microMovementValue) {
        OmniDriveCoefficients.CoefficientSet coefficientSet = this.omniDriveCoefficients.calculateCoefficientsWithPower(
                gamepad.left_stick_y / microMovementValue,
                gamepad.left_stick_x / microMovementValue,
                gamepad.right_stick_x / microMovementValue
        );

        this.frontLeft.setPower(coefficientSet.frontLeft);
        this.frontRight.setPower(coefficientSet.frontRight);
        this.backLeft.setPower(coefficientSet.backLeft);
        this.backRight.setPower(coefficientSet.backRight);
    }

    public double getEncoderInches() {
        return this.frontRight.getEncoderInches();
    }

    public void rotateTo(double targetAngle, double maxError, AngleUnit angleUnit) {
        throw new RuntimeException("not implemented!");
    }

    public void drive(LocalizationEngine engine, double lateralInches, double horizontalInches, double acceleration, double maxVelocity) {
        TrapezoidalMotionProfile lateral = new TrapezoidalMotionProfile(acceleration, 0, lateralInches, maxVelocity);
        TrapezoidalMotionProfile horizontal = new TrapezoidalMotionProfile(acceleration, 0, horizontalInches, maxVelocity);
        Pose initialPose = engine.getPoseEstimate(AngleUnit.RADIANS);

        while (!Thread.currentThread().isInterrupted()) {
            Pose currentPose = engine.getPoseEstimate(AngleUnit.RADIANS);
            double lateralDistance = currentPose.getX() - initialPose.getX();
            double horizontalDistance = currentPose.getY() - initialPose.getY();
            double lateralVelocity = lateral.computeVelocity(lateralDistance);
            double horizontalVelocity = horizontal.computeVelocity(horizontalDistance);

            if (lateralDistance >= lateralInches) {
                lateralVelocity = 0;
            }

            if (horizontalDistance >= horizontalInches) {
                horizontalVelocity = 0;
            }

            this.setVelocity(new Vector3D(lateralVelocity, horizontalVelocity, 0));
            
            if (lateralVelocity == 0 && horizontalVelocity == 0) {
                return;
            }
        }
    }
}
