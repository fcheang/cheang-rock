package com.suntek.scheduler.appsvcs;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.suntek.scheduler.appsvcs.persistence.Appointment;
import com.suntek.scheduler.appsvcs.persistence.Evaluation;
import com.suntek.scheduler.appsvcs.persistence.Patient;
import com.suntek.scheduler.appsvcs.persistence.ProviderNode;

public interface ReadSvcI {

	public abstract boolean login(String user, String pass);

	public abstract List<String> getRoles(String user);

	public abstract List getAllProviderName();
	
	public abstract List<ProviderNode> getAllProviderNode();
	
	public abstract List<ProviderNode> getInactiveProvider();

	public abstract List getAllReasonCode();
	
	public abstract List getCancelByPatientReasonCode();
	
	public abstract List getCancelByClinicReasonCode();

	public abstract boolean isRemoteUser(String user);

	public abstract boolean isTrustedIP(String host);


	/////////////
	// Patient //
	/////////////
	
	public abstract List<Patient> getAllPatientNode();	
	
	public abstract List<Patient> getDischargedPatient();

	public abstract Patient getPatientById(int refId);

	public abstract List getInsForRef(int refId);
	
    public boolean checkCredential(String provider, int refId);	

	public abstract boolean isApptCompleted(long apptId);

	public abstract int getPatientId(String lastName, String firstName);
	
	public abstract Date getPatientBirthDate(int refId);

	/**
	 * @return List of String of Clinic names
	 */
	public abstract List<String> getAllClinicName();

	/**
	 * @return List of String of Insurance Company names
	 */
	public abstract List getAllInsuranceCompanyName();

	/**
	 * @return List of Appointment objects
	 */
	public abstract List getApptForDay(String provider, Date aDate);

	/**
	 * @return List of Appointment objects
	 */
	public abstract List getApptForClinicAndDate(String clinic, Date aDate);

	/**
	 * @return List of Appointment objects
	 */
	public abstract List getApptForPatient(int refId);

	/**
	 * @return Appointment objects
	 */
	public abstract Appointment getApptForApptId(long apptId);

	/**
	 * get List of overlapping Appointment objects with startTime and endTime
	 */
	public abstract List getAllOverlapAppt(String provider, Date startTime,
			Date endTime);

	public abstract void acquireSeqLock(Connection con, String lockName);

	public abstract void releaseSeqLock(Connection con, String lockName);

	public abstract long getNextSeq(String tableName);

	/**
	 * @return List of Patient objects
	 */
	public abstract BigDecimal getBalForPatient(int refId);

	public abstract boolean getNtsForPatient(String lastName, String firstName);

	/** 
	 * @param date given date
	 * @return null if the given date is not a holiday
	 */
	public abstract String getHolidayDesc(Date date);

	public abstract List getHolidays();

	public abstract List getStdHolidays();

	public abstract boolean isActiveHoliday(String holiday);
	
	public abstract List<Appointment> getEvaluationApptForPatient(int refId, String evalYear);
	
	public abstract List<Integer> getRefIdWithEvalAppt(String evalYear);

	public abstract List<Patient> getEvalPatientByIns(String ins);
	
	public abstract Patient getEvalPatientByInsAndId(int refId, String ins);
	
	public abstract List<Integer> getEvaluatedRefId(int year);
	
	public abstract List<Evaluation> getAllEvaluation(int refId);
	
	public abstract List<Appointment> getAllEvaluationAppt(int refId);
	
	public abstract int getNumInEligibleAppt(int refId);

	public abstract List<Appointment> getCancelApptForPatient(int refId);

	public abstract List<Patient> getPatientByIns(String ins);
		
}