package com.suntek.scheduler.appsvcs.persistence;

import java.math.BigDecimal;
import java.util.Date;

import com.suntek.scheduler.ui.DateStringConverter;
import com.suntek.scheduler.util.Util;

public class Insurance {
	// i.insuranceId, i.insuranceCompany, 
	// i.memberId, i.copay, i.copayParity, i.phoneNumber, i.authorizationNumber,
	// i.numAuthorizedVisit, i.numAuthorizedVisitParity  
	
	private int insId;
	private int refId; 
	private Date refDate;
	private Date esd;
	private Date eed;
    private String insuranceCompany;
    private String memberId;
    private BigDecimal copay;
    private BigDecimal copayParity;
    private String insPhoneNumber;
    private String anForMD;
    private String anForMA;
    private int numAuthVisitForMD;
    private int numAuthVisitForMA;
    private String medicalId;
    private Date medIssueDate;
    private String notes;
        
    public void setInsId(int i){
    	insId = i;
    }
    
    public int getInsId(){
    	return insId;
    }
    
    public void setRefId(int i){
    	refId = i;
    }
    
    public int getRefId(){
    	return refId;
    }

    public void setRefDate(Date d){
    	refDate = d;
    }
    
    public Date getRefDate(){
    	return refDate;
    }
    
    public String getRefDateStr(){
    	if (refDate != null){
    		int m = Util.getMonth(refDate);
    		int d = Util.getDay(refDate);
    		int y = Util.getYear(refDate);
    		return m+"/"+d+"/"+y;
    	}else{
    		return "";
    	}
    }    
    
    public void setEffStartDate(Date d){
    	esd = d;
    }
    
    public Date getEffStartDate(){
    	return esd;
    }
    
    public String getEffStartDateStr(){
    	if (esd != null){
    		int m = Util.getMonth(esd);
    		int d = Util.getDay(esd);
    		int y = Util.getYear(esd);
    		return m+"/"+d+"/"+y;
    	}else{
    		return "";
    	}
    }    
    
    public void setEffEndDate(Date d){
    	eed = d;
    }
    
    public Date getEffEndDate(){
    	return eed;
    }
    
    public String getEffEndDateStr(){
    	if (eed != null){
    		int m = Util.getMonth(eed);
    		int d = Util.getDay(eed);
    		int y = Util.getYear(eed);
    		return m+"/"+d+"/"+y;
    	}else{
    		return "";
    	}
    }
    
    
    public void setInsCompany(String c){
    	insuranceCompany = c;
    }
    
    public String getInsCompany(){
    	if (insuranceCompany != null){
    		return insuranceCompany;
    	}else{
    		return "";
    	}
    }

    public void setMemberId(String m){
    	memberId = m;
    }
    
    public String getMemberId(){
    	if (memberId != null){
    		return memberId;
    	}else{
    		return "";
    	}
    }
    
    public void setCopay(BigDecimal bd){
    	if (bd != null){
    		copay = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    	}
    }
    
    public void setCopay(String cp){
    	if (cp != null && !cp.equals("")){
    		copay = new BigDecimal(cp).setScale(2, BigDecimal.ROUND_HALF_UP);
    	}
    }

    public BigDecimal getCopay(){
    	return copay;
    }
    
    public String getCopayStr(){
        if (copay != null){
            return copay.toString();
        }else{
            return "";
        }
    }
    
    public void setCopayParity(BigDecimal bd){
    	if (bd != null){
    		copayParity = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    	}
    }

    public void setCopayParity(String cp){
    	if (cp != null && !cp.equals("")){
    		copayParity = new BigDecimal(cp).setScale(2, BigDecimal.ROUND_HALF_UP);
    	}
    }
    
    public BigDecimal getCopayParity(){
    	return copayParity;
    }
    
    public String getCopayParityStr(){
        if (copayParity != null){
            return copayParity.toString();
        }else{
            return "";
        }
    }

    public void setInsPhoneNum(String pn){    	
    	insPhoneNumber = Constant.clearPhoneNumFormatting(pn);
    }
        
