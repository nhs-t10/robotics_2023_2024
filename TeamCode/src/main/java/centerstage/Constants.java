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
            new OmniDriveCoefficients(
                    new double[] { -1, -1, -1, -1 },
                    new double[] { -1, 1,  1,  1 },
                    new double[] { -1,  1,  -1, -1 },
                    new double[] { 1,  1, -1,  1 }
            )
    );

    public static final double MOTOR_TICK_COUNT = 753.2d;
}
