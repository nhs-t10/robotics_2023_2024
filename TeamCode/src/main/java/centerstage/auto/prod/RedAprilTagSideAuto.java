package centerstage.auto.prod;

import centerstage.StartSide;
import com.pocolifo.robobase.utils.Alliance;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class RedAprilTagSideAuto extends ProductionAutonomous {
    public RedAprilTagSideAuto() {
        super(Alliance.RED, StartSide.APRIL_TAG_SIDE);
    }
}
