package com.pocolifo.robobase.novel.hardware;

import centerstage.Constants;
import com.pocolifo.robobase.novel.OdometryCoefficientSet;
import com.pocolifo.robobase.reconstructor.Pose;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class NovelOdometry {
    private final OdometryCoefficientSet coefficients;
    public final NovelEncoder rightEncoder;
    public final NovelEncoder leftEncoder;
    public final NovelEncoder perpendicularEncoder;
    private double leftWheelPos;
    private double rightWheelPos;
    private double perpendicularWheelPos;
    private Pose relativePose;

    public NovelOdometry(OdometryCoefficientSet coefficients, NovelEncoder rightEncoder, NovelEncoder leftEncoder, NovelEncoder perpendicularEncoder) {
        this.coefficients = coefficients;
        this.rightEncoder = rightEncoder;
        this.leftEncoder = leftEncoder;
        this.perpendicularEncoder = perpendicularEncoder;

        this.resetRelativePose();
    }

    // Adapted from https://gm0.org/en/latest/docs/software/concepts/odometry.html
    public void update() {
        // Update to fetch current wheel positions
        double newLeftWheelPos = leftEncoder.getCurrentInches();
        double newRightWheelPos = rightEncoder.getCurrentInches();
        double newPerpendicularWheelPos = perpendicularEncoder.getCurrentInches();

        // Calculate deltas for each wheel position
        double deltaLeftWheelPos = coefficients.leftCoefficient * (newLeftWheelPos - leftWheelPos);
        double deltaRightWheelPos = coefficients.rightCoefficient * (newRightWheelPos - rightWheelPos);
        double deltaPerpendicularWheelPos = coefficients.perpendicularCoefficient * (newPerpendicularWheelPos - perpendicularWheelPos);

        // Calculate the change in orientation (phi)
        double averageMovementLeftRight = (deltaLeftWheelPos - deltaRightWheelPos) / 2;
//        System.out.println("Average Movement Left Right: " + averageMovementLeftRight);
        double phi = averageMovementLeftRight / (Constants.Odometry.ODOMETRY_LATERAL_WHEEL_DISTANCE);
//        System.out.println("phi: " + phi);

        // Calculate the average forward movement
        double deltaMiddlePos = (deltaLeftWheelPos + deltaRightWheelPos) / 2.0;
//        System.out.println("deltaMiddlePos: " + deltaMiddlePos);

        // Correct deltaPerpendicularPos to eliminate the rotational component
//        System.out.println("deltaPerpendicularWheelPos: " + deltaPerpendicularWheelPos);
        double deltaPerpendicularPos = deltaPerpendicularWheelPos - averageMovementLeftRight;
//        System.out.println("deltaPerpendicularPos: " + deltaPerpendicularPos);

        // Assuming currentOrientation is the robot's orientation at the start of this calculation
        double initialOrientation = relativePose.getHeading(AngleUnit.RADIANS); // Assuming this is the orientation at the start
        double newOrientation = initialOrientation + phi;

        //double heading = (phi + this.relativePose.getHeading(AngleUnit.RADIANS) + this.relativePose.getHeading(AngleUnit.RADIANS)) / 2;

        double currentOrientation = (initialOrientation + newOrientation) / 2.0;
        // Calculate global movement deltas
        double deltaX = deltaMiddlePos * Math.sin(currentOrientation) - deltaPerpendicularPos * Math.cos(currentOrientation);
        double deltaY = deltaMiddlePos * Math.cos(currentOrientation) + deltaPerpendicularPos * Math.sin(currentOrientation);


        this.relativePose = this.relativePose.add(new Pose(deltaX, deltaY, phi, AngleUnit.RADIANS));

        // Update encoder wheel position
        this.leftWheelPos = newLeftWheelPos;
        this.rightWheelPos = newRightWheelPos;
        this.perpendicularWheelPos = newPerpendicularWheelPos;
    }

    public Pose getRelativePose() {
        return this.relativePose;
    }

    public void resetRelativePose() {
        this.relativePose = new Pose(0, 0, 0, AngleUnit.RADIANS);
        this.leftWheelPos = this.leftEncoder.getCurrentInches();
        this.rightWheelPos = this.rightEncoder.getCurrentInches();
        this.perpendicularWheelPos = this.perpendicularEncoder.getCurrentInches();
    }
}
