package centerstage.auto;

import centerstage.Constants;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red " + BuildProperties.VERSION)
public class RedProductionAuto extends BaseProductionAuto {
    public RedProductionAuto() {
        super(new EdgeDetection(Constants.RED_YCBCR_MIN, Constants.RED_YCBCR_MAX));
    }
}
