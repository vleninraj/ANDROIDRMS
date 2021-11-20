package com.atlanta.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Hashtable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.rms.Adapter.OrderListAdapter;
import com.atlanta.rms.Adapter.OrderdtlAdapter;
import com.atlanta.rms.Models.OrderDTL;
import com.atlanta.rms.Models.OrderList;
import com.atlanta.rms.Models.Party;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderListActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SearchView search;
    GridView grdunbilled;
    GridView grdbilled;
    Button btnSearch,btnneworder;
    TabLayout tbp;
    final ArrayList<OrderList> _unbilledvouchers=new ArrayList<>();
    final ArrayList<OrderList> _unbilledvouchersfiltered=new ArrayList<>();
    final ArrayList<OrderList> _billedvouchers=new ArrayList<>();
    final ArrayList<OrderList> _billedvouchersfiltered=new ArrayList<>();

    String sIpAddress="";
    final ArrayList<String> _partynames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        search=findViewById(R.id.searchvw);
        grdbilled=findViewById(R.id.grdbilled);
        grdunbilled=findViewById(R.id.grdunbilled);
        btnSearch=findViewById(R.id.btnsearchorder);
        btnneworder=findViewById(R.id.btnneworder);
        tbp=(TabLayout)findViewById(R.id.tbmain);
        requestQueue = Volley.newRequestQueue(this);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setIconified(false);
            }
        });
        grdunbilled.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderList _order= _unbilledvouchers.get(i);
                if(_order!=null)
                {
                    if(_order.get_billed()==1)
                    {
                        Toast.makeText(OrderListActivity.this,"You can't edit billed order!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(OrderListActivity.this, NewOrderActivity.class);
                    intent.putExtra("NewRecord",false);
                    intent.putExtra("OrderID",_order.get_id());
                    startActivity(intent);
                   // Toast.makeText(OrderListActivity.this,_order.get_VoucherNo(),Toast.LENGTH_LONG).show();
                }
            }
        });
        grdbilled.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(OrderListActivity.this,"You can't edit billed order!",Toast.LENGTH_LONG).show();
                return;
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnSearch.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                return true;
            }
        });
        btnneworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Common.selectedTableName="";
                Common.selectedTableID="";
                if(Common.sCurrentOrderType.equals("Dine In"))
                {
                    Intent intent = new Intent(OrderListActivity.this, TableActivity.class);
                    startActivity(intent);
                }
                LayoutInflater layoutInflater=getLayoutInflater();
                final View DialougView = layoutInflater.inflate(R.layout.select_party, null);
                final AutoCompleteTextView txtcustomername=DialougView.findViewById(R.id.txtcustomername);
                final EditText txtmobilenumber=DialougView.findViewById(R.id.txtmobilenumber);
                final EditText txtVehicleName = DialougView.findViewById(R.id.txtvehiclename);
                final EditText txtVehicleNumber = DialougView.findViewById(R.id.txtvehiclenumber);
                txtcustomername.requestFocus();
                final AlertDialog alert=new AlertDialog.Builder(OrderListActivity.this).create();
                final Button btnSelectParty=DialougView.findViewById(R.id.btnselectparty);

                Common._parties=new Hashtable<String, Party>();
                _partynames.clear();
                String url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/Party";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray jsonArray = response;
                        try {
                            Common._parties=new Hashtable<String, Party>();
                            _partynames.clear();
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Party _party=new Party();
                                _party.set_id(jsonObject.getInt("id"));
                                _party.set_PartyCode(jsonObject.getString("PartyCode"));
                                _party.set_PartyName(jsonObject.getString("PartyName").trim());
                                _party.set_ArabicName(jsonObject.getString("ArabicName"));
                                _party.set_GSTNO(jsonObject.getString("GSTNO"));
                                _party.set_VATNO(jsonObject.getString("VATNO"));
                                _party.set_MobileNumber(jsonObject.getString("MobileNumber"));
                                _party.set_Balance(jsonObject.getDouble("Balance"));
                                _partynames.add(_party.get_PartyName());
                                Common._parties.put(_party.get_PartyName(),_party);
                            }
                            ArrayAdapter<String> _partyAdapter = new ArrayAdapter<String>(OrderListActivity.this, android.R.layout.simple_list_item_1, _partynames.toArray(new String[_partynames.size()]));
                            txtcustomername.setAdapter(_partyAdapter);
                            txtcustomername.setThreshold(0);
                        }
                        catch (Exception w)
                        {
                            Toast.makeText(OrderListActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonArrayRequest);

                btnSelectParty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {

                            if(txtcustomername.getText().toString().trim().equals(""))
                            {
                                Toast.makeText(getApplicationContext(),"Customer name must be selected!",Toast.LENGTH_LONG).show();
                                txtcustomername.requestFocus();
                                return;
                            }
                            Party _party=Common._parties.get(txtcustomername.getText().toString().trim());
                            if(_party.equals(null))
                            {
                                Toast.makeText(getApplicationContext(),"Customer name must be selected!",Toast.LENGTH_LONG).show();
                                txtcustomername.requestFocus();
                                return;
                            }
                            if(Common.sCurrentOrderType.equals("TakeAway")
                                    && txtmobilenumber.getText().toString().trim().equals(""))
                            {
                                Toast.makeText(getApplicationContext(),"Mobile number must be entered for take away!",Toast.LENGTH_LONG).show();
                                txtmobilenumber.requestFocus();
                                return;
                            }
                            Intent intent = new Intent(OrderListActivity.this, NewOrderActivity.class);
                            intent.putExtra("PartyID",_party.get_id());
                            intent.putExtra("PartyName",_party.get_PartyName());
                            intent.putExtra("ArabicName",_party.get_ArabicName());
                            intent.putExtra("MobileNumber",txtmobilenumber.getText().toString());
                            intent.putExtra("NewRecord",true);
                            intent.putExtra("TableName",Common.selectedTableName);
                            intent.putExtra("TableID",Common.selectedTableID);
                            intent.putExtra("VehicleName",txtVehicleName.getText().toString());
                            intent.putExtra("VehicleNumber",txtVehicleNumber.getText().toString());
                            startActivity(intent);
                            alert.dismiss();
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                alert.setView(DialougView);
                alert.show();
            }
        });

        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        getOrderList();



        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(tbp.getSelectedTabPosition()==0) {
                    _unbilledvouchersfiltered.clear();
                    for (OrderList _orderlist : _unbilledvouchers) {
                        if (_orderlist.get_VoucherNo().toUpperCase().startsWith(s.toString().toUpperCase())
                                || _orderlist.get_VoucherNo().toUpperCase().endsWith(s.toString().toUpperCase())) {
                            _unbilledvouchersfiltered.add(_orderlist);
                        }
                    }
                    OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, _unbilledvouchersfiltered);
                    grdunbilled.setAdapter(adapter);
                }
                else
                {
                    _billedvouchersfiltered.clear();
                    for (OrderList _orderlist : _billedvouchers) {
                        if (_orderlist.get_VoucherNo().toUpperCase().startsWith(s.toString().toUpperCase())
                                || _orderlist.get_VoucherNo().toUpperCase().endsWith(s.toString().toUpperCase())) {
                            _billedvouchersfiltered.add(_orderlist);
                        }
                    }
                    OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, _billedvouchersfiltered);
                    grdbilled.setAdapter(adapter);
                }


                return true;
            }
        });

    }

    private void getOrderList()
    {
        _billedvouchers.clear();
        _unbilledvouchers.clear();
        String url="";
        if(Common.sCurrentOrderType.equals("Dine In")) {
            url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/Order?WaiterID=" + Common.sCurrentWaiterID
                    + "&TypeID=688";
        }
        else
        {
            url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/Order?WaiterID=" + Common.sCurrentWaiterID
                    + "&TypeID=689";
        }
/*
        HashMap<String ,String> params=new HashMap<String, String>();
        params.put("WaiterID",Common.sCurrentWaiterID);
        if(Common.sCurrentOrderType.equals("DineIn")) {

            params.put("TypeID","688") ;
        }
        else
        {
            params.put("TypeID","689") ;
        }
*/
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _billedvouchers.clear();
                    _unbilledvouchers.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getInt("BilledStatus")==1)
                        {
                            OrderList _order=new OrderList();
                            _order.set_id(jsonObject.getInt("id"));
                            _order.set_VoucherNo(jsonObject.getString("VoucherNo"));
                            _order.set_Party(jsonObject.getString("Party"));
                            _order.set_MobileNumber(jsonObject.getString("MobileNumber"));
                            _order.set_TableName(jsonObject.getString("TableName"));
                            _order.set_billed(jsonObject.getInt("BilledStatus"));
                            _billedvouchers.add(_order);
                        }
                        else
                        {
                            OrderList _order=new OrderList();
                            _order.set_id(jsonObject.getInt("id"));
                            _order.set_VoucherNo(jsonObject.getString("VoucherNo"));
                            _order.set_Party(jsonObject.getString("Party"));
                            _order.set_MobileNumber(jsonObject.getString("MobileNumber"));
                            _order.set_TableName(jsonObject.getString("TableName"));
                            _order.set_billed(jsonObject.getInt("BilledStatus"));
                            _unbilledvouchers.add(_order);
                        }

                    }
                    OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, _unbilledvouchers);
                    grdunbilled.setAdapter(adapter);
                    OrderListAdapter adapter2 = new OrderListAdapter(OrderListActivity.this, _billedvouchers);
                    grdbilled.setAdapter(adapter2);
                }
                catch (Exception w)
                {
                    Toast.makeText(OrderListActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getOrderList();

    }
}