package com.example.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
    private int velocity;
    private Integer sentVelocity;
    boolean pressedUp;
    boolean isPlus;
    private TextView velocityTextView;
    private SeekBar velocityBar;
    String address = null;

    private Button firstPlus;
    private Button firstMinus;
    private Button secondPlus;
    private Button secondMinus;
    private Button thirdPlus;
    private Button thirdMinus;

    private int angleValue1;
    private int angleValue2;
    private int angleValue3;
    private int angleValue4;

    private int xValue;
    private int yValue;
    private int zValue;


    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jog_item);

        // set id to variables
        velocityBar = (SeekBar) findViewById(R.id.seekBar);
        firstPlus =(Button) findViewById(R.id.firstPlus);
        firstMinus =(Button) findViewById(R.id.firstMinus);
        secondPlus =(Button) findViewById(R.id.secondPlus);
        secondMinus =(Button) findViewById(R.id.secondMinus);
        thirdPlus =(Button) findViewById(R.id.thirdPlus);
        thirdMinus =(Button) findViewById(R.id.thirdMinus);
        velocityTextView=(TextView) findViewById(R.id.velocityTextView);


        // initialize values of variables
        velocity=1;
        sentVelocity=100;
        angleValue1=90;
        angleValue2=90;
        angleValue3=90;
        angleValue4=90;
        pressedUp = false;
        isPlus=true;


        // data for bluetooth handling from BlueActivity
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








    }



    @Override
    protected void onResume() {
        super.onResume();

        velocityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                velocity=i+1;
                sentVelocity=101-velocity;

               velocityTextView.setText("Velocity: "+velocity+"%");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        firstPlus.setOnTouchListener((view, motionEvent) ->{

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        isPlus=true;
                        if(!pressedUp){
                            pressedUp = true;
                            new SendPosition().execute(1,1,sentVelocity);
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        pressedUp = false;

                }
                return true;

        });

        firstMinus.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    isPlus=false;
                    if(!pressedUp){
                        pressedUp = true;
                        new SendPosition().execute(1,0,sentVelocity);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    pressedUp = false;

            }
            return true;
        });

        secondPlus.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    isPlus=false;
                    if(!pressedUp){
                        pressedUp = true;
                        new SendPosition().execute(2,1,sentVelocity);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    pressedUp = false;

            }
            return true;
        });

        secondMinus.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    isPlus=false;
                    if(!pressedUp){
                        pressedUp = true;
                        new SendPosition().execute(2,0,sentVelocity);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    pressedUp = false;

            }
            return true;
        });

        thirdPlus.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {


                case MotionEvent.ACTION_DOWN:

                    isPlus=false;
                    if(!pressedUp){
                        pressedUp = true;
                        new SendPosition().execute(3,1,sentVelocity);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    pressedUp = false;

            }
            return true;
        });

        thirdMinus.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    isPlus=false;
                    if(!pressedUp){
                        pressedUp = true;
                        new SendPosition().execute(3,0,sentVelocity);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    pressedUp = false;

            }
            return true;
        });












    }




    class SendPosition extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            while (pressedUp) {


                try {
                    if(integers[0]==1){
                        if (integers[1]==1)angleValue1 += 1;
                        else angleValue1 -= 1;
                        String msg=blueMessage(1,angleValue1,255);
                        btSocket.getOutputStream().write(msg.getBytes());
                        btSocket.getOutputStream().flush();
                        Thread.sleep(integers[2]);
                    }

                    if(integers[0]==2){
                        if (integers[1]==1)angleValue2 += 1;
                        else angleValue2 -= 1;
                        String msg=blueMessage(2,angleValue2,255);
                        btSocket.getOutputStream().write(msg.getBytes());
                        btSocket.getOutputStream().flush();
                        Thread.sleep(integers[2]);
                    }

                    if(integers[0]==3){
                        if (integers[1]==1)angleValue3 += 1;
                        else angleValue3 -= 1;
                        String msg=blueMessage(3,angleValue3,255);
                        btSocket.getOutputStream().write(msg.getBytes());
                        btSocket.getOutputStream().flush();
                        Thread.sleep(integers[2]);
                    }




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

//    class SendPositionMinus extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            while (pressedUp) {
//
//                try {
//                    angleValue1 -= 1;
//                    String msg=blueMessage(1,angleValue1,velocity);
//                    btSocket.getOutputStream().write(msg.getBytes());
//                    btSocket.getOutputStream().flush();
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                catch (IOException ee){
//                    ee.printStackTrace();
//                }
//
//            }
//            return null;
//        }
//    }



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


    private String blueMessage(int pin, int angle, int velocity){
        StringBuilder builder = new StringBuilder("alp://tone/");
        builder.append(pin);
        builder.append("/");
        builder.append(angle);
        builder.append("/");
        builder.append(velocity);
        builder.append("\n");
        String msg = builder.toString();
        return msg;


    }









}
