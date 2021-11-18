package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.rms.Adapter.ProductAdapter;
import com.atlanta.rms.Models.Product;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    Button btnlogin,btnopensettings;
    RequestQueue requestQueue;
    String sIpAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnlogin=(Button)findViewById(R.id.btnlogin);
        requestQueue = Volley.newRequestQueue(this);
        btnopensettings=(Button)findViewById(R.id.btnopensettings);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sIpAddress.equals(""))
                {
                    Toast.makeText(MainActivity.this,"You must set ip address using settings!",Toast.LENGTH_LONG).show();
                    return;
                }
                LoadCompanyDetails();
            }
        });
        btnopensettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IPSettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    public void LoadCompanyDetails()
    {
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        if(sIpAddress.equals(""))
        {
            Toast.makeText(MainActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
            return;
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Company";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response!=null) {
                        Common.CompanyName = response.getString("CompanyName");
                        Common.Address1 = response.getString("Address1");
                        Common.Address2 = response.getString("Address2");
                        Common.Address3 = response.getString("Address3");
                        Common.CurrencySymbol = response.getString("CurrencySymbol");
                        Common.Country = response.getString("Country");
                        Common.NoofDecimals = response.getInt("NoofDecimals");
                        Common.NoofDecimalsQty=response.getInt("NoofDecimalinQty");
                        Common.sDecimals="%." + Common.NoofDecimals + "f";
                        Common.sDecimalsQty="%." + Common.NoofDecimalsQty + "f";
                        Intent intent = new Intent(MainActivity.this, WaiterListActivity.class);
                        startActivity(intent);
                    }
                }
                catch (Exception w)
                {
                    Toast.makeText(MainActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}