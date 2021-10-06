package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class TableActivity extends AppCompatActivity {

    GridView grdtables;
    RequestQueue requestQueue;
    final ArrayList<Floor> _floors=new ArrayList<>();
    final ArrayList<Table> _tables=new ArrayList<>();
    final ArrayList<Table> _tablesfiltered=new ArrayList<>();
    ArrayList<String> floors = new ArrayList<>();
    AutoCompleteTextView txtfloor;
    ArrayList<String> floornames = new ArrayList<>();
    String sIpAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        grdtables=(GridView) findViewById(R.id.grdtables);
        txtfloor=(AutoCompleteTextView) findViewById(R.id.txtfloor);
        requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        getFloorList();
        txtfloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sfloorname=txtfloor.getText().toString();
                if(sfloorname.equals("")){ return;}
                Floor _floor= Common._floors.get(sfloorname);
                if(_floor!=null)
                {
                    getTableList(_floor.get_id());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        grdtables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolderTable holder=(ViewHolderTable) view.getTag();
                Common.selectedTableName=  holder.txttablename.getText().toString();
                Common.selectedTableID=holder.txttablename.getTag().toString();
                finish();
            }
        });

    }

    private void getFloorList()
    {
        floors.clear();
        floornames.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Floor";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _floors.clear();
                    floornames.clear();
                    Common._floors=new Hashtable<>();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Floor _floor=new Floor();
                        _floor.set_id(jsonObject.getInt("id"));
                        _floor.set_FloorName(jsonObject.getString("FloorName"));
                        _floors.add(_floor);
                        Common._floors.put(_floor.get_FloorName() ,_floor);
                        floornames.add(_floor.get_FloorName());
                    }
                    ArrayAdapter<String> _flooradapter = new ArrayAdapter<String>(TableActivity.this, android.R.layout.simple_list_item_1, floornames.toArray(new String[floornames.size()]));
                    txtfloor.setAdapter(_flooradapter);
                    txtfloor.setThreshold(0);
                }
                catch (Exception w)
                {
                    Toast.makeText(TableActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TableActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void getTableList(int iFloorID)
    {
        Common._tables=new Hashtable<String, Table>();
        _tables.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Table?FloorID=" + iFloorID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _tables.clear();
                    Common._tables=new Hashtable<String, Table>();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Table _table=new Table();
                        _table.set_id(jsonObject.getInt("id"));
                        _table.set_TableName(jsonObject.getString("TableName"));
                        _tables.add(_table);
                        Common._tables.put(_table.get_TableName() ,_table);
                    }
                    TableAdapter adapter = new TableAdapter(TableActivity.this, _tables);
                    grdtables.setAdapter(adapter);
                }
                catch (Exception w)
                {
                    Toast.makeText(TableActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TableActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}