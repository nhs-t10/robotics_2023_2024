package centerstage.auto;

import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BlueAprilTagSideAuto extends BlueProductionAuto {
    public BlueAprilTagSideAuto() {
        super(StartSide.FRONT_SIDE);
    }
}
