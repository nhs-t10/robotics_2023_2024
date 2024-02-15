package com.pocolifo.robobase.utils;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

import java.util.LinkedList;
import java.util.List;

public class MathUtils {
    public static VectorF quaternionToEuler(Quaternion q) {
        // roll (x-axis rotation)
        double sinr_cosp = 2 * (q.w * q.x + q.y * q.z);
        double cosr_cosp = 1 - 2 * (q.x * q.x + q.y * q.y);
        double roll = Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        double sinp = Math.sqrt(1 + 2 * (q.w * q.y - q.x * q.z));
        double cosp = Math.sqrt(1 - 2 * (q.w * q.y - q.x * q.z));
        double pitch = 2 * Math.atan2(sinp, cosp) - Math.PI / 2;

        // yaw (z-axis rotation)
        double siny_cosp = 2 * (q.w * q.z + q.x * q.y);
        double cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z);
        double yaw = Math.atan2(siny_cosp, cosy_cosp);

        return new VectorF((float) roll, (float) pitch, (float) yaw);
    }

    public static double weightedAverage(List<Double> x, List<Double> weights) {
        double weightedSum = 0;
        double totalWeight = sum(weights);

        for (int i = 0; x.size() > i; i++) {
            weightedSum += x.get(i) * weights.get(i);
        }

        return weightedSum / totalWeight;
    }

    public static double sum(List<Double> doubles) {
        double s = 0;

        for (double d : doubles) {
            s += d;
        }

        return s;
    }

    public static List<Double> normalize(List<Double> weights) {
        List<Double> normalized = new LinkedList<>();
        double s = sum(weights);

        for (Double weight : weights) {
            normalized.add(weight / s);
        }

        return normalized;
    }
}
