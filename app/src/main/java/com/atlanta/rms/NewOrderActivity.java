package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class NewOrderActivity extends AppCompatActivity {


    TextView txtparty,txtinvno,dtdate,txtsalestype,txtbillamount;
    TextView lblmobilenumber;
    Button btnAddItem;
    Boolean blnNewRecord=false;
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
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        txtparty.setText((String) bd.get("PartyName"));
        txtparty.setTag((Integer) bd.get("PartyID"));
        txtsalestype.setText(Common.sCurrentOrderType);
        lblmobilenumber.setText((String) bd.get("MobileNumber"));
        blnNewRecord=(Boolean) bd.get("NewRecord");



    }
}