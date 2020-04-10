package com.mobile.android.chart.helper;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.chart.data.WfaData;
import com.mobile.android.chart.ui.AverageTemperatureChart;
import com.mobile.android.chart.ui.AverageWeightChart;
import com.mobile.android.chart.utils.ReadTxtUtil;

import org.json.JSONArray;
import org.json.JSONException;


public class ChartDataHelper {
    public static final int DREI_TAGE = 3;
    public static final int EIN_WOCHE = 7;
    public static final int EIN_MONAT = 31;
    public static final int DREI_MONATE = 93;
    public static final int FUNF_MONATE = 155;
    public static final int ZEBEN_MONATE = 217;

    public static final String WFA_GIRLS_FILE = "wfa_girls_z_exp.txt";
    public static final String WFA_BOYS_FILE = "wfa_boys_z_exp.txt";

    public static final int COLUMN_DAY = 0;
    public static final int COLUMN_LOW_WEIGHT = 3;
    public static final int COLUMN_AVERAGE_WEIGHT = 5;
    public static final int COLUMN_HIGH_WEIGHT = 7;

    public static void parseWfaData(Context context, double[] days, boolean isFemale){
        //Log.e("", "Start WFA data parsing");
        String fileName = isFemale ? WFA_GIRLS_FILE : WFA_BOYS_FILE;
        String weightData = ReadTxtUtil.readTxt(context, fileName);
        try {
            JSONArray array = new JSONArray(weightData);
            WfaData wfaData = new WfaData(days.length);

            for(int i = days.length - 1; i >= 0; i--) {
                int currentDay = (int) days[i] - 1;
                if(currentDay < 0 || currentDay >= array.length())
                    currentDay = 0;
                Log.d("", "Current day " + currentDay);
                JSONArray line = array.getJSONArray(currentDay);

                int day = line.getJSONObject(COLUMN_DAY).getInt(ReadTxtUtil.DATA);
                double dataMin = line.getJSONObject(COLUMN_LOW_WEIGHT).getDouble(ReadTxtUtil.DATA);
                double dataAverage = line.getJSONObject(COLUMN_AVERAGE_WEIGHT).getDouble(ReadTxtUtil.DATA);
                double dataMax = line.getJSONObject(COLUMN_HIGH_WEIGHT).getDouble(ReadTxtUtil.DATA);

                wfaData.addDay(i, day);
                wfaData.addMin(i, dataMin);
                wfaData.addAverage(i, dataAverage);
                wfaData.addMax(i, dataMax);
            }
            DataManager.getInstance().setWfaData(wfaData);
            //Log.e("", "End WFA data parsing");

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Build chart lines
     * @param activity
     * @param measures
     * @return
     */
    public static AverageWeightChart buildWeightChart(Activity activity, double[] measures){
        AverageWeightChart chart = new AverageWeightChart();

        int periodOfDays = PreferenceManager.getDefaultSharedPreferences(activity).getInt("darstellung", FUNF_MONATE);
        chart.setPeriodOfDays(periodOfDays);

        chart.setIds(DataManager.getInstance().getWfaDays());
        chart.setWeightMinData(DataManager.getInstance().getWfaMins());
        chart.setWeightAverageData(DataManager.getInstance().getWfaAverages());
        chart.setWeightMaxData(DataManager.getInstance().getWfaMaxs());
        chart.setWeightData(measures);

        return chart;
    }

    public static AverageTemperatureChart buildTempChart (Activity activity, double[] temperatures){
        AverageTemperatureChart chart = new AverageTemperatureChart();

        int periodOfDays = ZEBEN_MONATE;
        chart.setPeriodOfDays(periodOfDays);

        chart.setIds(DataManager.getInstance().getWfaDays());

        chart.setTempData(temperatures);

        return chart;
    }
}
