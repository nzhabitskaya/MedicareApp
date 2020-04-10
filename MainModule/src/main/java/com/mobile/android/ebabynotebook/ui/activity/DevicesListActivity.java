package com.mobile.android.ebabynotebook.ui.activity;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.android.ebabynotebook.R;

public class DevicesListActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter = null;
	ArrayAdapter<String> mArrayAdapter = null;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mArrayAdapter.add(device.getName() + " (" + device.getAddress() + ")");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		setContentView(R.layout.select_devices);
		ListView deviceList = (ListView) findViewById(R.id.devices_list);

		TextView footer = new TextView(this);
		footer.setText(R.string.discover_more_devices);
		footer.setTextSize(22);
		footer.setGravity(Gravity.CENTER_HORIZONTAL);
		footer.setPadding(5, 5, 5, 5);
		deviceList.setFooterDividersEnabled(true);
		deviceList.addFooterView(footer, null, true);

		final List<String> devices = getIntent().getStringArrayListExtra("devices");
		mArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, devices);
		deviceList.setAdapter(mArrayAdapter);
		deviceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				if (parent.getAdapter().getItemViewType(pos) == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
					mBluetoothAdapter.startDiscovery();
				} else {
					String tmp = (String) parent.getItemAtPosition(pos);
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(tmp.split("\n")[1]);
					Intent data = new Intent();
					data.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
	}

	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
	}

	protected void onPause() {
		unregisterReceiver(mReceiver);
		super.onPause();
	}
}
