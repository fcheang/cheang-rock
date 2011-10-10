package com.suntek.scheduler.appsvcs.persistence;

import java.util.*;
import java.math.BigDecimal;

/**
 * <p>Title: Appointment Scheduler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Sunteksystems</p>
 *
 * @author Steve Cheang
 * @version 1.0
 */
public class Patient implements Comparable {

	public final static String OVERDUE = "Overdue";
	public final static String DUE_IN_60_DAYS = "Due in 60 days";
	
    public int refId;
    public String lastName;
    public String firstName;
    public String mi;

    // additional patient info
    public String gender;
    public String ssn;
    private Date birthDate;

    private Date createDate;
    
    private Date admitDate;
    private int admitYear;
    private int admitMonth;
    private int admitDay;
    private String evalStatus;
    
	private boolean isChild;
    public String streetAddress;
    public String apartmentNumber;
    public String city;
    public String state;
    public String zipCode;
    public String phoneNumber;
    public String email;
    public String legalGardianFirstName;
    public String legalGardianLastName;
    public String legalGardianMiddleInitial;
    public String legalGardianPhoneNumber;
    public String previousPsychiatrist;
    public Date lastSeen;
    public String currentMedications;
    public int daysLeft;
    public String previousMedications;
    public String previousDx;
    public String presentingProblem;
    public boolean needMedicalMgntSvc;
    public boolean needTherapy;
    public String isUrgent;
    public String clinic;
    public String comments;

    // insurance info
    private List<Insurance> insList = new ArrayList<Insurance>();
    
    private String reminder;
    private BigDecimal balance;
    private String balanceNotes;

    public static long oneYear = 31536000L;

    public void setReminder(String r){
        reminder = r;
        if (reminder != null)
            reminder.trim();
    }
    
    public void setBalance(BigDecimal bd){
    	balance = bd;
    }
    
    public void setBalance(String b){
    	balance = new BigDecimal(b).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public BigDecimal getBalance(){
    	return balance;
    }
    
    public String getBalanceStr(){
    	if (balance != null){
    		return balance.toString();
    	}else{
    		return "0";
    	}
    }
    
    public void setBalanceNotes(String balNotes){
    	this.balanceNotes = balNotes;
    }
    
    public String getBalanceNotes(){
    	return balanceNotes;
    }

    public String getReminder(){
        if (reminder == null){
            return "";
        }else{
            return reminder;
        }
    }

    public boolean getIsChild(){
        return isChild;
    }

    public void setInsAdmitDate(Date d){
    	if (d == null){
    		return;
    	}
    	admitDate = d;
    }
    
    public void setBirthDate(Date d){
        if (d == null){
            return;
        }
        birthDate = d;
        Calendar today = new GregorianCalendar();
        Calendar dob = new GregorianCalendar();
        dob.setTime(d);
        dob.add(Calendar.YEAR, 18);
        if (Constant.compareTo(dob, today) <= 0){
            // dob + 18 years is before or equal to today
            isChild = false;
        }else{
            isChild = true;
        }
    }

    public String getNeedMedMgntSvc(){
        if (needMedicalMgntSvc){
            return "yes";
        }else{
            return "no";
        }
    }

    public String getNeedTherapy(){
        if (needTherapy){
            return "yes";
        }else{
            return "no";
        }
    }

    public String getDaysLeft(){
        return ""+daysLeft;
    }

    public String getLastSeen(){
        if (lastSeen != null){
            return Constant.df_l.format(lastSeen);
        }else{
            return "";
        }
    }

    public String getPhoneNum(){
        if (phoneNumber != null){
            return Constant.formatPhoneNum(phoneNumber);
        }else{
            return "";
        }
    }


    public String getFullName(){
        return lastName+", "+firstName;
    }

    public String toHTML(){
        StringBuffer sb = new StringBuffer();
        sb.append(Constant.regFont);
        sb.append("<b>Patient name:</b> ").append(this.getFullName()).append("<br>");
        sb.append("<b>Patient Id:</b> ").append(refId).append("<br>");
        sb.append("<b>Date of birh:</b> ").append(this.getDateOfBirth()).append("<br>");
        sb.append("<b>Age:</b> ").append(getAge()).append("<br>");
        sb.append("<b>Age Group:</b> ").append(getAgeGroup()).append("<br>");
        if (getAge() < 18){
            sb.append("<b>Legal Gardian:</b> ").append(getLgFullName()).append("<br>");
        }
        sb.append("<b>email:</b> ").append(getEmail()).append("<br>");
        sb.append("<b>Address:</b> ").append(this.getFullAddressInHTML()).append("<br>");
        sb.append("<b>Clinic:</b> ").append(this.clinic).append("<br>");
        sb.append("<b>Previous Psychiatrist:</b> ").append(this.getPreviousPsychiatrist()).append("<br>");
        sb.append("</font>");
        return sb.toString();
    }

    public String getEmail(){
        if (email != null){
            return email;
        }else{
            return "";
        }
    }

    public String getPreviousPsychiatrist(){
        if (this.previousPsychiatrist != null){
            return this.previousPsychiatrist;
        }else{
            return "";
        }
    }
    public String getLgFullName(){
        if (this.legalGardianLastName != null){
            return legalGardianLastName + ", " + this.legalGardianFirstName;
        }else{
            return "";
        }
    }

    public String getAgeGroup(){
        if (getAge() >= 18){
            return "adult";
        }else{
            return "child";
        }
    }

    public int getAge(){
        long secs = (System.currentTimeMillis() - birthDate.getTime()) / 1000;
        long year = secs / oneYear;
        return (int)year;
    }

    public String getDateOfBirth(){
        return Constant.df_l.format(birthDate);
    }
    
    public Date getInsAdmitDate(){
    	return admitDate;
    }
    
    public String getInsAdmitDateStr(){
    	return Constant.df_l.format(admitDate);
    }
    
    public Date getCreateDate(){
    	return createDate;
    }
    
    public void setCreateDate(Date d){
    	if (d == null){
    		return;
    	}
    	this.createDate = d;
    }
    
    public String getCreateDateStr(){
    	return Constant.df_l.format(createDate);
    }    

    public String getFullAddressInHTML(){
        StringBuffer sb = new StringBuffer();
        if (streetAddress != null){
            sb.append(streetAddress).append("<br>");
        }
        if (apartmentNumber != null){
            sb.append(apartmentNumber).append("<br>");
        }
        if (city != null) {
            sb.append(city);

            if (state != null) {
                sb.append(" ");
                sb.append(state);
                if (zipCode != null){
                    sb.append(" ");
                    sb.append(zipCode);
                }
            }
            sb.append("<br>");
        }
        return sb.toString();
    }

    public int getRefId(){
        return refId;
    }
    public void setRefId(int r){
        refId = r;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String l){
        lastName = l;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String f){
        firstName = f;
    }
    public String getMi(){
        return mi;
    }
    public void setMi(String m){
        mi = m;
    }

    ///////////////
    // Insurance //
    ///////////////
    
    public void addIns(Insurance ins){
    	insList.add(ins);
    }
    
    public List<Insurance> getIns(){
    	return insList;
    }
    
    public String getInsuranceCompany(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getInsCompany();
    	}else{
    		return "";
    	}
    }
    
