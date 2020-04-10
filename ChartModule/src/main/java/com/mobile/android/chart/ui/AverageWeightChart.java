package com.mobile.android.chart.ui;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class AverageWeightChart extends AbstractDemoChart {
    private double[] weightData;
    private double[] weightAverageData;
    private double[] weightMinData;
    private double[] weightMaxData;
    private double[] ids;
    private int periodOfDays;

    public AverageWeightChart() {
    }

    public String getName() {
    return "Gewichtsverlauf";
    }

    public String getDesc() {
        return "The wight in 4 line chart";
    }

    public Intent execute(Context context) {
        String[] titles = new String[] { "3 Prozent","50 Prozent", "Aktuelles Gewicht", "97 Prozent" };
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            x.add(ids);
            x.add(ids);
            x.add(ids);
            x.add(ids);
        }
        List<double[]> values = new ArrayList<double[]>();
        values.add(weightMinData);
        values.add(weightAverageData);
        values.add(weightData);
        values.add(weightMaxData);

        int[] colors = new int[] { Color.BLACK, Color.rgb(0,255,127 ), Color.BLUE, Color.BLACK };
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Gewichtsverlauf", "Tage", "Gewicht", 0, periodOfDays, 0, 10,
                Color.BLACK, Color.BLACK);
        renderer.setXLabels(12);
        renderer.setYLabels(12);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[]{0, periodOfDays, 0, 10});
        renderer.setZoomLimits(new double[]{0, periodOfDays, 0, 10});
        changeBackgroundColor(renderer);

        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        XYSeries series = dataset.getSeriesAt(0);
        drawAnnotations(series);
        Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
                "Gewichtsverlauf");
        return intent;
    }

    private void changeBackgroundColor(XYMultipleSeriesRenderer renderer){
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setMarginsColor(Color.rgb(175,238,238  ));
    }

    private void drawAnnotations(XYSeries series){
      /*
        series.addAnnotation("1 week", 7, 9);
        series.addAnnotation("2 weeks", 14, 7);
        series.addAnnotation("3 weeks", 21, 7);
        series.addAnnotation("1 month", 28, 9);

        series.addAnnotation("2 month", 56, 9);
        series.addAnnotation("3 month", 84, 9);
        series.addAnnotation("4 month", 112, 9);
        series.addAnnotation("5 month", 155, 9);
        */
        series.addAnnotation("", 175, 9);
        series.addAnnotation("", 200, 9);

    }

    public void setWeightData(double[] weightData) {
        this.weightData = weightData;
    }

    public void setWeightMinData(double[] weightMinData) {
        this.weightMinData = weightMinData;  }

    public void setWeightAverageData(double[] weightAverageData) {
            this.weightAverageData = weightAverageData;  }


    public void setWeightMaxData(double[] weightMaxData) {
        this.weightMaxData = weightMaxData;
    }

    public void setIds(double[] ids) {
        this.ids = ids;
    }

    public void setPeriodOfDays(int periodOfDays) {
        this.periodOfDays = periodOfDays;
    }
}
