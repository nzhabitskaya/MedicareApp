package com.mobile.android.ebabynotebook.connect;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.mobile.android.ebabynotebook.ui.activity.SelectDeviceActivity;

public class ConnectThread extends Thread {
	public static final int CONNECTION_TIMEOUT = 30000;
	
	private BluetoothSocket mBluetoothSocket;
	private boolean isConnecting;
	
	private final BluetoothDevice mDevice;
	private final Handler mHandler;
	private BluetoothAdapter mAdapter;	

	public ConnectThread(String deviceID, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mDevice = mAdapter.getRemoteDevice(deviceID);
		mHandler = handler;
	}

	public void run() {
		setSocketConnection();
	}
	
	private void setSocketConnection(){
		Method method;
		try {
			isConnecting = true;
			method = mDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
			mBluetoothSocket = (BluetoothSocket) method.invoke(mDevice, Integer.valueOf(1));
			mAdapter.cancelDiscovery();
			mBluetoothSocket.connect();
			
			if(isConnecting) {
	        	manageConnectedSocket();
			}
			
			makePause(CONNECTION_TIMEOUT);
			cancel();
	  			
		} catch (IOException e) {
			e.printStackTrace();
			mHandler.obtainMessage(SelectDeviceActivity.CONNECT_ERROR, e.getMessage()).sendToTarget();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public static void makePause(int milliseconds){
		try {
			Thread.sleep(CONNECTION_TIMEOUT);
		} catch (InterruptedException e) {
		}
	}
	
	public void manageConnectedSocket() {
		ReadDataThread conn = new ReadDataThread(mBluetoothSocket, mHandler);
		mHandler.obtainMessage(SelectDeviceActivity.SOCKET_CONNECTED, conn).sendToTarget();
	}

	public void cancel() {
		try {
			mBluetoothSocket.close();
		} catch (IOException e) {
		}
	}
}
