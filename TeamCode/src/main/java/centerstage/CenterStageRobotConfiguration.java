package centerstage;

import com.pocolifo.robobase.RobotConfiguration;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.hardware.*;

public class CenterStageRobotConfiguration extends RobotConfiguration {
    @Hardware(name = "Webcam")
    public Webcam webcam;

    /***************\
    |* CONTROL HUB *|
    \***************/

    // Wheels
    // NOTE: LinearSlideLeft has the perpendicular  odometer encoder
    @Hardware(name = "LinearSlideLeft")
    public NovelMotor linearSlideLeft;

    @Hardware(name = "LinearSlideRight")
    public NovelMotor linearSlideRight;

    // NOTE: LinearSlideLeft has the right odometer encoder
    @Hardware(name = "SpinningIntake")
    public NovelMotor spinningIntake;

    // NOTE: LinearSlideLeft has the left odometer encoder
    @Hardware(name = "Roller")
    public NovelMotor roller;

    // Servos
    @Hardware(name = "AirplaneLauncher")
    public Servo airplaneLauncher;

    /*****************\
    |* EXPANSION HUB *|
    \*****************/

    // Wheels
    @Hardware(name = "FL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fl;

    @Hardware(name = "FR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fr;

    @Hardware(name = "BL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor bl;

    @Hardware(name = "BR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor br;

    // Servos
    @Hardware(name = "ContainerPixelHolder")
    public Servo containerPixelHolder;

    @Hardware(name = "ContainerRotationLeft")
    // NOTE: The left servo is able to rotate the container downwards from halfway, but not upwards from halfway.
    public Servo containerRotationLeft;

    @Hardware(name = "ContainerRotationRight")
    // NOTE: The right servo is able to rotate the container upwards from halfway, but not downwards from halfway.
    public Servo containerRotationRight;

    @Hardware(name = "imu")
    public IMU imu;

    public CenterStageRobotConfiguration(HardwareMap hardwareMap) {
        super(hardwareMap);
    }
}
