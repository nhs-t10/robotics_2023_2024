package centerstage.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.vision.DynamicYCrCbDetection;
import com.pocolifo.robobase.vision.SpotDetectionPipeline;

public class BlueProductionAuto extends BaseProductionAuto {
    public BlueProductionAuto(StartSide startSide, Pose2d startPosition) {
        super(new SpotDetectionPipeline(Alliance.BLUE), Alliance.BLUE, startSide, startPosition);
    }
}
