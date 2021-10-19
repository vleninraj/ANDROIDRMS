package com.atlanta.rms;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderProduct {
    public ImageView imgview;
    public TextView txtproductname;
    ViewHolderProduct(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imgwaiter);
        txtproductname=(TextView)v.findViewById(R.id.txtproductname);
    }
}
