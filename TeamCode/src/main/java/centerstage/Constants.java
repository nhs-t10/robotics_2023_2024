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
/* Motor Direction Overrides */ new double[] {  1,  -1,  1,  -1 }
            )
    );

    public static final Scalar RED_YCBCR_MIN = new Scalar(0, 113, 172);
    public static final Scalar RED_YCBCR_MAX = new Scalar(255, 90, 240);

    public static final Scalar BLUE_YCBCR_MIN = new Scalar(0, 172, 121);
    public static final Scalar BLUE_YCBCR_MAX = new Scalar(255, 240, 110);

    public static final double MOTOR_TICK_COUNT = 537.6;

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
