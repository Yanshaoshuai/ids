package com.ahut.ids;

public class MinMax {
    private double min;
    private double max;

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "MinMax{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }

    public void setMax(Double max) {
        this.max = max;
    }
}
