package centerstage.auto;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class BlueBackdropSideAuto extends BlueProductionAuto {
    public BlueBackdropSideAuto() {
        super(StartSide.BACKDROP_SIDE, Constants.START_POSE_BLUE_BACKDROP_SIDE);
    }
}
