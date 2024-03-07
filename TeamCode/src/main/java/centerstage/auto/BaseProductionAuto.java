package centerstage.auto;

import centerstage.BackdropAprilTagAligner;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import centerstage.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.SpotDetectionPipeline;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class BaseProductionAuto extends AutonomousOpMode {
    private CenterStageRobotConfiguration config;
    private RobotCapabilities capabilities;
    private final SpotDetectionPipeline spikeDetector;
    private final Alliance alliance;
    private final StartSide startSide;
    private NovelMecanumDrive driver;
    private double currentAngle;
    private int correctionSpeed = 2;
    private BackdropAprilTagAligner aprilTagAligner;
    private Telemetry.Item angleTelemtry;


    public BaseProductionAuto(SpotDetectionPipeline spikeDetector, Alliance alliance, StartSide startSide, Pose2d startPosition) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
    }

    @Override
    public void initialize() {
        this.config = new CenterStageRobotConfiguration(this.hardwareMap);
        this.driver = new NovelMecanumDrive(this.config.fl, this.config.fr, this.config.bl, this.config.br, Constants.PRODUCTION_COEFFICIENTS);
        this.capabilities = new RobotCapabilities(this.config);
        this.config.webcam.open(this.spikeDetector);
        angleTelemtry = this.telemetry.addData("angle: ", currentAngle);
    }

    @Override
    public void run() {
        try {
            config.imu.resetYaw();
            SpotDetectionPipeline pipeline = (SpotDetectionPipeline) this.config.webcam.getPipeline();
            SpikePosition spikePosition;
            do {
                spikePosition = pipeline.getResult();
                sleep(100);
            } while (spikePosition == null);

            System.out.println(spikePosition);

            switch (spikePosition) {
                case LEFT:
                    System.out.println("left");
                    driveVertical(-30, 2);
                    sleep(500);
                    turnTo90();
                    driveVertical(6, 1);
                    dropPixel();
                    driveVertical(-6, 1);
                    driveHorizontal(-28, 2);
                    break;

                case RIGHT:
                    System.out.println("right");
                    driveVertical(-30, 2);
                    sleep(500);
                    turnToNeg90();
                    driveVertical(6, 1);
                    dropPixel();
                    driveVertical(-6, 1);
                    driveHorizontal(28, 2);
                    break;

                case CENTER:
                    System.out.println("center");
                    driveVertical(-47, 4);
                    dropPixel();
                    driveVertical(-9, 1);
                    break;
            }

            if (alliance == Alliance.RED) turnTo90();
            else turnToNeg90();

            // ensure that robot is locked on target
            System.out.println(config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

            if (startSide == StartSide.APRIL_TAG_SIDE)
            {
                driveVertical(-50, 3);
                if(alliance == Alliance.RED) {
                    turnTo90();
                }
                else{
                    turnToNeg90();
                }
            }

            switch (alliance) {
                case RED:
                    driveVertical(-40, 2);
                    turnTo90();
                    driveHorizontal(30, 2);
                    break;
                case BLUE:
                    driveVertical(-40, 2);
                    turnToNeg90();
                    driveHorizontal(-30, 2);
                    break;
            }
            this.aprilTagAligner =  new BackdropAprilTagAligner(driver, spikePosition, config.webcam,alliance,0,0);
            double apriltagX = aprilTagAligner.getTagX();
            driveHorizontal(apriltagX, apriltagX/10);
            capabilities.moveLiftToPosition(3500,0.9);




            config.imu.resetYaw();
        } catch (Throwable e) {
            System.out.println("Error Thrown!");
            e.printStackTrace();
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
    public void turnTo90()
    {
        double angle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        while(angle < 90 || angle > 120)
        {
            driver.setVelocity(new Vector3D(0,0,(90-Math.abs(angle))/2));
            angle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        }
    }
    public void turnToNeg90()
    {
        double angle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        while(angle > -90 || angle < -120)
        {
            driver.setVelocity(new Vector3D(0,0,(-90-Math.abs(angle))/2));
            angle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        }
    }
    public void absoluteRotateIMU(double degrees) throws InterruptedException {
        double error = degrees;

        while (Math.abs(error) > 1) {
            error = config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - degrees;
            this.driver.setVelocity(new Vector3D(0, 0, error));
        }

        this.driver.stop();


        /*currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        System.out.println(currentAngle);
        angleTelemtry.setValue(currentAngle);
        telemetry.update();
        if(Math.abs(currentAngle) < 90)
        {
            while(Math.abs(currentAngle) < Math.abs(degrees))
            {
                driver.setVelocity(new Vector3D(0,0,10*Math.signum(degrees)));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            }
            while(Math.abs(currentAngle) > Math.abs(degrees))
            {
                driver.setVelocity(new Vector3D(0,0,-2*Math.signum(degrees)));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            }
        }
        else if(Math.abs(currentAngle) > 90)
        {
            while(Math.abs(currentAngle) > Math.abs(degrees))
            {
                driver.setVelocity(new Vector3D(0,0,-10*Math.signum(degrees)));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            }
            while(Math.abs(currentAngle) < Math.abs(degrees))
            {
                driver.setVelocity(new Vector3D(0,0,2*Math.signum(degrees)));
                currentAngle = -config.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            }
        }
        this.driver.setVelocity(new Vector3D(0,0, 0));*/
    }


    public void dropPixel()
    {
        this.capabilities.dropPixel();
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
