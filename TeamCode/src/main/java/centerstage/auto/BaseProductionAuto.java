package centerstage.auto;

import android.os.SystemClock;
import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.DynamicYCrCbDetection;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class BaseProductionAuto extends AutonomousOpMode {
    private final CenterStageRobotConfiguration c;
    private RobotCapabilities capabilities;
    private final DynamicYCrCbDetection spikeDetector;
    private final Alliance alliance;
    private final StartSide startSide;
    private NovelMecanumDrive driver;


    public BaseProductionAuto(DynamicYCrCbDetection spikeDetector, Alliance alliance, StartSide startSide, Pose2d startPosition) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.capabilities = new RobotCapabilities(this.c);
    }

    @Override
    public void initialize() {
        this.c.webcam.open(this.spikeDetector);
        this.driver = new NovelMecanumDrive(this.c.fl, this.c.fr, this.c.bl, this.c.br, Constants.PRODUCTION_COEFFICIENTS);
    }

    @Override
    public void run() {
        try {
            DynamicYCrCbDetection pipeline = (DynamicYCrCbDetection) this.c.webcam.getPipeline();
            SpikePosition spikePosition;
            do {
                spikePosition = pipeline.getResult();
                sleep(100);
            } while (spikePosition == null);
            System.out.println(spikePosition.toString());

            switch (spikePosition) {
                case LEFT:
                    System.out.println("left");
                    driveVertical(-27, 2);
                    sleep(500);
                    rotateIMU(90);
                    driveVertical(2, 0.5);
                    dropPixel();
                    driveVertical(-2, 0.5);
                    rotateIMU(-90);
                    //driveHorizontal(16, 1);
                    break;

                case RIGHT:
                    System.out.println("right");
                    driveVertical(-27, 2);
                    sleep(500);
                    rotateIMU(-90);
                    driveVertical(2, 0.5);
                    dropPixel();
                    driveVertical(-2, 0.5);
                    rotateIMU(90);
                    //driveHorizontal(-16, 1);
                    break;

                case CENTER:
                    System.out.println("center");
                    driveVertical(-51, 3);
                    dropPixel();
                    break;
            }

            // TODO: drop the auto pixel
            SystemClock.sleep(500);

            //Reset to neutral position - GOOD
            switch (spikePosition) {
                case LEFT:
                    //driveHorizontal(-16, 1);
                    break;

                case RIGHT:
                    //driveHorizontal(16, 1);
                    break;

                case CENTER:
                    driveVertical(24, 1.25);
                    break;
            }

            SystemClock.sleep(500);

            driveHorizontal((42 + startSide.getSideSwapConstantIn()) * alliance.getAllianceSwapConstant(), 1.5 + (startSide.getSideSwapConstantIn() / 16));
            SystemClock.sleep(500);

            rotateIMU(-90 * alliance.getAllianceSwapConstant());
            SystemClock.sleep(500);

            //this.aprilTagAligner = new BackdropAprilTagAligner(this.driver, SpikePosition.RIGHT, this.webcam, this.alliance, 30, 4);
            //alignWithAprilTag();
            switch (spikePosition) {
                case LEFT:
                    driveHorizontal(8, 0.5);
                    break;

                case RIGHT:
                    driveHorizontal(-8, 0.5);
                    break;

                case CENTER:
                    break;
            }

            SystemClock.sleep(500);

            //todo: place!!!

            switch (spikePosition){
                case LEFT:
                    driveHorizontal(-8,0.5);
                case RIGHT:
                    driveHorizontal(8,0.5);
                case CENTER:
                    //do nothing
            }
            driveHorizontal(24*alliance.getAllianceSwapConstant(),1.5);
            c.imu.resetYaw();
        } catch (Throwable e) {
            System.out.println("Stopped");
        }
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
    public void rotate(double degrees, double time) throws InterruptedException {
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0,
                (Math.toRadians(degrees) * (Constants.ROBOT_DIAMETER_IN)/time)));
        sleep((long)time*1000);
        this.driver.stop();
    }
    public void rotateIMU(double degrees) throws InterruptedException {
        int direction = 1;
        if(degrees < 0) {
            direction = -1;
        }
        c.imu.resetYaw();
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0, 20*direction));

        while(Math.abs(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) < 90)
        {
            System.out.println(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        System.out.println("correcting..." + (c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - 90));
        this.driver.setVelocity(new Vector3D(0,0,-4*direction));
        while(Math.abs(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) > degrees*direction)
        {
            System.out.println(c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        this.driver.stop();
    }


    public void dropPixel()
    {
        //todo: fix power
        this.capabilities.runOuttake();
        SystemClock.sleep(1000);
        this.capabilities.stopIntakeOuttake();
    }
    public void align(boolean imu_button) throws InterruptedException {
        double imu_init;
        double turnTo;
        if(imu_button) {
            imu_init = c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) % 360;
            c.imu.resetYaw();
            turnTo = 360 - imu_init;
            rotateIMU(turnTo);
        }
    }
}
