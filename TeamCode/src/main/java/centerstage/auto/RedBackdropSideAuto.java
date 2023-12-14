package centerstage.auto;

import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class RedBackdropSideAuto extends RedProductionAuto {
    public RedBackdropSideAuto() {
        super(StartSide.BACKDROP_SIDE);
    }
}
