package com.atlanta.rms.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atlanta.rms.Common;
import com.atlanta.rms.Models.Product;
import com.atlanta.rms.Models.UnitRate;
import com.atlanta.rms.R;
import com.atlanta.rms.ViewHolder.ViewHolderProduct;
import com.atlanta.rms.ViewHolder.ViewHolderUnitSelection;

import java.util.ArrayList;

public class UnitAdapter extends BaseAdapter  {

    private static final String TAG = "UnitAdapter";
    Activity _context;
    ArrayList<UnitRate> _unitrates;
    RadioGroup rgp;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;

    public UnitAdapter(Activity  context, ArrayList<UnitRate> unitRates) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._unitrates=unitRates;
        rgp = new RadioGroup(context);
    }
    @Override
    public int getCount() {
        return _unitrates.size();
    }

    @Override
    public Object getItem(int i) {
        return _unitrates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderUnitSelection _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.unit_adapter,viewGroup,false);
            _viewholder=new ViewHolderUnitSelection(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderUnitSelection) vw.getTag();
        }

        final UnitRate _unitrate = (UnitRate) this.getItem(position);
        _viewholder.txtselunitname.setText(_unitrate.get_Unit());
        _viewholder.txtselunitname.setTag(_unitrate.get_id());
       _viewholder.txtunitsalesrate.setText(String.format(Common.sDecimals,_unitrate.get_SalesRate()));
       _viewholder.txtunitsalesratecap.setText(Common.CurrencySymbol);

        _viewholder.radunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((position != mSelectedPosition && mSelectedRB != null)) {
                    mSelectedRB.setChecked(false);
                }

                mSelectedPosition = position;
                mSelectedRB = (RadioButton) view;
            }
        });

        if (mSelectedPosition != position) {
            _viewholder.radunit.setChecked(false);
        } else {
            _viewholder.radunit.setChecked(true);
            if (mSelectedRB != null && _viewholder.radunit != mSelectedRB) {
                mSelectedRB = _viewholder.radunit;
            }
        }

        return vw;
    }
}