    public String getInsPhoneNum(){
    	return insPhoneNumber;
    }
    
    public String getInsPhoneNumFormated(){
        if (insPhoneNumber != null){
            return Constant.formatPhoneNum(insPhoneNumber);
        }else{
            return "";
        }
    }       

    public void setAuthNumForMD(String an){
    	this.anForMD = an;
    }
    
    public String getAuthNumForMD(){
    	if (anForMD != null){
    		return anForMD;
    	}else{
    		return "";
    	}
    }
    
    public void setAuthNumForMA(String anParity){
    	this.anForMA = anParity;
    }
    
    public String getAuthNumForMA(){
    	if (anForMA != null){
    		return anForMA;
    	}else{
    		return "";
    	}
    }    
    
    public void setNumAuthVisitForMD(int nav){
    	numAuthVisitForMD = nav;
    }
    
    public int getNumAuthVisitForMD(){
    	return numAuthVisitForMD;
    }
    
    public String getNumAuthVisitForMDStr(){
        return Integer.toString(numAuthVisitForMD);
    }

    public void setNumAuthVisitForMA(int nav){
    	numAuthVisitForMA = nav;
    }
    
    public int getNumAuthVisitForMA(){
    	return numAuthVisitForMA;
    }
    
    public String getNumAuthVisitForMAStr(){
    	return Integer.toString(numAuthVisitForMA);
    }
    
    public void setMedicalId(String m){
    	medicalId = m;
    }
    
    public String getMedicalId(){
    	if (medicalId != null){
    		return medicalId;
    	}else{
    		return "";
    	}
    }
    
    public void setMedIssueDate(Date d){
    	medIssueDate = d;
    }
    
    public void setMedIssueDateStr(String dateStr){
    	medIssueDate = DateStringConverter.getDateFromDateStr(dateStr);
    }
    
    public void setRefDateStr(String dateStr){
    	refDate = DateStringConverter.getDateFromDateStr(dateStr);
    }
    
    public void setEffStartDateStr(String dateStr){
    	esd = DateStringConverter.getDateFromDateStr(dateStr);
    }

    public void setEffEndDateStr(String dateStr){
    	eed = DateStringConverter.getDateFromDateStr(dateStr);
    }
    
    public Date getMedIssueDate(){
    	return medIssueDate;
    }
    
    public String getMedIssueDateStr(){
    	if (medIssueDate != null){
    		int m = Util.getMonth(medIssueDate);
    		int d = Util.getDay(medIssueDate);
    		int y = Util.getYear(medIssueDate);
    		return m+"/"+d+"/"+y;
    	}else{
    		return "";
    	}
    }
    
    public String getMedIssueDateForSQL(){
    	if (medIssueDate != null){
    		int m = Util.getMonth(medIssueDate);
    		int d = Util.getDay(medIssueDate);
    		int y = Util.getYear(medIssueDate);
    		return y+"-"+m+"-"+d;
    	}else{
    		return "";
    	}
    }

    public String getRefDateForSQL(){
    	if (refDate != null){
    		int m = Util.getMonth(refDate);
    		int d = Util.getDay(refDate);
    		int y = Util.getYear(refDate);
    		return y+"-"+m+"-"+d;
    	}else{
    		return "";
    	}
    }    
    
    public String getEffStartDateForSQL(){
    	if (esd != null){
    		int m = Util.getMonth(esd);
    		int d = Util.getDay(esd);
    		int y = Util.getYear(esd);
    		return y+"-"+m+"-"+d;
    	}else{
    		return "";
    	}
    }    
    
    public String getEffEndDateForSQL(){
    	if (eed != null){
    		int m = Util.getMonth(eed);
    		int d = Util.getDay(eed);
    		int y = Util.getYear(eed);
    		return y+"-"+m+"-"+d;
    	}else{
    		return "";
    	}
    }    
    
    
    
    public void setNotes(String n){
    	notes = n;
    }
    
    public String getNotes(){
    	if (notes != null){
    		return notes;
    	}else{
    		return "";
    	}
    }
            
}

