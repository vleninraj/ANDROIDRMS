package com.atlanta.rms.Models;

public class Product {
    private int _id;
    private String _productCode;
    private String _productName;
    private String _DefaultUnit;
    private Double _PurchaseRate;
    private Double _SalesRate;
    private int _UnitID;
    private String _ProductImage;

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

    public String get_DefaultUnit() {
        return _DefaultUnit;
    }

    public void set_DefaultUnit(String _DefaultUnit) {
        this._DefaultUnit = _DefaultUnit;
    }

    public Double get_PurchaseRate() {
        return _PurchaseRate;
    }

    public void set_PurchaseRate(Double _PurchaseRate) {
        this._PurchaseRate = _PurchaseRate;
    }

    public Double get_SalesRate() {
        return _SalesRate;
    }

    public void set_SalesRate(Double _SalesRate) {
        this._SalesRate = _SalesRate;
    }

    public int get_UnitID() {
        return _UnitID;
    }

    public void set_UnitID(int _UnitID) {
        this._UnitID = _UnitID;
    }

    public String get_ProductImage() {
        return _ProductImage;
    }

    public void set_ProductImage(String _ProductImage) {
        this._ProductImage = _ProductImage;
    }
}
