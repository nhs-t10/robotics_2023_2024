package centerstage.auto;

import android.os.SystemClock;

import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import centerstage.Constants;

@Autonomous(name = "RotateTest")
public class RotateTest extends AutonomousOpMode {
    @Hardware(name = "FL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fl;

    @Hardware(name = "FR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fr;

    @Hardware(name = "BL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor bl;

    @Hardware(name = "BR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor br;

    private NovelMecanumDrive driver;

    @Hardware(name = "imu")
    private IMU imu;
    private IMU.Parameters parameters;

    public RotateTest() {

    }

    @Override
    public void initialize() {
        imu = hardwareMap.get(IMU.class,"imu");
        this.driver = new NovelMecanumDrive(this.fl, this.fr, this.bl, this.br, new OmniDriveCoefficients(new double[]{1, -1, -1, -1}));
        parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
    }

    @Override
    public void run() {
        try {
            //rotateCompare(90,2);
            rotateIMU(90);
        }
        catch (Throwable e)
        {

        }
    }

    public void driveVertical(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(inches / time, 0, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }

    public void driveHorizontal(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(0, inches / time, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }
    public void rotate(double degrees, double time) throws InterruptedException {
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0,
                (Math.toRadians(degrees) * (Constants.ROBOT_DIAMETER_IN)/time)));
        sleep((long)time*1000);
        this.driver.stop();
    }
    public void rotateIMU(double degrees) throws InterruptedException {
        int direction = 1;
        if(degrees < 0)
        {
            direction = -1;
        }
        imu.resetYaw();
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0, 15*direction));

        while(Math.abs(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) < 90)
        {
            System.out.println(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        System.out.println("correcting..." + (imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - 90));
        this.driver.setVelocity(new Vector3D(0,0,-3*direction));
        while(Math.abs(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) > 90)
        {
            System.out.println(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        this.driver.stop();
    }

    public void rotateCompare(double degrees, double time)
    {
        imu.resetYaw();
        this.driver.setVelocity(new Vector3D(0,0, (Math.toRadians(degrees) * (Constants.ROBOT_DIAMETER_IN)/time)));
        SystemClock.sleep((long)time*1000/3);
        System.out.println("Turned 30 degrees");
        System.out.println("Imu: " + imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        SystemClock.sleep((long)time*1000/6);
        System.out.println("Turned 45 degrees");
        System.out.println("Imu: " + imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        SystemClock.sleep((long)time*1000/6);
        System.out.println("Turned 60 degrees");
        System.out.println("Imu: " + imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        SystemClock.sleep((long)time*1000/3);
        System.out.println("Turned 90 degrees");
        System.out.println("Imu: " + imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        this.driver.stop();
    }
}
