package com.atlanta.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    GridView grdproducts;
    RequestQueue requestQueue;
    final ArrayList<Product> _products=new ArrayList<>();
    final ArrayList<Product> _productsfiltered=new ArrayList<>();
    ArrayList<String> productnames = new ArrayList<>();
    AutoCompleteTextView txtproductsearch;
    String sIpAddress="";
    String sGroupID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        sGroupID=(String) bd.get("GroupID");
        grdproducts=(GridView)findViewById(R.id.grdproducts);
        txtproductsearch=(AutoCompleteTextView)findViewById(R.id.txtproductsearch);
        requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        getProductList(sGroupID);
        ArrayAdapter<String> _productadapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_list_item_1, productnames.toArray(new String[productnames.size()]));
        txtproductsearch.setAdapter(_productadapter);
        txtproductsearch.setThreshold(0);
        txtproductsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence str, int i, int i1, int i2) {

                _productsfiltered.clear();
                for(Product _product:_products)
                {
                    if(_product.get_productName().toUpperCase().startsWith(str.toString().toUpperCase())
                            || _product.get_productName().toUpperCase().endsWith(str.toString().toUpperCase()))
                    {
                        _productsfiltered.add(_product);
                    }
                }
                ProductAdapter adapter = new ProductAdapter(ProductActivity.this, _productsfiltered);
                grdproducts.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        grdproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolderProduct holder=(ViewHolderProduct) view.getTag();
                Common.selectedProductName=  holder.txtproductname.getText().toString();
                Common.selectedProductID=holder.txtproductname.getTag().toString();
                //  Intent intent = new Intent(GroupActivity.this, OrderTypeActivity.class);
                //  startActivity(intent);
            }
        });
    }
    private  void getProductList(String sGroupID)
    {
        productnames.clear();
        _products.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Product?GroupID=" + sGroupID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    productnames.clear();
                    _products.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product _product=new Product();
                        _product.set_id(jsonObject.getInt("id"));
                        _product.set_productCode(jsonObject.getString("ProductCode"));
                        _product.set_productName(jsonObject.getString("ProductName"));
                        productnames.add(_product.get_productName());
                        _products.add(_product);
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