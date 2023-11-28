package centerstage;

import centerstage.auto.RedProductionAuto;
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
/*Total for motor*/ new double[] {  1,   1,   1,   1 },
/*Forward/backward*/new double[] {  1,  -1,   1,  -1 },
/*side-to-side*/    new double[] { -1,  -1,   1,   1 },
/*rotational*/      new double[] { -1,  -1,  -1,  -1 }
            )
    );

    public static final double MOTOR_TICK_COUNT = 537.7;

    // Camera: C270
    // Units: Pixels
    // Source: https://horus.readthedocs.io/en/release-0.2/source/scanner-components/camera.html
    public static final double C270_FOCAL_LENGTH_X = 1430;
    public static final double C270_FOCAL_LENGTH_Y = 1430;
    public static final double C270_OPTICAL_CENTER_X = 480;
    public static final double C270_OPTICAL_CENTER_Y = 620;

    public static final double APRIL_TAG_SIZE_METERS = 0.0508;

    public static final int CAMERA_X_EDGE_DETECTION_OFFSET = 0;
    public static final int CAMERA_RES_WIDTH = 640;
    public static final int CAMERA_RES_HEIGHT = 480;
}
