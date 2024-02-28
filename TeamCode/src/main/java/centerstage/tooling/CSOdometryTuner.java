package centerstage.tooling;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.tooling.AutomaticOdometryCoefficientTuner;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class CSOdometryTuner extends AutomaticOdometryCoefficientTuner {
    @Override
    public void initialize() {
        CenterStageRobotConfiguration c = new CenterStageRobotConfiguration(this.hardwareMap);
        NovelOdometry odometry = c.createOdometry();
        this.driver = c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.le = odometry.leftEncoder;
        this.pe = odometry.perpendicularEncoder;
        this.re = odometry.rightEncoder;
    }
}
