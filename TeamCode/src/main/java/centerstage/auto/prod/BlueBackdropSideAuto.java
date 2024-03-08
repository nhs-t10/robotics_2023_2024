package centerstage.auto.prod;

import centerstage.StartSide;
import com.pocolifo.robobase.utils.Alliance;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BlueBackdropSideAuto extends ProductionAutonomous {
    public BlueBackdropSideAuto() {
        super(Alliance.BLUE, StartSide.BACKDROP_SIDE);
    }
}
