package com.mobile.android.chart.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class AverageTemperatureChart extends AbstractDemoChart {
    private double[] tempData;
    private double[] ids;
    private int periodOfDays;

    public AverageTemperatureChart() {
    }

    public String getName() {
    return "Temperaturverlauf";
    }

    public String getDesc() {
        return "The temperature in 1 line chart";
    }

    public Intent execute(Context context) {
        String[] titles = new String[] { "Aktuelle Temperatur" };
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            x.add(ids);

        }
        List<double[]> values = new ArrayList<double[]>();
        values.add(tempData);


        int[] colors = new int[] { Color.rgb(0,255,127 ) };
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Temperaturverlauf", "Tage", "Temperatur", 0, periodOfDays, 0, 60,
                Color.BLACK, Color.BLACK);
        renderer.setXLabels(12);
        renderer.setYLabels(12);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[]{0, periodOfDays, 0, 60});
        renderer.setZoomLimits(new double[]{0, periodOfDays, 0, 60});
        changeBackgroundColor(renderer);

        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        XYSeries series = dataset.getSeriesAt(0);
        drawAnnotations(series);
        Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
                "Temperaturverlauf");
        return intent;
    }

    private void changeBackgroundColor(XYMultipleSeriesRenderer renderer){
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setMarginsColor(Color.rgb(175,238,238  ));
    }

    private void drawAnnotations(XYSeries series){

        series.addAnnotation("", 175, 9);
        series.addAnnotation("", 200, 9);

    }

    public void setTempData(double[] tempData) {
        this.tempData = tempData;
    }


    public void setIds(double[] ids) {
        this.ids = ids;
    }

    public void setPeriodOfDays(int periodOfDays) {
        this.periodOfDays = periodOfDays;
    }
}
