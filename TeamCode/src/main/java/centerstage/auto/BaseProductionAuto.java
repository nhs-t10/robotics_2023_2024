package centerstage.auto;

import centerstage.Constants;
import centerstage.SpikePosition;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.motor.Motor;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.pocolifo.robobase.vision.apriltag.AprilTagDetectionPipeline;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.openftc.apriltag.AprilTagDetection;

import static centerstage.Constants.ROBOT;

public class BaseProductionAuto extends AutonomousOpMode {
    private final NovelYCrCbDetection edgeDetection;
    private final Alliance alliance;
    private Webcam webcam;
//    private AprilTagDetectionPipeline aprilTagDetectionPipeline;
    private CarWheels carWheels;
    private final StartSide startSide;

    public BaseProductionAuto(NovelYCrCbDetection edgeDetection, Alliance alliance, StartSide startSide) {
        this.edgeDetection = edgeDetection;
        this.alliance = alliance;
        this.startSide = startSide;
    }

    @Override
    public void initialize() {
        this.webcam = new Webcam(this.hardwareMap, "Webcam");
        this.webcam.open(this.edgeDetection);

//        this.aprilTagDetectionPipeline = new AprilTagDetectionPipeline(
//                Constants.APRIL_TAG_SIZE_METERS,
//                Constants.C270_FOCAL_LENGTH_X,
//                Constants.C270_FOCAL_LENGTH_Y,
//                Constants.C270_OPTICAL_CENTER_X,
//                Constants.C270_OPTICAL_CENTER_Y
//        );

        this.carWheels = new CarWheels(
                hardwareMap,
                Constants.MOTOR_TICK_COUNT,
                10d,
                ROBOT,
                "FL",
                "FR",
                "BL",
                "BR",
                "FL"
        );

//        this.outtakeServo = this.hardwareMap.get(CRServo.class, "Outtake");
//        this.rotationLeftServo = this.hardwareMap.get(CRServo.class, "RotationLeft");
//        this.rotationRightServo = this.hardwareMap.get(CRServo.class, "RotationRight");
//
//        this.spinningIntake = new Motor(this.hardwareMap.get(DcMotor.class, "SpinningIntake"), Constants.MOTOR_TICK_COUNT); // Port 0 Motor Expansion Hub
//        this.leftLinearSlide = new Motor(this.hardwareMap.get(DcMotor.class, "LeftLinearSlide"), Constants.MOTOR_TICK_COUNT); // Port 1 Motor Expansion Hub
//        this.rightLinearSlide = new Motor(this.hardwareMap.get(DcMotor.class, "RightLinearSlide"), Constants.MOTOR_TICK_COUNT); // Port 1 Motor Expansion Hub
    }

    @Override
    public void run() {
        System.out.println("Beginning Movement");
        moveToTargetPosition(edgeDetection.getResult());
//        carWheels.driveOmni(1,0,0);
//        sleep(2000);
//        carWheels.driveOmni(0,0,0);
//        carWheels.close();

        carWheels.drive(45, 1, false);
        carWheels.driveOmni(0, 0, 0);

        System.out.println("Completed Movement");
    }

    public void moveToTargetPosition(SpikePosition spikePosition) {
        carWheels.drive(50, false);
        switch (spikePosition) {
            case RIGHT:
                carWheels.rotateClockwise(90, 0.5);
                break;
            case LEFT:
                carWheels.rotateCounterclockwise(90, 0.5);
                break;
        }
        //TODO: Drop Pixel Here

        if (this.startSide == StartSide.NEAR) {
            switch (alliance) {
                case RED:
                    carWheels.drive(50, true);
                    break;
                case BLUE:
                    carWheels.drive(-50, true);
                    break;
            }
        }
    }
}
