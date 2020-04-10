package com.mobile.android.ebabynotebook.ui.fragments.nodes.temperatur;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.android.chart.ui.CustomChartFactory;
import com.mobile.android.database.DataSourceHolder;
import com.mobile.android.database.TemperaturDataSource;
import com.mobile.android.database.beans.Temperatur;

import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.List;

public class TemperaturChartFragment extends Fragment {
    private static final int LINES_COUNT = 1;

    private GraphicalView mView;
    private AbstractChart mChart;

    public TemperaturChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle extras = calculateChartData().getExtras();
        this.mChart = (AbstractChart)extras.getSerializable("chart");
        this.mView = new GraphicalView(getActivity(), this.mChart);

        return this.mView;
    }

    private Intent calculateChartData(){
        Intent chartIntent = null;

        // Get measures from db
        try {
            TemperaturDataSource datasource = DataSourceHolder.getInstance(getActivity()).getTemperaturDataSource();
            datasource.open();

            String accountName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("name", "");
            List<Temperatur> values = datasource.getAllTemperatur(accountName);
            chartIntent = CustomChartFactory.getTimeChartIntent(getActivity(), getDateDemoDataset(values), getDemoRenderer(), null, "");

            datasource.close();
        } catch (java.sql.SQLException e){

        }
        return chartIntent;
    }

    private XYMultipleSeriesDataset getDateDemoDataset(List<Temperatur> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < LINES_COUNT; i++) {
            TimeSeries series = new TimeSeries("Aktuelle Temperatur");

            for (Temperatur temperatur : values) {
                // Get temperature and date from List of temperatures
                double temperaturValue = Double.parseDouble(temperatur.getTemperatur());
                Date dateValue = new Date(Long.parseLong(temperatur.getTimestamp()) * 1000 - 24 * 60 * 60);
                series.add(dateValue, temperaturValue);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    private XYMultipleSeriesRenderer getDemoRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setAxisTitleTextSize(20);
        renderer.setChartTitleTextSize(35);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(20);
        renderer.setPointSize(8f);
        renderer.setAxesColor(Color.DKGRAY);
        renderer.setLabelsColor(Color.BLUE);
        renderer.setMargins(new int[] { 120, 45, 45, 25 });

        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        r.setColor(Color.GREEN);
        r.setLineWidth(2);
        r.setPointStyle(PointStyle.CIRCLE);
        renderer.addSeriesRenderer(r);

        return renderer;
    }
}