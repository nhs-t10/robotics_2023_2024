package com.pocolifo.robobase.movement.mecanum;

import android.os.SystemClock;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.movement.Displacement;
import com.pocolifo.robobase.movement.Path;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class MecanumDrive {
    public static final double MAX_DEGREES_OF_ERROR = 1.5;
    public static final double MAX_CM_OF_ERROR = 2;
    public static final int MAX_TICKS_OF_ERROR = 4;
    public final MecanumWheels mecanumWheels;
    public final OmniDriveCoefficients omniDriveCoefficients;
    private final BNO055IMU imu;

    public MecanumDrive(MecanumWheels mecanumWheels, OmniDriveCoefficients omniDriveCoefficients, BNO055IMU imu) {
        assert imu.isSystemCalibrated();

        this.mecanumWheels = mecanumWheels;
        this.omniDriveCoefficients = omniDriveCoefficients;
        this.imu = imu;
    }

    public MecanumDrive(MecanumWheels mecanumWheels, BNO055IMU imu) {
        this(mecanumWheels, new OmniDriveCoefficients(), imu);
    }

    public void driveOmni(double verticalPower, double horizontalPower, double rotationalPower) {
        // Drive the wheels to match the controller input
        OmniDriveCoefficients.CoefficientSet vals = this.omniDriveCoefficients.calculateCoefficientsWithPower(
                verticalPower,
                horizontalPower,
                rotationalPower
        );

        this.mecanumWheels.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.mecanumWheels.setPower(vals);
    }

    public void drive(double verticalCm, double horizontalCm, double power) {
        while (verticalCm > MAX_CM_OF_ERROR || horizontalCm > MAX_CM_OF_ERROR) {
            double distanceCm = Math.hypot(verticalCm, horizontalCm);
            double powerX = (horizontalCm / distanceCm) * power; // cos
            double powerY = (verticalCm / distanceCm) * power; // sin

            OmniDriveCoefficients.CoefficientSet coefficientSet = this.omniDriveCoefficients.calculateCoefficientsWithPower(powerY, powerX, 0);
            Position initialPosition = this.imu.getPosition();

            this.mecanumWheels.setPower(coefficientSet);
            this.mecanumWheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            this.mecanumWheels.setDriveTarget(distanceCm, coefficientSet);

            while (!this.mecanumWheels.isCloseToTargetPosition(MAX_TICKS_OF_ERROR)) {
                SystemClock.sleep(10);
            }

            Position currentPosition = this.imu.getPosition();

            double totalVerticalDisplacementCm = (currentPosition.y - initialPosition.y) / 100;
            double totalHorizontalDisplacementCm = (currentPosition.x - initialPosition.x) / 100;
            double verticalErrorCm = horizontalCm - totalHorizontalDisplacementCm;
            double horizontalErrorCm = verticalCm - totalVerticalDisplacementCm;

            verticalCm = verticalErrorCm;
            horizontalCm = horizontalErrorCm;
        }

        this.mecanumWheels.stopMoving();
    }

    public double getAngleFacing() {
        return this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public void rotateTo(double degrees, double power) {
        double degreeError = this.getAngleFacing() - degrees;

        while (Math.abs(degreeError) > MAX_DEGREES_OF_ERROR) {
            OmniDriveCoefficients.CoefficientSet coefficientSet = this.omniDriveCoefficients.calculateCoefficientsWithPower(0, 0, power * Math.signum(degreeError));
            this.mecanumWheels.setPower(coefficientSet);
            degreeError = this.getAngleFacing() - degrees;
        }

        this.mecanumWheels.stopMoving();
    }

    public void rotate(double degrees, double power) {
        rotateTo(this.getAngleFacing() + degrees, power);
    }

    public void follow(Path path, double power) {
        for (Displacement displacement : path.displacements) {
            drive(displacement.verticalCm, displacement.horizontalCm, power);
        }
    }
}
