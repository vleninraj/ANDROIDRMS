package com.atlanta.rms;

public class Product {
    private int _id;
    private String _productCode;
    private String _productName;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_productCode() {
        return _productCode;
    }

    public void set_productCode(String _productCode) {
        this._productCode = _productCode;
    }

    public String get_productName() {
        return _productName;
    }

    public void set_productName(String _productName) {
        this._productName = _productName;
    }
}
