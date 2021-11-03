package com.atlanta.rms.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlanta.rms.R;


public class ViewHolderProduct {
    public ImageView imgview;
    public TextView txtproductname;
    public TextView txtprditemdesription;
    public TextView txtprdsalesrate;
    public TextView txtprdunit;
    public ViewHolderProduct(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imgproduct);
        txtproductname=(TextView)v.findViewById(R.id.txtproductname);
        txtprditemdesription=(TextView)v.findViewById(R.id.txtprditemdesription);
        txtprdsalesrate=(TextView)v.findViewById(R.id.txtprdsalesrate);
        txtprdunit=(TextView)v.findViewById(R.id.txtprdunit);
    }
}
