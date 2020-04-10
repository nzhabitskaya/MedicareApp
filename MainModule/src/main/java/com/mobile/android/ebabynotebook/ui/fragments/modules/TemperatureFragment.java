package com.mobile.android.ebabynotebook.ui.fragments.modules;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.ui.tabs.IconTabActivity;

public class TemperatureFragment extends Fragment {
    private View view;
    private Button manuell;
    private Button graphics;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_temperature, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        manuell = (Button) view.findViewById(R.id.btn_mannuell);
        manuell.setOnClickListener(listener);
        graphics = (Button) view.findViewById(R.id.btn_graphische_dartstellung);
        graphics.setOnClickListener(listener);
    }
    View.OnClickListener listener = new View.OnClickListener(){
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_mannuell:
                    switchFragment(IconTabActivity.MANUELL_FRAGMENT);
                    break;
                case R.id.btn_graphische_dartstellung:
                    switchFragment(IconTabActivity.TEMPERATUR_CHART_FRAGMENT);
                    break;

            }
        }
    };

    private void switchFragment(int fragmentId){
        IconTabActivity activity = (IconTabActivity) getActivity();
        activity.replaceFragment(IconTabActivity.TEMPERATURE_FRAGMENT, fragmentId);
    }
}