package com.suntek.scheduler.appsvcs.persistence;

import java.util.Date;

import com.suntek.scheduler.ui.DateStringConverter;
import com.suntek.scheduler.util.Util;

public class Holiday {

	private int id;
	private Date startDate;
	private Date endDate;
	private String desc;
	
	public void setId(int i){
		id = i;
	}
	
	public void setStartDate(Date d){
		startDate = d;
	}
	
    public void setStartDateStr(String dateStr){
    	startDate = DateStringConverter.getDateFromDateStr(dateStr);
    }	
	
	public void setEndDate(Date d){
		endDate = d;
	}

    public void setEndDateStr(String dateStr){
    	endDate = DateStringConverter.getDateFromDateStr(dateStr);
    }	
	
	public void setDesc(String d){
		desc = d;
	}
	
	public int getId(){
		return id;
	}
	
	public Date getStartDate(){
		return startDate;
	}
	
	public String getStartDateStr(){
		if (startDate != null){
			return DateStringConverter.getStrFromDate(startDate);
		}else{
			return "";
		}
	}
	
	public Date getEndDate(){
		return endDate;
	}
	
	public String getEndDateStr(){
		if (endDate != null){
			return DateStringConverter.getStrFromDate(endDate);
		}else{
			return "";
		}
	}
	
	public String getDesc(){
		return desc;
	}
	
    public String getStartDateForSQL(){
    	if (startDate != null){
    		int m = Util.getMonth(startDate);
    		int d = Util.getDay(startDate);
    		int y = Util.getYear(startDate);
    		return y+"-"+m+"-"+d;
    	}else{
    		return "";
    	}
    }    
    
    public String getEndDateForSQL(){
    	if (endDate != null){
    		int m = Util.getMonth(endDate);
    		int d = Util.getDay(endDate);
    		int y = Util.getYear(endDate);
    		return y+"-"+m+"-"+d;
    	}else{
    		return "";
    	}
    }    
	
}
