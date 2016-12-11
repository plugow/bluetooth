package com.example.android.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Pawel on 2016-11-03.
 */
public class LedActivity extends AppCompatActivity{
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.led_item);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        Intent newint = getIntent();
        address = newint.getStringExtra(BlueActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device
        new ConnectBT().execute(); //Call the class to connect


        TextView ledOn = (TextView) findViewById(R.id.on);
        TextView ledOff = (TextView) findViewById(R.id.off);

        ledOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOn();
            }
        });

        ledOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOff();
            }
        });

        View decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    |View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        decorView.setSystemUiVisibility(uiOption);




    }
    private void turnOn()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("*10|5|3#".toString().getBytes());
            }
            catch (IOException e)
            {

            }
        }
    }
    private void turnOff()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("*10|5|2#".toString().getBytes());
            }
            catch (IOException e)
            {

            }
        }
    }





    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected Void doInBackground(Void... voids) {

            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }


            return null;
        }
    }
}
