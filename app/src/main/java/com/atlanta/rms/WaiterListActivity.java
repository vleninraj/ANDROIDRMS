package com.atlanta.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.atlanta.rms.Adapter.ProductAdapter;
import com.atlanta.rms.Adapter.WaiterAdapter;
import com.atlanta.rms.Models.Product;
import com.atlanta.rms.Models.Waiter;
import com.atlanta.rms.ViewHolder.ViewHolderWaiter;

import org.json.JSONArray;
import org.json.JSONObject;

public class WaiterListActivity extends AppCompatActivity {

    GridView grdWaiters;
    RequestQueue requestQueue;
    final ArrayList<Waiter> _waiters=new ArrayList<>();
    final ArrayList<Waiter> _waiterfiltered=new ArrayList<>();
    ArrayList<String> waiternames = new ArrayList<>();
    SearchView txtwaitersearch;
    String sIpAddress="";
    Button btnsearchwaiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_list);
        grdWaiters=(GridView) findViewById(R.id.grdwaiters);
        txtwaitersearch=(SearchView) findViewById(R.id.txtwaitersearch);
        btnsearchwaiter=(Button)findViewById(R.id.btnsearchwaiter);
        requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        getWaiterList();
        txtwaitersearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnsearchwaiter.setVisibility(View.VISIBLE);
                txtwaitersearch.setVisibility(View.GONE);
                return true;
            }
        });
        btnsearchwaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsearchwaiter.setVisibility(View.GONE);
                txtwaitersearch.setVisibility(View.VISIBLE);
                txtwaitersearch.setIconified(false);
            }
        });
        txtwaitersearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String str) {
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
                return true;
            }
        });
        grdWaiters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolderWaiter holder=(ViewHolderWaiter) view.getTag();
               Common.sCurrentWaiterName=  holder.txtwaitername.getText().toString();
               Common.sCurrentWaiterID=holder.txtwaitername.getTag().toString();
               Waiter currentWaiter=null;
                for(Waiter _waiter:_waiters)
                {
                    if(_waiter.get_id()==Integer.valueOf(Common.sCurrentWaiterID))
                    {
                        currentWaiter=_waiter;
                        break;
                    }
                }
                if(currentWaiter!=null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    final View DialougView = layoutInflater.inflate(R.layout.activity_waiterpin, null);
                    final Button btnwaiterLogin = DialougView.findViewById(R.id.btnwaiterlogin);
                    final EditText txtwaiterpin = DialougView.findViewById(R.id.txtwaiterpin);
                    String sWaiterpin=currentWaiter.get_PINNumber().trim();
                    txtwaiterpin.requestFocus();
                    final AlertDialog alert=new AlertDialog.Builder(WaiterListActivity.this).create();
                    btnwaiterLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(sWaiterpin.equals(""))
                            {
                                Toast.makeText(WaiterListActivity.this,"Set PIN Number to this waiter!",Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(sWaiterpin.equals(txtwaiterpin.getText().toString().trim()))
                            {
                                Intent intent = new Intent(WaiterListActivity.this, OrderTypeActivity.class);
                                startActivity(intent);
                                Toast.makeText(WaiterListActivity.this,"Login successful!",Toast.LENGTH_LONG).show();
                                alert.dismiss();
                            }
                            else
                            {
                                Toast.makeText(WaiterListActivity.this,"Invalid Pin Number!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    alert.setView(DialougView);
                    alert.show();



                }
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
                        _waiter.set_PINNumber(jsonObject.getString("PINNumber"));
                        waiternames.add(_waiter.get_WaiterName());
                        _waiters.add(_waiter);
                    }
                    WaiterAdapter adapter = new WaiterAdapter(WaiterListActivity.this, _waiters);
                    grdWaiters.setAdapter(adapter);
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