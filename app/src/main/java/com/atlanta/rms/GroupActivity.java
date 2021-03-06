package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.rms.Adapter.GroupAdapter;
import com.atlanta.rms.Adapter.ProductAdapter;
import com.atlanta.rms.Models.Group;
import com.atlanta.rms.Models.Product;
import com.atlanta.rms.ViewHolder.ViewHolderGroup;

import org.json.JSONArray;
import org.json.JSONObject;

public class GroupActivity extends AppCompatActivity {

    GridView grdgroups;
    RequestQueue requestQueue;
    final ArrayList<Group> _groups=new ArrayList<>();
    final ArrayList<Group> _groupsfiltered=new ArrayList<>();
    ArrayList<String> groupnames = new ArrayList<>();
    SearchView txtgroupsearch;
    String sIpAddress="";
    Button btnsearchgroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        grdgroups=(GridView)findViewById(R.id.grdgroups);
        txtgroupsearch=(SearchView)findViewById(R.id.txtgroupsearch);
        btnsearchgroup=(Button)findViewById(R.id.btnsearchgroup);
        requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        getGroupList();
        /*
        ArrayAdapter<String> _groupadapter = new ArrayAdapter<String>(GroupActivity.this, android.R.layout.simple_list_item_1, groupnames.toArray(new String[groupnames.size()]));
        txtgroupsearch.setAdapter(_groupadapter);
        txtgroupsearch.setThreshold(0);
         */
        txtgroupsearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnsearchgroup.setVisibility(View.VISIBLE);
                txtgroupsearch.setVisibility(View.GONE);
                return true;
            }
        });
        btnsearchgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsearchgroup.setVisibility(View.GONE);
                txtgroupsearch.setVisibility(View.VISIBLE);
                txtgroupsearch.setIconified(false);
            }
        });
        txtgroupsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String str) {
                _groupsfiltered.clear();
                for(Group _group:_groups)
                {
                    if(_group.get_GroupName().toUpperCase().startsWith(str.toString().toUpperCase())
                            || _group.get_GroupName().toUpperCase().endsWith(str.toString().toUpperCase()))
                    {
                        _groupsfiltered.add(_group);
                    }
                }
                GroupAdapter adapter = new GroupAdapter(GroupActivity.this, _groupsfiltered);
                grdgroups.setAdapter(adapter);
                return true;
            }
        });

        grdgroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolderGroup holder=(ViewHolderGroup) view.getTag();
                Common.selectedGroupName=  holder.txtgroupname.getText().toString();
                Common.selectedGroupID=holder.txtgroupname.getTag().toString();
                Intent intent = new Intent(GroupActivity.this, ProductActivity.class);
                intent.putExtra("GroupName", Common.selectedGroupName);
                intent.putExtra("GroupID", Common.selectedGroupID);

                startActivity(intent);
            }
        });

    }
    private  void getGroupList()
    {
        groupnames.clear();
        _groups.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Group";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    groupnames.clear();
                    _groups.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Group _group=new Group();
                        _group.set_id(jsonObject.getInt("id"));
                        _group.set_GroupCode(jsonObject.getString("GroupCode"));
                        _group.set_GroupName(jsonObject.getString("GroupName"));
                        _group.set_GroupImage(jsonObject.getString("GroupImage"));
                        groupnames.add(_group.get_GroupName());
                        _groups.add(_group);
                    }
                    GroupAdapter adapter = new GroupAdapter(GroupActivity.this, _groups);
                    grdgroups.setAdapter(adapter);
                }
                catch (Exception w)
                {
                    Toast.makeText(GroupActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}