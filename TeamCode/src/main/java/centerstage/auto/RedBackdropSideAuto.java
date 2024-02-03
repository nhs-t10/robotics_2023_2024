package centerstage.auto;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.StartSide;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.Const;

@Autonomous
@Config
public class RedBackdropSideAuto extends RedProductionAuto {
    public RedBackdropSideAuto() {
        super(StartSide.BACKDROP_SIDE, Constants.START_POSE_RED_BACKDROP_SIDE);
    }
}
