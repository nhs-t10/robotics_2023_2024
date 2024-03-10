package centerstage.teleop;

import android.annotation.SuppressLint;
import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.gamepad.*;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.LocalizationEngine;
import com.pocolifo.robobase.reconstructor.Pose;
import com.pocolifo.robobase.vision.AprilTagRetriever;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Kearing v4")
public class KearingTeleop extends TeleOpOpMode {
    private RobotCapabilities capabilities;
    private NovelMecanumDriver driver;
    private GController gamepadController;
    private CenterStageRobotConfiguration c;
    private int position = 1;
    private static final double[] positions = {0, 0.53};
    private Telemetry.Item gripState;
    private Telemetry.Item micromovementState;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.c.airplaneLauncher.setPosition(-1);
        this.capabilities = new RobotCapabilities(this.c);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.gripState = this.telemetry.addData("Grip ", "[---]");
        this.micromovementState = this.telemetry.addData("Micro ", "[---]");

        this.gamepadController = new GController(this.gamepad1)
                .x.initialToggleState(true).ok()  // micro-movement
                .y.onPress(this.capabilities::launchAirplane).ok()
                .a.onToggleOn(this.capabilities::gripPixels).onToggleOff(this.capabilities::releasePixelGrip).ok()
                .rightTrigger.whileDown(this.capabilities::upLift).onRelease(this.capabilities::stopLift).ok()
                .leftTrigger.whileDown(this.capabilities::downLift).onRelease(this.capabilities::stopLift).ok()
                .rightBumper.onPress(() -> position = (position + 1) % positions.length).ok()
                .leftBumper.onPress(() -> {
                    position--;

                    if (position < 0) {
                        position = positions.length - 1;
                    }
                }).ok()
                .dpadUp.whileDown(() -> this.capabilities.runIntake(0.96)).onRelease(this.capabilities::stopIntakeOuttake).ok()
                .dpadDown.whileDown(() -> this.capabilities.runOuttake(0.5d)).onRelease(this.capabilities::stopIntakeOuttake).ok();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void loop() {
        this.capabilities.update();
        this.capabilities.rotateContainer(positions[position]);
        this.gamepadController.update();
        this.driver.useGamepad(this.gamepad1, this.gamepadController.x.isToggled() ? 4 : 1);
        this.gripState.setValue(this.c.containerPixelHolder.getPosition() == 0.0 ? "[ON]" : "[off]");
        this.micromovementState.setValue(this.gamepadController.x.isToggled() ? "[ON]" : "[off]");
        this.telemetry.update();
    }
}
