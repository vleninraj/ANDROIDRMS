package com.atlanta.rms.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.atlanta.rms.R;
public class ViewHolderWaiter {
    public ImageView imgview;
    public TextView txtwaitername;
    public ViewHolderWaiter(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imgwaiter);
        txtwaitername=(TextView)v.findViewById(R.id.txtwaitername);
    }
}

