package com.suntek.scheduler.appsvcs.persistence;

import java.text.DateFormat;
import java.util.Date;

public class Evaluation {

	private int refId;
	private int year;
	private Date checkoffDate;
	private String checkoffBy;
	private String notes;
	
	private static DateFormat df = DateFormat.getDateInstance(DateFormat.LONG); 
	
	public int getRefId() {
		return refId;
	}
	public void setRefId(int refId) {
		this.refId = refId;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Date getCheckoffDate() {
		return checkoffDate;
	}
	public void setCheckoffDate(Date checkoffDate) {
		this.checkoffDate = checkoffDate;
	}
	public String getCheckoffBy() {
		return checkoffBy;
	}
	public void setCheckoffBy(String checkoffBy) {
		this.checkoffBy = checkoffBy;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String toString(){
		return "Evaluation for "+year+" checked off by "+checkoffBy+" on "+df.format(checkoffDate)+".\n"+
		"Checkoff Notes: "+notes;
	}
}
