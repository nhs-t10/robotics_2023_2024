package centerstage;

import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.novel.OdometryCoefficientSet;
import com.pocolifo.robobase.novel.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.hardware.NovelEncoder;
import com.pocolifo.robobase.novel.hardware.NovelMotor;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;
import com.pocolifo.robobase.utils.RobotConfiguration;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

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
    @Hardware(
            name = "FL",
            diameterIn = Constants.Robot.ACTUAL_DIAMETER_IN,
            ticksPerRevolution = Constants.TickCounts.MOVEMENT_MOTOR_TICK_COUNT,
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    )
    public NovelMotor fl;

    @Hardware(
            name = "FR",
            diameterIn = Constants.Robot.ACTUAL_DIAMETER_IN,
            ticksPerRevolution = Constants.TickCounts.MOVEMENT_MOTOR_TICK_COUNT,
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    )
    public NovelMotor fr;

    @Hardware(
            name = "BL",
            diameterIn = Constants.Robot.ACTUAL_DIAMETER_IN,
            ticksPerRevolution = Constants.TickCounts.MOVEMENT_MOTOR_TICK_COUNT,
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    )
    public NovelMotor bl;

    @Hardware(
            name = "BR",
            diameterIn = Constants.Robot.ACTUAL_DIAMETER_IN,
            ticksPerRevolution = Constants.TickCounts.MOVEMENT_MOTOR_TICK_COUNT,
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    )
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

        this.imu.initialize(
                new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                ))
        );
    }

    public NovelMecanumDriver createDriver(OmniDriveCoefficients coefficients) {
        return new NovelMecanumDriver(
                this.fl,
                this.fr,
                this.bl,
                this.br,
                this.imu,
                coefficients
        );
    }

    public NovelOdometry createOdometry() {
        return new NovelOdometry(
                new OdometryCoefficientSet(),
                new NovelEncoder(this.spinningIntake.motor, Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN, Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION),
                new NovelEncoder(this.linearSlideLeft.motor, Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN, Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION),
                new NovelEncoder(this.roller.motor, Constants.Odometry.ODOMETRY_WHEEL_DIAMETER_IN, Constants.Odometry.TICKS_PER_ODOMETRY_REVOLUTION)
        );
    }
}
