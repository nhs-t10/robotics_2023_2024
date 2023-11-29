package com.pocolifo.robobase.movement;

import com.pocolifo.robobase.Robot;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;

import java.util.LinkedList;

public class DisplacementSequence {
    public final LinkedList<Displacement> displacements;
    private LinkedList<SolvedDisplacement> solvedDisplacements;


    public DisplacementSequence() {
        this.displacements = new LinkedList<>();
    }

    public DisplacementSequence add(double xCm, double yCm) {
        this.displacements.add(new Displacement(xCm, yCm));
        return this;
    }

    public DisplacementSequence reverse() {
        DisplacementSequence reversed = new DisplacementSequence();

        for (int i = 0; displacements.size() > i; i++) {
            Displacement d = displacements.get(displacements.size()-i-1);
            reversed.add(-d.xCm, -d.yCm);
        }

        return reversed;
    }

    public void solve(CarWheels carWheels) {
        int tickNumber = 0;

        for (int i = 0; i < displacements.size(); i++) {
            Displacement displacement = displacements.get(i);

            double cm = Math.hypot(displacement.xCm, displacement.yCm);
            int ticks = (int) (cm / carWheels.specialWheel.circumferenceCm * carWheels.specialWheel.tickCount);
            solvedDisplacements.add(new SolvedDisplacement(displacement, tickNumber, ticks));

            tickNumber += ticks;
        }
    }

//    public OmniDriveCoefficients.CoefficientSet at(int tick) {
//        for (int i = 0; i < this.solvedDisplacements.size(); i++) {
//            SolvedDisplacement solvedDisplacement = this.solvedDisplacements.get(i);
//
//            if (solvedDisplacement.startAtTick >= tick && solvedDisplacement.endAtTick > tick) {
////                solvedDisplacement.
//                break;
//            }
//        }
//    }
}
