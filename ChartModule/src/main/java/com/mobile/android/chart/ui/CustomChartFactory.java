package com.mobile.android.chart.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.mobile.android.chart.helper.ChartDataHelper;

import org.achartengine.GraphicalActivity;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Calendar;

public class CustomChartFactory{

    public CustomChartFactory() {
        super();
    }

    public static final Intent getTimeChartIntent(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, String format, String activityTitle) {
        Intent intent = new Intent(context, GraphicalActivity.class);
        CustomTimeChart chart = new CustomTimeChart(dataset, renderer);

        long gebursDatum = PreferenceManager.getDefaultSharedPreferences(context).getLong("geburtsdate", Calendar.getInstance().getTimeInMillis());
        long today = Calendar.getInstance().getTimeInMillis();

        chart.setChartSettings(renderer, "Temperaturverlauf", "Tage", "Temperatur", gebursDatum, today, 0, 80,
                Color.BLACK, Color.BLACK);

        chart.setDateFormat(format);
        intent.putExtra("chart", chart);
        intent.putExtra("title", activityTitle);
        return intent;
    }
}
