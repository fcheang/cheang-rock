package com.suntek.scheduler.appsvcs.persistence;

import java.text.DateFormat;
import java.util.*;

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
public class Appointment {

    private DateFormat df_l = DateFormat.getDateInstance(DateFormat.LONG);
    private DateFormat tf_s = DateFormat.getTimeInstance(DateFormat.SHORT);
    private String blank = "&nbsp;";

    private long apptId;
    private String provider;
    private java.util.Date startDate;
    private java.util.Date endDate;
    private int referralId;
    private String lastName;
    private String firstName;
    private String type;
    private String notes;
    private String clinic;
    private boolean needTS;
    private String lang = "";
    private boolean collRcv;
    private boolean walkIn;
    private String authNum = "";
    private String countyNum = "";
	private String canceledBy;
	private boolean cancelByPatient;
    private boolean cancelByClinic;
    private boolean isWnTwentyFourHrs;
    private String isEligible = "";
    private String status;
    private String reasonCode;
    private String phoneNum;
	private String cancelReason;
    private String cancelOtherReason;
    private Date birthDate;

    
    public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getCancelOtherReason() {
		return cancelOtherReason;
	}
	public void setCancelOtherReason(String cancelOtherReason) {
		this.cancelOtherReason = cancelOtherReason;
	}
    
    public String getCanceledBy() {
		return canceledBy;
	}
	public void setCanceledBy(String canceledBy) {
		this.canceledBy = canceledBy;
	}
    
    public boolean isWalkIn() {
		return walkIn;
	}
    public String isWalkInStr() {
		if (walkIn){
			return "yes";
		}else{
			return "no";
		}
	}

	public void setWalkIn(boolean walkIn) {
		this.walkIn = walkIn;
	}

	public boolean isCancelByPatient() {
		return cancelByPatient;
	}
	public String isCancelByPatientStr() {
		if (cancelByPatient){
			return "yes";
		}else{
			return "no";
		}
	}

	public void setCancelByPatient(boolean cancelByPatient) {
		this.cancelByPatient = cancelByPatient;
	}

	public boolean isCancelByClinic() {
		return cancelByClinic;
	}
	public String isCancelByClinicStr() {
		if (cancelByClinic){
			return "yes";
		}else{
			return "no";
		}
	}

	public void setCancelByClinic(boolean cancelByClinic) {
		this.cancelByClinic = cancelByClinic;
	}

	public boolean isWnTwentyFourHrs() {
		return isWnTwentyFourHrs;
	}
	public String isWnTwentyFourHrsStr() {
		if (isWnTwentyFourHrs){
			return "yes";
		}else{
			return "no";
		}
	}

	public void setWnTwentyFourHrs(boolean isWnTwentyFourHrs) {
		this.isWnTwentyFourHrs = isWnTwentyFourHrs;
	}

	private boolean isChild;
    private String memberId;
    private String statusNotes;
    private String owner;
    private boolean isEval;
    private String evalYear;

    public String getEvalYear() {
		return evalYear;
	}

	public void setEvalYear(String evalYear) {
		this.evalYear = evalYear;
	}

	public boolean isEval() {
		return isEval;
	}

	public void setEval(boolean isEval) {
		this.isEval = isEval;
	}

	public Appointment(){
    }

    public void setOwner(String o){
        owner = o;
    }

    public String getOwner(){
        return owner;
    }

    public boolean isOwner(String o){
        if (owner == null){
            return true;
        }

        if (owner.equalsIgnoreCase("Unknown")){
            return true;
        }

        if (o != null && o.equals(owner)){
            return true;
        }else{
            return false;
        }
    }

    public Date getBirthDate(){
    	return birthDate;
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

        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        dob.set(Calendar.HOUR, 0);
        dob.set(Calendar.MINUTE, 0);
        dob.set(Calendar.SECOND, 0);
        dob.set(Calendar.MILLISECOND, 0);

        if (Constant.compareTo(dob, today) <= 0){
            // dob + 18 years is before or equal to today
            isChild = false;
        }else{
            isChild = true;
        }
    }

    public void setMemberId(String m){
        memberId = m;
    }

