package centerstage.auto;

import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.vision.NovelColorBoxDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.opencv.core.Scalar;

@Autonomous(name = "Blue " + BuildProperties.VERSION)
public class BlueProductionAuto extends BaseProductionAuto {
    public BlueProductionAuto() {
        super(
                new NovelColorBoxDetection(
                        new double[] { 215, 120, 100 }
                ), Alliance.RED);

        // rgb (100, 120, 215)
        // bgr (215, 120, 100)
    }
}
