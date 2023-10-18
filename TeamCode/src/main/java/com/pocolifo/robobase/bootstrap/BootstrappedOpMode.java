package com.pocolifo.robobase.bootstrap;

import android.os.SystemClock;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
    protected void sleep(long milliseconds) {
        SystemClock.sleep(milliseconds);
    }

    @Override
    public void init() {
        this.configureSystemOut();
    }
}
