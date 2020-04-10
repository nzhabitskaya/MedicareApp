package com.mobile.android.ebabynotebook.utils;

public class TempUtil {
	public static final int CELSIUS_UNIT = 0;
	public static final int FAHRENHEIT_UNIT = 1;
	
	public static float convertToFahrenheit(float celsiusData){
		return celsiusData * 1.8000f + 32.00f;
	}
	
	public static float convertTemperature(String data, int unit) {
		float temperatureData = Float.parseFloat(data) / 10;
		if(unit == TempUtil.FAHRENHEIT_UNIT){
			temperatureData = TempUtil.convertToFahrenheit(temperatureData);
		}
		return temperatureData;
	}
}
