package com.pocolifo.robobase.bootstrap;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Hardware {
    String name();

    // Optional parameters just for certain types of hardware
    double ticksPerRevolution() default -1;
    DcMotor.ZeroPowerBehavior zeroPowerBehavior() default DcMotor.ZeroPowerBehavior.BRAKE;
    int gearRatio() default 1;
    double wheelDiameterIn() default -1;
}
