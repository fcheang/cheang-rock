package com.suntek.scheduler.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.suntek.scheduler.appsvcs.persistence.StandardHoliday;

public class HolidayCalculator {
	 
	 private static HolidayCalculator singleton = null;
	 private static Object lock = new Object();
	 
	 private List cachedYear = new ArrayList();
	 private Map dateToHolidayName = new Hashtable();
	 
	 private HolidayCalculator(){
	 }
	 
	 public static HolidayCalculator getInstance(){
		 if (singleton == null){
			 synchronized (lock){
				 if (singleton == null){
					 singleton = new HolidayCalculator();
				 }
			 }
		 }
		 return singleton;
	 }
	 
	 /** 
	  * @param date given date
	  * @return the holiday name if the given date is a holiday, otherwise
	  * returns null
	  */
	 public String getHoliday(Date date){
		 Calendar cal = new GregorianCalendar();
		 cal.setTime(date);
		 int y = cal.get(Calendar.YEAR);
		 initHoliday(y);
		 return (String)dateToHolidayName.get(getKey(date));
	 }
	 
	 private String getKey(Date d){
		 Calendar c = new GregorianCalendar();
		 c.setTime(d);
		 int y = c.get(Calendar.YEAR);
		 int m = c.get(Calendar.MONTH);
		 int t = c.get(Calendar.DATE);
		 return ""+y+"-"+m+"-"+t;
	 }
	 	 
	 private void initHoliday(int y){
		 if (y < 1900){
			 y += 1900;
		 }
		 
		 if (!cachedYear.contains(new Integer(y))){
			 synchronized (this){
				 if (!cachedYear.contains(new Integer(y))){
					 if (cachedYear.size() > 3){
						 dateToHolidayName.clear();
						 cachedYear.clear();
					 }
					 
					 // cache holiday for year + next new year day
					 Date d = getNewYearsDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.NEW_YEARS_DAY);

					 d = getNewYearsDay(y + 1); // next year's new year day in case it falls on Saturday
					 dateToHolidayName.put(getKey(d), StandardHoliday.NEW_YEARS_DAY);

					 d = getMartinLutherKingDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.MLK_DAY);
					 					 
					 d = getPresidentsDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.PRESIDENTS_DAY);
					 
					 d = getMemorialDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.MEMORIAL_DAY);
					 
