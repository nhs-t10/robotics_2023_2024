package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DistanceMovement {
    private static final double PRECISION_IN = 0.5;
    private static final double SPEED = 5;
    private final NovelMecanumDriver movementController;

    private final NovelOdometry odometry;

    public DistanceMovement(NovelMecanumDriver movementController, NovelOdometry odometry) {
        this.movementController = movementController;
        this.odometry = odometry;
    }

    public void move(Vector3D movement) {
        Vector3D position = getPosition();
        Vector3D target = position.add(movement);
        while (position.distance(target) > PRECISION_IN) {
            odometry.update();
            position = getPosition();
            movementController.setVelocity(getNewVelocity(position, target));
        }
        movementController.setVelocity(Vector3D.ZERO);
    }

    public Vector3D poseToVector3D(Pose pose) {
        return new Vector3D(pose.getX(), pose.getY(), pose.getHeading(AngleUnit.DEGREES));
    }

    public Vector3D getPosition() {
        return poseToVector3D(odometry.getRelativePose());
    }

    private static Vector3D getNewVelocity(Vector3D position, Vector3D target) {
        double horizontalMovement = target.getX() - position.getX();
        double verticalMovement = target.getY() - position.getY();
        double rotation = target.getZ() - position.getZ();

        return new Vector3D(horizontalMovement, verticalMovement, rotation)
                .normalize().scalarMultiply(SPEED);
    }
}