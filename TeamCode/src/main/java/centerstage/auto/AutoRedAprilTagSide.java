package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import centerstage.vision.DynamicYCrCbDetection;

import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.utils.Alliance;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous
@Config
public class AutoRedAprilTagSide extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private RobotCapabilities capabilities;
    private NovelMecanumDriver driver;
    private DynamicYCrCbDetection spikeDetector;
    private Alliance alliance = Alliance.BLUE;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.capabilities = new RobotCapabilities(this.c);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.spikeDetector = new DynamicYCrCbDetection(alliance);
        this.c.webcam.open(spikeDetector);
    }

    @Override
    public void run() {
        boolean isRed = alliance == Alliance.RED;
        try {
            SpikePosition spikePos = getSpikePosition();

            driveVertical((12 + 12 + 9), 1);

            switch (spikePos) {
                case RIGHT:
                    rotateIMU(90);
                    break;
                case CENTER:
                    driveVertical(25, 3);
                    break;
                case LEFT:
                    rotateIMU(-90);
                    break;
            }
            driveVertical(-5, 2);

            this.capabilities.runRoller(-1);
            sleep(1000);
            this.capabilities.runRoller(0);

            driveVertical(5, 0.5);

            switch (spikePos) {
                case RIGHT:
                    rotateIMU(-90);
                    break;
                case LEFT:
                    rotateIMU(90);
                    break;
            }

            driveVertical(12 - 5, 1);

            rotateIMU(-90);
            driveVertical(84, 5);
            rotateIMU(90);
            driveVertical(18, 3);
            rotateIMU(-90);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SpikePosition getSpikePosition() throws InterruptedException {
        DynamicYCrCbDetection pipeline = (DynamicYCrCbDetection) this.c.webcam.getPipeline();
        SpikePosition spikePosition;
        do {
            spikePosition = pipeline.getResult();
            sleep(100);
        } while (spikePosition == null);
        System.out.println(spikePosition.toString());
        return spikePosition;
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
}