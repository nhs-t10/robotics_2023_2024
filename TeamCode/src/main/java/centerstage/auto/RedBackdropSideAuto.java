package centerstage.auto;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import centerstage.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class RedBackdropSideAuto extends RedProductionAuto {
    public RedBackdropSideAuto() {
        super(StartSide.BACKDROP_SIDE, Constants.StartPoses.START_POSE_RED_BACKDROP_SIDE);
    }
}
