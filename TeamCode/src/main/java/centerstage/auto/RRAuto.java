package centerstage.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import roadrunner.drive.RRInterface;
import roadrunner.trajectorysequence.TrajectorySequence;
import roadrunner.trajectorysequence.TrajectorySequenceBuilder;

@Autonomous
public class RRAuto extends AutonomousOpMode {
    RRInterface rrInterface;
    Pose2d startPose;

    @Override
    public void initialize() {

    }

    @Override
    public void run() {
        TrajectorySequenceBuilder trajectorySequenceBuilder = rrInterface.trajectorySequenceBuilder(startPose);
        trajectorySequenceBuilder = trajectorySequenceBuilder.forward(24);
        trajectorySequenceBuilder = trajectorySequenceBuilder.turn(Math.PI/2);
        trajectorySequenceBuilder = trajectorySequenceBuilder.forward(24);
        trajectorySequenceBuilder = trajectorySequenceBuilder.turn(Math.PI/2);
        trajectorySequenceBuilder = trajectorySequenceBuilder.forward(24);
        trajectorySequenceBuilder = trajectorySequenceBuilder.turn(Math.PI/2);
        trajectorySequenceBuilder = trajectorySequenceBuilder.forward(24);
        trajectorySequenceBuilder = trajectorySequenceBuilder.turn(Math.PI/2);
        rrInterface.followTrajectorySequence(trajectorySequenceBuilder.build());
    }
}
