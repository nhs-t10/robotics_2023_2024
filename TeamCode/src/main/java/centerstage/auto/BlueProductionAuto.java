package centerstage.auto;

import centerstage.Constants;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue " + BuildProperties.VERSION)
public class BlueProductionAuto extends BaseProductionAuto {
    public BlueProductionAuto() {
        super(new EdgeDetection(Constants.BLUE_YCBCR_MIN, Constants.BLUE_YCBCR_MAX));
    }
}
