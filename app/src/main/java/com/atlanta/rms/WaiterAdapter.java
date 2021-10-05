package com.atlanta.rms;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class WaiterAdapter  extends BaseAdapter {

    private static final String TAG = "WaiterAdapter";
    Activity _context;
    ArrayList<Waiter> _waiters;

    public WaiterAdapter(Activity  context, ArrayList<Waiter> waiters) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._waiters=waiters;
    }
    @Override
    public int getCount() {
        return _waiters.size();
    }

    @Override
    public Object getItem(int i) {
        return _waiters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderWaiter _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.activity_waiteradapter,viewGroup,false);
            _viewholder=new ViewHolderWaiter(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderWaiter) vw.getTag();
        }

            final Waiter _waiter = (Waiter) this.getItem(i);
            _viewholder.txtwaitername.setText(_waiter.get_WaiterName());
            _viewholder.txtwaitername.setTag(_waiter.get_id());
            Log.d(TAG, "From View" + _waiter.get_WaiterName());
        return vw;
    }
}

