package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.gamepad.*;
import com.pocolifo.robobase.novel.motion.NovelMecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Kearing 3")
public class KearingTeleop extends TeleOpOpMode {
    private RobotCapabilities capabilities;
    private NovelMecanumDrive driver;
    private GController gamepadController;
    private Telemetry.Item telemetryItem;
    private CenterStageRobotConfiguration c;
    private int position = 1;
    private static final double[] positions = {0.95, -0.748, -1};

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.capabilities = new RobotCapabilities(c);
        this.driver = new NovelMecanumDrive(c.fl, c.fr, c.bl, c.br, Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.gamepadController = new GController(this.gamepad1)
                .x.initialToggleState(true).ok()  // micro-movement
                .y.onPress(this.capabilities::launchAirplane).ok()
                .a.onToggleOn(this.capabilities::gripPixels).onToggleOff(this.capabilities::releasePixelGrip).ok()
                .rightTrigger.whileDown(this.capabilities::upLift).onRelease(this.capabilities::stopLift).ok()
                .leftTrigger.whileDown(this.capabilities::downLift).onRelease(this.capabilities::stopLift).ok()
                .rightBumper.onPress(() -> position = (position + 1) % 3).ok()
                .leftBumper.onPress(() -> {
                    position--;

                    if (position < 0) {
                        position = positions.length - 1;
                    }
                }).ok()
                .dpadUp.whileDown(() -> this.capabilities.runIntake(0.96)).onRelease(this.capabilities::stopIntakeOuttake).ok()
                .dpadDown.whileDown(() -> this.capabilities.runOuttake(0.96)).onRelease(this.capabilities::stopIntakeOuttake).ok();
        this.telemetryItem = this.telemetry.addData("position ", 0);
    }

    @Override
    public void loop() {
        this.capabilities.rotateContainer(positions[position]);
        this.telemetryItem.setValue(this.c.linearSlideRight.motor.getCurrentPosition());
        this.capabilities.update();
        this.gamepadController.update();
        this.driver.useGamepad(this.gamepad1, this.gamepadController.x.isToggled() ? 4 : 1);
    }
}
