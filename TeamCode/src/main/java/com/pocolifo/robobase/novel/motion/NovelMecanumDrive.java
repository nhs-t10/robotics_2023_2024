package com.pocolifo.robobase.novel.motion;

import com.pocolifo.robobase.novel.hardware.NovelMotor;
import com.pocolifo.robobase.novel.OmniDriveCoefficients;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class NovelMecanumDrive {
    private final NovelMotor frontLeft;
    private final NovelMotor frontRight;
    private final NovelMotor backLeft;
    private final NovelMotor backRight;
    private final OmniDriveCoefficients omniDriveCoefficients;

    public NovelMecanumDrive(NovelMotor frontLeft, NovelMotor frontRight, NovelMotor backLeft, NovelMotor backRight, OmniDriveCoefficients omniDriveCoefficients) {
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

    public void drive(double distanceInches, double acceleration) {
        double velocity = 0;
        double initialPosition = getEncoderInches();

        while (getEncoderInches() - initialPosition < distanceInches / 2) {
            velocity += acceleration;
            setVelocity(new Vector3D(0, velocity, 0));
        }

        while (getEncoderInches() - initialPosition < distanceInches && velocity > 0) {
            velocity -= acceleration;
            setVelocity(new Vector3D(0, velocity, 0));
        }

        this.stop();
    }

    public void applyProfileTimeBased(AbstractMotionProfile profile) {
        long start = System.currentTimeMillis();
        double duration = profile.calculateDuration();

        while (true) {
            double elapsedSeconds = (System.currentTimeMillis() - start) / 1000d;

            if (elapsedSeconds >= duration) {
                break;
            }

            Vector3D vector3D = profile.solveTime(elapsedSeconds);
            this.setVelocity(vector3D);
        }

        this.stop();
    }

    public void applyProfileDisplacementBased(AbstractMotionProfile profile) {
        double frontLeftInchesStart = this.frontLeft.getEncoderInches();
        double frontRightInchesStart = this.frontRight.getEncoderInches();
        double backLeftInchesStart = this.backLeft.getEncoderInches();
        double backRightInchesStart = this.backRight.getEncoderInches();

        while (true) {
            double frontLeftInches = (this.frontLeft.getEncoderInches() - frontLeftInchesStart) * this.omniDriveCoefficients.totals.frontLeft;
            double frontRightInches = (this.frontRight.getEncoderInches() - frontRightInchesStart) * this.omniDriveCoefficients.totals.frontRight;
            double backLeftInches = (this.backLeft.getEncoderInches() - backLeftInchesStart) * this.omniDriveCoefficients.totals.backLeft;
            double backRightInches = (this.backRight.getEncoderInches() - backRightInchesStart) * this.omniDriveCoefficients.totals.backRight;

            Vector3D displacementVector = this.omniDriveCoefficients.calculatePowerWithCoefficients(
                    new OmniDriveCoefficients.CoefficientSet(
                        frontLeftInches,
                        frontRightInches,
                        backLeftInches,
                        backRightInches
                    )
            );

            if (displacementVector.getNorm() >= profile.targetDisplacement.getNorm()) {
                break;
            }

            Vector3D velocity = profile.solveDisplacement(displacementVector);

            this.setVelocity(velocity);
        }

        this.stop();
    }
}
