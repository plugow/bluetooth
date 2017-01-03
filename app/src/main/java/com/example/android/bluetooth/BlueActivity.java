package com.example.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;



public class BlueActivity extends AppCompatActivity{


    private BluetoothAdapter mBluetoothAdapter;
    public static String EXTRA_ADDRESS = "device_address";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();


        ListView listView=(ListView) findViewById(R.id.list);
        ArrayList list2=pairedDevicesList();

        final ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent,view,position,id)->{
            Toast.makeText(getApplicationContext(),"dziala",Toast.LENGTH_LONG).show();
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(BlueActivity.this, JogActivity.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        });


    }

    private ArrayList pairedDevicesList()
    {
        Set<BluetoothDevice> pairedDevices;
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();


        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        return list;



    }

}
