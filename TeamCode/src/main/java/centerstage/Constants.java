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
                    // NOTE: Documentation may be wrong. Tinker at your own peril.
/*Total for motor*/ new double[] { -1, -1, -1, 1 },
/*Forward/backward*/new double[] { -1,  1,  1,  1 },
/*side-to-side*/    new double[] { 1,  1, 1, -1 },
/*rotational*/      new double[] { 1,  1, -1,  1 }
            )
    );

    public static final Scalar RED_YCBCR_MIN = new Scalar(100, 135, 135);
    public static final Scalar RED_YCBCR_MAX = new Scalar(255, 170, 170);

    public static final Scalar BLUE_YCBCR_MIN = new Scalar(0, 100, 120);
    public static final Scalar BLUE_YCBCR_MAX = new Scalar(255, 140, 160);

    public static final double MOTOR_TICK_COUNT = 753.2d;

    // Camera: C270
    // Units: Pixels
    // Source: https://horus.readthedocs.io/en/release-0.2/source/scanner-components/camera.html
    public static final double C270_FOCAL_LENGTH_X = 1430;
    public static final double C270_FOCAL_LENGTH_Y = 1430;
    public static final double C270_OPTICAL_CENTER_X = 480;
    public static final double C270_OPTICAL_CENTER_Y = 620;
}
