package centerstage.auto;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import centerstage.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class RedAprilTagSideAuto extends RedProductionAuto {
    public RedAprilTagSideAuto() {
        super(StartSide.APRIL_TAG_SIDE, Constants.StartPoses.START_POSE_RED_APRIL_TAG_SIDE);
    }
}
