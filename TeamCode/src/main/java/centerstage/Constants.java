package centerstage;

import com.pocolifo.robobase.Robot;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import org.opencv.core.Scalar;

public class Constants {
    public static final Robot ROBOT = new Robot(
            -1,
            -1,
            -1,
            4096,
            null,
            false,
            new OmniDriveCoefficients( //FL, FR, BL, BR
/*Total for motor*/ new double[] { -1, -1, -1, -1 },
/*Forward/backward*/new double[] { -1,  1,  1,  1 },
/*side-to-side*/    new double[] { 1,  1, 1, -1 },
/*rotational*/      new double[] { 1,  1, -1,  1 }
            )
    );

    public static final Scalar RED_ycbcr_MIN = new Scalar(166, 77, 63);
    public static final Scalar RED_ycbcr_MAX = new Scalar(75, 110, 172);

    public static final Scalar BLUE_ycbcr_MIN = new Scalar(0, 128, 96);
    public static final Scalar BLUE_ycbcr_MAX = new Scalar(128, 255, 128);

    public static final double MOTOR_TICK_COUNT = 753.2d;
}
