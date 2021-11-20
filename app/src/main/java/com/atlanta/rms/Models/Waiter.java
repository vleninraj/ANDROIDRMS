package com.atlanta.rms.Models;

import java.security.PrivateKey;

public class Waiter {

    private int _id;
    private String _WaiterName;
    private String _WaiterCode;
    private String _PINNumber;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_WaiterName() {
        return _WaiterName;
    }

    public void set_WaiterName(String _WaiterName) {
        this._WaiterName = _WaiterName;
    }

    public String get_WaiterCode() {
        return _WaiterCode;
    }

    public void set_WaiterCode(String _WaiterCode) {
        this._WaiterCode = _WaiterCode;
    }

    public String get_PINNumber() {
        return _PINNumber;
    }

    public void set_PINNumber(String _PINNumber) {
        this._PINNumber = _PINNumber;
    }
}
