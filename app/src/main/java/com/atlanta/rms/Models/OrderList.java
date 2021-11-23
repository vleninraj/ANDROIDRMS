package com.atlanta.rms.Models;

public class OrderList {

    private int _id;
    private String _VoucherNo;
    private String _Party;
    private String _MobileNumber;
    private String _TableName;
    private Double _GrandAmount;
    private int _billed;
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_VoucherNo() {
        return _VoucherNo;
    }

    public void set_VoucherNo(String _VoucherNo) {
        this._VoucherNo = _VoucherNo;
    }

    public String get_Party() {
        return _Party;
    }

    public void set_Party(String _Party) {
        this._Party = _Party;
    }

    public String get_MobileNumber() {
        return _MobileNumber;
    }

    public void set_MobileNumber(String _MobileNumber) {
        this._MobileNumber = _MobileNumber;
    }

    public String get_TableName() {
        return _TableName;
    }

    public void set_TableName(String _TableName) {
        this._TableName = _TableName;
    }

    public int get_billed() {
        return _billed;
    }

    public void set_billed(int _billed) {
        this._billed = _billed;
    }

    public Double get_GrandAmount() {
        return _GrandAmount;
    }

    public void set_GrandAmount(Double _GrandAmount) {
        this._GrandAmount = _GrandAmount;
    }
}
