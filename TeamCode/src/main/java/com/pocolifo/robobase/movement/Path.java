package com.pocolifo.robobase.movement;

import com.pocolifo.robobase.reconstructor.VirtualField;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.LinkedList;
import java.util.List;

public class Path {
    public final LinkedList<Displacement> displacements = new LinkedList<>();

    public Path(List<VirtualField.Point> points) {
        for (int i = 0; i < points.size()-1; i++) {
            VirtualField.Point point1 = points.get(i);
            VirtualField.Point point2 = points.get(i+1);

            this.displacements.add(new Displacement(
                    DistanceUnit.METER.toCm(point2.x - point1.x),
                    DistanceUnit.METER.toCm(point2.y - point1.y)
            ));
        }
    }
}
