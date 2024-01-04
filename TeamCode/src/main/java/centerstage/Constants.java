package centerstage;

import com.pocolifo.robobase.Robot;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;

public class Constants {
    public static final Robot ROBOT = new Robot(
            -1,
            -1,
            -1,
            4096,
            null,
            false,
            new OmniDriveCoefficients( //FL, FR, BL, BR
/*Total for motor*/ new double[] {  1,   1,   -1,   1 }
            ));

    public static final double MOTOR_TICK_COUNT = ((((1+(46d/17d))) * (1+(46d/11d))) * 28);  // This equation is pulled straight from https://www.gobilda.com/5203-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-24mm-length-8mm-rex-shaft-312-rpm-3-3-5v-encoder/

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
    public static final double LIFT_MOTOR_TICK_COUNT = 5281.1;

    public static final double ROBOT_DIAMETER_IN = 17.341;
}
