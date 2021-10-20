package com.atlanta.rms.Adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.atlanta.rms.R;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.atlanta.rms.Models.Table;
import com.atlanta.rms.ViewHolder.ViewHolderTable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class TableAdapter  extends BaseAdapter {

    private static final String TAG = "TableAdapter";
    Activity _context;
    ArrayList<Table> _tables;

    public TableAdapter(Activity  context, ArrayList<Table> tables) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._tables=tables;
    }
    @Override
    public int getCount() {
        return _tables.size();
    }

    @Override
    public Object getItem(int i) {
        return _tables.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderTable _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.table_adapter,viewGroup,false);
            _viewholder=new ViewHolderTable(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderTable) vw.getTag();
        }

        final Table _table = (Table) this.getItem(i);
        _viewholder.txttablename.setText(_table.get_TableName());
        _viewholder.txttablename.setTag(_table.get_id());
        Log.d(TAG, "From View" + _table.get_TableName());
        return vw;
    }
}
