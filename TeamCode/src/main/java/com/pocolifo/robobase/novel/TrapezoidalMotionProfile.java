package com.pocolifo.robobase.novel;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class TrapezoidalMotionProfile extends AbstractMotionProfile {
    private double secondsToMaxVelocity;
    private double beginToDecelerateTime;

    public TrapezoidalMotionProfile(Vector3D targetDisplacement, Vector3D initialVelocity, double maxVelocity, double maxAcceleration, double minAcceleration) {
        super(targetDisplacement, initialVelocity, maxVelocity, maxAcceleration, minAcceleration);

        this.secondsToMaxVelocity = (maxVelocity - initialVelocity.getNorm()) / maxAcceleration;
        this.beginToDecelerateTime = this.duration - this.secondsToMaxVelocity;

        if (this.secondsToMaxVelocity > this.beginToDecelerateTime) {
            // set them to the same thing at duration / 2
            this.secondsToMaxVelocity = this.duration / 2;
            this.beginToDecelerateTime = this.secondsToMaxVelocity;
        }
    }

    @Override
    public double calculateDuration() {
        return 2 * Math.sqrt(this.targetDisplacement.getNorm() / this.maxAcceleration);
    }

    @Override
    public Vector3D solveTime(double elapsed) {
        double velocity;

        // Normal circumstances
        if (this.secondsToMaxVelocity > elapsed) {
            velocity = this.maxAcceleration * elapsed;
        } else if (elapsed >= this.secondsToMaxVelocity && this.beginToDecelerateTime > elapsed) {
            velocity = this.maxAcceleration;
        } else {
            velocity = this.maxVelocity * (this.duration - elapsed);
        }

        return this.getVelocityInDirectionOfTarget(velocity);
    }

    @Override
    public Vector3D solveDisplacement(Vector3D currentDisplacement) {
        //TODO Incorporate non-zero initial velocities.
        double time = Math.sqrt(
                (2 * currentDisplacement.getNorm()) / this.maxAcceleration
        );

        return this.solveTime(time);
    }
}
