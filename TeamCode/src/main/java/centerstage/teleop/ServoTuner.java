package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class ServoTuner extends TeleOpOpMode {
    private CenterStageRobotConfiguration c;
    Telemetry.Item line;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        line = this.telemetry.addData("val", 0);
    }

    @Override
    public void loop() {
        this.c.containerPixelHolder.setPosition(this.gamepad1.right_trigger * 2 - 1);
        line.setValue(this.c.containerPixelHolder.getPosition());
        this.telemetry.update();
    }
}
