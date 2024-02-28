package com.pocolifo.robobase.novel.motion.pathing;

import android.os.SystemClock;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.PathFinder;
import com.pocolifo.robobase.reconstructor.Point;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Iterator;
import java.util.List;

public class DiscretePathFollower implements PathFollower {
    private final Iterator<Vector3D> pointsIterator;
    private final List<Vector3D> points;
    private final double maxSpeed;
    private final double maxAcceleration;
    private Vector3D currentPoint;

    public DiscretePathFollower(List<Vector3D> points, double maxSpeed, double maxAcceleration) {
        if (points.size() >= 2)
            throw new IllegalArgumentException("Points must have at least 2 points (a start and an end point)");

        this.points = points;
        this.pointsIterator = points.iterator();
        this.currentPoint = points.get(0);
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
    }

    @Override
    public void followPath(NovelMecanumDriver driver) {
        while (this.pointsIterator.hasNext()) {
            Vector3D nextPoint = this.pointsIterator.next();
            double verticalDelta = nextPoint.getX() - this.currentPoint.getX();
            double horizontalDelta = nextPoint.getY() - this.currentPoint.getY();
            double maxTime = Math.max(Math.abs(horizontalDelta), Math.abs(verticalDelta)) / this.maxSpeed;

            driver.setVelocity(new Vector3D(
                    verticalDelta / maxTime,
                    horizontalDelta / maxTime,
                    0
            ));

            SystemClock.sleep((long) this.maxSpeed * 1000);

            // TODO: add a section here to correct movement
            this.currentPoint = nextPoint;
        }

        driver.setVelocity(new Vector3D(0, 0, 0));
    }

    @Override
    public double getDuration() {
        double duration = 0;

        for (int i = 0; this.points.size() - 1 > i; i++) {
            Vector3D nextPoint = this.points.get(i + 1);
            double verticalDelta = nextPoint.getX() - this.currentPoint.getX();
            double horizontalDelta = nextPoint.getY() - this.currentPoint.getY();
            double maxTime = Math.max(Math.abs(horizontalDelta), Math.abs(verticalDelta)) / this.maxSpeed;

            duration += maxTime;
        }

        return duration;
    }
}
