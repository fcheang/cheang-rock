package com.suntek.scheduler.appsvcs;

import java.util.Date;
import java.util.List;

import com.suntek.scheduler.appsvcs.persistence.Appointment;
import com.suntek.scheduler.appsvcs.persistence.Holiday;
import com.suntek.scheduler.appsvcs.persistence.Insurance;

public interface WriteSvcI {

	public abstract void updateAppt(Appointment oldAppt, Appointment newAppt);

	public abstract void deleteAppt(Appointment oldAppt);

	public abstract void cancelAppt(Appointment oldAppt);

	public abstract void insertAppt(Appointment appt);

	public abstract void showup(long apptId, int refId, String notes,
			String reason);

	public abstract void noShows(long apptId, int refId, String notes);

	public abstract void execDynamicSql(String sql, List args);

	public abstract void undoStatusChange(long apptId);

	public abstract void clearReminder(long apptId);

	public abstract void createInsurance(Insurance ins);

	public abstract void deleteInsurance(int insId, int refId);

	public abstract void createHoliday(Holiday h);

	public abstract void deleteHoliday(int id);

	public abstract void updateStdHolidayStatus(String name, boolean isActive);

	public abstract void updateProviderInactiveBit(int providerId, boolean isArchive, Date archiveDate);
	
	public abstract void updatePatientDischargedBit(int refId, boolean isArchive, Date archiveDate);
	
	public abstract void checkoffEvaluation(int refId, int evalYear, String notes);
	
	public abstract void updateCanceledApptCancelBy(long apptId, boolean isCancelByPatient);
	
	public abstract void updateCanceledApptIsWnTwentyFourHrs(long apptId, boolean isWnTwentyFourHrs);
}