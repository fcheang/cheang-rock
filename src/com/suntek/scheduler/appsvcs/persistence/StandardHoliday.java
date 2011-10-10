package com.suntek.scheduler.appsvcs.persistence;

public class StandardHoliday {

	public static final String NEW_YEARS_DAY = "New Years Day";
	public static final String MLK_DAY = "Martin Luther King Day";
	public static final String PRESIDENTS_DAY = "Presidents Day";
	public static final String MEMORIAL_DAY = "Memorial Day";
	public static final String INDEPENDENCE_DAY = "Independence Day";	 
	public static final String LABOR_DAY = "Labor Day";
	public static final String COLUMBUS_DAY = "Columbus Day";	 
	public static final String VETERANS_DAY = "Veterans Day";	 
	public static final String THANKSGIVING_DAY = "Thanksgiving Day";
	public static final String CHRISTMAS_DAY = "Christmas Day";
	
	private String name;
	private boolean isActive;
	
	public StandardHoliday(String n, boolean a){
		name = n;
		isActive = a;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isActive(){
		return isActive;
	}	
	
}
