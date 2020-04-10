package com.mobile.android.chart.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;

public class GraphicalFragment extends Fragment {
    private GraphicalView mView;
    private AbstractChart mChart;

    public GraphicalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle extras = getActivity().getIntent().getExtras();
        this.mChart = (AbstractChart)extras.getSerializable("chart");
        this.mView = new GraphicalView(getActivity(), this.mChart);
        return this.mView;
    }
}
