package centerstage.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.vision.ConvolutionalColorDetection;
import com.pocolifo.robobase.vision.DynamicYCrCbDetection;
import com.pocolifo.robobase.vision.SpotDetectionPipeline;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class PipelineTester extends AutonomousOpMode {
    public PipelineTester() {

    }

    @Override
    public void initialize() {
        PipelineTesterConfig config = new PipelineTesterConfig(this.hardwareMap);
        SpotDetectionPipeline spikeDetectionPipeline = new SpotDetectionPipeline(Alliance.BLUE);
        config.webcam.open(spikeDetectionPipeline);
    }

    @Override
    public void run() {

    }
}
