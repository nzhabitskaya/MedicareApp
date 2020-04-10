package com.mobile.android.ebabynotebook.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class TempAlarm {
	private static TempAlarm instance;
	private MediaPlayer mMediaPlayer;
	private Vibrator vibrator;
	
	class PhoneThread extends Thread {
		boolean isRunning;
		final TempAlarm tempAlarm;

		public void run() {
			try {
				Thread.sleep(30000L);
			} catch (Exception exception) {
			}
			if (isRunning)
			stopPhone();
		}

		public void stopPhone() {
			isRunning = false;
		}
		
		PhoneThread() {
			tempAlarm = TempAlarm.this;
			isRunning = true;
		}
	}

	public static TempAlarm getInstance() {
		if (instance == null)
			instance = new TempAlarm();
		return instance;
	}

	public void init(Context context) {
		vibrator = (Vibrator) context.getSystemService("vibrator");
		Uri uri = RingtoneManager.getDefaultUri(4);
		if (uri == null) {
			uri = RingtoneManager.getDefaultUri(2);
			if (uri == null)
				uri = RingtoneManager.getDefaultUri(1);
		}
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, uri);
			if (((AudioManager) context.getSystemService("audio")).getStreamVolume(4) != 0) {
				mMediaPlayer.setAudioStreamType(4);
				mMediaPlayer.setLooping(true);
			}
			return;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean sound() {
		try {
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
	}

	public void stopAlarm() {
		if (mMediaPlayer != null)
			mMediaPlayer.stop();
		if (vibrator != null)
			vibrator.cancel();
	}

	public void vibrate() {
		Vibrator vibrator1 = vibrator;
		long al[] = new long[2];
		al[1] = 60000L;
		vibrator1.vibrate(al, 0);
	}
}
