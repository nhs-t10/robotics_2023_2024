package com.pocolifo.robobase.tooling;

import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.OdometryCoefficientSet;
import com.pocolifo.robobase.novel.hardware.NovelEncoder;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class AutomaticOdometryCoefficientTuner extends AutonomousOpMode {
    protected NovelMecanumDriver driver;
    protected NovelEncoder re;
    protected NovelEncoder pe;
    protected NovelEncoder le;

    private static final OdometryCoefficientSet[] sets = {
            new OdometryCoefficientSet(-1, 1, -1),
            new OdometryCoefficientSet(1, 1, 1),
            new OdometryCoefficientSet(-1, 1, 1),
            new OdometryCoefficientSet(-1, -1, 1),
            new OdometryCoefficientSet(-1, -1, -1),
            new OdometryCoefficientSet(1, -1, -1),
    };

    @Override
    public void initialize() {
        if (this.driver == null || re == null || pe == null || le == null) {
            throw new IllegalStateException("please make sure to set the odometry encoders and driver");
        }
    }

    @Override
    public void run() {
        Telemetry.Item item = this.telemetry.addData("Testing coefficient set: ", "");
        Telemetry.Item pose = this.telemetry.addData("Pose: ", "");

        for (int setIndex = 0; sets.length > setIndex; setIndex++) {
            NovelOdometry odometry = new NovelOdometry(sets[setIndex], re, le, pe);
            Vector3D movementVector;

            if (setIndex % 2 == 0) {
                movementVector = new Vector3D(12, 12, 0);
            } else {
                movementVector = new Vector3D(-12, -12, 0);
            }

            item.setValue(sets[setIndex].rightCoefficient + " " + sets[setIndex].perpendicularCoefficient + " " + sets[setIndex].leftCoefficient);
            this.telemetry.update();

            this.driver.setVelocity(movementVector);
            long endTime = System.currentTimeMillis() + 2000;

            while (System.currentTimeMillis() < endTime) {
                odometry.update();
                Pose rp = odometry.getRelativePose();
                pose.setValue(
                        "x: " + rp.getX() + ", y: " + rp.getY() + ", deg: " + rp.getHeading(AngleUnit.DEGREES)
                );
                this.telemetry.update();
            }

            this.driver.stop();

            Pose relativePose = odometry.getRelativePose();
            Pose expectedPose = new Pose(movementVector.scalarMultiply(2), Constants.Robot.ROBOT_DIAMETER_IN);

            if (relativePose.distanceTo(expectedPose) < 1) {
                item.setCaption("CORRECT coefficient set: ");
                this.telemetry.update();
                return;
            }

            setIndex++;
        }
    }
}
