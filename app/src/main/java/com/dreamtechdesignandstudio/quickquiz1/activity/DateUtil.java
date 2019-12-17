package com.dreamtechdesignandstudio.quickquiz1.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
public static  Date date=new Date();
public  static  String monthCurrent(){
    SimpleDateFormat format=new SimpleDateFormat("MMyyyy");
    return  format.format(date);
}
public static  String today(){
    SimpleDateFormat format=new SimpleDateFormat("ddMMyyyy");
    return format.format(date);
}
public  static String yesterDay(){
    Calendar calendar = Calendar.getInstance();

    calendar.add(Calendar.DATE, -1);
    Date yesterday = calendar.getTime();
    SimpleDateFormat format=new SimpleDateFormat("ddMMyyyy");
    return format.format(yesterday);
}
}
