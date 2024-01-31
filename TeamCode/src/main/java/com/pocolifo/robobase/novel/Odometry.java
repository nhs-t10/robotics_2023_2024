package com.pocolifo.robobase.novel;

import android.os.health.SystemHealthManager;
import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Const;

public class Odometry {
    private Pose2d robotPosition;
    private double leftWheelPos;
    private double rightWheelPos;
    private double perpendicularWheelPos;
    public final DcMotor rightWheel;
    public final DcMotor leftWheel;
    public final DcMotor perpendicularWheel;

    public Odometry(Pose2d robotPosition, DcMotor rightWheel, DcMotor leftWheel, DcMotor perpendicularWheel) {
        this.robotPosition = robotPosition;

        this.rightWheel = rightWheel;
        this.leftWheel = leftWheel;
        this.perpendicularWheel = perpendicularWheel;

        this.rightWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.perpendicularWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.updateWheelPositions();
    }

    public Odometry(HardwareMap hardwareMap, Pose2d robotPosition, String rightWheel, String leftWheel, String perpendicularWheel) {
        this(robotPosition, hardwareMap.dcMotor.get(rightWheel), hardwareMap.dcMotor.get(leftWheel), hardwareMap.dcMotor.get(perpendicularWheel));
    }

    public void update() {
        // Get new wheel positions
        double newLeftWheelPos = this.getLeftWheelPosIn();
        double newRightWheelPos = this.getRightWheelPosIn();
        double newPerpendicularWheelPos = this.getPerpendicularWheelPosIn();

        // Get changes in odometer wheel positions since last update
        double flWheelPosChange = newLeftWheelPos - leftWheelPos;
        double frWheelPosChange = newRightWheelPos - rightWheelPos;
        double perpendicularWheelPosChange = newPerpendicularWheelPos - perpendicularWheelPos;

        // Calculate rotational and positional changes relative to last update
        double rotationChange = (flWheelPosChange - frWheelPosChange) / Constants.ODOMETRY_LATERAL_WHEEL_DISTANCE;
        double centerPosChange = (flWheelPosChange + flWheelPosChange) / 2;
        double horizontalPosChange = perpendicularWheelPosChange - (Constants.ODOMETRY_ROTATIONAL_WHEEL_OFFSET * rotationChange);

        // Utilize last rotation to apply positional changes to absolute field
        double xPosChange = centerPosChange * Math.cos(robotPosition.getHeading()) - horizontalPosChange * Math.sin(robotPosition.getHeading());
        double yPosChange = centerPosChange * Math.sin(robotPosition.getHeading()) + horizontalPosChange * Math.cos(robotPosition.getHeading());

        // Apply positional changes
        Pose2d positionChange = new Pose2d(xPosChange, yPosChange, rotationChange);
        robotPosition = new Pose2d(robotPosition.getX() + xPosChange,
                robotPosition.getY() + yPosChange,
                (robotPosition.getHeading() + rotationChange) % 360);

        // Update encoder wheel position
        leftWheelPos = newLeftWheelPos;
        rightWheelPos = newRightWheelPos;
        perpendicularWheelPos = newPerpendicularWheelPos;
    }

    public double getX() {
        return robotPosition.getX();
    }

    public double getY() {
        return robotPosition.getY();
    }

    public double getRotation() {
        return robotPosition.getHeading();
    }

    public void updateWheelPositions() {
        this.leftWheelPos = leftWheel.getCurrentPosition() / Constants.TICKS_PER_ODOMETRY_REVOLUTION * Constants.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
        this.rightWheelPos = rightWheel.getCurrentPosition() / Constants.TICKS_PER_ODOMETRY_REVOLUTION * Constants.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
        this.perpendicularWheelPos = perpendicularWheel.getCurrentPosition() / Constants.TICKS_PER_ODOMETRY_REVOLUTION * Constants.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }

    public double getLeftWheelPosIn() {
        return leftWheel.getCurrentPosition() / Constants.TICKS_PER_ODOMETRY_REVOLUTION * Constants.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }

    public double getRightWheelPosIn() {
        return rightWheel.getCurrentPosition() / Constants.TICKS_PER_ODOMETRY_REVOLUTION * Constants.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }

    public double getPerpendicularWheelPosIn() {
        return perpendicularWheel.getCurrentPosition() / Constants.TICKS_PER_ODOMETRY_REVOLUTION * Constants.ODOMETRY_WHEEL_DIAMETER_IN * Math.PI;
    }
}
