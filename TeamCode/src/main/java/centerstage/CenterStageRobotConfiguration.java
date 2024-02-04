package centerstage;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.novel.OdometryCoefficientSet;
import com.pocolifo.robobase.novel.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDrive;
import com.pocolifo.robobase.utils.RobotConfiguration;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.novel.hardware.NovelMotor;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
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

        imu.initialize(
                new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                ))
        );
    }

    public NovelMecanumDrive createDriver(OmniDriveCoefficients coefficients) {
        return new NovelMecanumDrive(fl, fr, bl, br, coefficients);
    }

    public NovelOdometry createOdometry(Pose2d startPose) {
        return new NovelOdometry(startPose, new OdometryCoefficientSet(), linearSlideRight.motor, roller.motor, linearSlideLeft.motor);
    }
}
