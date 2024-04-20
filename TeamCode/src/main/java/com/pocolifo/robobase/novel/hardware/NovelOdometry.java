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
        // Get new wheel positions
        double newLeftWheelPos = this.leftEncoder.getCurrentInches();
        double newRightWheelPos = this.rightEncoder.getCurrentInches();
        double newPerpendicularWheelPos = this.perpendicularEncoder.getCurrentInches();

        // Get changes in odometer wheel positions since last update
        double deltaLeftWheelPos =          this.coefficients.leftCoefficient          * (newLeftWheelPos          - this.leftWheelPos);
        double deltaRightWheelPos =         this.coefficients.rightCoefficient         * (newRightWheelPos         - this.rightWheelPos); // Manual adjustment for inverted odometry wheel
        double deltaPerpendicularWheelPos = this.coefficients.perpendicularCoefficient * (newPerpendicularWheelPos - this.perpendicularWheelPos);

        double phi = (deltaLeftWheelPos - deltaRightWheelPos) / Constants.Odometry.ODOMETRY_LATERAL_WHEEL_DISTANCE;
        double deltaMiddlePos = (deltaLeftWheelPos + deltaRightWheelPos) / 2d;
        double deltaPerpendicularPos = deltaPerpendicularWheelPos - Constants.Odometry.ODOMETRY_ROTATIONAL_WHEEL_OFFSET * phi;

        // Heading of movement is assumed average between last known and current rotation
        //                    CURRENT ROTATION                                             LAST SAVED ROTATION
        // double currentRotation = phi + this.relativePose.getHeading(AngleUnit.RADIANS);
        // double lastRotation = this.relativePose.getHeading(AngleUnit.RADIANS);
        // double averageRotationOverObservationPeriod = (currentRotation + lastRotation) / 2;
        double heading = phi + this.relativePose.getHeading(AngleUnit.RADIANS);
        double deltaX = -(deltaMiddlePos * Math.sin(-heading) + deltaPerpendicularPos * Math.cos(-heading));
        double deltaY = deltaMiddlePos * Math.cos(-heading) - deltaPerpendicularPos * Math.sin(-heading);

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

    public Pose getAbsolutePose(double startRotation) {
        double absoluteX = this.relativePose.getX() * Math.cos(startRotation) + this.relativePose.getY() * Math.sin(startRotation);
        double absoluteY = (this.relativePose.getY() * Math.cos(startRotation) + this.relativePose.getX() * Math.sin(startRotation));
        return new Pose(absoluteX, absoluteY, this.relativePose.getHeading(AngleUnit.RADIANS), AngleUnit.RADIANS);
    }
}
