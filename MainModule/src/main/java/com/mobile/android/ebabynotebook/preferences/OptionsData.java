package com.mobile.android.ebabynotebook.preferences;

public class OptionsData {
	private boolean alarm_phone;
	private boolean alarm_sound;
	private boolean alarm_vibrate;
	private String deviceAddress;
	private String deviceName;
	private String phone;
	private float tempAlarm;
	private int tempUnit;

	public OptionsData() {
		tempUnit = 0;
		tempAlarm = 380;
		phone = "";
		alarm_sound = false;
		alarm_phone = false;
		alarm_vibrate = false;
		deviceAddress = "";
		deviceName = "";
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getPhone() {
		return phone;
	}

	public float getTempAlarm() {
		return tempAlarm;
	}

	public int getTempUnit() {
		return tempUnit;
	}

	public boolean isAlarmPhone() {
		return alarm_phone;
	}

	public boolean isAlarmSound() {
		return alarm_sound;
	}

	public boolean isAlarmVibrate() {
		return alarm_vibrate;
	}

	public void setAlarmPhone(boolean flag) {
		alarm_phone = flag;
	}

	public void setAlarmSound(boolean flag) {
		alarm_sound = flag;
	}

	public void setAlarmVibrate(boolean flag) {
		alarm_vibrate = flag;
	}

	public void setPhone(String phoneStr) {
		phone = phoneStr;
	}

	public void setTempAlarm(float tempAlarmF) {
		tempAlarm = tempAlarmF;
	}

	public void setTempUnit(int tempUnitInt) {
		tempUnit = tempUnitInt;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
}
