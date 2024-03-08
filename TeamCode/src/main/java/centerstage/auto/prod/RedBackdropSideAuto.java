package centerstage.auto.prod;

import centerstage.StartSide;
import com.pocolifo.robobase.utils.Alliance;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class RedBackdropSideAuto extends ProductionAutonomous {
    public RedBackdropSideAuto() {
        super(Alliance.RED, StartSide.BACKDROP_SIDE);
    }
}
