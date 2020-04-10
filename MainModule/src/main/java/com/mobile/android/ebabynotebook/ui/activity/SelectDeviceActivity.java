package com.mobile.android.ebabynotebook.ui.activity;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.connect.ConnectThread;
import com.mobile.android.ebabynotebook.connect.ReadDataThread;
import com.mobile.android.ebabynotebook.ui.tabs.IconTabActivity;
import com.mobile.android.ebabynotebook.utils.Constants;

public class SelectDeviceActivity extends Activity {
	private static final String TAG = SelectDeviceActivity.class.getName();
	public static final UUID APP_UUID = UUID
			.fromString("143FC143-A8BD-E867-F851-98448C31B8A5");

	private static final int REQUEST_ENABLE_BT = 0;
	private static final int SELECT_SERVER = 1;

	public static final int DATA_RECEIVED = 0;
	public static final int SOCKET_CONNECTED = 1;
	public static final int CONNECT_ERROR = 2;

	private ConnectThread socketConnection;
	private ReadDataThread readData;
	private byte data[];
	
	private MyTimerTask myTask;
    private Timer myTimer;
    private String mDeviceId;
    private boolean isConnect = false;

	private Button selectDeviceButton;
	private BluetoothAdapter mBluetoothAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Bluetooth not supported");
			finish();
		}

		setContentView(R.layout.select_device);

		selectDeviceButton = (Button) findViewById(R.id.client_button);
		selectDeviceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectServer();
			}
		});

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetoothIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
		} else {
			selectDeviceButton.setEnabled(true);
		}

		myTask = new MyTimerTask();
	    myTimer = new Timer();
	}

	private void selectServer() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		ArrayList<String> pairedDeviceStrings = new ArrayList<String>();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				pairedDeviceStrings.add(device.getName() + "\n"
						+ device.getAddress());
			}
		}
		Intent showDevicesIntent = new Intent(this, DevicesListActivity.class);
		showDevicesIntent.putStringArrayListExtra("devices",
				pairedDeviceStrings);
		startActivityForResult(showDevicesIntent, SELECT_SERVER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		selectDeviceButton.setEnabled(false);
		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			selectDeviceButton.setEnabled(true);
		} else if (requestCode == SELECT_SERVER && resultCode == RESULT_OK) {
			BluetoothDevice device = data
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			// Start connection to device
			mDeviceId = device.getAddress();
			//connectToBluetoothServer(mDeviceId);
			myTimer.schedule(myTask, 0, ConnectThread.CONNECTION_TIMEOUT * 2);
		}
	}

	/**
	 * Create socket connection to thermometer
	 */
	private void connectToBluetoothServer(String deviceId) {
		Log.e(TAG, "Connecting to Server " + deviceId);
		socketConnection = new ConnectThread(deviceId, mHandler);
		socketConnection.start();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case CONNECT_ERROR: {
				showErrorDialog();
				break;
			}
			case SOCKET_CONNECTED: {
				isConnect = true;
				
				// Begin to read data
				readData = (ReadDataThread) message.obj;
				readData.scheduleTimer();

				// Open tabbed activity
				Intent tabActivity = new Intent(SelectDeviceActivity.this, IconTabActivity.class);
				startActivity(tabActivity);
				break;
			}
			case DATA_RECEIVED: {
				data = (byte[]) message.obj;
				convertData(data);
				isConnect = false;
			}
			default:
				break;
			}
		}
	};

	private void notifyDataReceived(float temperature) {
		Intent intent = new Intent();
		intent.putExtra(Constants.TEMPERATURE, temperature);
		intent.setAction(Constants.ACTION_TEMP_RECEIVED);
		sendBroadcast(intent);
	}

	private void convertData(byte abyte0[]) {
		float curTemp;
		if (abyte0[12] >= 0)
			curTemp = 256 * abyte0[13] + abyte0[12];
		else
			curTemp = 256 + (256 * abyte0[13] + abyte0[12]);
		Log.e(SelectDeviceActivity.class.getName(), "curTemp: "
				+ (curTemp / 10f));
		notifyDataReceived(curTemp);
	}

	private void showErrorDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(R.string.warning);
		alertDialog.setMessage(getString(R.string.msg_connection_error));
		alertDialog.setButton(getString(R.string.close),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
						selectDeviceButton.setEnabled(true);
					}
				});
		alertDialog.show();
	}
	
	class MyTimerTask extends TimerTask {
		public void run() {
			if(!isConnect)
				connectToBluetoothServer(mDeviceId);
		}
	}
}