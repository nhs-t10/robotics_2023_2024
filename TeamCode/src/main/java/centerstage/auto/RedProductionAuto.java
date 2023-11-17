package centerstage.auto;

import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.vision.NovelColorBoxDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.opencv.core.Scalar;

@Autonomous(name = "Red " + BuildProperties.VERSION)
public class RedProductionAuto extends BaseProductionAuto {
    public RedProductionAuto() {
        super(
                new NovelColorBoxDetection(
                        new double[] { 100, 100, 255 }
                ), Alliance.RED);
        // rgb (255, 100, 100)
        // bgr (100, 100, 255)

    }
}
