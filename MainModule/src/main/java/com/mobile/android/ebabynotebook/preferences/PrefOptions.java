package com.mobile.android.ebabynotebook.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefOptions {
	public static final String Key_alarm_phone = "alarm_phone";
	public static final String Key_alarm_sound = "alarm_sound";
	public static final String Key_alarm_vibrate = "alarm_vibrate";
	public static final String Key_device_address = "device_address";
	public static final String Key_device_name = "device_name";
	public static final String Key_phone = "phone";
	public static final String Key_sms = "sms";
	public static final String Key_temp_alarm = "temp_alarm";
	public static final String Key_temp_unit = "temp_unit";
	public static final String PREFS_NAME = "prefs_tempmeter";
	private static PrefOptions instance;
	private Context mContext;
	private SharedPreferences prefs;
	private Editor prefs_editor;

	private PrefOptions(Context context) {
		mContext = context;
		prefs = context.getSharedPreferences("prefs_tempmeter", 0);
		prefs_editor = prefs.edit();
	}

	public static void init(Context context1, OptionsData optionsdata) {
		if (instance == null)
			instance = new PrefOptions(context1);
		initOptions(optionsdata);
	}

	public static void initOptions(OptionsData optionsdata) {
		instance.initOptionsData(optionsdata);
	}

	private void initOptionsData(OptionsData optionsdata) {
		optionsdata.setAlarmPhone(prefs.getBoolean("alarm_phone", true));
		optionsdata.setAlarmSound(prefs.getBoolean("alarm_sound", true));
		optionsdata.setAlarmVibrate(prefs.getBoolean("alarm_vibrate", true));
		optionsdata.setPhone(prefs.getString("phone", ""));
		optionsdata.setTempUnit(prefs.getInt("temp_unit", 0));
		optionsdata.setTempAlarm(prefs.getFloat("temp_alarm", 100.4f));
	}

	public static void saveOptions(OptionsData optionsdata) {
		instance.prefs_editor.putBoolean("alarm_phone", optionsdata.isAlarmPhone());
		instance.prefs_editor.putBoolean("alarm_sound", optionsdata.isAlarmSound());
		instance.prefs_editor.putBoolean("alarm_vibrate", optionsdata.isAlarmVibrate());
		instance.prefs_editor.putString("phone", optionsdata.getPhone());
		instance.prefs_editor.putInt("temp_unit", optionsdata.getTempUnit());
		instance.prefs_editor.putFloat("temp_alarm", optionsdata.getTempAlarm());
		
		instance.prefs_editor.putString("device_address", optionsdata.getDeviceAddress());
		instance.prefs_editor.putString("device_name", optionsdata.getDeviceName());
		instance.prefs_editor.commit();
	}
}
