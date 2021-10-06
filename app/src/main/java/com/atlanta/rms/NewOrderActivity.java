package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/*
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
*/
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity {


    TextView txtparty,txtinvno,dtdate,txtsalestype,txtbillamount;
    TextView lblmobilenumber;
    Button btnAddItem,btnCalandar;
    Boolean blnNewRecord=false;
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
        btnAddItem=findViewById(R.id.btnadditem);
        btnCalandar=findViewById(R.id.btnCalandar);
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        txtparty.setText((String) bd.get("PartyName"));
        txtparty.setTag((Integer) bd.get("PartyID"));
        txtsalestype.setText(Common.sCurrentOrderType);
        lblmobilenumber.setText((String) bd.get("MobileNumber"));
        blnNewRecord=(Boolean) bd.get("NewRecord");
        dtdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
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
}