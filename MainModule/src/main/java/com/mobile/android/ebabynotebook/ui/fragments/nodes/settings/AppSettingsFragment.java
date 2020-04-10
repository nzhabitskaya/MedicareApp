package com.mobile.android.ebabynotebook.ui.fragments.nodes.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.mobile.android.ebabynotebook.R;

public class AppSettingsFragment extends Fragment {
    private ToggleButton module1;
    private ToggleButton module2;
    private ToggleButton module3;
    private ToggleButton module4;
    private ToggleButton module5;
    private ToggleButton module6;
    private ToggleButton module7;
    private ToggleButton module8;
    private Button buttonSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_settings, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        restoreFieldsFromSharedPrefs();
    }

    private void initViews(View view){
        module1 = (ToggleButton) view.findViewById(R.id.toggle_btn_waage);
        module2 = (ToggleButton) view.findViewById(R.id.toggle_btn_thermometer);
        module3 = (ToggleButton) view.findViewById(R.id.toggle_btn_medikament);
        module4 = (ToggleButton) view.findViewById(R.id.toggle_btn_babyflasche);
        module5 = (ToggleButton) view.findViewById(R.id.toggle_btn_spritze);
        module6 = (ToggleButton) view.findViewById(R.id.toggle_btn_schlafmuetze);
        module7 = (ToggleButton) view.findViewById(R.id.toggle_btn_foto);
        module8 = (ToggleButton) view.findViewById(R.id.toggle_btn_planning);
        buttonSave = (Button) view.findViewById(R.id.btn_save);
        buttonSave.setOnClickListener(listener);
        module1.setOnClickListener(listener);
        module2.setOnClickListener(listener);
        module3.setOnClickListener(listener);
        module4.setOnClickListener(listener);
        module5.setOnClickListener(listener);
        module6.setOnClickListener(listener);
        module7.setOnClickListener(listener);
        module8.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener(){
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_save:
                    saveFieldsToSharedPrefs();
                    getActivity().finish();
                    break;
                default:
                    saveFieldsToSharedPrefs();
                    break;
            }
        }
    };

    private void saveFieldsToSharedPrefs(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.mobile.android.ebabynotebook", Context.MODE_PRIVATE).edit();
        editor.putBoolean("module1", module1.isChecked());
        editor.putBoolean("module2", module2.isChecked());
        editor.putBoolean("module3", module3.isChecked());
        editor.putBoolean("module4", module4.isChecked());
        editor.putBoolean("module5", module5.isChecked());
        editor.putBoolean("module6", module6.isChecked());
        editor.putBoolean("module7", module7.isChecked());
        editor.putBoolean("module8", module8.isChecked());
        editor.apply();
    }

    private void restoreFieldsFromSharedPrefs(){
        SharedPreferences prefs = getActivity().getSharedPreferences("com.mobile.android.ebabynotebook", Context.MODE_PRIVATE);
        module1.setChecked(prefs.getBoolean("module1", false));
        module2.setChecked(prefs.getBoolean("module2", false));
        module3.setChecked(prefs.getBoolean("module3", false));
        module4.setChecked(prefs.getBoolean("module4", false));
        module5.setChecked(prefs.getBoolean("module5", false));
        module6.setChecked(prefs.getBoolean("module6", false));
        module7.setChecked(prefs.getBoolean("module7", false));
        module8.setChecked(prefs.getBoolean("module8", false));
    }
}
