package centerstage.auto;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import centerstage.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class BlueAprilTagSideAuto extends BlueProductionAuto {
    public BlueAprilTagSideAuto() {
        super(StartSide.APRIL_TAG_SIDE, Constants.StartPoses.START_POSE_BLUE_APRIL_TAG_SIDE);
    }
}
