package com.pocolifo.robobase.novel;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class NaiveConstantMotionProfile extends AbstractMotionProfile {
    public NaiveConstantMotionProfile(Vector3D targetDisplacement, Vector3D initialVelocity, double maxVelocity, double maxAcceleration, double minAcceleration) {
        super(targetDisplacement, initialVelocity, maxVelocity, maxAcceleration, minAcceleration);
    }

    @Override
    public double calculateDuration() {
        return this.targetDisplacement.getNorm() / this.maxVelocity;
    }

    @Override
    public Vector3D solveTime(double elapsed) {
        return this.solve();
    }

    @Override
    public Vector3D solveDisplacement(Vector3D currentDisplacement) {
        return this.solve();
    }

    private Vector3D solve() {
        return this.getVelocityInDirectionOfTarget(this.maxVelocity);
    }
}