    /*
    public String toHTML(){
        StringBuffer sb = new StringBuffer();
        sb.append(Constant.regFont);
        sb.append("<b>Status:</b> ").append(getStatus()).append("<br>");
        sb.append("<b>Reason Code:</b> ").append(getReasonCode()).append("<br>");
        sb.append("<b>Provider:</b> ").append(getProvider()).append("<br>");
        sb.append("<b>Date:</b> ").append(getApptDateStr()).append("<br>");
        sb.append("<b>Start time:</b> ").append(getStartTimeStr()).append("<br>");
        sb.append("<b>End time:</b> ").append(getEndTimeStr()).append("<br>");
        sb.append("<b>Session:</b> ").append(getType()).append("<br>");
        sb.append("<b>Clinic:</b> ").append(getClinic()).append("<br>");
        sb.append("<b>Notes:</b> ").append(getNotes()).append("<br>");
        sb.append("</font>");
        return sb.toString();
    }
    */

    public String getPhoneNumHTML(){
        if (phoneNum == null){
            return blank;
        }else{
            return Constant.formatPhoneNum(phoneNum);
        }
    }

    public void setPhoneNum(String pn){
        phoneNum = pn;
    }

    public String getApptDateStr(){
        return Constant.df_l.format(startDate);
    }

    public String getStartTimeStr(){
        return Constant.tf_s.format(startDate);
    }

    public String getEndTimeStr(){
        return Constant.tf_s.format(endDate);
    }

    public String getLastAction(){
        if (status != null){
            if (status.equals(Constant.NotSeen)){
                return "No Shows";
            }else if (status.equals(Constant.Seen)){
                return "Showup";
            }else{
                return "";
            }
        }else{
            return "";
        }
    }

    public boolean isBlockTime(){
    	return type.equals(Constant.Blocked);
    }

