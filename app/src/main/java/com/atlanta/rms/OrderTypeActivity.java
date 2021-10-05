package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderTypeActivity extends AppCompatActivity {

    Button btnDineIn,btnTakeAway;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_type);

        btnDineIn=findViewById(R.id.btndinein);
        btnTakeAway=findViewById(R.id.btntakeaway);
        btnDineIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Common.sCurrentOrderType="DineIn";
                Intent intent = new Intent(OrderTypeActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
        btnTakeAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.sCurrentOrderType="TakeAway";
                Intent intent = new Intent(OrderTypeActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });

    }
}