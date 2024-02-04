package centerstage.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.utils.Alliance;
import centerstage.StartSide;
import centerstage.vision.DynamicYCrCbDetection;

public class BlueProductionAuto extends BaseProductionAuto {
    public BlueProductionAuto(StartSide startSide, Pose2d startPosition) {
        super(new DynamicYCrCbDetection(false), Alliance.BLUE, startSide, startPosition);
    }
}
