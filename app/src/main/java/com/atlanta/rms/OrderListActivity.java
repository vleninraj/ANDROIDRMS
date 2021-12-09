package com.atlanta.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.rms.Adapter.FragmentAdapter;
import com.atlanta.rms.Models.Party;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderListActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Button btnneworder;
    TabLayout tbp;
    ViewPager2 pager2;
    FragmentAdapter fradapter;
    String sIpAddress="";
    final ArrayList<String> _partynames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        btnneworder=findViewById(R.id.btnneworder);
        tbp=(TabLayout)findViewById(R.id.tbmain);
        pager2=findViewById(R.id.vwpager);
        requestQueue = Volley.newRequestQueue(this);
        FragmentManager fm=getSupportFragmentManager();
        fradapter=new FragmentAdapter(fm,getLifecycle());
        pager2.setAdapter(fradapter);
        tbp.addTab(tbp.newTab().setText("Un Billed"));
        tbp.addTab(tbp.newTab().setText("Billed"));
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        tbp.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tbp.selectTab(tbp.getTabAt(position));
            }
        });
        btnneworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderListActivity.this, NewOrderActivity.class);
                intent.putExtra("PartyID",0);
                intent.putExtra("PartyName","");
                intent.putExtra("ArabicName","");
                intent.putExtra("MobileNumber","");
                intent.putExtra("NewRecord",true);
                intent.putExtra("TableName",Common.selectedTableName);
                intent.putExtra("TableID",Common.selectedTableID);
                intent.putExtra("VehicleName","");
                intent.putExtra("VehicleNumber","");
                startActivity(intent);
            }
        });


       // getOrderList();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // getOrderList();

    }
}