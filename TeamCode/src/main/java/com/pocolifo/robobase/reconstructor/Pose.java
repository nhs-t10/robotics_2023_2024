package com.pocolifo.robobase.reconstructor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Pose extends Point {
    protected double headingRadians;

    public Pose(double x, double y, double heading, AngleUnit angleUnit) {
        super(x, y);
        this.headingRadians = angleUnit.toRadians(heading);
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
}
