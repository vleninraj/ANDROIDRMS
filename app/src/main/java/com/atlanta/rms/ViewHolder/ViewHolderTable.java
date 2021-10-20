package com.atlanta.rms.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlanta.rms.R;

public class ViewHolderTable {

    public TextView txttablename;
    public ViewHolderTable(View v)
    {
        txttablename=(TextView)v.findViewById(R.id.txttablename);
    }
}
