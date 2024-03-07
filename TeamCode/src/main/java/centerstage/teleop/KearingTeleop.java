package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.*;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "Kearing v4")
public class KearingTeleop extends TeleOpOpMode {
    private RobotCapabilities capabilities;
    private NovelMecanumDrive driver;
    private GamepadController gamepadController;
    private Telemetry.Item telemetryItem;
    private CenterStageRobotConfiguration c;
    private int position = 0;
    private static final double[] positions = {0.95, -0.748, -1};

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
                .rightBumper(new NPressable().onPress(() -> position = (position + 1) % 3))
                .leftBumper(new NPressable().onPress(() -> {
                    position--;

                    if (position < 0) {
                        position = positions.length-1;
                    }
                }))
                .up(new NDownable().onDown(this.capabilities::runIntake).onRelease(this.capabilities::stopIntakeOuttake))
                .down(new NDownable().onDown(this.capabilities::runOuttake).onRelease(this.capabilities::stopIntakeOuttake));
        this.telemetryItem = this.telemetry.addData("angle ", 0);
    }

    @Override
    public void loop() {
        this.capabilities.rotateContainer(positions[position]);
        this.telemetryItem.setValue(this.c.linearSlideRight.motor.getCurrentPosition());
        this.capabilities.update();
        this.gamepadController.update();
        this.driver.useGamepad(this.gamepad1, this.gamepadController.x() ? 4 : 1);

        YawPitchRollAngles angles = this.c.imu.getRobotYawPitchRollAngles();
        this.telemetryItem.setValue(angles.getYaw(AngleUnit.DEGREES));
    }
}
