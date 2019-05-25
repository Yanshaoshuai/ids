package com.ahut.ids.model;

import java.util.Map;

public class Result {

    private double recall;
    private double precision;
    private double accuracy;

    private Map<String,Result_inner> result_innerMap;

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public Map<String, Result_inner> getResult_innerMap() {
        return result_innerMap;
    }

    public void setResult_innerMap(Map<String, Result_inner> result_innerMap) {
        this.result_innerMap = result_innerMap;
    }
}
