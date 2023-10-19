package centerstage.auto;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.vision.Webcam;
import com.pocolifo.robobase.vision.apriltag.AprilTagDetectionPipeline;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.Arrays;

import centerstage.Constants;
import centerstage.SpikePosition;

@Autonomous(name = "AprilTagTesting " + BuildProperties.VERSION)
public class AprilTagTestingAuto extends AutonomousOpMode {

    private Webcam webcam;
    private AprilTagDetectionPipeline aprilTagDetectionPipeline;
    private final double TAG_SIZE_METERS = 0.0508;


    @Override
    public void initialize() {
        this.webcam = new Webcam(this.hardwareMap, "Webcam");
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(TAG_SIZE_METERS, Constants.C270_FOCAL_LENGTH_X, Constants.C270_FOCAL_LENGTH_Y, Constants.C270_OPTICAL_CENTER_X, Constants.C270_OPTICAL_CENTER_Y);
        this.webcam.open(aprilTagDetectionPipeline);
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
            System.out.println(this.aprilTagDetectionPipeline.getResult());

            System.out.println("Closing!");
            this.webcam.close();

        } catch (InterruptedException ignored) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
