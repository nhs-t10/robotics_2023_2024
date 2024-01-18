package centerstage.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
@Config
public class ServoTuner extends TeleOpOpMode {
    @Hardware(name = "Airplane")
    public CRServo ap;

    public static double multi = 5;

    @Override
    public void initialize() {
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        ap.setPower(this.gamepad1.right_trigger * multi - multi / 2);
        this.telemetry.addData("ap ", ap.getPower());
        this.telemetry.update();
    }
}
