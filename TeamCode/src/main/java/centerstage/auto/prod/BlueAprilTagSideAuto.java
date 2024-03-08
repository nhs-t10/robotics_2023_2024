package centerstage.auto.prod;

import centerstage.StartSide;
import com.pocolifo.robobase.utils.Alliance;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BlueAprilTagSideAuto extends ProductionAutonomous {
    public BlueAprilTagSideAuto() {
        super(Alliance.BLUE, StartSide.APRIL_TAG_SIDE);
    }
}
