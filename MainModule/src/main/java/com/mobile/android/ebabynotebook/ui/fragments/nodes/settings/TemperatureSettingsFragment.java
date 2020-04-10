package com.mobile.android.ebabynotebook.ui.fragments.nodes.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.preferences.OptionsData;
import com.mobile.android.ebabynotebook.preferences.PrefOptions;
import com.mobile.android.ebabynotebook.utils.TempUtil;

public class TemperatureSettingsFragment extends Fragment {
	protected OptionsData optionsData;
	private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.temperature_settings, container, false);
        initSettings(view);
        return view;
    }
	
	private void initSettings(View view) {
		this.optionsData = new OptionsData();
		PrefOptions.init(getActivity(), this.optionsData);
		
		RadioButton rb1 = (RadioButton) view.findViewById(R.id.rb_tempC);
		RadioButton rb2 = (RadioButton) view.findViewById(R.id.rb_tempF);
		int tempUnit = optionsData.getTempUnit();
		if(tempUnit == TempUtil.CELSIUS_UNIT)
			rb1.setChecked(true);
		else
			rb2.setChecked(true);
		EditText et = ((EditText) view.findViewById(R.id.phone));
		et.setText(optionsData.getPhone());
		EditText et2 = ((EditText) view.findViewById(R.id.highTemp));
		et2.setText(String.valueOf(optionsData.getTempAlarm()));
		CheckBox cb1 = ((CheckBox) view.findViewById(R.id.alarm_sound));
		cb1.setChecked(optionsData.isAlarmSound());
		CheckBox cb2 = ((CheckBox) view.findViewById(R.id.alarm_phone));
		cb2.setChecked(optionsData.isAlarmPhone());
		CheckBox cb3 = ((CheckBox) view.findViewById(R.id.alarm_vibration));
		cb3.setChecked(optionsData.isAlarmVibrate());
		
		Button saveButton = (Button) view.findViewById(R.id.save_settings);
		saveButton.setOnClickListener(onClickListener);
	}
	
	private void saveSettings(View view) {
	    EditText localEditText1 = (EditText) view.findViewById(R.id.phone);
        optionsData.setPhone(localEditText1.getText().toString());
        CheckBox localCheckBox1 = (CheckBox) view.findViewById(R.id.alarm_sound);
        optionsData.setAlarmSound(localCheckBox1.isChecked());
        CheckBox localCheckBox2 = (CheckBox) view.findViewById(R.id.alarm_phone);
        optionsData.setAlarmPhone(localCheckBox2.isChecked());
        CheckBox localCheckBox3 = (CheckBox) view.findViewById(R.id.alarm_vibration);
        optionsData.setAlarmVibrate(localCheckBox3.isChecked());

        EditText localEditText2 = (EditText) view.findViewById(R.id.highTemp);
        optionsData.setTempAlarm(Float.valueOf(localEditText2.getText().toString()));
        RadioButton rb1 = (RadioButton) view.findViewById(R.id.rb_tempC);
        optionsData.setTempUnit(rb1.isChecked() ? 0 : 1);
        
        PrefOptions.saveOptions(this.optionsData);
    }
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		public void onClick(View paramView) {
			switch (paramView.getId()) {
				case R.id.save_settings:
					saveSettings(view);
					Toast.makeText(getActivity(), "Changes saved.", 1000).show();
					return;
				default:
					return;
			}
		}
	};
}