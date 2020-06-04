package com.example.bluetoothconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    CheckBox enable_bt,visible_bt;
    ImageView search_bt;
    ListView listView;
    TextView tvName;

    private BluetoothAdapter BA;

    Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         enable_bt=findViewById(R.id.enable_bt);
         listView=findViewById(R.id.listView);
         visible_bt=findViewById(R.id.visible_bt);
         search_bt=findViewById(R.id.search_bt);
         tvName=findViewById(R.id.tvName);

         tvName.setText(getLocalBluetoothName());

        BA = BluetoothAdapter.getDefaultAdapter();
         if(BA==null)
         {
             Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
         }

         if(BA.isEnabled())
         {
            enable_bt.setChecked(true);
         }
         enable_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if(!b)
                 {
                    BA.disable();
                     Toast.makeText(MainActivity.this, "Turned OFF", Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     Intent IntentOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                     startActivityForResult(IntentOn, 0);
                     Toast.makeText(MainActivity.this, "Turned ON", Toast.LENGTH_SHORT).show();
                 }
             }
         });

         visible_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                 if(b)
                 {
                     Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                     //getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 400);
                     startActivityForResult(getVisible,0);
                     Toast.makeText(MainActivity.this, "Visible for 2 min", Toast.LENGTH_SHORT).show();
                 }
             }
         });

         search_bt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 list();
             }
         });
    }

    private void list() {
        pairedDevices=BA.getBondedDevices();
        ArrayList list = new ArrayList();

            for(BluetoothDevice bt: pairedDevices)
            {
                String devicename = bt.getName();
                //String macAddress = bt.getAddress();
                list.add("Name: "+devicename);
            }

            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);

    }

    public String getLocalBluetoothName()
    {
        if(BA==null)
        {
            BA = BluetoothAdapter.getDefaultAdapter();
        }
        String name=BA.getName();
        if(name==null)
        {
            name=BA.getAddress();
        }
        return name;
    }

}
