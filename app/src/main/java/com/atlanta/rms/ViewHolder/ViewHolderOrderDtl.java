package com.atlanta.rms.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atlanta.rms.R;

public class ViewHolderOrderDtl {

    public TextView lblneworderproductname;
    public TextView lblneworderqty;
    public TextView lblneworderunit;
    public TextView lblnewordersalesrate;
    public TextView lblneworderamount;
    public Button btnincrement;
    public Button btndecrement;
    public Button btnremoveitem;
    public Button btnCancelItem;
    public TextView lblnotesandmodifiers;
    public TextView lblorderitemstatus;

    public ViewHolderOrderDtl(View v)
    {
        lblneworderproductname=(TextView)v.findViewById(R.id.lblneworderproductname);
        lblneworderqty=(TextView)v.findViewById(R.id.lblneworderqty);
        lblneworderunit=(TextView)v.findViewById(R.id.lblneworderunit);
        lblnewordersalesrate=(TextView)v.findViewById(R.id.lblnewordersalesrate);
        btnincrement=(Button)v.findViewById(R.id.btnneworderincrement);
        btndecrement=(Button)v.findViewById(R.id.btnneworderdecrement);
        btnremoveitem=(Button)v.findViewById(R.id.btnremoveitem);
        btnCancelItem=(Button)v.findViewById(R.id.btncancelitem);
        lblneworderamount=(TextView)v.findViewById(R.id.lblneworderamount);
        lblnotesandmodifiers=(TextView)v.findViewById(R.id.lblnotesandmodifiers);
        lblorderitemstatus=(TextView)v.findViewById(R.id.lblorderitemstatus);
    }
}


