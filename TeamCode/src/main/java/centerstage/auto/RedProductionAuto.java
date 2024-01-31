package centerstage.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

public class RedProductionAuto extends BaseProductionAuto {
    public RedProductionAuto(StartSide startSide, Pose2d startPosition) {
        super(new NovelYCrCbDetection(2), Alliance.RED, startSide, startPosition);
    }
}
