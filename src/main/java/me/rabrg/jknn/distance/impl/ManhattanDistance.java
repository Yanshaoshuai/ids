package me.rabrg.jknn.distance.impl;

import me.rabrg.jknn.distance.Distance;

public final class ManhattanDistance implements Distance {

    public double getDistance(final double[] features1, final double[] features2) {
        double sum = 0;
        for (int i = 0; i < features1.length; i++)
            sum += Math.abs(features1[i] - features2[i]);
        return sum;
    }
}
