package com.mobile.android.chart.data;

public class WfaData {
    private double[] dayValues;
    private double[] minValues;
    private double[] AverageValues;
    private double[] maxValues;

    public WfaData(int size) {
        dayValues = new double[size];
        AverageValues = new double[size];
        minValues = new double[size];
        maxValues = new double[size];
    }

    public double[] getDayValues() {
        return dayValues;
    }
    public double[] getAverageValues() {
        return AverageValues;
    }
    public double[] getMinValues() {
        return minValues;
    }
    public double[] getMaxValues() {
        return maxValues;
    }

    public void addDay(int index, double value) {
        dayValues[index] = value;
    }

    public void addAverage(int index, double value) {
        AverageValues[index] = value;
    }

    public void addMin(int index, double value) {
        minValues[index] = value;
    }

    public void addMax(int index, double value) {
        maxValues[index] = value;
    }
}
