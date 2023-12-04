package centerstage;

import com.pocolifo.robobase.bootstrap.BootstrappedOpMode;
import com.pocolifo.robobase.movement.mecanum.MecanumDrive;
import com.pocolifo.robobase.movement.mecanum.MecanumWheels;

public class Constants {
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
    public static final double MOTOR_DIAMETER_CM = 9.6;

    public static MecanumDrive createDriver(BootstrappedOpMode opMode) {
        return new MecanumDrive(
                new MecanumWheels(opMode.hardwareMap, "FL", "FR", "BL", "BR", Constants.MOTOR_TICK_COUNT, Constants.MOTOR_DIAMETER_CM),
                opMode.imu
        );
    }
}
