package com.sam.ibeacon.app.ibeacon_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Samuel Tanor-Gyedu
 *
 */
public class MainActivity extends Activity {

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
	 */

	private Button On, Off, Visible, list, find;
	private BluetoothAdapter BA;
	private Set<BluetoothDevice> pairedDevices;
	private ListView lv;
	private TextView text;
	private static final int REQUEST_ENABLE_BT = 1;
	List<String> viewList = new ArrayList<String>();

	private ArrayAdapter<String> BTArrayAdapter;

	// private ListView myListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked);

		On = (Button) findViewById(R.id.button1);
		Off = (Button) findViewById(R.id.button2);
		Visible = (Button) findViewById(R.id.button3);
		list = (Button) findViewById(R.id.button4);
		find = (Button) findViewById(R.id.button5);
		text = (TextView) findViewById(R.id.text);

		lv = (ListView) findViewById(R.id.listView1);

		BA = BluetoothAdapter.getDefaultAdapter();

		On.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				on(v);

			}
		});

		Off.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				off(v);

			}
		});

		Visible.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				visible(v);

			}
		});

		list.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				list(v);

			}
		});

		find.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				find(v);

			}

		});

		lv.setAdapter(BTArrayAdapter);
		BTArrayAdapter.notifyDataSetChanged();

	}

	public void on(View view) {
		if (!BA.isEnabled()) {
			Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOn, 0);
			Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
		}
	}

	public void list(View view) {
		pairedDevices = BA.getBondedDevices();

		ArrayList list = new ArrayList();
		for (BluetoothDevice bt : pairedDevices)
			list.add(bt.getName());

		Toast.makeText(getApplicationContext(), "Showing Paired Devices ", Toast.LENGTH_SHORT).show();
		// final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, list);
		lv.setAdapter(BTArrayAdapter);

	}

	public void off(View view) {
		BA.disable();
		text.setText("Bluetooth off");
		Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
	}

	public void visible(View view) {
		Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(getVisible, 0);
	}

	public void find(View view) {

		if (BA.isDiscovering()) {

			// the button is pressed when it discovers, so cancel the discovery
			BA.cancelDiscovery();
		}

		else {
			// BTArrayAdapter.clear();

			BA.startDiscovery();

			registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
			BTArrayAdapter.notifyDataSetChanged();
			// BTArrayAdapter.notifyDataSetChanged();

			// List< String>list = viewList;
			// for (String name : list) {
			// BTArrayAdapter.add(name);
			// BTArrayAdapter.notifyDataSetChanged();
			// }

		}

	}

	final BroadcastReceiver bReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			// When discovery finds a device

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				// Get the BluetoothDevice object from the Intent

				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// add the name and the MAC address of the object to the arrayAdapter
				viewList.add(device.getName() + "\n" + device.getAddress());
				// BTArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, viewList);

				// BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				List<String> list = viewList;
				for (String name : list) {
					BTArrayAdapter.add(name);
					
				}
			}

		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO Auto-generated method stub

		if (requestCode == REQUEST_ENABLE_BT) {

			if (BA.isEnabled()) {

				text.setText("Status: Enabled");

			} else {

				text.setText("Status: Disabled");

			}

		}

	}

	@Override
	protected void onDestroy() {

		// TODO Auto-generated method stub

		super.onDestroy();

		unregisterReceiver(bReceiver);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.sam.ibeacon.app.ibeacon_app.R.menu.main, menu);
		return true;
	}

}
