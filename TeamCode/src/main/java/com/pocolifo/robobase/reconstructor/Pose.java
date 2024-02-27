package com.pocolifo.robobase.reconstructor;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Pose extends Point {
    protected double headingRadians;

    public Pose(double x, double y, double heading, AngleUnit angleUnit) {
        super(x, y);
        this.headingRadians = angleUnit.toRadians(heading);
    }

    /**
     * IMPORTANT: Novel uses INCHES for x, y, and ROTATION. Therefore, this assumes the Z coordinate of the vector is in inches.
     * @param novelVector A vector that you would pass into a Novel setVelocity call.
     */
    public Pose(Vector3D novelVector, double robotDiameter) {
        super(novelVector.getX(), novelVector.getY());

        /*
         * Proof of headingRadians calculation.
         *
         * 1.
         * Robot circumference = PI * robotDiameter
         * > Robot circumference is the number of inches of a full rotation by definition.
         *
         * 2.
         * Fraction of circumference rotated = (novelVector.getZ()) / (Robot circumference)
         * > Fraction of circumference rotated is also the fraction of a full rotation.
         *
         * 3.
         * Heading in radians = (Fraction of circumference rotated) * (2 * PI)
         * > Recall that a full rotation is 2 * PI radians. Therefore, multiplying the fraction
         * > of circumference rotated will end with the number of radians rotated.
         *
         * Simplification is what headingRadians is set to below..
         */
        this.headingRadians = (2 * novelVector.getZ()) / (robotDiameter);
    }

    public void setHeading(double heading, AngleUnit angleUnit) {
        this.headingRadians = angleUnit.toRadians(heading);
    }

    public double getHeading(AngleUnit angleUnit) {
        return angleUnit.fromRadians(this.headingRadians);
    }

    public Pose add(Pose pose) {
        return new Pose(
                this.x + pose.x,
                this.y + pose.y,
                this.headingRadians + pose.headingRadians,
                AngleUnit.RADIANS
        );
    }

    public Pose subtract(Pose pose) {
        return new Pose(
                this.x - pose.x,
                this.y - pose.y,
                this.headingRadians - pose.headingRadians,
                AngleUnit.RADIANS
        );
    }

    public static Vector3D toVector3D(Pose pose) {
        return new Vector3D(pose.getX(), pose.getY(), pose.getHeading(AngleUnit.DEGREES));
    }

    public static Pose fromVector3D(Vector3D vector3D) {
        return new Pose(vector3D.getX(), vector3D.getY(), vector3D.getZ(), AngleUnit.DEGREES);
    }
}
