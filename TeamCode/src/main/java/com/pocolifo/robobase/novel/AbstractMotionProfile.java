package com.pocolifo.robobase.novel;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class AbstractMotionProfile {
    public final Vector3D targetDisplacement;
    public final double maxVelocity;
    public final double maxAcceleration;
    public final double minAcceleration;
    public final Vector3D initialVelocity;
    public final double duration;

    public AbstractMotionProfile(Vector3D targetDisplacement, Vector3D initialVelocity, double maxVelocity, double maxAcceleration, double minAcceleration) {
        this.targetDisplacement = targetDisplacement;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.minAcceleration = minAcceleration;
        this.initialVelocity = initialVelocity;
        this.duration = this.calculateDuration();
    }

    protected abstract double calculateDuration();
    public abstract Vector3D solveTime(double elapsed);
    public abstract Vector3D solveDisplacement(Vector3D currentDisplacement);

    protected Vector3D getVelocityInDirectionOfTarget(double velocity) {
        double reciprocal = velocity / this.targetDisplacement.getNorm();

        return new Vector3D(
                this.targetDisplacement.getX() * reciprocal,
                this.targetDisplacement.getY() * reciprocal,
                this.targetDisplacement.getZ() * reciprocal
        );
    }
}
