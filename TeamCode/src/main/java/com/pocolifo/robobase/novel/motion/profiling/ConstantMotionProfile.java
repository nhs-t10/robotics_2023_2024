package com.pocolifo.robobase.novel.motion.profiling;

public class ConstantMotionProfile implements MotionProfile {
    private final double maxVelocity;
    private final double distance;

    public ConstantMotionProfile(double maxVelocity, double distance) {
        this.maxVelocity = maxVelocity;
        this.distance = distance;
    }

    @Override
    public double computeVelocity(double currentDistance) {
        if (this.distance > currentDistance) {
            return this.maxVelocity;
        }

        return 0;
    }
}
