package com.mobile.android.chart.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataManager {
	private static DataManager manager;
	private String oauthToken;
	private String oauthSecret;

	// Wfa data
	private WfaData wfaData;

	DataManager(){
	}
	
	public static DataManager getInstance(){
		if(manager == null)
			manager = new DataManager();
		return manager;
	}

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
    }

	public String getAccessToken(Context context) {
		return restoreFieldFromSharedPrefs(context, "access_token", "");
	}

	public void setAccessToken(Context context, String oauthToken) {
		saveFieldToSharedPrefs(context, "access_token", oauthToken);
	}

	public String getOauthSecret() {
		return oauthSecret;
	}

	public void setOauthSecret(String oauthSecret) {
		this.oauthSecret = oauthSecret;
	}

	public void setUserId(Context context, String userId) {
		saveFieldToSharedPrefs(context, "user_id", userId);
	}

	public String getUserId(Context context) {
		return restoreFieldFromSharedPrefs(context, "user_id", "");
	}

	public void setWfaData(WfaData wfaData) {
		this.wfaData = wfaData;
	}

	public double[] getWfaDays() {
		printWfaDays(wfaData.getDayValues());
		return wfaData.getDayValues();
	}

    public double[] getWfaAverages() {
        return wfaData.getAverageValues();
    }

	public double[] getWfaMins() {
		return wfaData.getMinValues();
	}

	public double[] getWfaMaxs() {
		return wfaData.getMaxValues();
	}

	private void saveFieldToSharedPrefs(Context context, String key, String value){
		SharedPreferences.Editor editor = context.getSharedPreferences("com.mobile.android.ebabynotebook", Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.apply();
	}

	private String restoreFieldFromSharedPrefs(Context context, String key, String defaultValue){
		SharedPreferences prefs = context.getSharedPreferences("com.mobile.android.ebabynotebook", Context.MODE_PRIVATE);
		return prefs.getString(key, defaultValue);
	}

	private void printWfaDays(double[] wfaDays){
		for(int i = 0; i < wfaDays.length; i++){
			Log.d("", "" + "day " + i + ": " + wfaDays[i]);
		}
	}
}
