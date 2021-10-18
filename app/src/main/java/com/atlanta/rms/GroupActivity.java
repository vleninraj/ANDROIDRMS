package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;

import java.util.ArrayList;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class GroupActivity extends AppCompatActivity {

    GridView grdgroups;
    RequestQueue requestQueue;
    final ArrayList<Group> _groups=new ArrayList<>();
    final ArrayList<Group> _groupsfiltered=new ArrayList<>();
    ArrayList<String> groupnames = new ArrayList<>();
    AutoCompleteTextView txtgroupsearch;
    String sIpAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


    }
}