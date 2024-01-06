package centerstage.auto;

import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous
public class RRAuto extends AutonomousOpMode {
    MecanumDrive mecanumDrive;

    @Override
    public void initialize() {
        this.mecanumDrive = new MecanumDrive(
                new NovelMotor(hardwareMap.get(DcMotorEx.class, "FL"), Constants.MOTOR_TICK_COUNT, 9.6, 1),
                new NovelMotor(hardwareMap.get(DcMotorEx.class, "FR"), Constants.MOTOR_TICK_COUNT, 9.6, 1),
                new NovelMotor(hardwareMap.get(DcMotorEx.class, "BL"), Constants.MOTOR_TICK_COUNT, 9.6, 1),
                new NovelMotor(hardwareMap.get(DcMotorEx.class, "BR"), Constants.MOTOR_TICK_COUNT, 9.6, 1),
                new OmniDriveCoefficients(new double[] {  -1,   -1,   1,   -1 })
        );
    }

    @Override
    public void run() {
        mecanumDrive.drive(24, 0.01);
    }
}
