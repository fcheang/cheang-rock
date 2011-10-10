package com.suntek.scheduler.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Utility class. Place for all static helper function.
 * 
 * @author Steve Cheang
 */
public class Util {

    public static Date END_OF_TIME = null;
    public static DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    public static DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
	
	public static boolean isValidNumber(String n){
		try{
			new BigDecimal(n);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public static Date getNextDate(Date today){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(today);
		cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
    public static Date getEndOfTime(){
        if (END_OF_TIME == null){
            END_OF_TIME = (new GregorianCalendar(2200, 0, 1)).getTime();
        }
        return END_OF_TIME;
    }
    
    public static Date getDateForDB(int month, int day, int year){
        Date d = null;
        if (year != 0){
            d = new GregorianCalendar(year, month - 1, day).getTime();
        }
        return d;    	
    }
	
	public static String getDateStr(Date d){
		return df.format(d);
	}
	
	public static String getDateTimeStr(Date d){
		return dtf.format(d);
	}
 
    public static int getMonth(Date date){
        int val = -1;
        // gc is zero based
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        val = cal.get(Calendar.MONTH);
        return val + 1;
    }

    public static int getDay(Date date){
        int val = -1;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        val = cal.get(Calendar.DATE);
        return val;
    }

    public static int getYear(Date date){
        int val = -1;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        val = cal.get(Calendar.YEAR);
        return val;
    }
	
    public static String getMonthStr(Date date){
    	int m = getMonth(date);
    	if (m != -1){
    		return Integer.toString(m);
    	}else{
    		return "";
    	}
    }

    public static String getDayStr(Date date){
    	int m = getDay(date);
    	if (m != -1){
    		return Integer.toString(m);
    	}else{
    		return "";
    	}
    }

    public static String getYearStr(Date date){
    	int m = getYear(date);
    	if (m != -1){
    		return Integer.toString(m);
    	}else{
    		return "";
    	}
    }
    	
}
