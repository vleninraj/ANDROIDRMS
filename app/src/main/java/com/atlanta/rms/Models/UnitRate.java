package com.atlanta.rms.Models;

public class UnitRate {

    private int _id;
    private String _Unit;
    private double _SalesRate;
    private double _cf;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_Unit() {
        return _Unit;
    }

    public void set_Unit(String _Unit) {
        this._Unit = _Unit;
    }

    public double get_SalesRate() {
        return _SalesRate;
    }

    public void set_SalesRate(double _SalesRate) {
        this._SalesRate = _SalesRate;
    }

    public double get_cf() {
        return _cf;
    }

    public void set_cf(double _cf) {
        this._cf = _cf;
    }
}
