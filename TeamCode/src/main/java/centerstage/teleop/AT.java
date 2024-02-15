package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.vision.AprilTagRetriever;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@TeleOp
@Config
public class AT extends TeleOpOpMode {
    private CenterStageRobotConfiguration c;
    private AprilTagRetriever retriever;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.retriever = new AprilTagRetriever(this.c.webcam);
    }

    @Override
    public void loop() {
        List<AprilTagDetection> detections = this.retriever.getDetections();
        this.telemetry.clearAll();

        for (int i = 0; i < detections.size(); i++) {
            AprilTagDetection detection = detections.get(i);
            this.telemetry.addLine("#" + i);
            this.telemetry.addLine().addData("x: ", detection.rawPose.x);
            this.telemetry.addLine().addData("y: ", detection.rawPose.y);
            this.telemetry.addLine().addData("range: ", detection.ftcPose.range);
            this.telemetry.addLine().addData("bearing: ", detection.ftcPose.bearing);
        }

        this.telemetry.update();
    }
}
