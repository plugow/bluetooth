package com.example.android.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;


public class JogActivity extends AppCompatActivity {
    int velocity;
    boolean pressedUp;
    TextView velocityTextView;
    SeekBar velocityBar;
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
        setContentView(R.layout.jog_item);

        velocity=90;
        pressedUp = false;
        velocityTextView=(TextView) findViewById(R.id.velocityTextView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        Intent newint = getIntent();
        address = newint.getStringExtra(BlueActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device
        new ConnectBT().execute(); //Call the class to connect


        // full screen mode
        View decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOption);


        // checking and handling seekbar
        velocityBar = (SeekBar) findViewById(R.id.seekBar);





    }



    @Override
    protected void onResume() {
        super.onResume();

        velocityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                velocity=i+1;

                velocityTextView.setText("Velocity: "+velocity+"%");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Button rightRight=(Button) findViewById(R.id.rightRight);

        rightRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        if(pressedUp == false){
                            pressedUp = true;
                            new SendVolumeUpTask().execute();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        velocityTextView.setText("Velocity: "+velocity+"%");
                        pressedUp = false;

                }
                return true;
            }
        });







    }




    class SendVolumeUpTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            while (pressedUp) {

                try {
                    velocity += 1;
                    btSocket.getOutputStream().write(("alp://tone/6/"+velocity+"/255/").toString().getBytes());
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (IOException ee){
                    ee.printStackTrace();
                }

            }
            return null;
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
