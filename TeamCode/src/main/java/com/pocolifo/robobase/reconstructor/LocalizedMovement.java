package com.pocolifo.robobase.reconstructor;

import android.os.SystemClock;

import com.pocolifo.robobase.novel.motion.NovelMecanumDrive;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.util.List;

public class LocalizedMovement {
    NovelMecanumDrive drive;
    PathFinder pathFinder;
    LocalizationEngine localizer;
    private double maxSpeed;
    private final static double EPSILON_POSITION_INCHES = 1; // The robot may be at most one inch away from where it should be.

    public LocalizedMovement(NovelMecanumDrive drive, PathFinder pathFinder, LocalizationEngine localizer) {
        this(drive, pathFinder, localizer, 8.0);
    }

    public LocalizedMovement(NovelMecanumDrive drive, PathFinder pathFinder, LocalizationEngine localizer, double maxSpeed) {
        this.drive = drive;
        this.pathFinder = pathFinder;
        this.localizer = localizer;
        this.maxSpeed = maxSpeed;
    }

    public void driveToPosition(PathFinder.Point destination) {
        PathFinder.Point start = getCurrentPositionEstimateAsPoint();
        do {
            List<PathFinder.Point> points = pathFinder.findPath(start, destination);
            PathFinder.Point currentPos = start;
            for (PathFinder.Point point : points) {
                int vertical = point.getY() - currentPos.getY();
                int horizontal = point.getX() - currentPos.getX();
                Movement movement = getMovement(vertical, horizontal);
                System.out.println("moving x: " + movement.getXSpeed() + " by y: " + movement.getYSpeed() + "\nfor: " + ((long) (movement.getTime() * 1000)) + " ms");
                drive.setVelocity(new Vector3D(movement.getYSpeed(), movement.getXSpeed(), 0));
                SystemClock.sleep((long) (movement.getTime() * 1000));
                currentPos = point;
            }
            drive.setVelocity(new Vector3D(0, 0, 0));
        } while (destination.distanceTo(getCurrentPositionEstimateAsPoint()) > EPSILON_POSITION_INCHES);
    }

    private PathFinder.Point getCurrentPositionEstimateAsPoint() {
        Position startPosition = localizer.getPositionEstimate().toUnit(DistanceUnit.INCH);
        return new PathFinder.Point((int)startPosition.x, (int)startPosition.y);
    }

    private Movement getMovement(double xDist, double yDist) {
        double maxTime = Math.max(Math.abs(xDist), Math.abs(yDist)) / maxSpeed;
        double xSpeed = xDist / maxTime;
        double ySpeed = yDist / maxTime;
        return new Movement(xSpeed, ySpeed, maxTime);
    }
}

class Movement {
    private double xSpeed;
    private double ySpeed;
    private double time;

    public Movement(double xSpeed, double ySpeed, double time) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.time = time;
    }

    public double getXSpeed() {
        return xSpeed;
    }

    public double getYSpeed() {
        return ySpeed;
    }

    public double getTime() {
        return time;
    }
}