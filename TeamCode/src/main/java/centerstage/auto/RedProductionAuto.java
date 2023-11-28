package centerstage.auto;

import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red " + BuildProperties.VERSION)
public class RedProductionAuto extends BaseProductionAuto {
    public RedProductionAuto() {
        super(new NovelYCrCbDetection(2), Alliance.RED);
    }
}
