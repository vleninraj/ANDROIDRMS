package com.atlanta.rms.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.atlanta.rms.R;
public class ViewHolderUnitSelection {

    public TextView txtselunitname;
    public TextView txtunitsalesratecap;
    public TextView txtunitsalesrate;
    public RadioButton radunit;
    public ViewHolderUnitSelection(View v)
    {
        txtselunitname=(TextView)v.findViewById(R.id.txtselunitname);
        txtunitsalesratecap=(TextView)v.findViewById(R.id.txtunitsalesratecap);
        txtunitsalesrate=(TextView)v.findViewById(R.id.txtunitsalesrate);
        radunit=(RadioButton) v.findViewById(R.id.radunit);
    }
}
