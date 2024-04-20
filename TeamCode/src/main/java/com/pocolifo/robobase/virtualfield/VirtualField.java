package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.PathFinder;
import com.pocolifo.robobase.reconstructor.Pose;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;
import java.util.List;

import centerstage.CenterStageRobotConfiguration;

/**
 * A system for using NovelOdometry, DistanceMovement, and PathFinder to pathfind to any safe point on the field
 * @see NovelOdometry
 * @see DistanceMovement
 * @see PathFinder
 */
public class VirtualField {
    private static final double SPEED = 10;
    private final DistanceMovement movement;
    private final NovelOdometry odometry;
    private final Vector3D positionOffset;
    private final PathFinder pathFinder = new PathFinder("points.txt", "turnPoints.txt");

    public VirtualField(NovelMecanumDriver driver, NovelOdometry odometry, CenterStageRobotConfiguration c, Vector3D startPosition) throws IOException {
        this.movement = new DistanceMovement(driver, odometry, c.imu, startPosition.getZ(), SPEED);
        this.odometry = odometry;
        positionOffset = startPosition;
    }

    /**
     * A getter for the current absolute field position
     * @return the current absolute field position with rotation as Z in degrees
     */
    public Vector3D getFieldPosition() {
        return Pose.toVector3D(odometry.getAbsolutePose(Math.toRadians(positionOffset.getZ()))).add(positionOffset);
    }

    private void resetRotation() {
        rotateTo(0);
    }

    /**
     * Rotate to face a given absolute rotation
     * @param degrees the rotation to face in degrees
     */
    public void rotateTo(double degrees) {
        double degreesNeeded = degrees - getFieldPosition().getZ();
        movement.rotate(degreesNeeded);
    }

    /**
     * Path to a given absolute position including rotation as Z as degrees
     * @param position the target position
     */
    public void pathTo(Vector3D position) {
//        resetRotation();

        PathFinder.Path path = pathFinder.findPath(round(getFieldPosition()), round(new Vector3D(position.getX(), position.getY(), 0)))  ;

        for (Vector3D point : path.getPoints()) {
            if (point.equals(path.getTurnPoint())) {
                rotateTo(position.getZ());
            }
            Vector3D diff = point.subtract(getFieldPosition());
            movement.transform(diff.getX(), diff.getY());
        }
    }

    /**
     * A getter for the object's instance of DistanceMovement
     * @return the object's instance of DistanceMovement
     */
    public DistanceMovement getDistanceMovement() {
        return movement;
    }

    private Vector3D round(Vector3D vector) {
        return new Vector3D(Math.round(vector.getX()), Math.round(vector.getY()), Math.round(vector.getZ()));
    }
}