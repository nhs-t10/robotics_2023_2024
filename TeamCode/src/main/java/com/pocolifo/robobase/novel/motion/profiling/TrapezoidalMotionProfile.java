package com.pocolifo.robobase.novel.motion.profiling;

public class TrapezoidalMotionProfile implements MotionProfile {
    private final double acceleration;
    private final double initialVelocity;
    private final double targetDistance;
    private final double estimatedDuration;
    private final double distanceToStartDecelerating;
    private double maxVelocity;
    private double distanceAtFullAcceleration;

    public TrapezoidalMotionProfile(double acceleration, double initialVelocity, double targetDistance, double maxVelocity) {
        this.acceleration = acceleration;
        this.initialVelocity = initialVelocity;
        this.targetDistance = targetDistance;
        this.maxVelocity = maxVelocity;

        // Lincoln's calculations
        this.distanceAtFullAcceleration = solveDisplacement(maxVelocity, initialVelocity, acceleration);

        if (this.distanceAtFullAcceleration > targetDistance / 2) {
            this.distanceAtFullAcceleration = targetDistance / 2;
            this.maxVelocity = solveVelocity(initialVelocity, acceleration, this.distanceAtFullAcceleration);
        }

        this.distanceToStartDecelerating = targetDistance - this.distanceAtFullAcceleration;

        double accelerationDuration = solveTime(this.distanceAtFullAcceleration, initialVelocity, acceleration);
        double cruiseDuration = (targetDistance - (this.distanceAtFullAcceleration * 2)) / maxVelocity;
        this.estimatedDuration = 2 * accelerationDuration + cruiseDuration;
    }

    /**
     * @param currentDistance How far the robot has traveled since the profile began.
     * @return The velocity the robot should travel at this `currentDistance` to go the distance.
     */
    public double computeVelocity(double currentDistance) {
        // Without the following if statement, the robot will never start moving.
        // B/c the velocity will always be 0 when currentDistance=0, so the robot will never actually start moving.
        // Therefore, you have to give the algorithm a little boost when starting out.
        if (currentDistance == 0) {
            currentDistance = 1;
        }

        if (currentDistance > this.targetDistance) {
            return 0;
        }

        if (currentDistance >= this.distanceToStartDecelerating) {
            // Decelerate
            return solveVelocity(this.maxVelocity, -this.acceleration, Math.abs(this.targetDistance - currentDistance - this.distanceAtFullAcceleration));  // TODO: clean up the math for the Math.abs call
        } else if (currentDistance >= this.distanceAtFullAcceleration) {
            // Cruise at max velocity
            return this.maxVelocity;
        } else {
            // Accelerate
            return solveVelocity(this.initialVelocity, this.acceleration, currentDistance);
        }
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    // The following are derived from basic kinematic equations.
    private static double solveVelocity(double initialVelocity, double acceleration, double displacement) {
        return Math.sqrt(Math.pow(initialVelocity, 2) + 2 * acceleration * displacement);
    }

    private static double solveDisplacement(double finalVelocity, double initialVelocity, double acceleration) {
        return (Math.pow(finalVelocity, 2) - Math.pow(initialVelocity, 2)) / (2 * acceleration);
    }

    private static double solveTime(double displacement, double initialVelocity, double acceleration) {
        return quadraticFormula(
                0.5 * acceleration,
                initialVelocity,
                -displacement
        );
    }

    private static double quadraticFormula(double a, double b, double c) {
        double determinant = Math.pow(b, 2) - 4 * a * c;
        double numerator = -b + Math.sqrt(determinant);
        double denominator = 2 * a;

        return numerator / denominator;
    }
}
