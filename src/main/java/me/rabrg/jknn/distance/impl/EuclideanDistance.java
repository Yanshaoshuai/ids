package me.rabrg.jknn.distance.impl;

import me.rabrg.jknn.distance.Distance;

public final class EuclideanDistance implements Distance {

    public double getDistance(final double[] features1, final double[] features2) {
        double sum = 0;
        for (int i = 0; i < features1.length; i++)
            sum += Math.pow(features1[i] - features2[i], 2);
        return Math.sqrt(sum);
    }
}
