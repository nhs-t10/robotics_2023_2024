package centerstage.teleop;

import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadMecanumDrive;
import com.pocolifo.robobase.movement.mecanum.MecanumDrive;
import com.pocolifo.robobase.movement.mecanum.MecanumWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Kearing II")
public class KearingTeleop extends TeleOpOpMode {
    private MecanumDrive driver;
    private GamepadMecanumDrive gamepadMecanumDrive;


    @Override
    public void initialize() {
        this.driver = Constants.createDriver(this);
        this.gamepadMecanumDrive = new GamepadMecanumDrive(this.driver, this.gamepad1);
    }

    @Override
    public void loop() {
        this.gamepadMecanumDrive.update();
    }
}
