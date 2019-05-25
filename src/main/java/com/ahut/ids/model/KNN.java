package com.ahut.ids.model;

public class KNN {
    private String trainFileName;
    private String testFileName;
    private String testStr;
    private String predictedLabel;
    private int k;
    private double accuracy;
    private int perfectK;
    private String predictfileName;

    public String getPredictfileName() {
        return predictfileName;
    }

    public void setPredictfileName(String predictfileName) {
        this.predictfileName = predictfileName;
    }

    public String getTrainFileName() {
        return trainFileName;
    }

    public void setTrainFileName(String trainFileName) {
        this.trainFileName = trainFileName;
    }

    public String getTestFileName() {
        return testFileName;
    }

    public void setTestFileName(String testFileName) {
        this.testFileName = testFileName;
    }

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }

    public String getPredictedLabel() {
        return predictedLabel;
    }

    public void setPredictedLabel(String resultLabel) {
        this.predictedLabel = resultLabel;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getPerfectK() {
        return perfectK;
    }

    public void setPerfectK(int perfectK) {
        this.perfectK = perfectK;
    }
}
