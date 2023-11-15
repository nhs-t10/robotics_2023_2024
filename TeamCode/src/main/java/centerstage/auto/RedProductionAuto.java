package centerstage.auto;

import centerstage.Constants;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.vision.ColorFilterBoundingBoxPipeline;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@Autonomous(name = "Red " + BuildProperties.VERSION)
public class RedProductionAuto extends BaseProductionAuto {
    public RedProductionAuto() {
        super(
                new ColorFilterBoundingBoxPipeline(
//                        new Scalar(0, 0, 0),
                        new Scalar(150, 208, 88)
                ), Alliance.RED);
    }
}
