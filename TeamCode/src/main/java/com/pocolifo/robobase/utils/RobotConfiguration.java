package com.pocolifo.robobase.utils;

import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.novel.hardware.NovelEncoder;
import com.pocolifo.robobase.novel.hardware.NovelMotor;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.lang.reflect.Field;

public class RobotConfiguration {
    /**
     * Sets fields marked with @{@link Hardware} to their initialized value based on the {@link OpMode#hardwareMap}.
     * <p>
     * Example where a webcam and wheel are auto initialized using @{@link Hardware}. You would be able to use these
     * variables just like normal. RoboBase does the initialization for you. The example class:
     *
     * <pre>{@code
     * public class MyRobotConfiguration extends RobotConfiguration {
     *      @Hardware(name = "Webcam")
     *      public Webcam webcam;
     *
     *      @Hardware(
     *          name = "Chain",
     *          wheelDiameterCm = 9.6,
     *          ticksPerRevolution = 500,
     *          zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
     *      )
     *      public NovelMotor chainWheel;
     * }
     * }</pre>
     *
     * <b>Note:</b> Fields must be {@code public} and not {@code final} or {@code private}.
     */
    public RobotConfiguration(HardwareMap hardwareMap) {
        try {
            for (Field field : this.getClass().getFields()) {
                Hardware hardware = field.getAnnotation(Hardware.class);

                if (hardware != null) {
                    String configName = hardware.name();
                    Class<?> type = field.getType();
                    Object o;

                    if (type.equals(Webcam.class)) {
                        o = new Webcam(hardwareMap, configName);
                    } else if (type.equals(NovelMotor.class)) {
                        o = new NovelMotor(hardwareMap.get(DcMotorEx.class, configName), hardware.ticksPerRevolution(), hardware.diameterIn(), hardware.gearRatio());
                        ((NovelMotor) o).motor.setZeroPowerBehavior(hardware.zeroPowerBehavior());
                    } else if (type.equals(NovelEncoder.class)) {
                        o = new NovelEncoder(hardwareMap.get(DcMotor.class, configName), hardware.diameterIn(), hardware.ticksPerRevolution());
                    } else {
                        o = hardwareMap.get(field.getType(), configName);
                    }

                    field.setAccessible(true);
                    field.set(this, o);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("could not create RobotConfiguration");
        }
    }
}
