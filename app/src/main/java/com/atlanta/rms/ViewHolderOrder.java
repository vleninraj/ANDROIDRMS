package com.atlanta.rms;

import android.view.View;
import android.widget.TextView;

public class ViewHolderOrder {
    public TextView lblvoucherno,lblparty,lbltable,lblgrandamount;
    ViewHolderOrder(View v)
    {
        lblvoucherno=(TextView)v.findViewById(R.id.lblvoucherno);
        lblparty=(TextView)v.findViewById(R.id.lblparty);
        lbltable=(TextView)v.findViewById(R.id.lbltable);
        lblgrandamount=(TextView)v.findViewById(R.id.lblgrandamount);
    }
}
