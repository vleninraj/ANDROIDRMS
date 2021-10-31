package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
*/
import com.android.volley.RequestQueue;
import com.atlanta.rms.Adapter.OrderdtlAdapter;
import com.atlanta.rms.Models.OrderDTL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.rms.Models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewOrderActivity extends AppCompatActivity {


    TextView txtparty,txtinvno,dtdate,txtsalestype,txtbillamount;
    TextView lblmobilenumber,lbltablename;
    Button btnAddItem,btnCalandar,btnSaveOrder;
    Boolean blnNewRecord=false;
    GridView grdneworder;
    String sIpAddress="";
    RequestQueue requestQueue;
    Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        txtparty=findViewById(R.id.txtparty);
        txtinvno=findViewById(R.id.txtinvno);
        dtdate=findViewById(R.id.dtdate);
        txtsalestype=findViewById(R.id.txtsalestype);
        txtbillamount=findViewById(R.id.txtbillamount);
        lblmobilenumber=findViewById(R.id.lblnewordermobileno);
        lbltablename=findViewById(R.id.lblnewordertablename);
        btnAddItem=findViewById(R.id.btnadditem);
        btnCalandar=findViewById(R.id.btnCalandar);
        grdneworder=findViewById(R.id.grdneworder);
        btnSaveOrder=findViewById(R.id.btnsaveorder);
        requestQueue = Volley.newRequestQueue(this);
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        blnNewRecord=(Boolean) bd.get("NewRecord");
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        if(blnNewRecord)
        {
           txtinvno.setTag(0);
            dtdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            Common._orderdtls=new ArrayList<>();
            Common._orderdtls.clear();
            txtparty.setText((String) bd.get("PartyName"));
            txtparty.setTag((Integer) bd.get("PartyID"));
            txtsalestype.setText(Common.sCurrentOrderType);
            lblmobilenumber.setText((String) bd.get("MobileNumber"));
            lbltablename.setText((String) bd.get("TableName"));
            lbltablename.setTag((String) bd.get("TableID"));
        }
        else
        {
            txtinvno.setTag((Integer) bd.get("OrderID"));
            fillOrderData(txtinvno.getTag().toString());
            // Existing Record
        }
        CalcTotals();
        OrderdtlAdapter _orderDtlAdapter=new OrderdtlAdapter(NewOrderActivity.this,Common._orderdtls);
        grdneworder.setAdapter(_orderDtlAdapter);
        _orderDtlAdapter.notifyDataSetChanged();
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent intent = new Intent(NewOrderActivity.this, GroupActivity.class);
                  startActivity(intent);
            }
        });
        grdneworder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SaveData();

            }
        });
