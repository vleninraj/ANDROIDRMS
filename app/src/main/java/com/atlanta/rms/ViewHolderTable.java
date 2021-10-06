package com.atlanta.rms;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderTable {

    public TextView txttablename;
    ViewHolderTable(View v)
    {
        txttablename=(TextView)v.findViewById(R.id.txttablename);
    }
}
