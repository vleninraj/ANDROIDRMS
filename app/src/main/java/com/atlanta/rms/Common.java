package com.atlanta.rms;

import java.util.Dictionary;

public class Common {

    public static String DomainName="atACCWeb";
    public static String sCurrentWaiterName="";
    public static String sCurrentWaiterID="";
    public static String sCurrentOrderType="";// Take Away or Dine In
    public static Dictionary<String,Party> _parties;
    public static Dictionary<String,Table> _tables;
    public static Dictionary<String,Floor> _floors;
    public static String selectedTableName;
    public static String selectedTableID;
}
