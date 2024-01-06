package centerstage;

import android.os.SystemClock;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class RobotCapabilities {
    public final CRServo clawGrip;
    public final CRServo clawRotation;
    public final CRServo airplaneLauncher;
    public final DcMotorEx lift;

    public RobotCapabilities(CRServo clawGrip, CRServo clawRotation, CRServo airplaneLauncher, DcMotorEx lift) {
        this.clawGrip = clawGrip;
        this.clawRotation = clawRotation;
        this.airplaneLauncher = airplaneLauncher;
        this.lift = lift;
        this.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.airplaneLauncher.setPower(1);
    }

    public void moveToCollectPosition() {
        new Thread(() -> {
            this.openGrip();
            this.downLift(0.75);
            this.rotateBackward();

            SystemClock.sleep(1000);

            this.downLift(0.25);
            this.rotateBackward();
            SystemClock.sleep(500);
            this.rotateBackward();
            SystemClock.sleep(1000);
            this.rotateForward();
            SystemClock.sleep(500);
            this.stopLift();
            this.stopRotating();
        }).start();
    }

    public void moveToDropPosition() {
        new Thread(() -> {
            this.closeGrip();
            this.rotateBackward();
            SystemClock.sleep(500);
            this.rotateForward();
            this.upLift(1);
            SystemClock.sleep(750);
            this.upLift(0.5);
            SystemClock.sleep(500);
            this.upLift(0.15);
            this.stopRotating();
            SystemClock.sleep(500);
            this.stopLift();
        }).start();
    }

    public void downLift(double power) {
        this.lift.setPower(Math.abs(power));
    }

    public void upLift(double power) {
        this.lift.setPower(-Math.abs(power));
    }

    public void stopLift() {
        this.lift.setPower(0);
    }

    public void openGrip() {
        this.clawGrip.setPower(1);
    }

    public void closeGrip() {
        this.clawGrip.setPower(0);
    }

    public void rotateForward() {
        this.clawRotation.setPower(0.8);
    }

    public void rotateBackward() {
        this.clawRotation.setPower(-0.8);
    }

    public void stopRotating() {
        this.clawRotation.setPower(0);
    }

    public void launchAirplane() {
        new Thread(() -> {
            this.airplaneLauncher.setPower(0);
            SystemClock.sleep(1000);
            this.airplaneLauncher.setPower(1);
        }).start();
    }
}
