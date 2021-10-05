package com.atlanta.rms;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderWaiter {
    public ImageView imgview;
    public TextView txtview;
    ViewHolderWaiter(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imgwaiter);
        txtview=(TextView)v.findViewById(R.id.txtwaitername);
    }
}
