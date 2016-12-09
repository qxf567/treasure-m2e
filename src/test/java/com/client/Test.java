package com.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class Test {

    public static void main(String[] args) {
//	String s = "2016-12-08 10:17:38.00000679";
//	System.out.println(DateUtil.parse(s, "yyyy-MM-dd HH:mm:ss.SSS"));
	
	
//	 // String s = "Sun Sep 02 2012 08:00:00 GMT+08:00";
//	  String s = "Fri Jan 29 19:41:00 GMT+08:00 2016";
//	        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
//	        try {
//		    Date d = sf.parse(s);
//		    System.out.println(d);
//		} catch (ParseException e) {
//		    e.printStackTrace();
//		}
	String databaseTable = "treasure01&orders";
	System.out.println( StringUtils.endsWith(databaseTable, "orders"));
	        
    }

}
