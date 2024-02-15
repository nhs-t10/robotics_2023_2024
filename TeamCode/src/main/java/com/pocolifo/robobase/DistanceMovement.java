package com.pocolifo.robobase;

import com.pocolifo.robobase.novel.NovelMecanumDrive;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
public class DistanceMovement {
    private static final double PRECISION_IN = 0.5;
    private static final double MAX_SPEED = 5;
    private NovelMecanumDrive movementController;

    public DistanceMovement(NovelMecanumDrive movementController) {
        this.movementController = movementController;
    }

    public void move(Vector3D movement) {
        Vector3D position = DeadWheels.getPosition();
        Vector3D target = position.add(movement);
        while (position.distance(target) > PRECISION_IN) {
            position = DeadWheels.getPosition();
            movementController.setVelocity(getNewVelocity(position, target));
        }
    }

    private static Vector3D getNewVelocity(Vector3D position, Vector3D target) {
        double horizontalMovement = target.getX() - position.getX();
        double verticalMovement = target.getY() - position.getY();
        double rotation = target.getZ() - position.getZ();

        return new Vector3D(horizontalMovement, verticalMovement, rotation)
                .normalize().scalarMultiply(MAX_SPEED);
    }
}
