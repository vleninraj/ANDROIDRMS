package com.atlanta.rms.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompatSideChannelService;

import com.atlanta.rms.Common;
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
                    _viewholder.lblneworderqty.setText(String.format(Common.sDecimalsQty,_orderitem.get_Qty()));
                    Double dblRate=Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString());
                    _viewholder.lblneworderamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
                    _orderitem.set_Qty(Double.valueOf(_viewholder.lblneworderqty.getText().toString()));
                    _orderitem.set_Rate(Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString()));
                    _orderitem.set_Amount(Double.valueOf(_viewholder.lblneworderamount.getText().toString()));
                    CalcTotals();
                }
            });
            _viewholder.btndecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double dblQty=_orderitem.get_Qty();
                    if(!dblQty.equals(1))
                    {
                        dblQty--;
                        _orderitem.set_Qty(dblQty);
                        _viewholder.lblneworderqty.setText(String.format(Common.sDecimalsQty,_orderitem.get_Qty()));
                        Double dblRate=Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString());
                        _viewholder.lblneworderamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
                        _orderitem.set_Qty(Double.valueOf(_viewholder.lblneworderqty.getText().toString()));
                        _orderitem.set_Rate(Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString()));
                        _orderitem.set_Amount(Double.valueOf(_viewholder.lblneworderamount.getText().toString()));
                        CalcTotals();
                    }

                }
            });

            if(_orderitem.get_OrderStatus()==1)
            {
                _viewholder.btnCancelItem.setVisibility(View.VISIBLE);
            }
            else
            {
                _viewholder.btnCancelItem.setVisibility(View.GONE);
            }
            _viewholder.btnCancelItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(_orderitem!=null) {
                        if (_orderitem.get_OrderStatus() == 1) {
                            new AlertDialog.Builder(v.getRootView().getContext())
                                    .setTitle("Cancel Item")
                                    .setMessage("Atr you sure to Cancel this item ?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            _orderitem.set_OrderStatus(2); // Cancelled
                                            notifyDataSetChanged();
                                            ChangeOrderStatus(_orderitem,_viewholder);
                                            Toast.makeText(_context.getApplicationContext(),"Item Cancelled!",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }
            });
            _viewholder.btnremoveitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(_orderitem.get_OrderStatus()==0) {
                        _orderitems.remove(i);
                        notifyDataSetChanged();
                        CalcTotals();
                    }
                    else
                    {
                        Toast.makeText(_context.getApplicationContext(), "You can't remove printed order!",Toast.LENGTH_LONG).show();
                    }
                }
            });

            _viewholder.lblneworderproductname.setText(_orderitem.get_ProductName());
            _viewholder.lblneworderproductname.setTag(_orderitem.get_productid());
            _viewholder.lblneworderqty.setText(String.format(Common.sDecimalsQty,_orderitem.get_Qty()));
            _viewholder.lblnewordersalesrate.setText(String.format(Common.sDecimals,_orderitem.get_Rate()));
            _viewholder.lblneworderunit.setText(_orderitem.get_Unit());
            _viewholder.lblneworderunit.setTag(_orderitem.get_unitid());
            _viewholder.btnincrement.setTag(_orderitem.get_id());
            _viewholder.lblnotesandmodifiers.setText(_orderitem.get_KitchenNote() + " " + _orderitem.get_Modifiers());
            if(_viewholder.lblnotesandmodifiers.getText().toString().equals(""))
            {
                _viewholder.lblnotesandmodifiers.setVisibility(View.GONE);
            }
            else
            {
                _viewholder.lblnotesandmodifiers.setVisibility(View.VISIBLE);
            }
            Double dblQty=Double.valueOf(_viewholder.lblneworderqty.getText().toString());
            Double dblRate=Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString());
            _viewholder.lblneworderamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
            _orderitem.set_Qty(Double.valueOf(_viewholder.lblneworderqty.getText().toString()));
            _orderitem.set_Rate(Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString()));
            _orderitem.set_Amount(Double.valueOf(_viewholder.lblneworderamount.getText().toString()));
            ChangeOrderStatus(_orderitem,_viewholder);
        }
        else{
            final ViewHolderOrderDtl _viewholder=(ViewHolderOrderDtl) vw.getTag();
            _viewholder.lblneworderproductname.setText(_orderitem.get_ProductName());
            _viewholder.lblneworderproductname.setTag(_orderitem.get_productid());
            _viewholder.lblneworderqty.setText(String.format(Common.sDecimalsQty,_orderitem.get_Qty()));
            _viewholder.lblnewordersalesrate.setText(String.format(Common.sDecimals,_orderitem.get_Rate()));
            _viewholder.lblneworderunit.setText(_orderitem.get_Unit());
            _viewholder.lblneworderunit.setTag(_orderitem.get_unitid());
            _viewholder.btnincrement.setTag(_orderitem.get_id());
            _viewholder.lblnotesandmodifiers.setText(_orderitem.get_KitchenNote() + " " + _orderitem.get_Modifiers());

            Double dblQty=Double.valueOf(_viewholder.lblneworderqty.getText().toString());
            Double dblRate=Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString());
            _viewholder.lblneworderamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
            _orderitem.set_Qty(Double.valueOf(_viewholder.lblneworderqty.getText().toString()));
            _orderitem.set_Rate(Double.valueOf(_viewholder.lblnewordersalesrate.getText().toString()));
            _orderitem.set_Amount(Double.valueOf(_viewholder.lblneworderamount.getText().toString()));
            if(_viewholder.lblnotesandmodifiers.getText().toString().equals(""))
            {
                _viewholder.lblnotesandmodifiers.setVisibility(View.GONE);
            }
            else
            {
                _viewholder.lblnotesandmodifiers.setVisibility(View.VISIBLE);
            }
            ChangeOrderStatus(_orderitem,_viewholder);
        }

     //   _viewholder.txtwaitername.setText(_orderitem.get_WaiterName());
      //  _viewholder.txtwaitername.setTag(_orderitem.get_id());
       // Log.d(TAG, "From View" + _orderitem.get_WaiterName());
        return vw;
    }
    private  void ChangeOrderStatus(OrderDTL _orderitem,ViewHolderOrderDtl _viewholder)
    {
        switch (_orderitem.get_OrderStatus())
        {
            case 0:
                _viewholder.lblorderitemstatus.setText("New Order");
                _viewholder.lblorderitemstatus.setTextColor(_context.getResources().getColor(R.color.AtlantaThemeBlue));
                break;
            case 1:
                _viewholder.lblorderitemstatus.setText("Printed To Kitchen");
                _viewholder.lblorderitemstatus.setTextColor(_context.getResources().getColor(R.color.green));
                break;
            case 2:
                _viewholder.lblorderitemstatus.setText("Cancelled Order");
                _viewholder.lblorderitemstatus.setTextColor(_context.getResources().getColor(R.color.red));
                break;
        }

    }
    private void CalcTotals()
    {
        Double dblNetAmount=0.0;
        for(OrderDTL dtl: Common._orderdtls)
        {
            dblNetAmount=dblNetAmount + (dtl.get_Qty()*dtl.get_Rate());
        }
        TextView txtbillamount=_context.findViewById(R.id.txtbillamount);
        txtbillamount.setText(String.format(Common.sDecimals,dblNetAmount));
    }
}