    public String getInsPhoneNumber(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getInsPhoneNum();
    	}else{
    		return "";
    	}    	
    }

    public String getInsPhoneNumberFormated(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getInsPhoneNumFormated();
    	}else{
    		return "";
    	}    	
    }
    
    public String getMemberId(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getMemberId();
    	}else{
    		return "";
    	}    	    	
    }
    
    public String getCopayStr(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getCopayStr();
    	}else{
    		return "";
    	}    	    	    	
    }

    public BigDecimal getCopayBD(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getCopay();
    	}else{
    		return null;
    	}    	    	    	    	
    }
    
    public String getAuthorizationNumber(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getAuthNumForMD();
    	}else{
    		return "";
    	}    	    	    	    	
    }
    
    public String getNumAuthorizedVisit(int i){
    	if (insList.size() > 0){
    		return ((Insurance)insList.get(i)).getNumAuthVisitForMDStr();
    	}else{
    		return "";
    	}    	    	    	    	    	
    }

    public int getInsAdmitYear(){
    	return admitYear;
    }
    
    public void setInsAdmitYear(int year){
    	this.admitYear = year;
    }
    
    public int getInsAdmitMonth() {
		return admitMonth;
	}

	public void setInsAdmitMonth(int admitMonth) {
		this.admitMonth = admitMonth;
	}

	public int getInsAdmitDay() {
		return admitDay;
	}

	public void setInsAdmitDay(int admitDay) {
		this.admitDay = admitDay;
	}

	public String getEvalStatus() {
		return evalStatus;
	}

	public void setEvalStatus(String evalStatus) {
		this.evalStatus = evalStatus;
	}

    public String toString(){
        return lastName+", "+firstName;
    }
    
    public int compareTo(Object o){    	
    	if (o instanceof Patient){
    		Patient p = (Patient)o;
    		int compareVal = this.lastName.toLowerCase().compareTo(p.lastName.toLowerCase()); 
    		if (compareVal == 0){
    			return this.firstName.toLowerCase().compareTo(p.firstName.toLowerCase());
    		}else{
    			return compareVal;
    		}
    	}else{
    		return -1;
    	}
    }
}
