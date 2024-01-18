package centerstage.auto;

import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class BlueAprilTagSideAuto extends BlueProductionAuto {
    public BlueAprilTagSideAuto() {
        super(StartSide.APRIL_TAG_SIDE);
    }
}
