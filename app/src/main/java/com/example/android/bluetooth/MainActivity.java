package com.example.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private BluetoothAdapter mBluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter==null){
            Toast.makeText(getApplicationContext(),"not suppurted",Toast.LENGTH_LONG).show();
        }


        if (!mBluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);

        }





        //find TextViews

        TextView Blue=(TextView) findViewById(R.id.bluetooth);
        TextView Pin=(TextView) findViewById(R.id.pin);


        Blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent blueIntent = new Intent(MainActivity.this,BlueActivity.class);
                // Start the new activity
                startActivity(blueIntent);

            }
        });











    }
}
