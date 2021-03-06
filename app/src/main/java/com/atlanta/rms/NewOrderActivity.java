package com.atlanta.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
*/
import com.android.volley.DefaultRetryPolicy;
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
import com.atlanta.rms.Models.Party;
import com.atlanta.rms.Models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewOrderActivity extends AppCompatActivity {


    TextView txtparty,txtinvno,dtdate,txtsalestype,txtbillamount;
    TextView lblmobilenumber,lbltablename,lbltablenamecap;
    Button btnAddItem,btnCalandar,btnSaveOrder,btncancelitem;
    Boolean blnNewRecord=false;
    GridView grdneworder;
    String sIpAddress="";
    RequestQueue requestQueue;
    String sVehicleName="",sVehicleNumber="";
    Boolean blnSavingStart=false;
    Calendar cal = Calendar.getInstance();
    OrderdtlAdapter _orderDtlAdapter;
    final ArrayList<String> _partynames = new ArrayList<>();
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
        lbltablenamecap=findViewById(R.id.lbltablecap);
        btnAddItem=findViewById(R.id.btnadditem);
        btnCalandar=findViewById(R.id.btnCalandar);
        grdneworder=findViewById(R.id.grdneworder);
        btnSaveOrder=findViewById(R.id.btnsaveorder);
        btncancelitem=findViewById(R.id.btncancelitem);
        requestQueue = Volley.newRequestQueue(this);
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        blnNewRecord=(Boolean) bd.get("NewRecord");
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        if(Common.sCurrentOrderType.equals("Dine In"))
        {
            lbltablename.setVisibility(View.VISIBLE);
            lbltablenamecap.setVisibility(View.VISIBLE);
        }
        else
        {
            lbltablename.setVisibility(View.INVISIBLE);
            lbltablenamecap.setVisibility(View.INVISIBLE);
        }

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
            sVehicleName =(String)bd.get("VehicleName");
            sVehicleNumber=(String)bd.get("VehicleNumber");
        }
        else
        {
            txtinvno.setTag((Integer) bd.get("OrderID"));
            fillOrderData(txtinvno.getTag().toString());
            // Existing Record
        }
        CalcTotals();
        _orderDtlAdapter =new OrderdtlAdapter(NewOrderActivity.this,Common._orderdtls);
        grdneworder.setAdapter(_orderDtlAdapter);
        _orderDtlAdapter.notifyDataSetChanged();
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent intent = new Intent(NewOrderActivity.this, GroupActivity.class);
                  startActivity(intent);
            }
        });

        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.selectedTableName="";
                Common.selectedTableID="";
                if(Common.sCurrentOrderType.equals("Dine In"))
                {
                    Intent intent = new Intent(NewOrderActivity.this, TableActivity.class);
                    startActivity(intent);
                }
                LayoutInflater layoutInflater=getLayoutInflater();
                final View DialougView = layoutInflater.inflate(R.layout.select_party, null);
                final AutoCompleteTextView txtcustomername=DialougView.findViewById(R.id.txtcustomername);
                final EditText txtmobilenumber=DialougView.findViewById(R.id.txtmobilenumber);
                final EditText txtVehicleName = DialougView.findViewById(R.id.txtvehiclename);
                final EditText txtVehicleNumber = DialougView.findViewById(R.id.txtvehiclenumber);
                final AlertDialog alert=new AlertDialog.Builder(NewOrderActivity.this).create();
                final Button btnSelectParty=DialougView.findViewById(R.id.btnselectparty);
                txtcustomername.setText(txtparty.getText().toString());
                txtcustomername.setTag(txtparty.getTag().toString());
                txtmobilenumber.setText(lblmobilenumber.getText().toString());
                txtVehicleName.setText(sVehicleName);
                txtVehicleNumber.setText(sVehicleNumber);

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
                            ArrayAdapter<String> _partyAdapter = new ArrayAdapter<String>(NewOrderActivity.this, android.R.layout.simple_list_item_1, _partynames.toArray(new String[_partynames.size()]));
                            txtcustomername.setAdapter(_partyAdapter);
                            txtcustomername.setThreshold(0);
                            txtcustomername.setText("Cash Account");
                            txtmobilenumber.requestFocus();
                        }
                        catch (Exception w)
                        {
                            Toast.makeText(NewOrderActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewOrderActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
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

                            txtcustomername.setText(_party.get_PartyName());
                            txtcustomername.setTag(_party.get_id());
                            lblmobilenumber.setText(txtmobilenumber.getText().toString());
                            sVehicleName=txtVehicleName.getText().toString();
                            sVehicleNumber=txtVehicleNumber.getText().toString();

                            SaveData();

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
        if(blnSavingStart){ return; }
        blnSavingStart=true;
        btnSaveOrder.setVisibility(View.INVISIBLE);
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
                obj.put("ProductID",_dtl.get_productid());
                obj.put("Qty",_dtl.get_Qty());
                obj.put("Rate",_dtl.get_Rate());
                obj.put("UnitID",_dtl.get_unitid());
                obj.put("VehicleName",sVehicleName);
                obj.put("VehicleNumber",sVehicleNumber);
                if(blnNewRecord)
                {
                    obj.put("NewRecord",1);
                }
                else
                {
                    obj.put("NewRecord",0);
                }
                obj.put("WaiterID",Common.sCurrentWaiterID);
                obj.put("MobileNumber",lblmobilenumber.getText().toString());
                obj.put("OrderStatus",_dtl.get_OrderStatus());
                obj.put("KitchenNote",_dtl.get_KitchenNote());
                obj.put("Modifiers",_dtl.get_Modifiers());
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
                  try {
                      JSONObject _jsnresponse=response.getJSONObject(0);
                      String sVoucherNo= _jsnresponse.getString("VoucherNo");
                      Integer iStatus=_jsnresponse.getInt("Status");
                      if(iStatus==1) {
                            if(blnNewRecord) {
                                Toast.makeText(NewOrderActivity.this, "Order saved with Voucher No " + sVoucherNo, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(NewOrderActivity.this, "Order Updated with Voucher No " + sVoucherNo, Toast.LENGTH_LONG).show();
                            }
                              finish();
                      }
                      else if(iStatus==2)
                      {
                          String sError=_jsnresponse.getString("ErrorString");
                          String sMessage="Order while printing ! " +sError;
                          Toast.makeText(NewOrderActivity.this, sMessage, Toast.LENGTH_LONG).show();
                          blnSavingStart=false;
                          btnSaveOrder.setVisibility(View.VISIBLE);
                      }
                      else
                      {
                          String sError=_jsnresponse.getString("ErrorString");
                          String sMessage="Order Saving Failed! " +sError;
                          Toast.makeText(NewOrderActivity.this, sMessage, Toast.LENGTH_LONG).show();
                          blnSavingStart=false;
                          btnSaveOrder.setVisibility(View.VISIBLE);

                      }
                  }
                  catch (JSONException e)
                  {
                      e.printStackTrace();
                      blnSavingStart=false;
                      btnSaveOrder.setVisibility(View.VISIBLE);
                  }
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewOrderActivity.this, "Order Saving failed!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                        _dtl.set_OrderStatus(jsonObject.getInt("OrderStatus"));
                        _dtl.set_KitchenNote(jsonObject.getString("KitchenNote"));
                        _dtl.set_Modifiers(jsonObject.getString("Modifiers"));
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
                        sVehicleName=jsonObject.getString("VehicleName");
                        sVehicleNumber=jsonObject.getString("VehicleNo");
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
        txtbillamount.setText(String.format(Common.sDecimals,dblNetAmount));
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