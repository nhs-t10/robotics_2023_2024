package com.pocolifo.robobase.novel;

import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Odometry {
    private final Pose2d robotPosition;
    private double leftWheelPos;
    private double rightWheelPos;
    private double perpendicularWheelPos;
    private final DcMotor rightWheel;
    private final DcMotor leftWheel;
    private final DcMotor perpendicularWheel;

    public Odometry(Pose2d robotPosition, DcMotor rightWheel, DcMotor leftWheel, DcMotor perpendicularWheel) {
        this.robotPosition = robotPosition;

        this.rightWheel = rightWheel;
        this.leftWheel = leftWheel;
        this.perpendicularWheel = perpendicularWheel;

        this.leftWheelPos = leftWheel.getCurrentPosition();
        this.rightWheelPos = rightWheel.getCurrentPosition();
        this.perpendicularWheelPos = perpendicularWheel.getCurrentPosition();
    }

    public void update() {
        //Get new wheel positions
        double newLeftWheelPos = leftWheel.getCurrentPosition();
        double newRightWheelPos = rightWheel.getCurrentPosition();
        double newPerpendicularWheelPos = perpendicularWheel.getCurrentPosition();

        //Get changes in odometry wheel positions since last update
        double flWheelPosChange = newLeftWheelPos - leftWheelPos;
        double frWheelPosChange = newRightWheelPos - rightWheelPos;
        double perpendicularWheelPosChange = newPerpendicularWheelPos - perpendicularWheelPos;

        //Calculate rotational and positional changes relative to last update
        double rotationChange = (flWheelPosChange - frWheelPosChange) / Constants.ODOMETRY_LATERAL_WHEEL_DISTANCE;
        double centerPosChange = (flWheelPosChange + flWheelPosChange) / 2;
        double horizontalPosChange = perpendicularWheelPosChange - (Constants.ODOMETRY_ROTATIONAL_WHEEL_OFFSET * rotationChange);

        //Utilize last rotation to apply positional changes to absolute field
        double xPosChange = centerPosChange * Math.cos(robotPosition.getHeading()) - horizontalPosChange * Math.sin(robotPosition.getHeading());
        double yPosChange = centerPosChange * Math.sin(robotPosition.getHeading()) + horizontalPosChange * Math.cos(robotPosition.getHeading());

        //Apply positional changes
        Pose2d positionChange = new Pose2d(xPosChange, yPosChange, rotationChange);
        robotPosition.plus(positionChange);

        //Update encoder wheel position
        leftWheelPos = newLeftWheelPos;
        rightWheelPos = newRightWheelPos;
        perpendicularWheelPos = newPerpendicularWheelPos;
    }

    public double getX() {return robotPosition.getX();}

    public double getY() {return robotPosition.getY();}

    public double getRotation() {return robotPosition.getHeading();}


}
