package com.atlanta.rms.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.atlanta.rms.R;

import com.atlanta.rms.Models.Product;
import com.atlanta.rms.ViewHolder.ViewHolderProduct;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private static final String TAG = "GroupAdapter";
    Activity _context;
    ArrayList<Product> _products;

    public ProductAdapter(Activity  context, ArrayList<Product> products) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._products=products;
    }
    @Override
    public int getCount() {
        return _products.size();
    }

    @Override
    public Object getItem(int i) {
        return _products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderProduct _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.activity_productadapter,viewGroup,false);
            _viewholder=new ViewHolderProduct(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderProduct) vw.getTag();
        }

        final Product _product = (Product) this.getItem(i);
        _viewholder.txtproductname.setText(_product.get_productName());
        _viewholder.txtproductname.setTag(_product.get_id());
        Log.d(TAG, "From View" + _product.get_productName());
        return vw;
    }
}
