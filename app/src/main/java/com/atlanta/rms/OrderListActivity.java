package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderListActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SearchView search;
    GridView grdOrders;
    Button btnSearch,btnneworder;
    final ArrayList<OrderList> _orders=new ArrayList<>();
    final ArrayList<OrderList> _ordersfiltered=new ArrayList<>();
    String sIpAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        search=findViewById(R.id.searchvw);
        grdOrders=findViewById(R.id.grdorders);
        btnSearch=findViewById(R.id.btnsearchorder);
        btnneworder=findViewById(R.id.btnneworder);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setIconified(false);
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

                _ordersfiltered.clear();
                for(OrderList _orderlist:_orders)
                {
                    if(_orderlist.get_VoucherNo().toUpperCase().startsWith(s.toString().toUpperCase())
                            || _orderlist.get_VoucherNo().toUpperCase().endsWith(s.toString().toUpperCase()))
                    {
                        _ordersfiltered.add(_orderlist);
                    }
                }
                OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, _ordersfiltered);
                grdOrders.setAdapter(adapter);
                return true;
            }
        });

    }
    private void getOrderList()
    {
        _orders.clear();
        String url="";
        url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/Order";
        HashMap<String ,String> params=new HashMap<String, String>();
        params.put("WaiterID",Common.sCurrentWaiterID);
        if(Common.sCurrentOrderType.equals("DineIn")) {

            params.put("TypeID","688") ;
        }
        else
        {
            params.put("TypeID","689") ;
        }
        CustomJsonRequest jsonArrayRequest = new CustomJsonRequest(Request.Method.GET, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _orders.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OrderList _order=new OrderList();
                        _order.set_id(jsonObject.getInt("id"));
                        _order.set_VoucherNo(jsonObject.getString("VoucherNo"));
                        _order.set_Party(jsonObject.getString("Party"));
                        _order.set_MobileNumber(jsonObject.getString("MobileNumber"));
                        _order.set_TableName(jsonObject.getString("TableName"));
                        _orders.add(_order);
                    }
                    OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, _orders);
                    grdOrders.setAdapter(adapter);
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
}