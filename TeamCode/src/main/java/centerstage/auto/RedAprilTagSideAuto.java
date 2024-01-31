package centerstage.auto;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.Const;

@Autonomous
@Config
public class RedAprilTagSideAuto extends RedProductionAuto {
    public RedAprilTagSideAuto() {
        super(StartSide.APRIL_TAG_SIDE, Constants.START_POSE_RED_APRIL_TAG_SIDE);
    }
}
