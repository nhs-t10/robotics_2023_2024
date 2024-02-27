package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous
@Config
public class AutoRedAprilTagSide extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private RobotCapabilities capabilities;
    private NovelMecanumDriver driver;
    private static double timeMultiplier = 2;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.capabilities = new RobotCapabilities(this.c);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
    }

    @Override
    public void run() {
        try {
            runPath(SpikePosition.LEFT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void runPath(SpikePosition spikePosition) throws InterruptedException {
        placePixel(spikePosition);
        driveVertical(84, 4 * timeMultiplier);
        lockRotation(-90, 0.5);
        sleep(100);

        switch (spikePosition) {
            case LEFT:
                driveHorizontal(-18 - 8 * 1, 2.5 * timeMultiplier);
                break;

            case CENTER:
                driveHorizontal(-18 - 8 * 2, 3 * timeMultiplier);
                break;

            case RIGHT:
                driveHorizontal(-18 - 8 * 3, 4 * timeMultiplier);
                break;
        }

        sleep(100);
        lockRotation(-90, 0.25);
    }

    public void placePixel(SpikePosition position) throws InterruptedException {
        switch (position) {
            case LEFT:
                driveVertical((12 + 12 + 9), 1 * timeMultiplier);
                rotateIMU(-90, 2);
                driveVertical(-5, 0.5 * timeMultiplier);
                dropAutoPixel();
                driveVertical(5, 0.5 * timeMultiplier);
                driveHorizontal(12 + 5 + 5, 1 * timeMultiplier);
                lockRotation(-90, 0.5);
                sleep(100);
                break;

            case CENTER:
                driveVertical((12 + 12 + 9 + 10), 2 * timeMultiplier);
                dropAutoPixel();
                driveVertical(12, 0.75 * timeMultiplier);
                lockRotation(-90, 0.5);
                break;

            case RIGHT:
                driveVertical((12 + 12 + 9), 1 * timeMultiplier);
                rotateIMU(90, 2);
                driveVertical(-5, 0.5 * timeMultiplier);
                dropAutoPixel();
                driveVertical(5, 0.5 * timeMultiplier);
                lockRotation(-90, 0.5);
                driveHorizontal(12 + 5 + 5, 1 * timeMultiplier);
                sleep(100);
                break;
        }
    }

    public void dropAutoPixel() throws InterruptedException {
        this.capabilities.runRoller(-1);
        sleep(1000);
        this.capabilities.runRoller(0);
    }

    public void driveVertical(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(inches / time, 0, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }

    public void driveHorizontal(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(0, inches / time, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }

    public void rotateIMU(double degrees, double precision) throws InterruptedException {
        double initialRotation = c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double relativeRotation;
        double error = degrees;

        while (Math.abs(error) > precision && !Thread.currentThread().isInterrupted()) {
            relativeRotation = c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - initialRotation;
            error = relativeRotation - degrees;

            this.driver.setVelocity(new Vector3D(0, 0, error));
        }

        this.driver.stop();


//        double initialRotation =
//        int direction = 1;
//        if(degrees < 0) {
//            direction = -1;
//        }
//        c.imu.resetYaw();
//        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
//        this.driver.setVelocity(new Vector3D(0,0, 20*direction));
//
//        while(Math.abs(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) < 90)
//        {
//            System.out.println(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
//        }
//        System.out.println("correcting..." + (c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - 90));
//        this.driver.setVelocity(new Vector3D(0,0,-4*direction));
//        while(Math.abs(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) > degrees*direction)
//        {
//            System.out.println(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
//        }
//        this.driver.stop();
    }

    public void lockRotation(double degrees, double precision) throws InterruptedException {
        rotateIMU(-(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - degrees), precision);
    }
}
