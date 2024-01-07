package centerstage.auto;

import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class BaseProductionAuto extends AutonomousOpMode {
    @Hardware(name = "Webcam")
    public Webcam webcam;

    @Hardware(name = "FL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fl;

    @Hardware(name = "FR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fr;

    @Hardware(name = "BL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor bl;

    @Hardware(name = "BR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor br;

    @Hardware(name = "Lift", ticksPerRevolution = Constants.MOTOR_TICK_COUNT)
    public DcMotorEx liftMotor;

    @Hardware(name = "AirplaneLauncher")
    public CRServo airplaneLauncher;

    @Hardware(name = "PixelDropper")
    public CRServo pixelDropper;

    @Hardware(name = "ClawGrip")
    public CRServo clawGrip;

    @Hardware(name = "ClawRotation")
    public CRServo clawRotation;
    private RobotCapabilities capabilities;

    private final NovelYCrCbDetection spikeDetector;
    private final Alliance alliance;
    private final StartSide startSide;
    private NovelMecanumDrive driver;

    public BaseProductionAuto(NovelYCrCbDetection spikeDetector, Alliance alliance, StartSide startSide) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
    }

    @Override
    public void initialize() {
        this.webcam.open(this.spikeDetector);
        this.driver = new NovelMecanumDrive(this.fl, this.fr, this.bl, this.br, new OmniDriveCoefficients(new double[]{1, 1, -1, 1}));
        this.capabilities = new RobotCapabilities(this.clawGrip, this.clawRotation, this.airplaneLauncher, this.liftMotor, this.pixelDropper);
    }

    @Override
    public void run() {
        try {
            NovelYCrCbDetection pipeline = (NovelYCrCbDetection) this.webcam.getPipeline();
            SpikePosition spikePosition;

            do {
                spikePosition = pipeline.getResult();
                sleep(100);
            } while (spikePosition == null);

            this.webcam.close();

            switch (spikePosition) {
                case LEFT:
                    driveVertical(-26, 2);
                    sleep(500);
                    driveHorizontal(-16, 1);
                    break;

                case RIGHT:
                    driveVertical(-26, 2);
                    sleep(500);
                    driveHorizontal(16, 1);
                    break;

                case CENTER:
                    driveVertical(-34, 2.5);
                    break;
            }

            this.capabilities.dropAutoPixel();

//            switch (spikePosition) {
//                case LEFT:
//                    driveHorizontal(16, 1);
//                    sleep(500);
//                    driveVertical(-6, 0.25);
//                    break;
//
//                case RIGHT:
//                    driveHorizontal(-16, 1);
//                    sleep(500);
//                    driveVertical(-6, 0.25);
//                    break;
//            }
//
//            driveVertical(-20, 1.5);
//            sleep(500);
//
//            if (this.startSide == StartSide.FRONT_SIDE) {
//                driveHorizontal(110, 9);
//            } else if (this.startSide == StartSide.BACKDROP_SIDE) {
//                driveHorizontal(60, 6);
//            }


//            if (this.startSide == StartSide.BACKDROP_SIDE) {
//                driveHorizontal();
//            }

        } catch (Throwable e) {
            System.out.println("Stopped");
        }

//        //todo: check this is properly aligned after going back
//        switch(spikePosition) {
//            case LEFT:
//                driveHorizontal(16,1);
//                break;
//
//            case RIGHT:
//                driveHorizontal(-16,1);
//                break;
//            case CENTER:
//                driveVertical(7,1);
//                break;
//        }
//        sleep(500);
//        rotate(90*allianceMultConstant,1);
//        sleep(500);
//        if(startSide == StartSide.BACKDROP_SIDE) {
//            driveVertical(80, 6);
//        }
//        else {
//            driveVertical(40, 3);
//        }
//        //todo: place pixels!
//        if(startSide == StartSide.BACKDROP_SIDE)
//        {
//            driveHorizontal(24*allianceMultConstant, 2);
//        }
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
        this.driver.setVelocity(new Vector3D(0,0,(Math.toRadians(degrees)*(Constants.ROBOT_DIAMETER_IN/2))/time));
        sleep((long)time*1000);
        this.driver.stop();
    }
}
