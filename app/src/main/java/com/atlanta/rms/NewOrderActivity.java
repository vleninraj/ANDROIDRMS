package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
/*
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
*/
import com.atlanta.rms.Adapter.OrderdtlAdapter;
import com.atlanta.rms.Models.OrderDTL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity {


    TextView txtparty,txtinvno,dtdate,txtsalestype,txtbillamount;
    TextView lblmobilenumber,lbltablename;
    Button btnAddItem,btnCalandar;
    Boolean blnNewRecord=false;
    GridView grdneworder;

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
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        txtparty.setText((String) bd.get("PartyName"));
        txtparty.setTag((Integer) bd.get("PartyID"));
        txtsalestype.setText(Common.sCurrentOrderType);
        lblmobilenumber.setText((String) bd.get("MobileNumber"));
        lbltablename.setText((String) bd.get("TableName"));
        lbltablename.setTag((String) bd.get("TableID"));
        blnNewRecord=(Boolean) bd.get("NewRecord");
        dtdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        if(blnNewRecord)
        {
            Common._orderdtls=new ArrayList<>();
            Common._orderdtls.clear();

        }
        else
        {
            // Existing Record
        }
        OrderdtlAdapter _orderDtlAdapter=new OrderdtlAdapter(NewOrderActivity.this,Common._orderdtls);
        grdneworder.setAdapter(_orderDtlAdapter);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent intent = new Intent(NewOrderActivity.this, GroupActivity.class);
                  startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();


        OrderdtlAdapter _orderDtlAdapter=new OrderdtlAdapter(NewOrderActivity.this,Common._orderdtls);
        grdneworder.setAdapter(_orderDtlAdapter);

    }
}