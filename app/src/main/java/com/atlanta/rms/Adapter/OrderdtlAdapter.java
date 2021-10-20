package com.atlanta.rms.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.atlanta.rms.R;
import com.atlanta.rms.Models.OrderDTL;
import com.atlanta.rms.ViewHolder.ViewHolderOrderDtl;


import java.util.ArrayList;

public class OrderdtlAdapter extends BaseAdapter {
    private static final String TAG = "WaiterAdapter";
    Activity _context;
    ArrayList<OrderDTL> _orderitems;

    public OrderdtlAdapter(Activity  context, ArrayList<OrderDTL> orderitems) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._orderitems=orderitems;
    }
    @Override
    public int getCount() {
        return _orderitems.size();
    }

    @Override
    public Object getItem(int i) {
        return _orderitems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        final OrderDTL _orderitem = (OrderDTL) this.getItem(i);
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.neworder_adapter,viewGroup,false);
            final ViewHolderOrderDtl _viewholder=new ViewHolderOrderDtl(vw);
            vw.setTag(_viewholder);
            _viewholder.btnincrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double dblQty=_orderitem.get_Qty();
                    dblQty++;
                    _orderitem.set_Qty(dblQty);
                    _viewholder.lblneworderqty.setText(String.format("%.2f",_orderitem.get_Qty()));
                }
            });
            _viewholder.btndecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double dblQty=_orderitem.get_Qty();
                    if(!dblQty.equals(0.0))
                    {
                        dblQty--;
                        _orderitem.set_Qty(dblQty);
                        _viewholder.lblneworderqty.setText(String.format("%.2f",_orderitem.get_Qty()));
                    }

                }
            });
            _viewholder.btnremoveitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _orderitems.remove(i);

                }
            });


            _viewholder.lblneworderproductname.setText(_orderitem.get_ProductName());
            _viewholder.lblneworderproductname.setTag(_orderitem.get_productid());
            _viewholder.lblneworderqty.setText(String.format("%.2f",_orderitem.get_Qty()));
            _viewholder.lblnewordersalesrate.setText(String.format("%.2f",_orderitem.get_Rate()));
            _viewholder.lblneworderunit.setText(_orderitem.get_Unit());
            _viewholder.lblneworderunit.setTag(_orderitem.get_unitid());
            _viewholder.btnincrement.setTag(_orderitem.get_id());
        }
        else{
            final ViewHolderOrderDtl _viewholder=(ViewHolderOrderDtl) vw.getTag();
            _viewholder.lblneworderproductname.setText(_orderitem.get_ProductName());
            _viewholder.lblneworderproductname.setTag(_orderitem.get_productid());
            _viewholder.lblneworderqty.setText(String.format("%.2f",_orderitem.get_Qty()));
            _viewholder.lblnewordersalesrate.setText(String.format("%.2f",_orderitem.get_Rate()));
            _viewholder.lblneworderunit.setText(_orderitem.get_Unit());
            _viewholder.lblneworderunit.setTag(_orderitem.get_unitid());
            _viewholder.btnincrement.setTag(_orderitem.get_id());
        }




     //   _viewholder.txtwaitername.setText(_orderitem.get_WaiterName());
      //  _viewholder.txtwaitername.setTag(_orderitem.get_id());
       // Log.d(TAG, "From View" + _orderitem.get_WaiterName());
        return vw;
    }
}
