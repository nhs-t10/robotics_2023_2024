package centerstage.auto;

import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Config
public class RedAprilTagSideAuto extends RedProductionAuto {
    public RedAprilTagSideAuto() {
        super(StartSide.APRIL_TAG_SIDE);
    }
}