    public String getStatus(){
        if (status != null){
            return status;
        }else{
            return "";
        }
    }
    public String getReasonCode(){
        if (reasonCode != null){
            return reasonCode;
        }else{
            return "&nbsp;";
        }
    }
    public void setStatus(String s){
        status = s;
    }
    public void setReasonCode(String r){
        reasonCode = r;
    }
    public long getApptId(){
        return apptId;
    }
    public void setApptId(long a){
        apptId = a;
    }
    public String needTranSvcStr(){
        if (needTS){
            return "yes";
        }else{
            return "no";
        }
    }
    public boolean needTranSvc(){
        return needTS;
    }
    public void setNeedTranSvc(boolean nts){
        needTS = nts;
    }
    public String collateralReceivedStr(){
        if (collRcv){
            return "yes";
        }else{
            return "no";
        }
    }
    public String getLang(){
    	return lang;
    }    
    public void setLang(String l){
    	this.lang = l;
    }
    public boolean collateralReceived(){
        return collRcv;
    }
    public void setCollateralReceived(boolean cr){
        collRcv = cr;
    }
    public String isEligible(){
    	return isEligible;
    }
    public void setIsEligible(String isElig){
    	if (isElig != null){
    		isEligible = isElig;
    	}else{
    		isEligible = "";
    	}
    }
    public String getClinic(){
        return clinic;
    }
    public void setClinic(String c){
        clinic = c;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setReferralId(int referralId) {
        this.referralId = referralId;
    }
    public int getReferralId() {
        return referralId;
    }
    public void setLastName(String lastName) {
        if (lastName != null){
            this.lastName = lastName;
        }else{
            this.lastName = "";
        }
    }
    public String getLastName() {
        return lastName;
    }
    public void setFirstName(String firstName) {
        if (firstName != null){
            this.firstName = firstName;
        }else{
            this.firstName = "";
        }
    }
    public String getFirstName() {
        return firstName;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public void setStatusNotes(String n){
        statusNotes = n;
    }
    public String getStatusNotes(){
        if (statusNotes != null){
            return statusNotes;
        }else{
            return "";
        }
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getNotes() {
        if (notes != null){
            return notes;
        }else{
            return "";
        }
    }
    public String getNotesHTML(){
        if (notes != null){
            return notes;
        }else{
            return blank;
        }
    }

    public String getFirstSlotDisplayText(){
        StringBuffer sb = new StringBuffer("");
        if (type.equals(Constant.Blocked)){
            sb.append("BLOCKED");
            if (notes != null && !notes.equals("")){
                sb.append(" (").append(notes).append(")");
            }
        }else{
            if (lastName != null && firstName != null) {
                sb.append(lastName).append(", ").append(firstName);
            }
            if (notes != null && !notes.equals("")) {
                sb.append(" (").append(notes).append(")");
            }
        }
        return sb.toString();
    }

    public String getSecondSlotDisplayText(){
        StringBuffer sb = new StringBuffer("");
        if (!type.equals(Constant.Blocked)){
            if (phoneNum != null){
                sb.append(Constant.formatPhoneNum(phoneNum)).append(" ");
            }
            if (isChild){
                sb.append("Child").append(" ");
            }else{
                sb.append("Adult").append(" ");
            }
            if (memberId != null){
                sb.append(memberId);
            }
            return sb.toString();
        }else{
            return "/";
        }
    }

    public String getDetail(){
        StringBuffer sb = new StringBuffer();
        sb.append("Patient: ").append(lastName).append(", ").append(firstName).append("\n");
        sb.append("Provider: ").append(provider).append("\n");
        sb.append("Appointment Date: ").append(df_l.format(startDate)).append("\n");
        sb.append("Appointment Time: ").append(tf_s.format(startDate)).append("\n");
        sb.append("Notes: ").append(notes).append("\n");
        return sb.toString();
    }

    public String getFullName(){
        return lastName + ", "+firstName;
    }
    
    public String getAuthNum() {
		return authNum;
	}
	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}
	public String getCountyNum() {
		return countyNum;
	}
	public void setCountyNum(String countyNum) {
		this.countyNum = countyNum;
	}    

    public String toString(){
        return apptId+", "+referralId+", "+provider+", "+type;
    }

    public boolean equals(Object obj){
        if (obj instanceof Appointment){
            if (obj == this){
                return true;
            }else{
                Appointment appt = (Appointment)obj;
                if (appt.provider.equals(this.provider) &&
                    appt.referralId == this.referralId &&
                    appt.startDate.equals(this.startDate) &&
                    appt.endDate.equals(this.endDate) &&
                    appt.type.equals(this.type)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }else{
            return false;
        }
    }
    public boolean noChange(Appointment newAppt){
        if (this.equals(newAppt)){
            if (this.collRcv == newAppt.collRcv &&
                this.needTS == newAppt.needTS &&
                this.clinic.equals(newAppt.clinic) &&
                this.notes.equals(newAppt.notes) &&
                this.lang.equals(newAppt.lang) &&
                this.isEligible.equals(newAppt.isEligible) &&
                this.walkIn == newAppt.walkIn &&
                this.authNum.equals(newAppt.authNum) &&
                this.countyNum.equals(newAppt.countyNum)){
                return true;
            }
        }
        return false;
    }

    public boolean shouldCheckReminder(){
    	if (this.type.equals(Constant.Blocked)){
    		return false;
    	}    	
        if (status == null){
            return false;
        }else{
            if (status.equals(Constant.Seen) || status.equals(Constant.NotSeen)){
                return false;
            }else{
                Calendar sd = new GregorianCalendar();
                sd.setTime(startDate);
                Calendar now = new GregorianCalendar();
                if (sd.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    sd.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                    sd.get(Calendar.DATE) == now.get(Calendar.DATE)){
                    // scheduled
                    return true;
                }else{
                    return false;
                }
            }
        }
    }

    public boolean allowEdit(){
        if (status == null){
            // blocked
            return true;
        }else{
            if (status.equals(Constant.Seen) || status.equals(Constant.NotSeen)){
                return false;
            }else{
                // Scheduled, Not Scheduled
                return true;
            }
        }
    }
    public boolean allowUndo(){
        if (status != null &&
            (status.equals(Constant.Seen) || status.equals(Constant.NotSeen))) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public String toStringForEvaluation(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("Evaluation appointment for ").append(evalYear);
    	sb.append(" was ").append(status);
    	sb.append(" on ").append(getApptDateStr()).append(" ").append(getStartTimeStr());
    	sb.append(" with ").append(provider);
    	return sb.toString();
    }

    public void copyValue(Appointment appt){    	
    	this.owner = appt.owner;
    	this.birthDate = appt.birthDate; 
    	this.isChild = appt.isChild;
    	this.phoneNum = appt.phoneNum;
    	this.memberId = appt.memberId;
    }
}
