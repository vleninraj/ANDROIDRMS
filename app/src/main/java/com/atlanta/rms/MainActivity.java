package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;

public class MainActivity extends AppCompatActivity {

    EditText txtipaddress;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtipaddress=(EditText) findViewById(R.id.txtipaddress);
        btnlogin=(Button)findViewById(R.id.btnlogin);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        txtipaddress.setText(ipAddress.getString("ipaddress", ""));
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor ipAddressEditor = ipAddress.edit();
                ipAddressEditor.putString("ipaddress", txtipaddress.getText().toString().trim());
                ipAddressEditor.apply();

           /*     Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            */

            }
        });

    }
}