package com.atlanta.rms.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.atlanta.rms.R;

public class ViewHolderGroup {
    public ImageView imgview;
    public TextView txtgroupname;
    public ViewHolderGroup(View v)
    {
        imgview=(ImageView)v.findViewById(R.id.imggroup);
        txtgroupname=(TextView)v.findViewById(R.id.txtgroupname);
    }
}
