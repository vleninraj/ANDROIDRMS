package com.atlanta.rms;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderGroup {
    public ImageView imgview;
    public TextView txtgroupname;
    ViewHolderGroup(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imgwaiter);
        txtgroupname=(TextView)v.findViewById(R.id.txtgroupname);
    }
}
