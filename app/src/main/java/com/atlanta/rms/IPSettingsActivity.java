package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IPSettingsActivity extends AppCompatActivity {

    EditText txtipaddress;
    Button btnsavesettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsettings);
        txtipaddress=(EditText) findViewById(R.id.txtipaddress);
        btnsavesettings=(Button)findViewById(R.id.btnsavesettings);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        txtipaddress.setText(ipAddress.getString("ipaddress", ""));
        btnsavesettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtipaddress.getText().toString().trim().equals(""))
                {
                    Toast.makeText(IPSettingsActivity.this,"IP can't be blank!",Toast.LENGTH_LONG).show();
                    return;
                }
                SharedPreferences.Editor ipAddressEditor = ipAddress.edit();
                ipAddressEditor.putString("ipaddress", txtipaddress.getText().toString().trim());
                ipAddressEditor.apply();
                Toast.makeText(IPSettingsActivity.this,"IP Settings Saved...",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}