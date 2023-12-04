/*package centerstage.auto;

import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.vision.Webcam;
import com.pocolifo.robobase.vision.apriltag.AprilTagDetectionPipeline;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.ArrayList;
import java.util.Arrays;

import centerstage.Constants;
import centerstage.SpikePosition;

import static centerstage.Constants.ROBOT;

@Autonomous(name = "AprilTagTesting " + BuildProperties.VERSION)
public class AprilTagTestingAuto extends AutonomousOpMode {

    private Webcam webcam;
    private AprilTagDetectionPipeline aprilTagDetectionPipeline;
    private final double TAG_SIZE_METERS = 0.0508;
    private SpikePosition spikePosition;
    private Alliance alliance;
    private AprilTagDetection targetAprilTag;
    private CarWheels carWheels;


    @Override
    public void initialize() {
        this.webcam = new Webcam(this.hardwareMap, "Webcam");
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(TAG_SIZE_METERS, Constants.C270_FOCAL_LENGTH_X, Constants.C270_FOCAL_LENGTH_Y, Constants.C270_OPTICAL_CENTER_X, Constants.C270_OPTICAL_CENTER_Y);
        this.webcam.open(aprilTagDetectionPipeline);

        this.spikePosition = SpikePosition.CENTER;

        this.alliance = Alliance.RED;

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
    public void run()
    {
        try {
            while (this.aprilTagDetectionPipeline.getResult() == null) {
                this.sleep(500);
                System.out.println("Finding AprilTags...");
            }

            System.out.println("Detected");
            moveToTargetPosition();
            System.out.println("Closing!");
            this.webcam.close();

        } catch (InterruptedException ignored) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void moveToTargetPosition() {
        targetAprilTag = this.aprilTagDetectionPipeline.getTargetAprilTagDetection(alliance, spikePosition);
        if (targetAprilTag != null) {
            System.out.println(targetAprilTag.id);
            System.out.println(targetAprilTag.center.horizontalCm);
            System.out.println(targetAprilTag.center.verticalCm);

            while (targetAprilTag.center.horizontalCm > 320) {
                this.carWheels.driveOmni(0, 0.5f, 0);
                targetAprilTag = aprilTagDetectionPipeline.getTargetAprilTagDetection(alliance, spikePosition);
            }
            while (targetAprilTag.center.horizontalCm < 320) {
                this.carWheels.driveOmni(0, -0.5f, 0);
                targetAprilTag = aprilTagDetectionPipeline.getTargetAprilTagDetection(alliance, spikePosition);
            }
            this.carWheels.driveOmni(0,0,0);
            this.carWheels.close();
        }
    }
}
*/