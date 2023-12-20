package com.pocolifo.robobase.novel;

import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class MecanumDrive {
    private final NovelMotor fl, fr, bl, br;

    private final OmniDriveCoefficients omniDriveCoefficients;

    public MecanumDrive(NovelMotor fl, NovelMotor fr, NovelMotor bl, NovelMotor br, OmniDriveCoefficients omniDriveCoefficients) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
        this.omniDriveCoefficients = omniDriveCoefficients;
        this.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void drive(double distanceInches, double acceleration) {
        double velocity = 0;
        double initialPosition = getEncoderValue();
        while (getEncoderValue() - initialPosition < distanceInches / 2) {
            velocity += acceleration;
            driveIndividually(velocity, velocity, velocity, velocity);
        }
        while (getEncoderValue() - initialPosition < distanceInches && velocity > 0) {
            velocity -= acceleration;
            driveIndividually(velocity, velocity, velocity, velocity);
        }
        driveIndividually(0,0,0,0);
    }

    public void driveOmni(double verticalPower, double horizontalPower, double rotationalPower) {
        // Drive the wheels to match the controller input
        OmniDriveCoefficients.CoefficientSet vals = this.omniDriveCoefficients.calculateCoefficientsWithPower(
                verticalPower,
                horizontalPower,
                rotationalPower
        );

        this.driveIndividually(
                vals.frontLeft,
                vals.frontRight,
                vals.backLeft,
                vals.backRight
        );
    }

    public void driveIndividually(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.fl.setVelocity(frontLeft * omniDriveCoefficients.totals.frontLeft, AngleUnit.RADIANS);
        this.fr.setVelocity(frontRight * omniDriveCoefficients.totals.frontRight, AngleUnit.RADIANS);
        this.bl.setVelocity(backLeft * omniDriveCoefficients.totals.backLeft, AngleUnit.RADIANS);
        this.br.setVelocity(backRight * omniDriveCoefficients.totals.backRight, AngleUnit.RADIANS);
    }

    private double getEncoderValue() {
        this.fr.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return this.fr.getEncoderInches();
    }
}
