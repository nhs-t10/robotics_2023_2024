package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.PathFinder;
import com.pocolifo.robobase.reconstructor.Pose;

import java.io.IOException;

import centerstage.CenterStageRobotConfiguration;

public class VirtualField {
    private final DistanceMovement movement;
    private final NovelOdometry odometry;
    private final Vector3D positionOffset;
    private final PathFinder pathFinder = new PathFinder("points.txt");

    public VirtualField(NovelMecanumDriver driver, NovelOdometry odometry, CenterStageRobotConfiguration c, Vector3D startPosition) throws IOException {
        this.movement = new DistanceMovement(driver, odometry, c.imu);
        this.odometry = odometry;
        positionOffset = startPosition;
    }

    public Vector3D getFieldPosition() {
        odometry.update();
        return Pose.toVector3D(odometry.getRelativePose()).add(positionOffset);
    }

    private void resetRotation() {
        double rotationNeeded = 0 - getFieldPosition().getZ();
        movement.rotate(rotationNeeded);
    }

    public void rotateTo(int degrees) {
        int degreesNeeded = degrees - getFieldPosition().getZ();
        movement.rotate(degreesNeeded);
    }

    public void pathTo(int x, int y, int degrees) {
        resetRotation();

        List<Vector3D> path = pathFinder.findPath(getFieldPosition(), new Vector3D(x, y, 0));

        for (Vector3D point : path) {
            Vector3D diff = point.subtract(getFieldPosition());

            movement.move(diff.getX(), diff.getY());
        }

        rotateTo(degrees);
    }
}