package centerstage.auto;

import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

public class RedProductionAuto extends BaseProductionAuto {
    public RedProductionAuto(StartSide startSide) {
        super(new NovelYCrCbDetection(2), Alliance.RED, startSide);
    }
}
