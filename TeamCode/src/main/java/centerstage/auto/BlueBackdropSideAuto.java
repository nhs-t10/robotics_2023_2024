package centerstage.auto;

import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BlueBackdropSideAuto extends BlueProductionAuto {
    public BlueBackdropSideAuto() {
        super(StartSide.BACKDROP_SIDE);
    }
}
