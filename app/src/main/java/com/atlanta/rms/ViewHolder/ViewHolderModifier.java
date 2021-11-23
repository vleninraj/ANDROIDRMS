package com.atlanta.rms.ViewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.atlanta.rms.R;

public class ViewHolderModifier {
    public TextView txtselmodifiername;
    public CheckBox chkmodifier;
    public ViewHolderModifier(View v)
    {
        txtselmodifiername=(TextView)v.findViewById(R.id.txtselmodifiername);
        chkmodifier=(CheckBox) v.findViewById(R.id.chkmodifier);
    }
}
