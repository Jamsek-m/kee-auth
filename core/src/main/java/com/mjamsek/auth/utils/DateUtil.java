package com.mjamsek.auth.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    
    private DateUtil() {
    
    }
    
    public static Date addToDate(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
}
