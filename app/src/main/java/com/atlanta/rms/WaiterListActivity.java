package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class WaiterListActivity extends AppCompatActivity {

    GridView grdWaiters;
    RequestQueue requestQueue;
    final ArrayList<Waiter> _waiters=new ArrayList<>();
    final ArrayList<Waiter> _waiterfiltered=new ArrayList<>();
    ArrayList<String> waiternames = new ArrayList<>();
    AutoCompleteTextView txtwaitersearch;
    String sIpAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_list);
        grdWaiters=(GridView) findViewById(R.id.grdwaiters);
        txtwaitersearch=(AutoCompleteTextView) findViewById(R.id.txtwaitersearch);
        requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        getWaiterList();
        WaiterAdapter adapter = new WaiterAdapter(WaiterListActivity.this, _waiters);
        grdWaiters.setAdapter(adapter);
        ArrayAdapter<String> _waiteradapter = new ArrayAdapter<String>(WaiterListActivity.this, android.R.layout.simple_list_item_1, waiternames.toArray(new String[waiternames.size()]));
        txtwaitersearch.setAdapter(_waiteradapter);
        txtwaitersearch.setThreshold(0);
        txtwaitersearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence str, int i, int i1, int i2) {

                _waiterfiltered.clear();
                for(Waiter _waiter:_waiters)
                {
                    if(_waiter.get_WaiterName().toUpperCase().startsWith(str.toString().toUpperCase())
                            || _waiter.get_WaiterName().toUpperCase().endsWith(str.toString().toUpperCase()))
                    {
                        _waiterfiltered.add(_waiter);
                    }
                }
                WaiterAdapter adapter = new WaiterAdapter(WaiterListActivity.this, _waiterfiltered);
                grdWaiters.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private  void getWaiterList()
    {
        waiternames.clear();
        _waiters.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Login";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    waiternames.clear();
                    _waiters.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Waiter _waiter=new Waiter();
                        _waiter.set_id(jsonObject.getInt("id"));
                        _waiter.set_WaiterCode(jsonObject.getString("WaiterCode"));
                        _waiter.set_WaiterName(jsonObject.getString("WaiterName"));
                        waiternames.add(_waiter.get_WaiterName());
                        _waiters.add(_waiter);
                    }
                }
                catch (Exception w)
                {
                    Toast.makeText(WaiterListActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WaiterListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}