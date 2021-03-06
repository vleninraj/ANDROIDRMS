package com.atlanta.rms;

import com.atlanta.rms.Models.Floor;
import com.atlanta.rms.Models.OrderDTL;
import com.atlanta.rms.Models.Party;
import com.atlanta.rms.Models.Table;

import java.util.ArrayList;
import java.util.Dictionary;

public class Common {

    public static String DomainName="atACCWeb";
    public static String sCurrentWaiterName="";
    public static String sCurrentWaiterID="";
    public static String sCurrentOrderType="";// Take Away or Dine In
    public static String selectedGroupName="";
    public static String selectedGroupID="";
    public static String selectedProductName="";
    public static String selectedProductID="";
    public static Dictionary<String, Party> _parties;
    public static Dictionary<String, Table> _tables;
    public static Dictionary<String, Floor> _floors;
    public static Dictionary<String,String> _selectedmodifiers;
    public static String selectedTableName="";
    public static String selectedTableID="";


    public static ArrayList<OrderDTL> _orderdtls;
    //Company Details
    public static String CompanyName="";
    public static String Address1="";
    public static String Address2="";
    public static String Address3="";
    public static int NoofDecimals=2;
    public static int NoofDecimalsQty=1;
    public static String CurrencySymbol="";
    public static String Country="";
    public static String sDecimals="";
    public static String sDecimalsQty="";
}