					 d = getIndependenceDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.INDEPENDENCE_DAY);
					 
					 d = getLaborDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.LABOR_DAY);
					 
					 d = getColumbusDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.COLUMBUS_DAY);
					 
					 d = getVeteransDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.VETERANS_DAY);
					 
					 d = getThanksgiving(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.THANKSGIVING_DAY);					 
					 
					 d = getChristmasDay(y);
					 dateToHolidayName.put(getKey(d), StandardHoliday.CHRISTMAS_DAY);
					 
					 cachedYear.add(new Integer(y));					 
				 }
			 }
		 }
	 }	 
	 
	 public static Date getNewYearsDay (int nYear)
     {
	    int nX;
	    int nMonth = 0;         // January
	    int nMonthDecember = 11;    // December
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 1);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
        {
	        case Calendar.SUNDAY : // Sunday
		        cal.set(nYear, nMonth, 2);
		        return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        case Calendar.TUESDAY : // Tuesday
	        case Calendar.WEDNESDAY : // Wednesday
	        case Calendar.THURSDAY : // Thursday
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 1);
	        	return cal.getTime();
	        default :
	        	// Saturday, then observe on friday of previous year
		        cal.set(--nYear, nMonthDecember, 31);
		        return cal.getTime();
        }
    }	 	
	    
    public Date getMartinLutherKingDay (int nYear)
    {    	
	    // Third Monday in January
	    int nX;
	    int nMonth = 0; // January
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 1);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 16);
	        	return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        	cal.set(nYear, nMonth, 15);
	        	return cal.getTime();
	        case Calendar.TUESDAY : // Tuesday
	        	cal.set(nYear, nMonth, 21);
	        	return cal.getTime();
	        case Calendar.WEDNESDAY : // Wednesday
	        	cal.set(nYear, nMonth, 20);
	        	return cal.getTime();
	        case Calendar.THURSDAY : // Thursday
	        	cal.set(nYear, nMonth, 19);
	        	return cal.getTime();
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 18);
	        	return cal.getTime();
	        default : // Saturday
	        	cal.set(nYear, nMonth, 17);
        		return cal.getTime();
        }
    }

    public static Date getPresidentsDay(int nYear)
    {
	    // Third Monday in February
	    int nX;
	    int nMonth = 1; // February
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 1);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
	        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 16);
	        	return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        	cal.set(nYear, nMonth, 15);
	        	return cal.getTime();
	        case Calendar.TUESDAY : // Tuesday
	        	cal.set(nYear, nMonth, 21);
	        	return cal.getTime();
	        case Calendar.WEDNESDAY : // Wednesday
	        	cal.set(nYear, nMonth, 20);
	        	return cal.getTime();
	        case Calendar.THURSDAY : // Thursday
	        	cal.set(nYear, nMonth, 19);
	        	return cal.getTime();
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 18);
	        	return cal.getTime();
	        default : // Saturday
	        	cal.set(nYear, nMonth, 17);
        		return cal.getTime();
	        }
	    }	                     	                     

    /**
     * Memorial Day, the last Monday in May, returns us back to relatively simple 
     * date calculations. 
     */
    public static Date getMemorialDay(int nYear)
    {
	    // Last Monday in May
	    int nX;
	    int nMonth = 4; //May
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 31);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
	        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 25);
	        	return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        	cal.set(nYear, nMonth, 31);
	        	return cal.getTime();
	        case Calendar.TUESDAY : // Tuesday
	        	cal.set(nYear, nMonth, 30);
	        	return cal.getTime();
	        case Calendar.WEDNESDAY : // Wednesday
	        	cal.set(nYear, nMonth, 29);
	        	return cal.getTime();
	        case Calendar.THURSDAY : // Thursday
	        	cal.set(nYear, nMonth, 28);
	        	return cal.getTime();
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 27);
	        	return cal.getTime();
	        default : // Saturday
	        	cal.set(nYear, nMonth, 26);
	        	return cal.getTime();
	        }
    }

	                     

    /**
     * Independence Day, July 4th, is another easy day to calculate
     * , since the only effort required is to see if the holiday falls 
     * on the weekend. If it does, then the observance rule should be 
     * applied to determine if businesses will observe the holiday on 
     * either the preceding Friday or the following Monday.
     */ 
    public static Date getIndependenceDay(int nYear)
	    {
	    int nX;
	    int nMonth = 6; // July
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 4);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
	        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 5);
	        	return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        case Calendar.TUESDAY : // Tuesday
	        case Calendar.WEDNESDAY : // Wednesday
	        case Calendar.THURSDAY : // Thursday
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 4);
	        	return cal.getTime();
	        default :
	        	// Saturday
	        	cal.set(nYear, nMonth, 3);
        		return cal.getTime();
	        }
    }                     

    /**
	 * Labor Day is the first Monday in September.
	 */ 
    public static Date getLaborDay(int nYear)
    {
	    // The first Monday in September
	    int nX;
	    int nMonth = 8; // September
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 1);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
	        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 2);
	        	return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        	cal.set(nYear, nMonth, 1);
	        	return cal.getTime();
	        case Calendar.TUESDAY : // Tuesday
	        	cal.set(nYear, nMonth, 7);
	        	return cal.getTime();
	        case Calendar.WEDNESDAY : // Wednesday
	        	cal.set(nYear, nMonth, 6);
	        	return cal.getTime();
	        case Calendar.THURSDAY : // Thursday
	        	cal.set(nYear, nMonth, 5);
	        	return cal.getTime();
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 4);
	        	return cal.getTime();
	        default : // Saturday
	        	cal.set(nYear, nMonth, 3);
        		return cal.getTime();
	        }
    }

	                     

    /**
     * Columbus Day is the second Monday in October. This holiday generally 
     * is observed in the banking industry.
     */ 
    public static Date getColumbusDay(int nYear)
    {
	    // Second Monday in October
	    int nX;
	    int nMonth = 9; // October
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 1);
	    nX = cal.get(Calendar.DAY_OF_WEEK);	    
	    switch(nX)
	        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 9);
	        	return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        	cal.set(nYear, nMonth, 8);
	        	return cal.getTime();
	        case Calendar.TUESDAY : // Tuesday
	        	cal.set(nYear, nMonth, 14);
	        	return cal.getTime();
	        case Calendar.WEDNESDAY : // Wednesday
	        	cal.set(nYear, nMonth, 13);
	        	return cal.getTime();
	        case Calendar.THURSDAY : // Thursday
	        	cal.set(nYear, nMonth, 12);
	        	return cal.getTime();
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 11);
	        	return cal.getTime();
	        default : // Saturday
	        	cal.set(nYear, nMonth, 10);
        		return cal.getTime();
	        }
    }

    /**
     * Thanksgiving Day, as celebrated in the United States, is the fourth Thursday 
     * in November.
     */ 
    public static Date getThanksgiving(int nYear)
    {
	    int nX;
	    int nMonth = 10; // November
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 1);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
	        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 26);
	        	return cal.getTime();
	        case Calendar.MONDAY : // Monday
	        	cal.set(nYear, nMonth, 25);
	        	return cal.getTime();
	        case Calendar.TUESDAY : // Tuesday
	        	cal.set(nYear, nMonth, 24);
	        	return cal.getTime();
	        case Calendar.WEDNESDAY : // Wednesday
	        	cal.set(nYear, nMonth, 23);
	        	return cal.getTime();
	        case Calendar.THURSDAY : // Thursday
	        	cal.set(nYear, nMonth, 22);
	        	return cal.getTime();
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 28);
	        	return cal.getTime();
	        default : // Saturday
	        	cal.set(nYear, nMonth, 27);
        		return cal.getTime();
	        }
	    } 

	                     

    /**
	 * Christmas Day is December 25th, and if the 25th falls on a Saturday, 
	 * Christmas is observed by businesses on Christmas Eve, December 24th. 
	 * If December 25th falls on a Sunday, then it will be observed by 
	 * businesses in the United States on the 26th. This day after Christmas 
	 * is known as Boxing Day in the United Kingdom.
	 */ 
    public static Date getChristmasDay(int nYear)
    {
	    int nX;
	    int nMonth = 11; // December
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 25);
	    nX = cal.get(Calendar.DAY_OF_WEEK);
	    switch(nX)
	        {
	        case Calendar.SUNDAY : // Sunday
	        	cal.set(nYear, nMonth, 26);
        		return cal.getTime();	        	
	        case Calendar.MONDAY : // Monday
	        case Calendar.TUESDAY : // Tuesday
	        case Calendar.WEDNESDAY : // Wednesday
	        case Calendar.THURSDAY : // Thursday
	        case Calendar.FRIDAY : // Friday
	        	cal.set(nYear, nMonth, 25);
        		return cal.getTime();
	        default :
	        // Saturday
	        	cal.set(nYear, nMonth, 24);
    			return cal.getTime();
	        }
    }

    public static Date getVeteransDay(int nYear)
    {
	    //November 11th
	    int nMonth = 10; // November
	    Calendar cal = new GregorianCalendar();
	    cal.set(nYear, nMonth, 11);
	    return cal.getTime();
    }
    
}
