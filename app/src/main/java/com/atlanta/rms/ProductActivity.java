package com.atlanta.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.rms.Adapter.ModifierAdapter;
import com.atlanta.rms.Adapter.ProductAdapter;
import com.atlanta.rms.Adapter.UnitAdapter;
import com.atlanta.rms.Models.Modifier;
import com.atlanta.rms.Models.OrderDTL;
import com.atlanta.rms.Models.OrderList;
import com.atlanta.rms.Models.Party;
import com.atlanta.rms.Models.Product;
import com.atlanta.rms.Models.UnitRate;
import com.atlanta.rms.ViewHolder.ViewHolderProduct;
import com.atlanta.rms.ViewHolder.ViewHolderUnitSelection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    GridView grdproducts;
    RequestQueue requestQueue;
    final ArrayList<Product> _products=new ArrayList<>();
    Dictionary<Integer,Product> _productlist=new Hashtable<Integer, Product>();
    final ArrayList<Product> _productsfiltered=new ArrayList<>();
    ArrayList<String> productnames = new ArrayList<>();
    SearchView txtproductsearch;
    String sIpAddress="";
    String sGroupID="";
    View DialougView;
    Button btnsearchproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        sGroupID=(String) bd.get("GroupID");
        btnsearchproduct=findViewById(R.id.btnsearchproduct);
        grdproducts=(GridView)findViewById(R.id.grdproducts);
        txtproductsearch=(SearchView) findViewById(R.id.txtproductsearch);
        requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        getProductList(sGroupID);
       /* ArrayAdapter<String> _productadapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_list_item_1, productnames.toArray(new String[productnames.size()]));
        txtproductsearch.setAdapter(_productadapter);
        txtproductsearch.setThreshold(0); */
        txtproductsearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnsearchproduct.setVisibility(View.VISIBLE);
                txtproductsearch.setVisibility(View.GONE);
                return true;
            }
        });
        btnsearchproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsearchproduct.setVisibility(View.GONE);
                txtproductsearch.setVisibility(View.VISIBLE);
                txtproductsearch.setIconified(false);
            }
        });
        txtproductsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                _productsfiltered.clear();
                for(Product _product:_products)
                {
                    if(_product.get_productName().toUpperCase().startsWith(s.toString().toUpperCase())
                            || _product.get_productName().toUpperCase().endsWith(s.toString().toUpperCase()))
                    {
                        _productsfiltered.add(_product);
                    }
                }
                ProductAdapter adapter = new ProductAdapter(ProductActivity.this, _productsfiltered);
                grdproducts.setAdapter(adapter);
                return true;
            }
        });
        grdproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolderProduct holder=(ViewHolderProduct) view.getTag();
                Common.selectedProductName=  holder.txtproductname.getText().toString();
                Common.selectedProductID=holder.txtproductname.getTag().toString();
                Product p=_productlist.get(Integer.valueOf( Common.selectedProductID));
                if(p!=null) {
                    SelectUnit(p);


                }

            }
        });
    }
    private void SelectUnit(Product p)
    {
        ArrayList<UnitRate> _unitrates=new ArrayList<>();
        ArrayList<Modifier> _modifiers=new ArrayList<>();
        final AlertDialog alert=new AlertDialog.Builder(ProductActivity.this).create();
        LayoutInflater layoutInflater=getLayoutInflater();
        DialougView = layoutInflater.inflate(R.layout.activity_unitselection, null);
        final ImageView imgunit=(ImageView)DialougView.findViewById(R.id.imgunitproduct);
        final TextView txtunitselunit=(TextView)DialougView.findViewById(R.id.txtunitselunit);
        final TextView txtunitproductname=(TextView)DialougView.findViewById(R.id.txtunitproductname);
        final TextView txtunititemdesription=(TextView)DialougView.findViewById(R.id.txtunititemdesription);
        final TextView txtunitselsalesratecap=(TextView)DialougView.findViewById(R.id.txtunitselsalesratecap);
        final TextView txtunitselsalesrate=(TextView)DialougView.findViewById(R.id.txtunitselsalesrate);
        final EditText txtKitchenNote=(EditText)DialougView.findViewById(R.id.txtkitchenNotes);
        final Button btnunitseldecrement=(Button)DialougView.findViewById(R.id.btnunitseldecrement);
        final Button btnunitselincrement=(Button)DialougView.findViewById(R.id.btnunitselincrement);
        final TextView lblunitselqty=(TextView)DialougView.findViewById(R.id.lblunitselqty);
        final GridView grdunits=(GridView)DialougView.findViewById(R.id.grdunits);
        final GridView grdmodifiers=(GridView)DialougView.findViewById(R.id.grdmodifiers);
        final RelativeLayout btnaddtocartunit=(RelativeLayout) DialougView.findViewById(R.id.reladdtocart);
        Common._selectedmodifiers=new Hashtable<>();
        String sProductImage=p.get_ProductImage();
        if(!sProductImage.equals("")) {
            byte[] decodedString= android.util.Base64.decode(sProductImage,android.util.Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgunit.setImageBitmap(bmp);
        }
        txtunitproductname.setText(p.get_productName());
        txtunititemdesription.setText(p.get_Description());
        txtunitselunit.setText(p.get_DefaultUnit());
        txtunitselunit.setTag(p.get_UnitID());
        txtunitselsalesratecap.setText(Common.CurrencySymbol);
        txtunitselsalesrate.setText(String.format(Common.sDecimals,p.get_SalesRate()));
        lblunitselqty.setText("1");
        txtKitchenNote.setText("");
        btnunitselincrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Double dblQty=Double.valueOf(lblunitselqty.getText().toString());
               dblQty=dblQty + 1;
               lblunitselqty.setText(String.format(Common.sDecimalsQty,dblQty));
            }
        });
        btnunitseldecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double dblQty=Double.valueOf(lblunitselqty.getText().toString());
                if(dblQty>1)
                {
                    dblQty=dblQty-1;
                    lblunitselqty.setText(String.format(Common.sDecimalsQty,dblQty));
                }
            }
        });


        _unitrates.clear();
        String url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/Unit?ProductID=" + p.get_id();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _unitrates.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        UnitRate _unitRate=new UnitRate();
                        _unitRate.set_Unit(jsonObject.getString("UnitName"));
                        _unitRate.set_id(jsonObject.getInt("id"));
                        _unitRate.set_cf(jsonObject.getDouble("CF"));
                        _unitRate.set_SalesRate(jsonObject.getDouble("SalesRate"));
                        _unitrates.add(_unitRate);
                    }
                    UnitAdapter adapter = new UnitAdapter(ProductActivity.this, _unitrates,DialougView);
                    grdunits.setAdapter(adapter);
                }
                catch (Exception w)
                {
                    Toast.makeText(ProductActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
        // Populating Modifiers
        _modifiers.clear();
        url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/Modifier?ProductID=" + p.get_id();
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _modifiers.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Modifier _modifier=new Modifier();
                        _modifier.set_Name(jsonObject.getString("Name"));
                        _modifier.set_id(jsonObject.getInt("id"));
                        _modifiers.add(_modifier);
                    }
                    ModifierAdapter adapter2= new ModifierAdapter(ProductActivity.this, _modifiers,DialougView);
                    grdmodifiers.setAdapter(adapter2);
                }
                catch (Exception w)
                {
                    Toast.makeText(ProductActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest2);

        btnaddtocartunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String smodifiers="";
                Enumeration<String> modi = Common._selectedmodifiers.keys();
                while(modi.hasMoreElements()) {
                    String k = modi.nextElement();
                    smodifiers = smodifiers + k + ",";
                }
                if(!smodifiers.equals(""))
                {
                    smodifiers=smodifiers.substring(0,smodifiers.length()-1);
                }
                OrderDTL _dtl=new OrderDTL();
                _dtl.set_id(Common._orderdtls.size() + 1);
                _dtl.set_productid(p.get_id());
                _dtl.set_ProductCode(p.get_productCode());
                _dtl.set_ProductName(p.get_productName());
                _dtl.set_Qty(Double.valueOf(lblunitselqty.getText().toString()));
                _dtl.set_unitid(Integer.valueOf(txtunitselunit.getTag().toString()));
                _dtl.set_Unit(txtunitselunit.getText().toString());
                _dtl.set_Rate(Double.valueOf(txtunitselsalesrate.getText().toString()));
                _dtl.set_Amount(p.get_SalesRate());
                _dtl.set_KitchenNote(txtKitchenNote.getText().toString());
                _dtl.set_Modifiers(smodifiers);
                _dtl.set_OrderStatus(0);
                Common._orderdtls.add(_dtl);
                Toast.makeText(ProductActivity.this,"Added to Cart!",Toast.LENGTH_LONG).show();
                alert.dismiss();
            }
        });
        alert.setView(DialougView);
        alert.show();

    }
    private  void getProductList(String sGroupID)
    {
        productnames.clear();
        _products.clear();
        _productlist=new Hashtable<Integer, Product>();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Product?GroupID=" + sGroupID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    productnames.clear();
                    _products.clear();
                    _productlist=new Hashtable<Integer, Product>();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product _product=new Product();
                        _product.set_id(jsonObject.getInt("id"));
                        _product.set_productCode(jsonObject.getString("ProductCode"));
                        _product.set_productName(jsonObject.getString("ProductName"));
                        _product.set_PurchaseRate(jsonObject.getDouble("PurchaseRate"));
                        _product.set_SalesRate(jsonObject.getDouble("SalesRate"));
                        _product.set_UnitID(jsonObject.getInt("UnitID"));
                        _product.set_DefaultUnit(jsonObject.getString("Unit"));
                        _product.set_ProductImage(jsonObject.getString("ProductImage"));
                        _product.set_Description(jsonObject.getString("Description"));
                        productnames.add(_product.get_productName());
                        _products.add(_product);
                        _productlist.put(_product.get_id(),_product);
                    }
                    ProductAdapter adapter = new ProductAdapter(ProductActivity.this, _products);
                    grdproducts.setAdapter(adapter);
                }
                catch (Exception w)
                {
                    Toast.makeText(ProductActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}