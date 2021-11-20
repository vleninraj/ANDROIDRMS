package com.atlanta.rms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.rms.Adapter.OrderListAdapter;
import com.atlanta.rms.Models.OrderList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnBilledFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnBilledFragment extends Fragment {

    final ArrayList<OrderList> _vouchers=new ArrayList<>();
    final ArrayList<OrderList> _vouchersfiltered=new ArrayList<>();
    RequestQueue requestQueue;
    String sIpAddress="";
    GridView grdvouchers;
    SearchView searchvw;
    Button btnsearch;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UnBilledFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnBilledFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnBilledFragment newInstance(String param1, String param2) {
        UnBilledFragment fragment = new UnBilledFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragvw=inflater.inflate(R.layout.fragment_un_billed, container, false);
        grdvouchers= fragvw.findViewById(R.id.grdunbilled);
        searchvw=fragvw.findViewById(R.id.searchvwunbilled);
        btnsearch=fragvw.findViewById(R.id.btnsearchunbilled);
        getOrderList();
        searchvw.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnsearch.setVisibility(View.VISIBLE);
                searchvw.setVisibility(View.GONE);
                return true;
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsearch.setVisibility(View.GONE);
                searchvw.setVisibility(View.VISIBLE);
                searchvw.setIconified(false);
            }
        });
        searchvw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                _vouchersfiltered.clear();
                    for (OrderList _orderlist : _vouchers) {
                        if (_orderlist.get_VoucherNo().toUpperCase().startsWith(s.toString().toUpperCase())
                                || _orderlist.get_VoucherNo().toUpperCase().endsWith(s.toString().toUpperCase())) {
                            _vouchersfiltered.add(_orderlist);
                        }
                    }
                    OrderListAdapter adapter = new OrderListAdapter(getActivity(), _vouchersfiltered);
                    grdvouchers.setAdapter(adapter);
                return true;
            }
        });
        grdvouchers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderList _order= _vouchers.get(i);
                if(_order!=null)
                {
                    if(_order.get_billed()==1)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"You can't edit billed order!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(getActivity().getApplicationContext(), NewOrderActivity.class);
                    intent.putExtra("NewRecord",false);
                    intent.putExtra("OrderID",_order.get_id());
                    startActivity(intent);
                    // Toast.makeText(OrderListActivity.this,_order.get_VoucherNo(),Toast.LENGTH_LONG).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return fragvw;
    }
    private void getOrderList()
    {
        final SharedPreferences ipAddress = getActivity().getApplicationContext().getSharedPreferences("ipaddress", 0);
        sIpAddress=ipAddress.getString("ipaddress", "");
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        _vouchers.clear();
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
                    _vouchers.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getInt("BilledStatus")!=1)
                        {
                            OrderList _order=new OrderList();
                            _order.set_id(jsonObject.getInt("id"));
                            _order.set_VoucherNo(jsonObject.getString("VoucherNo"));
                            _order.set_Party(jsonObject.getString("Party"));
                            _order.set_MobileNumber(jsonObject.getString("MobileNumber"));
                            _order.set_TableName(jsonObject.getString("TableName"));
                            _order.set_billed(jsonObject.getInt("BilledStatus"));
                            _vouchers.add(_order);

                        }
                    }

                    OrderListAdapter adapter = new OrderListAdapter(getActivity(), _vouchers);
                    grdvouchers.setAdapter(adapter);
                }
                catch (Exception w)
                {
                    Toast.makeText(getActivity().getApplicationContext(),w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}