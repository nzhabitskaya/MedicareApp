package com.mobile.android.ebabynotebook.ui.fragments.nodes.weight;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.chart.data.MeasureDataWrapper;
import com.mobile.android.chart.helper.ChartDataHelper;
import com.mobile.android.chart.ui.AverageWeightChart;
import com.mobile.android.database.DataSourceHolder;
import com.mobile.android.database.WithingsDataSource;
import com.mobile.android.database.beans.Measure;

import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;

import java.util.List;

public class WeightChartFragment extends Fragment {
    private GraphicalView mView;
    private AbstractChart mChart;

    public WeightChartFragment() {
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
            WithingsDataSource datasource = DataSourceHolder.getInstance(getActivity()).getWithingsDataSource();
            datasource.open();

            String accountId = DataManager.getInstance().getUserId(getActivity());
            String accountName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("name", "");
            List<Measure> values = datasource.getAllMeasures(accountId, accountName);

            MeasureDataWrapper weightData = new MeasureDataWrapper(values);
            weightData.parseData();
            double[] measures = weightData.getMeasures();
            double[] daysOfLife = weightData.getDaysOfLife();

            boolean isFemale = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("geschlecht", 0) == 0 ? false : true;
            ChartDataHelper.parseWfaData(getActivity(), daysOfLife, isFemale);
            AverageWeightChart chart = ChartDataHelper.buildWeightChart(getActivity(), measures);
            chartIntent = chart.execute(getActivity());

            datasource.close();

        } catch (java.sql.SQLException e){

        }
        return chartIntent;
    }
}