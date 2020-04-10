package com.mobile.android.ebabynotebook.ui.fragments.modules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.ui.tabs.IconTabActivity;

public class WeightFragment extends Fragment {
    private View view;
    private Button btn_mannuell;
    private Button btn_graphics;
    private Button btn_wachstumuberwachung;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_weight, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        btn_mannuell = (Button) view.findViewById(R.id.btn_mannuell);
        btn_graphics = (Button) view.findViewById(R.id.btn_graphische_dartstellung);
        btn_wachstumuberwachung = (Button) view.findViewById(R.id.btn_wachstumuberwachung);
        btn_mannuell.setOnClickListener(listener);
        btn_graphics.setOnClickListener(listener);
        btn_wachstumuberwachung.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener(){
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_mannuell:
                    switchFragment(IconTabActivity.MANUELL_ERFASSEN_FRAGMENT);
                    break;
                case R.id.btn_graphische_dartstellung:
                    switchFragment(IconTabActivity.GRAPHISCHE_DARTSTELLUNG_FRAGMENT);
                    break;
                case R.id.btn_wachstumuberwachung:
                    switchFragment(IconTabActivity.WEIGHT_CHART_FRAGMENT);
                    break;
            }
        }
    };

    private void switchFragment(int fragmentId){
        IconTabActivity activity = (IconTabActivity) getActivity();
        activity.replaceFragment(IconTabActivity.WEIGHT_FRAGMENT, fragmentId);
    }
}