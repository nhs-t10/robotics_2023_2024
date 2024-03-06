package centerstage.auto;

import android.os.SystemClock;

import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import centerstage.TestBotRobotConfiguration;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.DynamicYCrCbDetection;
import com.pocolifo.robobase.vision.SpotDetectionPipeline;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class BaseProductionAuto extends AutonomousOpMode {
    private TestBotRobotConfiguration config;
    private RobotCapabilities capabilities;
    private final SpotDetectionPipeline spikeDetector;
    private final Alliance alliance;
    private final StartSide startSide;
    private NovelMecanumDrive driver;
    private double currentAngle;


    public BaseProductionAuto(SpotDetectionPipeline spikeDetector, Alliance alliance, StartSide startSide, Pose2d startPosition) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
    }

    @Override
    public void initialize() {
        this.config = new TestBotRobotConfiguration(this.hardwareMap);
        this.driver = new NovelMecanumDrive(this.config.fl, this.config.fr, this.config.bl, this.config.br, Constants.TESTBOT_COEFFICIENTS);
    }

    @Override
    public void run() {
        try {
            config.imu.resetYaw();
            /*SpotDetectionPipeline pipeline = (SpotDetectionPipeline) this.c.webcam.getPipeline();
            SpikePosition spikePosition;
            do {
                spikePosition = pipeline.getResult();
                sleep(100);
            } while (spikePosition == null);*/
            SpikePosition spikePosition = SpikePosition.RIGHT;

            System.out.println(spikePosition.toString());

            switch (spikePosition) {
                case LEFT:
                    System.out.println("left");
                    driveVertical(-30, 2);
                    sleep(500);
                    absoluteRotateIMU(90);
                    driveVertical(6, 0.5);
                    dropPixel();
                    driveVertical(-6, 0.5);
                    if (alliance == Alliance.BLUE) {absoluteRotateIMU(-90);} //TODO when IMU is fixed revert to one call of 180 degrees
                    driveHorizontal(24, 2);
                    break;

                case RIGHT:
                    System.out.println("right");
                    driveVertical(-30, 2);
                    sleep(500);
                    rotateIMU(-90);
                    driveVertical(6, 0.5);
                    dropPixel();
                    driveVertical(-6, 0.5);
                    if (alliance == Alliance.RED) {absoluteRotateIMU(90);} //TODO when IMU is fixed revert to one call of 180 degrees
                    driveHorizontal(-24, 2);
                    break;

                case CENTER:
                    System.out.println("center");
                    driveVertical(-46, 4);
                    dropPixel();
                    driveVertical(-5, 1);
                    if (alliance == Alliance.RED) absoluteRotateIMU(90);
                    else absoluteRotateIMU(-90);
                    break;
            }

            // ensure that robot is locked on target
            System.out.println(config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

            if (startSide == StartSide.APRIL_TAG_SIDE) driveVertical(-50, 5);

            switch (alliance) {
                case RED:
                    driveVertical(-40, 4);
                    driveHorizontal(30, 3);
                    break;
                case BLUE:
                    driveVertical(-40, 4);
                    driveHorizontal(-30, 3);
            }




            config.imu.resetYaw();
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
        config.imu.resetYaw();
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0, 20*direction));

        while(Math.abs(config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) < 90)
        {
            System.out.println(config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        System.out.println("correcting..." + (config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - 90));
        this.driver.setVelocity(new Vector3D(0,0,-4*direction));
        while(Math.abs(config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) > degrees*direction)
        {
            System.out.println(config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        this.driver.stop();
    }
    public void absoluteRotateIMU(double degrees) throws InterruptedException {
        int direction = 0;
        currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        System.out.println(currentAngle);
        if(degrees < currentAngle + 180)
        {
            System.out.println("Case 1");
            while(currentAngle < degrees)
            {
                direction = 1;
                this.driver.setVelocity(new Vector3D(0,0, 10));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                System.out.println(currentAngle);
            }
        }
        else if (degrees < currentAngle - 180)
        {
            direction = 1;
            System.out.println("Case 2");
            while(!(currentAngle < 0) || currentAngle < degrees)
            {
                this.driver.setVelocity(new Vector3D(0,0, 10));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                System.out.println(currentAngle);
            }
        }
        else if(degrees > currentAngle + 180)
        {
            direction = -1;
            System.out.println("Case 3");
            while(currentAngle < 0 || currentAngle > degrees)
            {
                this.driver.setVelocity(new Vector3D(0,0, -10));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                System.out.println(currentAngle);
            }
        }
        else if (degrees > currentAngle - 180)
        {
            direction = -1;
            System.out.println("Case 4");
            while(currentAngle > 0 || currentAngle < degrees)
            {
                this.driver.setVelocity(new Vector3D(0,0, -10));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                System.out.println(currentAngle);
            }
        }

        if(Math.abs(degrees) > 170) {
            if (direction > 0) {
                while (currentAngle > degrees) {
                    currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                    this.driver.setVelocity(new Vector3D(0, 0, -3 * direction));
                }
            }
            else if (direction < 0)
            {
                while (currentAngle < degrees) {
                    currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                    this.driver.setVelocity(new Vector3D(0, 0, -3 * direction));
                }
            }
        }
        else if(direction > 0){
            while(currentAngle > degrees )
            {
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                this.driver.setVelocity(new Vector3D(0,0, -3*direction));
            }
        }
        else
        {
            while(currentAngle < degrees)
            {
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                this.driver.setVelocity(new Vector3D(0,0, -3*direction));
            }
        }
    }


    public void dropPixel()
    {
        //todo: implement
    }
    public void align(boolean imu_button) throws InterruptedException {
        double imu_init;
        double turnTo;
        if(imu_button) {
            imu_init = config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) % 360;
            config.imu.resetYaw();
            turnTo = 360 - imu_init;
            rotateIMU(turnTo);
        }
    }
}
