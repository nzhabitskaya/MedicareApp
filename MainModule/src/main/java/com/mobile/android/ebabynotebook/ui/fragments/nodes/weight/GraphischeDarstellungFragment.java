package com.mobile.android.ebabynotebook.ui.fragments.nodes.weight;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.chart.helper.ChartDataHelper;

public class GraphischeDarstellungFragment extends Fragment {
    private RadioGroup darstellungRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_darstellung, container, false);
        darstellungRadioGroup = (RadioGroup) rootView.findViewById(R.id.radiogroup_darstellung);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        restoreFieldsFromSharedPrefs();
    }

    @Override
    public void onPause(){
        super.onStart();
        saveFieldsToSharedPrefs();
    }

    private void saveFieldsToSharedPrefs(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putInt("darstellung", getGeschlecht());
        editor.apply();
    }

    private void restoreFieldsFromSharedPrefs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        selectGeschlecht(prefs.getInt("darstellung", ChartDataHelper.FUNF_MONATE));
    }

    private int getGeschlecht() {
        int checkedId = darstellungRadioGroup.getCheckedRadioButtonId();
        switch (checkedId){
            case R.id.darstellung_1:
                return ChartDataHelper.DREI_TAGE;
            case R.id.darstellung_2:
                return ChartDataHelper.EIN_WOCHE;
            case R.id.darstellung_3:
                return ChartDataHelper.EIN_MONAT;
            case R.id.darstellung_4:
                return ChartDataHelper.DREI_MONATE;
            case R.id.darstellung_5:
                return ChartDataHelper.FUNF_MONATE;
            default:
                return ChartDataHelper.FUNF_MONATE;
        }
    }

    private void selectGeschlecht(int value) {
        switch (value){
            case ChartDataHelper.DREI_TAGE:
                darstellungRadioGroup.check(R.id.darstellung_1);
                break;
            case ChartDataHelper.EIN_WOCHE:
                darstellungRadioGroup.check(R.id.darstellung_2);
                break;
            case ChartDataHelper.EIN_MONAT:
                darstellungRadioGroup.check(R.id.darstellung_3);
                break;
            case ChartDataHelper.DREI_MONATE:
                darstellungRadioGroup.check(R.id.darstellung_4);
                break;
            case ChartDataHelper.FUNF_MONATE:
                darstellungRadioGroup.check(R.id.darstellung_5);
                break;
            default:
                break;
        }
    }
}
