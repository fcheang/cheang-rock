package com.suntek.scheduler.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateStringConverter {

	private static final String DELIMITER = "/"; 
	
	/**
	 * returns true if dateStr is in the format MM/DD/YYYY or MM/DD/YY
	 */
	public static boolean isDateStringValid(String dateStr){
		if (dateStr != null){
			int i = dateStr.indexOf(DELIMITER);
			if (i > 0){
				i = dateStr.indexOf(DELIMITER, i+1);
				if (i > 0){
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getMonthFromDateStr(String dateStr){
		int i = dateStr.indexOf(DELIMITER);
		String s = dateStr.substring(0, i);		
		return Integer.parseInt(s);
	}
	
	public static int getDayFromDateStr(String dateStr){
		int i = dateStr.indexOf(DELIMITER);
		int j = dateStr.indexOf(DELIMITER, i+1);
		String s = dateStr.substring(i+1, j);
		return Integer.parseInt(s);
	}

	public static int getYearFromDateStr(String dateStr){
		int i = dateStr.indexOf(DELIMITER);
		int j = dateStr.indexOf(DELIMITER, i+1);
		String s = dateStr.substring(j+1);
		return Integer.parseInt(s);
	}
	
	public static Date getDateFromDateStr(String dateStr){
		int year = getYearFromDateStr(dateStr);
		int month = getMonthFromDateStr(dateStr);
		int day = getDayFromDateStr(dateStr);
		Calendar cal = new GregorianCalendar(year, month-1, day);
		return cal.getTime();
	}
	
	public static String getStrFromDate(Date date){
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH)+1;
		int d = c.get(Calendar.DATE);
		return m + DELIMITER + d + DELIMITER + y;
	}

}
