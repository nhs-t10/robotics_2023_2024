package centerstage.auto.deprecated;

import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.CarWheels;

import static centerstage.Constants.ROBOT;

public class ParkingAutoNear extends AutonomousOpMode {
    private CarWheels carWheels;

    @Override
    public void initialize() {
        this.carWheels = new CarWheels(
                hardwareMap,
                1120,
                10d,
                ROBOT,
                "FL",
                "FR",
                "BL",
                "BR",
                "FL"
        );
    }

    @Override
    public void run() {
        System.out.println("Beginning Movement");
        carWheels.driveOmni(1,0,0);
        sleep(2000);
        carWheels.driveOmni(0,0,0);
        carWheels.close();
        System.out.println("Completed Movement");
    }
}
