package com.atlanta.rms.Models;

public class Group {

    private  int _id;
    private  String _GroupName;
    private  String _GroupCode;
    private String _GroupImage;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_GroupName() {
        return _GroupName;
    }

    public void set_GroupName(String _GroupName) {
        this._GroupName = _GroupName;
    }

    public String get_GroupCode() {
        return _GroupCode;
    }

    public void set_GroupCode(String _GroupCode) {
        this._GroupCode = _GroupCode;
    }

    public String get_GroupImage() {
        return _GroupImage;
    }

    public void set_GroupImage(String _GroupImage) {
        this._GroupImage = _GroupImage;
    }
}
