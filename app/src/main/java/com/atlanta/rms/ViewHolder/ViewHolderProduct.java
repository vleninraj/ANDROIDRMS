package com.atlanta.rms.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlanta.rms.R;

public class ViewHolderProduct {
    public ImageView imgview;
    public TextView txtproductname;
    public ViewHolderProduct(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imgproduct);
        txtproductname=(TextView)v.findViewById(R.id.txtproductname);

    }
}
