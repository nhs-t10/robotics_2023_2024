package com.pocolifo.robobase.novel.hardware;

import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.novel.OdometryCoefficientSet;
import com.qualcomm.robotcore.hardware.DcMotor;

public class NovelOdometry {
    private final OdometryCoefficientSet coefficients;
    private Pose2d robotPosition;
    private double leftWheelPos;
    private double rightWheelPos;
    private double perpendicularWheelPos;
    public final DcMotor rightWheel;
    public final DcMotor leftWheel;
    public final DcMotor perpendicularWheel;

    public NovelOdometry(Pose2d robotPosition, OdometryCoefficientSet coefficients, DcMotor rightWheel, DcMotor leftWheel, DcMotor perpendicularWheel) {
        this.robotPosition = robotPosition;
        this.coefficients = coefficients;

        this.rightWheel = rightWheel;
        this.leftWheel = leftWheel;
        this.perpendicularWheel = perpendicularWheel;

        this.rightWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.perpendicularWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.updateWheelPositions();
    }

    public void update() {
        // Get new wheel positions
        double newLeftWheelPos = this.getLeftWheelPosIn();
        double newRightWheelPos = this.getRightWheelPosIn();
        double newPerpendicularWheelPos = this.getPerpendicularWheelPosIn();

        // Get changes in odometer wheel positions since last update
        double deltaLeftWheelPos = this.coefficients.leftCoefficient * (newLeftWheelPos - this.leftWheelPos);
        double deltaRightWheelPos = this.coefficients.rightCoefficient * (newRightWheelPos - this.rightWheelPos); // Manual adjustment for inverted odometry wheel
        double deltaPerpendicularWheelPos = this.coefficients.perpendicularCoefficient * (newPerpendicularWheelPos - this.perpendicularWheelPos);

        double phi = (deltaLeftWheelPos - deltaRightWheelPos) / Constants.Odometry.ODOMETRY_LATERAL_WHEEL_DISTANCE;
        double deltaMiddlePos = (deltaLeftWheelPos + deltaRightWheelPos) / 2d;
        double deltaPerpendicularPos = deltaPerpendicularWheelPos - Constants.Odometry.ODOMETRY_ROTATIONAL_WHEEL_OFFSET * phi;

        double deltaX = deltaMiddlePos * Math.cos(this.getHeading()) - deltaPerpendicularPos * Math.sin(this.getHeading());
        double deltaY = deltaMiddlePos * Math.sin(this.getHeading()) + deltaPerpendicularPos * Math.cos(this.getHeading());

        this.robotPosition = new Pose2d(
                this.robotPosition.getX() + deltaX,
                this.robotPosition.getY() + deltaY,
                this.robotPosition.getHeading() + phi
        );

        // Update encoder wheel position
        this.leftWheelPos = newLeftWheelPos;
        this.rightWheelPos = newRightWheelPos;
        this.perpendicularWheelPos = newPerpendicularWheelPos;
    }

    public double getX() {
        return robotPosition.getX();
    }

    public double getY() {
        return robotPosition.getY();
    }

    public void updateWheelPositions() {
        this.leftWheelPos = leftWheel.getCurrentPosition() / Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION * Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
        this.rightWheelPos = rightWheel.getCurrentPosition() / Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION * Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
        this.perpendicularWheelPos = perpendicularWheel.getCurrentPosition() / Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION * Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }

    public double getLeftWheelPosIn() {
        return leftWheel.getCurrentPosition() / Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION * Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }

    public double getRightWheelPosIn() {
        return rightWheel.getCurrentPosition() / Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION * Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }

    public double getPerpendicularWheelPosIn() {
        return perpendicularWheel.getCurrentPosition() / Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION * Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }

    public double getHeading() {
        return this.robotPosition.getHeading();
    }
}
