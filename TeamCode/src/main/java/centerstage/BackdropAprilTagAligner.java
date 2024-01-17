package centerstage;

import android.util.Size;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.Webcam;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Optional;

public class BackdropAprilTagAligner implements AutoCloseable {
    private final NovelMecanumDrive driver;
    private final int aprilTagId;
    private final int webcamCenterX;
    private final int pixelErrorThreshold;
    private final double alignmentSpeed;
    private final AprilTagProcessor aprilTagProcessor;
    private final VisionPortal aprilTagVisionPortal;
    private AlignmentStatus status;

    public BackdropAprilTagAligner(NovelMecanumDrive driver, SpikePosition detectedPosition, Webcam webcam, Alliance alliance, int pixelErrorThreshold, double alignmentSpeed) {
        this.driver = driver;
        this.aprilTagId = alliance.getAprilTagIDForAlliance(detectedPosition);
        this.webcamCenterX = Constants.CAMERA_RES_WIDTH / 2;
        this.aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)  // inches and degrees match the april tag library metadata
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();
        this.aprilTagVisionPortal = new VisionPortal.Builder()
                .setCameraResolution(new Size(Constants.CAMERA_RES_WIDTH, Constants.CAMERA_RES_HEIGHT))
                .setCamera(webcam.webcamDevice)
                .addProcessor(this.aprilTagProcessor)
                .enableLiveView(false)
                .build();
        this.pixelErrorThreshold = pixelErrorThreshold;
        this.alignmentSpeed = alignmentSpeed;
    }

    @Override
    public void close() {
        this.aprilTagVisionPortal.close();
    }

    public void updateAlignment() {
        Optional<AprilTagDetection> possibleTag = this.aprilTagProcessor.getDetections().stream().filter(detection -> detection.id == this.aprilTagId).findFirst();

        if (!possibleTag.isPresent()) {
            this.status = AlignmentStatus.TAGS_NOT_FOUND;
            return;
        }

        this.status = AlignmentStatus.ALIGNING;

        AprilTagDetection tag = possibleTag.get();
        double tagCenterX = tag.center.x;

        if (this.webcamCenterX - this.pixelErrorThreshold > tagCenterX) {
            // move right
            this.driver.setVelocity(new Vector3D(0, -this.alignmentSpeed, 0));
        } else if (this.webcamCenterX + this.pixelErrorThreshold < tagCenterX) {
            // move left
            this.driver.setVelocity(new Vector3D(0, this.alignmentSpeed, 0));
        } else {
            this.status = AlignmentStatus.ALIGNMENT_COMPLETE;
            this.driver.stop();
        }
    }

    public AlignmentStatus getStatus() {
        return status;
    }

    public boolean isNotAligned() {
        return status != AlignmentStatus.ALIGNMENT_COMPLETE;
    }

    public enum AlignmentStatus {
        TAGS_NOT_FOUND,
        ALIGNING,
        ALIGNMENT_COMPLETE
    }
}
