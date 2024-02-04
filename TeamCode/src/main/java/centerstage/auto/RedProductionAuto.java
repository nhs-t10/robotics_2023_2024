package centerstage.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.utils.Alliance;
import centerstage.StartSide;
import centerstage.vision.DynamicYCrCbDetection;

public class RedProductionAuto extends BaseProductionAuto {
    public RedProductionAuto(StartSide startSide, Pose2d startPosition) {
        super(new DynamicYCrCbDetection(true), Alliance.RED, startSide, startPosition);
    }
}
