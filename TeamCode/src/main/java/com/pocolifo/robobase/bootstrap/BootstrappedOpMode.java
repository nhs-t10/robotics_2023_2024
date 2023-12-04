package com.pocolifo.robobase.bootstrap;

import android.os.SystemClock;
import com.pocolifo.robobase.dashboard.DashboardHTTPServer;
import com.pocolifo.robobase.motor.Motor;
import com.pocolifo.robobase.motor.Wheel;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * <p>"Bootstrapping" is the preparing another program to initialize.
 * This OpMode allows RoboCore to initialize. Upon initialization of RoboCore,
 * - {@link System#out} and {@link System#err} become available</p>
 *
 * <p><strong>IMPORTANT!</strong> When extending this class and overriding {@link BootstrappedOpMode#init()},
 * make sure that {@code super.init()} is called!</p>
 *
 * @author youngermax
 * @see OpMode
 * @see System#out
 * @see System#err
 */
public abstract class BootstrappedOpMode extends OpMode {
    /**
     * The dashboard HTTP server.
     */
    protected DashboardHTTPServer dashboardHTTPServer;

    /**
     * The {@link BNO055IMU} if it is registered as {@code imu} in the {@link BootstrappedOpMode#hardwareMap}.
     * <p>
     * It is autoconfigured to use m/s/s and radians.
     */
    @Hardware(name = "imu")
    public BNO055IMU imu;

    /**
     * Sets {@link System#out} and {@link System#err} to an instance of {@link RobotDebugPrintStream}.
     * This allows {@link System#out} and {@link System#err} to be used for printing debug messages.
     *
     * @author youngermax
     * @see System#out
     * @see System#err
     */
    private void configureSystemOut() {
        System.setOut(new RobotDebugPrintStream(this.telemetry));
        System.setErr(new RobotDebugPrintStream(this.telemetry));
    }

    /**
     * Sets fields marked with @{@link Hardware} to their initialized value based on the {@link OpMode#hardwareMap}.
     * <p>
     * Example where a webcam and wheel are auto initialized using @{@link Hardware}. You would be able to use these
     * variables just like normal. RoboBase does the initialization for you.
     *
     * <pre>
     * {@code
     * @Hardware(name = "Webcam")
     * public Webcam webcam;
     *
     * @Hardware(name = "Chain", wheelDiameterCm = 9.6, ticksPerRevolution = 500, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT)
     * public Wheel chainWheel;
     * }
     * </pre>
     *
     * @throws IllegalAccessException A field has the wrong access. Fields must be {@code public} and not {@code final}.
     */
    private void configureHardwareVariables() throws IllegalAccessException {
        for (Field field : this.getClass().getFields()) {
            Hardware hardware = field.getAnnotation(Hardware.class);

            if (hardware != null) {
                String configName = hardware.name();
                Class<?> type = field.getType();
                Object o;

                if (type.equals(Webcam.class)) {
                    o = new Webcam(this.hardwareMap, configName);
                } else if (type.equals(Wheel.class)) {
                    // TODO: warn if ticksPerRevolution, wheelDiameterCm is a bad value, but don't assert (if less than 0)
                    o = new Wheel(this.hardwareMap.get(DcMotor.class, configName), hardware.ticksPerRevolution(), hardware.wheelDiameterCm());
                    ((Wheel) o).motor.setZeroPowerBehavior(hardware.zeroPowerBehavior());
                } else if (type.equals(Motor.class)) {
                    // TODO: warn if ticksPerRevolution, wheelDiameterCm is a bad value, but don't assert (if less than 0)
                    o = new Motor(this.hardwareMap.get(DcMotor.class, configName), hardware.ticksPerRevolution());
                    ((Motor) o).motor.setZeroPowerBehavior(hardware.zeroPowerBehavior());
                } else {
                    o = this.hardwareMap.get(field.getType(), configName);
                }

                field.setAccessible(true);
                field.set(this, o);
            }
        }
    }

    /**
     * Starts the dashboard.
     */
    private void configureDashboard() throws IOException {
        this.dashboardHTTPServer = new DashboardHTTPServer();
        this.dashboardHTTPServer.start();

        System.out.println("[bootstrap] [dashboard] running on http://" + this.dashboardHTTPServer.getHostname() + ":" + this.dashboardHTTPServer.getListeningPort());
    }

    private void configureIMU() {
        if (this.imu == null) {
            System.out.println("[bootstrap] BNO055IMU not found in hardware map as `imu`, so it cannot be autoconfigured");
            return;
        }

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        this.imu.initialize(parameters);
        this.imu.startAccelerationIntegration(
                new Position(DistanceUnit.CM, 0, 0, 0, 0),
                new Velocity(DistanceUnit.CM, 0, 0, 0, 0),
                10
        );
    }

    /**
     * Debug method that detects all devices and the runtime environment in general.
     *
     * @author youngermax
     */
    public void dumpEnvironment() {
        System.out.println("========== Environment: detected devices ==========");

        for (HardwareMap.DeviceMapping<? extends HardwareDevice> deviceMapping : this.hardwareMap.allDeviceMappings) {
            System.out.printf("+ %s devices%n", deviceMapping.getDeviceTypeClass().getName());

            for (HardwareDevice hardwareDevice : deviceMapping) {
                System.out.printf("  - %s%n", hardwareDevice.getDeviceName());
                System.out.printf("     - Manufacturer: %s%n", hardwareDevice.getManufacturer());
                System.out.printf("     - Version: %s%n", hardwareDevice.getVersion());
                System.out.printf("     - Connection info: %s%n", hardwareDevice.getConnectionInfo());
            }
        }

        System.out.println("========== Environment: detected gamepads ==========");
        System.out.printf("Gamepad 1: %s%nGamepad 2: %s%n", this.gamepad1, this.gamepad2);
    }

    /**
     * Waits a desired number of milliseconds.
     * <strong>{@link Thread#sleep(long)} does not work, use this instead!</strong>
     *
     * @param milliseconds Number of milliseconds to wait
     */
    public void sleep(long milliseconds) {
        SystemClock.sleep(milliseconds);
    }

    @Override
    public void init() {
        try {
            this.configureSystemOut();
            this.configureHardwareVariables();
            this.configureDashboard();
            this.configureIMU();

            System.out.println("[bootstrap] Initialization complete");
        } catch (IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
