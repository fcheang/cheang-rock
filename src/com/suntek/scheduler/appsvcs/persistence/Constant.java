package com.suntek.scheduler.appsvcs.persistence;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>Title: Appointment Scheduler</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Sunteksystems</p>
 * @author Steve Cheang
 * @version 1.0
 */

public class Constant {

    public static boolean debugOn = Boolean.getBoolean("debugOn");

    public static final String version = "1.21";
    public static Calendar lastUpdated = new GregorianCalendar(2011, 6-1, 20);
    
    public static Date lastActive = new Date();

    // Theme
    public static final Color MAIN_APPT_COLOR = new Color(255, 255, 155);
    public static final Color SEEN_COLOR = new Color(128, 255, 128);
    public static final Color NOT_SEEN_COLOR = new Color(155, 255, 255);

    public static final Color BLOCK_COLOR = new Color(255, 193, 193);
    public static final Color TIME_INTERVAL_COLOR = new Color(200, 200, 200);

    public static final Color TITLE_BAR_COLOR = new Color(119, 0, 0);
    public static final Color TITLE_BAR_TEXT_COLOR = Color.white;
    public static final Color TREE_PANEL_COLOR = new Color(210, 210, 210);
    
    public static final Color OVERDUE_SELECTED_COLOR = new Color(192, 118, 118);
    public static final Color OVERDUE_COLOR = new Color(255, 193, 193);

    // Constant
    public static final DateFormat df_l = DateFormat.getDateInstance(DateFormat.LONG);
    public static final DateFormat df_s = DateFormat.getDateInstance(DateFormat.SHORT);
    public static final DateFormat tf_s = DateFormat.getTimeInstance(DateFormat.SHORT);
    public static final DateFormat dtf_s = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

    public static final String Appointment = "Appointment";
    public static final String DoubleBook = "Double Book";
    public static final String Blocked = "Blocked";

    // Global variable
    public static String appUser = null;
    public static String appPass = null;
    public static List<String> appRole = null;
    public static String selectedClinic = null;
    
    // Available roles
    public static String ADMINISTRATOR = "Administrator";
    public static String MANAGER = "Manager";
    public static String RECEPTIONIST = "Receptionist";
    public static String BILLING = "Billing";
    public static String ELIGIBLITY_CHECK = "EligibilityCheck";

    // Table names
    public static String APPOINTMENT = "appointment";

    // Appointment status
    public static String Scheduled = "Scheduled";
    public static String NotScheduled = "Not Scheduled";
    public static String Waitlist = "Waitlist";
    public static String Seen = "Seen";
    public static String NotSeen = "Not Seen";
    public static String CANCELED = "Canceled";
    
    // Evaluation status
    public static String Eval_Scheduled = "Scheduled";
    public static String Eval_completed = "Completed";
    
    public static String CONTRA_COSTA_ACCESS = "Contra Costa Access";
    public static String HEALTHPAC_PREFIX = "HealthPac";

    public static java.util.Date END_OF_TIME = (new GregorianCalendar(2200, 0, 1)).getTime();
    
    // html style sheet

    public static String subTitleFont = "<font color=#660000 font-size=16px font-style=oblique>";
    public static String regFont = "<font color=#333333 font-size=14px>";
    public static String thFont = "<font color=#330000 font-size=12px>";

    public static TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");

    public static Calendar DEC_FIRST_CALENDAR = null;
    public static Date DEC_FIRST_DATE = null;
    static {
    	Calendar c = new GregorianCalendar();
    	c.set(Calendar.YEAR, 2010);
    	c.set(Calendar.MONTH, 11);
    	c.set(Calendar.DAY_OF_MONTH, 1);
    	c.set(Calendar.HOUR_OF_DAY, 0);
    	c.set(Calendar.MINUTE, 0);
    	c.set(Calendar.SECOND, 0);
    	c.set(Calendar.MILLISECOND, 0);
    	DEC_FIRST_CALENDAR = c;
    	DEC_FIRST_DATE = c.getTime();        	
    }
    
    public static String formatPhoneNum(String pn){
        if (pn == null){
            return pn;
        }
        if (pn.contains("-")){
        	return pn;
        }
        StringBuffer sb = new StringBuffer();

        if (pn.length() == 11){
            // 1-800-123-8000
            sb.append(pn.substring(0, 1)).append("-").append(pn.substring(1, 4)).append("-");
            sb.append(pn.substring(4, 7)).append("-").append(pn.substring(7));
        }else if (pn.length() == 10){
            // 510-432-9000
            sb.append(pn.substring(0, 3)).append("-").append(pn.substring(3, 6)).append("-");
            sb.append(pn.substring(6));
        }else if (pn.length() == 7){
            // 489-8000
            sb.append(pn.substring(0, 3)).append("-").append(3);
        }else{
            sb.append(pn);
        }
        return sb.toString();
    }
    
    public static String clearPhoneNumFormatting(String pn){
    	if (pn == null)
    		return pn;
    	// strip off -
    	if (pn.contains("-")){
    		pn = pn.replaceAll("-", "");
    	}
    	// strip off ()
    	if (pn.contains("(")){
    		pn = pn.replaceAll("(", "");
    	}
    	if (pn.contains(")")){
    		pn = pn.replaceAll(")", "");
    	}
    	return pn;
    }

    public static int compareTo(Calendar cal1, Calendar cal2) {
        Calendar c1 = (Calendar)cal1.clone();
        Calendar c2 = (Calendar)cal2.clone();
        c1.setLenient(true);
        c2.setLenient(true);
        long thisTime = c1.getTimeInMillis();
        long t = c2.getTimeInMillis();
        return (thisTime > t) ? 1 : (thisTime == t) ? 0 : -1;
    }

}
