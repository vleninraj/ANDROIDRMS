package com.atlanta.rms.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.atlanta.rms.R;

public class ViewHolderOrderList {
    public TextView lblvoucherno,lblparty,lbltable,lblgrandamount;
    public ViewHolderOrderList(View v)
    {
        lblvoucherno=(TextView)v.findViewById(R.id.lblvoucherno);
        lblparty=(TextView)v.findViewById(R.id.lblparty);
        lbltable=(TextView)v.findViewById(R.id.lbltable);
        lblgrandamount=(TextView)v.findViewById(R.id.lblgrandamount);
    }
}
