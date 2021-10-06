package com.atlanta.rms;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {


    private static final String TAG = "OrderListAdapter";
    Activity _context;
    ArrayList<OrderList> _orders;

    public OrderListAdapter(Activity  context, ArrayList<OrderList> orders) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._orders=orders;
    }
    @Override
    public int getCount() {
        return _orders.size();
    }

    @Override
    public Object getItem(int i) {
        return _orders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderOrder _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.orderlist_adapter,viewGroup,false);
            _viewholder=new ViewHolderOrder(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderOrder) vw.getTag();
        }

        final OrderList _order = (OrderList) this.getItem(i);
        _viewholder.lblvoucherno.setText(_order.get_VoucherNo());
        _viewholder.lblparty.setText(_order.get_Party());
        if(Common.sCurrentOrderType.equals("DineIn")) {
            _viewholder.lbltable.setText(_order.get_TableName());
        }
        else
        {
            _viewholder.lbltable.setText(_order.get_MobileNumber());
        }
        _viewholder.lblgrandamount.setText(_order.get_GrandAmount());
        Log.d(TAG, "From View" + _order.get_VoucherNo());
        return vw;
    }

}