/*
        btnCalandar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                OnSelectDateListener listener = new OnSelectDateListener() {
                    @Override
                    public void onSelect(final List<Calendar> calendars) {

                        Calendar calendar = Calendar.getInstance();
                        if (calendar.compareTo(calendars.get(0)) <= 0) {
                            cal = calendars.get(0);
                            dtdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(calendars.get(0).getTime()));

                        } else {
                            dtdate.setText("Select Date");

                        }
                    }
                };
                DatePickerBuilder builder = new DatePickerBuilder(NewOrderActivity.this, listener)
                        .pickerType(CalendarView.ONE_DAY_PICKER).date(cal);

                com.applandeo.materialcalendarview.DatePicker datePicker = builder.build();
                datePicker.show();
            }
        });
*/
    }
    private void SaveData()
    {
        JSONObject jsnSaveData=new JSONObject();
        JSONArray jsnArray=new JSONArray();
        for(OrderDTL _dtl :Common._orderdtls)
        {
            JSONObject obj=new JSONObject();
            try {
                obj.put("hdrid",txtinvno.getTag().toString());
                obj.put("VoucherNo",txtinvno.getText().toString());
                obj.put("SalesType",txtsalestype.getText().toString());
                obj.put("PartyID",txtparty.getTag().toString());
                obj.put("TableID",lbltablename.getTag().toString());
                obj.put("GrandTotal",Double.valueOf(txtbillamount.getText().toString()));
                obj.put("ProductID",_dtl.get_id());
                obj.put("Qty",_dtl.get_Qty());
                obj.put("Rate",_dtl.get_Rate());
                obj.put("UnitID",_dtl.get_unitid());
                jsnArray.put(obj);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Order";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, url, jsnArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              if(response!=null) {
                  Toast.makeText(NewOrderActivity.this, "Order Saved...", Toast.LENGTH_LONG).show();
                  finish();
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void fillOrderData(String sOrderID)
    {
        Common._orderdtls=new ArrayList<>();
        Common._orderdtls.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Order?OrderID=" + sOrderID;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Common._orderdtls=new ArrayList<>();
                Common._orderdtls.clear();
                JSONArray jsonArray = response;
                try {
                    String sPartyName="",sInvno="",sSalesType="",sTableName="",sMobileNumber="";
                    Integer iPartyID=0,iTableID=0,ihdrid=0;
                    Double dblGrandTotal=0.0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OrderDTL _dtl = new OrderDTL();
                        _dtl.set_id(jsonObject.getInt("dtlid"));
                        _dtl.set_productid(jsonObject.getInt("ProductID"));
                        _dtl.set_ProductName(jsonObject.getString("ProductName"));
                        _dtl.set_ProductCode(jsonObject.getString("ProductCode"));
                        _dtl.set_Qty(jsonObject.getDouble("Qty"));
                        _dtl.set_Rate(jsonObject.getDouble("Rate"));
                        _dtl.set_unitid(jsonObject.getInt("UnitID"));
                        _dtl.set_Unit(jsonObject.getString("Unit"));
                        _dtl.set_Amount(jsonObject.getDouble("Amount"));
                        Common._orderdtls.add(_dtl);
                        iPartyID=jsonObject.getInt("PartyID");
                        sPartyName=jsonObject.getString("PartyName");
                        sInvno=jsonObject.getString("VoucherNo");
                        sSalesType=jsonObject.getString("SalesType");
                        sTableName=jsonObject.getString("TableName");
                        sMobileNumber=jsonObject.getString("MobileNumber");
                        iTableID=jsonObject.getInt("TableID");
                        dblGrandTotal=jsonObject.getDouble("GrandTotal");
                        ihdrid=jsonObject.getInt("hdrid");
                    }
                    txtparty.setText(sPartyName);
                    txtparty.setTag(iPartyID);
                    txtinvno.setText(sInvno);
                    txtinvno.setTag(ihdrid);
                    txtsalestype.setText(sSalesType);
                    lblmobilenumber.setText(sMobileNumber);
                    lbltablename.setText(sTableName);
                    lbltablename.setTag(iTableID);
                    CalcTotals();
                    OrderdtlAdapter _orderDtlAdapter=new OrderdtlAdapter(NewOrderActivity.this,Common._orderdtls);
                    grdneworder.setAdapter(_orderDtlAdapter);
                    _orderDtlAdapter.notifyDataSetChanged();
                }
                catch(Exception ex)
                {
                    Toast.makeText(NewOrderActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewOrderActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void CalcTotals()
    {
        Double dblNetAmount=0.0;
        for(OrderDTL dtl: Common._orderdtls)
        {
            dblNetAmount=dblNetAmount + (dtl.get_Qty()*dtl.get_Rate());
        }
        txtbillamount.setText(String.format("%.2f",dblNetAmount));
    }
    @Override
    protected void onResume() {
        super.onResume();

        OrderdtlAdapter _orderDtlAdapter=new OrderdtlAdapter(NewOrderActivity.this,Common._orderdtls);
        grdneworder.setAdapter(_orderDtlAdapter);
        _orderDtlAdapter.notifyDataSetChanged();
        CalcTotals();
    }
}