package com.mobile.android.temperatur.data;

import com.mobile.android.database.beans.Temperatur;
import com.mobile.android.database.utils.TimestampUtil;

import java.util.List;

public class TemperaturDataWrapper {
    private List<Temperatur> temperaturList;
    private double[] temperatures;

    private String[] dates;
    private long[] timestamps;
    private double[] daysOfLife;

    public TemperaturDataWrapper(List<Temperatur> measuresList){
        this.temperaturList = measuresList;
    }

    public void parseData(){
        parseTemperatur();
        parseTimes();
    }

    // Array of temperatur data
    private void parseTemperatur(){
		temperatures = new double[temperaturList.size()];
		for(int i = 0; i < temperaturList.size(); i++) {
            temperatures[i] = Double.parseDouble(temperaturList.get(i).getTemperatur());
		}
	}

    // Days of life
    private void parseTimes(){
        dates = new String[temperaturList.size()];
        timestamps = new long[temperaturList.size()];
        daysOfLife = new double[temperaturList.size()];

        for(int i = 0; i < temperaturList.size(); i++) {
            timestamps[i] = Long.parseLong(temperaturList.get(i).getTimestamp());
            dates[i] = TimestampUtil.parseDate(timestamps[i]);
            daysOfLife[i] = TimestampUtil.findDifferenceInDays(timestamps[i], TimestampUtil.DATE_OF_BIRTH);
        }
    }

    public double[] getTemperatures() {
        return temperatures;
    }

    public String[] getDates() {
        return dates;
    }

    public double[] getDaysOfLife() {
        return daysOfLife;
    }
}
