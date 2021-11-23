package com.atlanta.rms.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atlanta.rms.Common;
import com.atlanta.rms.Models.Modifier;
import com.atlanta.rms.Models.Product;
import com.atlanta.rms.Models.UnitRate;
import com.atlanta.rms.R;
import com.atlanta.rms.ViewHolder.ViewHolderModifier;
import com.atlanta.rms.ViewHolder.ViewHolderProduct;

import java.util.ArrayList;

public class ModifierAdapter extends BaseAdapter  {

    private static final String TAG = "ModifierAdapter";
    Activity _context;
    ArrayList<Modifier> _modifiers;
    RadioGroup rgp;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    View _dlgview;
    public ModifierAdapter(Activity  context, ArrayList<Modifier> modifiers,View dlgview) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._modifiers=modifiers;
        this._dlgview=dlgview;
        rgp = new RadioGroup(context);
    }
    @Override
    public int getCount() {
        return _modifiers.size();
    }

    @Override
    public Object getItem(int i) {
        return _modifiers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderModifier _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.modifier_adapter,viewGroup,false);
            _viewholder=new ViewHolderModifier(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderModifier) vw.getTag();
        }

        final Modifier _modifier = (Modifier) this.getItem(position);
        _viewholder.txtselmodifiername.setText(_modifier.get_Name());
        _viewholder.txtselmodifiername.setTag(_modifier.get_id());
        _viewholder.chkmodifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox chk=(CheckBox)view;
                if(chk.isChecked())
                {
                    Common._selectedmodifiers.put(_modifier.get_Name().trim(),_modifier.get_Name().trim());
                }
                else
                {
                    if(Common._selectedmodifiers.get(_modifier.get_Name().trim())!=null) {
                        Common._selectedmodifiers.remove(_modifier.get_Name().trim());
                    }
                }
            }
        });

        return vw;
    }
}
