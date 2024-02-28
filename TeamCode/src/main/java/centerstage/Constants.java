package centerstage;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.novel.OmniDriveCoefficients;

public class Constants {
    /**
     * Camera: C270
     * Units: Pixels
     * <a href="https://horus.readthedocs.io/en/release-0.2/source/scanner-components/camera.html">Source</a>
     */
    public static class Webcam {
        public static final double C270_FOCAL_LENGTH_X = 1430;
        public static final double C270_FOCAL_LENGTH_Y = 1430;
        public static final double C270_OPTICAL_CENTER_X = 480;
        public static final double C270_OPTICAL_CENTER_Y = 620;
        public static final int CAMERA_RES_WIDTH = 640;
        public static final int CAMERA_RES_HEIGHT = 480;
    }

    public static class Robot {
        public static final double ROBOT_DIAMETER_IN = 17.341;

        public static final double WHEEL_DIAMETER_IN = 11.8737360135;
        public static final double ACTUAL_DIAMETER_IN = 3.7795275590551185;
    }

    public static class TickCounts {
        public static final double LIFT_MOTOR_TICK_COUNT = 5281.1;
        public static final double MOVEMENT_MOTOR_TICK_COUNT = ((((1 + (46d / 17d))) * (1 + (46d / 11d))) * 28);  // This equation is pulled straight from https://www.gobilda.com/5203-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-24mm-length-8mm-rex-shaft-312-rpm-3-3-5v-encoder/
    }

    public static class Odometry {
        public static final double ODOMETRY_LATERAL_WHEEL_DISTANCE = 12;

        /**
         * Distance (in) from the perpendicular odometry wheel to the center of rotation of the robot.
         */
        public static final double ODOMETRY_ROTATIONAL_WHEEL_OFFSET = 7;

        public static final double ODOMETRY_WHEEL_DIAMETER_IN = 2.0;
        public static final double TICKS_PER_ODOMETRY_REVOLUTION = 8192;

    }

    public static class Coefficients {
        public static final OmniDriveCoefficients PRODUCTION_COEFFICIENTS = new OmniDriveCoefficients(new double[]{
                1, -1, 1, -1
        });

        public static final OmniDriveCoefficients SOFTWARE_ROBOT_COEFFICIENTS = new OmniDriveCoefficients(new double[]{
                -1, -1, 1, 1
        });

    }

    public static class StartPoses {
        public static Pose2d START_POSE_RED_BACKDROP_SIDE = new Pose2d(60, 12, 90); //TODO Configure starting positions

        public static Pose2d START_POSE_RED_APRIL_TAG_SIDE = new Pose2d(60, -36, 90); //TODO Configure starting positions

        public static Pose2d START_POSE_BLUE_BACKDROP_SIDE = new Pose2d(-60, 12, -90); //TODO Configure starting positions

        public static Pose2d START_POSE_BLUE_APRIL_TAG_SIDE = new Pose2d(-60, -36, -90); //TODO Configure starting positions

    }

    public static final double APRIL_TAG_SIZE_METERS = 0.0508;

}
