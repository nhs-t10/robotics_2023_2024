package centerstage;

import android.os.SystemClock;

public class RobotCapabilities {
    public static final int LIFT_FULLY_EXTENDED_ENCODER_POS = 1500;
    public final CenterStageRobotConfiguration c;

    public RobotCapabilities(CenterStageRobotConfiguration c) {
        this.c = c;
    }

    public void downLift(double power) {
        this.c.linearSlideLeft.setPower(Math.abs(power));
        this.c.linearSlideRight.setPower(Math.abs(power));
//        if (Math.abs(this.c.linearSlideRight.motor.getCurrentPosition()) > 0) {
//        } else {
//            this.stopLift();
//        }
    }

    public void upLift(double power) {
        this.c.linearSlideLeft.setPower(-Math.abs(power));
        this.c.linearSlideRight.setPower(-Math.abs(power));
//        if (Math.abs(this.c.linearSlideRight.motor.getCurrentPosition()) < LIFT_FULLY_EXTENDED_ENCODER_POS) {
//        } else {
//            this.stopLift();
//        }
    }

    public void stopLift() {
        this.c.linearSlideLeft.setPower(0);
        this.c.linearSlideRight.setPower(0);
    }

    public void moveLiftToPosition(int position, double power) {
        this.c.linearSlideLeft.setPower(-Math.abs(power));
        this.c.linearSlideRight.setPower(-Math.abs(power));
        // todo
    }

    public void extendLiftFully() {
        this.moveLiftToPosition(LIFT_FULLY_EXTENDED_ENCODER_POS, 1);
    }

    public void retractLiftFully() {
        this.moveLiftToPosition(0, 1);
    }

    public void gripPixels() {
        System.out.println("grip");
        this.c.containerPixelHolder.setPosition(-1);
    }

    public void releasePixelGrip() {
        System.out.println("release");
        this.c.containerPixelHolder.setPosition(1);
    }

    public void runIntake(double speed) {
        this.c.roller.setPower(Math.abs(speed));
        this.c.spinningIntake.setPower(Math.abs(speed));
    }

    public void runOuttake(double speed) {
        this.c.roller.setPower(-Math.abs(speed));
        this.c.spinningIntake.setPower(-Math.abs(speed));
    }

    public void stopIntakeOuttake() {
        this.c.roller.setPower(0);
        this.c.spinningIntake.setPower(0);
    }

    public void launchAirplane() {
        new Thread(() -> {
            this.c.airplaneLauncher.setPosition(0.5);
            SystemClock.sleep(1000);
            this.c.airplaneLauncher.setPosition(-1);
            SystemClock.sleep(1000);
            this.c.airplaneLauncher.setPosition(0.5);
        }).start();
    }

    /**
     * 1 = fully up
     * -1 = down
     */
    public void rotateContainer(double position) {
        this.c.containerRotationLeft.setPosition(-position);
        this.c.containerRotationRight.setPosition(position);
    }

    public void update() {
        double slideEncoderAvg = this.c.linearSlideRight.motor.getCurrentPosition();

        if (slideEncoderAvg <= 0) {
            this.stopLift();
        }
    }
}
