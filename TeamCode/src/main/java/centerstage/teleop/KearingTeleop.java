package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.*;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Kearing 3")
public class KearingTeleop extends TeleOpOpMode {
    private RobotCapabilities capabilities;
    private NovelMecanumDrive driver;
    private GamepadController gamepadController;
    private Telemetry.Item telemetryItem;
    private CenterStageRobotConfiguration c;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.capabilities = new RobotCapabilities(c);
        this.driver = new NovelMecanumDrive(c.fl, c.fr, c.bl, c.br, Constants.PRODUCTION_COEFFICIENTS);

        this.gamepadController = new GamepadController(this.gamepad1)
                .x(new NToggleable()) // micro-movement
                .y(new NPressable().onPress(this.capabilities::launchAirplane))
                .a(new NToggleable().onToggleOn(this.capabilities::gripPixels).onToggleOff(this.capabilities::releasePixelGrip))
                .rightTrigger(new NTrigger().duringPress(this.capabilities::upLift).onRelease(this.capabilities::stopLift))
                .leftTrigger(new NTrigger().duringPress(this.capabilities::downLift).onRelease(this.capabilities::stopLift))
                .rightBumper(new NPressable().onPress(() -> this.capabilities.rotateContainer(0.95)))
                .leftBumper(new NPressable().onPress(() -> this.capabilities.rotateContainer(-0.748)))
                .up(new NDownable().onDown(this.capabilities::runIntake).onRelease(this.capabilities::stopIntakeOuttake))
                .down(new NDownable().onDown(this.capabilities::runOuttake).onRelease(this.capabilities::stopIntakeOuttake));
        this.telemetryItem = this.telemetry.addData("position ", 0);
    }

    @Override
    public void loop() {
        this.telemetryItem.setValue(this.c.linearSlideRight.motor.getCurrentPosition());
        this.capabilities.update();
        this.gamepadController.update();
        this.driver.useGamepad(this.gamepad1, this.gamepadController.x() ? 4 : 1);
    }
}
