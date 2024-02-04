package centerstage.auto;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import centerstage.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class BlueBackdropSideAuto extends BlueProductionAuto {
    public BlueBackdropSideAuto() {
        super(StartSide.BACKDROP_SIDE, Constants.StartPoses.START_POSE_BLUE_BACKDROP_SIDE);
    }
}
