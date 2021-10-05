package com.atlanta.rms;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderWaiter {
    public ImageView imgview;
    public TextView txtwaitername;
    ViewHolderWaiter(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imgwaiter);
        txtwaitername=(TextView)v.findViewById(R.id.txtwaitername);
    }
}
