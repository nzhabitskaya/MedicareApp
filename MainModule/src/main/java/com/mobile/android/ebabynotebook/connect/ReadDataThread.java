package com.mobile.android.ebabynotebook.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.mobile.android.ebabynotebook.ui.activity.SelectDeviceActivity;

public class ReadDataThread {
	BluetoothSocket mSocket;
	private final Handler mHandler;
	private InputStream mInStream;
	private OutputStream mOutStream;
	private byte[] message;
	
	private MyTimerTask myTask;
    private Timer myTimer;

	ReadDataThread(BluetoothSocket socket, Handler handler) {
		super();
		mSocket = socket;
		mHandler = handler;
		try {
			mInStream = mSocket.getInputStream();
			mOutStream = mSocket.getOutputStream();
		} catch (IOException e) {
			Log.e(ReadDataThread.class.getName(), e.getMessage());
		}
		
		myTask = new MyTimerTask();
	    myTimer = new Timer();
	}
	
	public void scheduleTimer(){
		myTimer.schedule(myTask, 0);
	}

	private void listenForMessage() {
		byte[] buffer = new byte[100];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mInStream.read(buffer);
                mHandler.obtainMessage(SelectDeviceActivity.DATA_RECEIVED, buffer).sendToTarget();
            } catch (IOException e) {
            	//Log.e(ReadDataThread.class.getName(), e.getMessage());
            }
        } 
	}
	
	private void write(byte[] bytes) {
		try {
			mOutStream.write(bytes);
		} catch (IOException e) {
			//Log.e(ReadDataThread.class.getName(), e.getMessage());
		}
	}

	private byte[] initMessage(){
		message = new byte[12];
		message[0] = 66;
		message[1] = 108;
		message[2] = 117;
		message[3] = 84;
		message[4] = 4;
		message[7] = -41;
		message[8] = 1;
		message[9] = -35;
		Log.e(ReadDataThread.class.getName(), "Write message " + message);
		return message;
	}
	
	private byte[] readDataMessage(){
		message = new byte[12];
		message[0] = 66;
		message[1] = 108;
		message[2] = 117;
		message[3] = 84;
		message[4] = 4;
		message[7] = 76;
		message[8] = 1;
		message[9] = 70;
		Log.e(ReadDataThread.class.getName(), "Write message " + message);
		return message;
	}
	
	private byte[] changeUnitMessage(){
		message = new byte[12];
		message[0] = 66;
		message[1] = 108;
		message[2] = 117;
		message[3] = 84;
		message[4] = 4;
		message[7] = 73;
		message[8] = 1;
		message[9] = 67;
		Log.e(ReadDataThread.class.getName(), "Write message " + message);
		return message;
	}
	
	class MyTimerTask extends TimerTask {
		public void run() {
			write(initMessage());  // First message init client
			listenForMessage();
	
			//write(readDataMessage()); // Second message reads temperature
			//listenForMessage();
	
			//write(changeUnitMessage()); // Third message change temperature unit to Celcium degree	
			//listenForMessage();
		}
	}
}
