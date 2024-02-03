package com.pocolifo.robobase;

import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.Motor;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.Wheel;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.lang.reflect.Field;

public class RobotConfiguration {
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
                    } else if (type.equals(Wheel.class)) {
                        // TODO: warn if ticksPerRevolution, wheelDiameterCm is a bad value, but don't assert (if less than 0)
                        o = new Wheel(hardwareMap.get(DcMotor.class, configName), hardware.ticksPerRevolution(), hardware.wheelDiameterIn());
                        ((Wheel) o).motor.setZeroPowerBehavior(hardware.zeroPowerBehavior());
                    } else if (type.equals(Motor.class)) {
                        // TODO: warn if ticksPerRevolution, wheelDiameterCm is a bad value, but don't assert (if less than 0)
                        o = new Motor(hardwareMap.get(DcMotor.class, configName), hardware.ticksPerRevolution());
                        ((Motor) o).motor.setZeroPowerBehavior(hardware.zeroPowerBehavior());
                    } else if (type.equals(NovelMotor.class)) {
                        // TODO: warn if ticksPerRevolution, wheelDiameterCm is a bad value, but don't assert (if less than 0)
                        o = new NovelMotor(hardwareMap.get(DcMotorEx.class, configName), hardware.ticksPerRevolution(), hardware.wheelDiameterIn(), hardware.gearRatio());
                        ((NovelMotor) o).motor.setZeroPowerBehavior(hardware.zeroPowerBehavior());
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
