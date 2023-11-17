package centerstage.auto;

import centerstage.Constants;
import centerstage.SpikePosition;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.vision.NovelColorBoxDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.pocolifo.robobase.vision.apriltag.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;

import static centerstage.Constants.ROBOT;

public class BaseProductionAuto extends AutonomousOpMode {
    private final NovelColorBoxDetection edgeDetection;
    private final Alliance alliance;
    private Webcam webcam;
    private AprilTagDetectionPipeline aprilTagDetectionPipeline;
    private CarWheels carWheels;

    public BaseProductionAuto(NovelColorBoxDetection edgeDetection, Alliance alliance) {
        this.edgeDetection = edgeDetection;
        this.alliance = alliance;
    }

    @Override
    public void initialize() {
        this.webcam = new Webcam(this.hardwareMap, "Webcam");
        this.webcam.open(this.edgeDetection);

        this.aprilTagDetectionPipeline = new AprilTagDetectionPipeline(
                Constants.APRIL_TAG_SIZE_METERS,
                Constants.C270_FOCAL_LENGTH_X,
                Constants.C270_FOCAL_LENGTH_Y,
                Constants.C270_OPTICAL_CENTER_X,
                Constants.C270_OPTICAL_CENTER_Y
        );

        this.carWheels = new CarWheels(
                hardwareMap,
                1120,
                10d,
                ROBOT,
                "FL",
                "FR",
                "BL",
                "BR",
                "FL"
        );
    }

    @Override
    public void run() {
        System.out.println("Running edge detection");

        try {
            while (this.edgeDetection.getResult() == SpikePosition.NOT_FOUND || this.edgeDetection.getResult() == null) {
                this.sleep(500);
                System.out.println("Finding spikes...");
            }

            System.out.println("Detected: " + this.edgeDetection.getResult());

//            this.carWheels.drive(25, 0.25, false);
//            this.carWheels.rotateClockwise(90, 0.25);
//            this.carWheels.drive(50, 0.25, false);
//
//            moveToTargetPosition(position);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void moveToTargetPosition(SpikePosition spikePosition) {
        AprilTagDetection targetAprilTag = this.aprilTagDetectionPipeline.getTargetAprilTagDetection(alliance, spikePosition);

        if (targetAprilTag != null) {
            System.out.println(targetAprilTag.id);
            System.out.println(targetAprilTag.center.x);
            System.out.println(targetAprilTag.center.y);

            while (targetAprilTag.center.x > 320) {
                this.carWheels.driveOmni(0, 0.5f, 0);
                targetAprilTag = aprilTagDetectionPipeline.getTargetAprilTagDetection(alliance, spikePosition);
            }
            while (targetAprilTag.center.x < 320) {
                this.carWheels.driveOmni(0, -0.5f, 0);
                targetAprilTag = aprilTagDetectionPipeline.getTargetAprilTagDetection(alliance, spikePosition);
            }
            this.carWheels.driveOmni(0,0,0);
            this.carWheels.close();
        }
    }
}
