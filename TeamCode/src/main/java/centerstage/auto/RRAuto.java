package centerstage.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import roadrunner.drive.RRInterface;

@Autonomous
public class RRAuto extends AutonomousOpMode {
    RRInterface rrInterface;
    Pose2d startPose;

    @Override
    public void initialize() {
        rrInterface = new RRInterface(hardwareMap);
        startPose = new Pose2d(0,0);
    }

    @Override
    public void run() {
        Trajectory trajectory = rrInterface.trajectoryBuilder(startPose).forward(5).build();
        rrInterface.followTrajectory(trajectory);
    }
}
