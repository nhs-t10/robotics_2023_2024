package centerstage.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
@Config
public class ServoTuner extends TeleOpOpMode {
    @Hardware(name = "SpikeClaw")
    public CRServo spikeDropper;

    public static double multi = 5;

    @Override
    public void initialize() {

    }

    @Override
    public void loop() {
        spikeDropper.setPower(this.gamepad1.right_trigger * multi - multi / 2);
        this.telemetry.addData("Spike", spikeDropper.getPower());
        this.telemetry.update();
    }
}
