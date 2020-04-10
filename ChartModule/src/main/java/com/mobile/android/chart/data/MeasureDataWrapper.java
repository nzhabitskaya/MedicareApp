package com.mobile.android.chart.data;

import com.mobile.android.database.beans.Measure;
import com.mobile.android.database.utils.TimestampUtil;

import java.util.List;

public class MeasureDataWrapper {
    private List<Measure> measuresList;
    private double[] measures;

    private String[] dates;
    private long[] timestamps;
    private double[] daysOfLife;

    public MeasureDataWrapper(List<Measure> measuresList){
        this.measuresList = measuresList;
    }

    public void parseData(){
        parseWithingsMeasures();
        parseWithingsTimes();
    }

    // Array of weight data from server
    private void parseWithingsMeasures(){
		measures = new double[measuresList.size()];
		for(int i = 0; i < measuresList.size(); i++) {
            measures[i] = Double.parseDouble(measuresList.get(i).getWeight());
		}
	}

    // Days of life
    private void parseWithingsTimes(){
        dates = new String[measuresList.size()];
        timestamps = new long[measuresList.size()];
        daysOfLife = new double[measuresList.size()];

        for(int i = 0; i < measuresList.size(); i++) {
            timestamps[i] = Long.parseLong(measuresList.get(i).getTimestamp());
            dates[i] = TimestampUtil.parseDate(timestamps[i]);
            daysOfLife[i] = TimestampUtil.findDifferenceInDays(timestamps[i], TimestampUtil.DATE_OF_BIRTH);
        }
    }

    public double[] getMeasures() {
        return measures;
    }

    public String[] getDates() {
        return dates;
    }

    public double[] getDaysOfLife() {
        return daysOfLife;
    }
}
