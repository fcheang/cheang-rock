package com.suntek.scheduler.appsvcs;

import com.suntek.scheduler.appsvcs.persistence.Constant;

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
public class WriteQueries {

    public static final String insertAppt =
        "insert into appointment (apptId, referralId, clinicName, appointmentDate, provider, "+
        "translationSvcNeeded, language, collateralReceived, isEligible, notes, userId, "+
        "endTime, type, modifyBy, modificationDate, isEvaluation, "+
        "evaluationYear, isWalkIn, authNum, countyNum) "+
        "values (?, ?, ?, ?, ?, "+
        "?, ?, ?, ?, ?, ?, "+
        "?, ?, ?, ?, ?, "+
        "?, ?, ?, ?)";

    public static final String insertDeletedAppt =
        "insert into deletedAppointment (apptId, referralId, clinicName, appointmentDate, provider, "+
        "translationSvcNeeded, collateralReceived, isEligible, notes, userId, endTime, "+
        "type, deletedBy, deleteDate, isEvaluation, evaluationYear, isWalkIn, authNum, countyNum) "+
        "values (?, ?, ?, ?, ?, "+
        "?, ?, ?, ?, ?, ?, "+
        "?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String insertCanceledAppt =
        "insert into canceledAppointment (apptId, referralId, clinicName, appointmentDate, provider, "+
        "translationSvcNeeded, collateralReceived, isEligible, notes, userId, endTime, "+
        "type, cancelBy, cancelDate, isEvaluation, evaluationYear, isWalkIn, authNum, countyNum, isCancelByPatient, isCancelByClinic, isWnTwentyFourHrs, "+
        "cancelReason, cancelOtherReason) "+
        "values (?, ?, ?, ?, ?, "+
        "?, ?, ?, ?, ?, ?, "+
        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String insertApptStatus =
        "insert into referralStatus (referralId, apptId, status, createDate, removeDate, isActive, notes, reasonCode) "+
        "values (?, ?, ?, now(), '2200-01-01', ?, ?, ?)";

    public static final String endDatePriorNotScheduledOrWaitlistStatus =
        "update referralStatus set removeDate = now() where removeDate = '2200-01-01' and referralId = ? "+
        "and ( status = '"+Constant.NotScheduled+"' or status = '"+Constant.Waitlist+"' )";

    public static final String endDatePriorScheduledStatus =
        "update referralStatus set removeDate = now() where removeDate = '2200-01-01' and apptId = ? "+
        "and ( status = '"+Constant.Scheduled+"' )";

    public static final String deleteAppt =
        "delete from appointment where apptId = ?";

    public static final String deleteApptStatus =
        "delete from referralStatus where apptId = ?";

    public static final String deleteLastStatus =
        "delete from referralStatus where apptId = ? and removeDate = '2200-01-01'";

    public static final String setLastStatusToEOT =
        "update referralStatus set removeDate = '2200-01-01' where apptId = ?";

    public static final String updateApptStartTime =
        "update appointment set appointmentDate = ? where apptId = ?";

    public static final String updateApptEndTime =
        "update appointment set endTime = ? where apptId = ?";

    public static final String updateApptClinic =
        "update appointment set clinicName = ? where apptId = ?";

    public static final String updateApptNotes =
        "update appointment set notes = ? where apptId = ?";

    public static final String updateApptNts =
        "update appointment set translationSvcNeeded = ? where apptId = ?";

    public static final String updateApptLang =
        "update appointment set language = ? where apptId = ?";
    
    public static final String updateApptCollRcv =
        "update appointment set collateralReceived = ? where apptId = ?";

    public static final String updateApptIsEligible =
        "update appointment set isEligible = ? where apptId = ?";

    public static final String updateApptIsWalkIn =
        "update appointment set isWalkIn = ? where apptId = ?";

    public static final String updateApptAuthNum =
        "update appointment set authNum = ? where apptId = ?";

    public static final String updateApptCountyNum =
        "update appointment set countyNum = ? where apptId = ?";

    public static final String clearReminder =
        "update referral set reminder = '' where referralId = ?";

    public static final String updateLastApptStatusToEOT =
        "update referralStatus set removeDate = '2200-01-01' where referralId = ? and removeDate = ?";
    
    public static final String createInsurance =
        "insert into insurance ( referralId, referralDate, eligEffDate, eligTermDate, insuranceCompany, phoneNumber, "+
        "memberId, copay, copayParity, numAuthVisitForMD, authNumForMD, numAuthVisitForMA, "+
        "authNumForMA, medicalId, medicalIssueDate, notes, isLast ) values "+
        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    public static final String deleteInsurance =
    	"delete from insurance where insuranceId = ?";
    
    public static final String resetIsLastForIns =
    	"update insurance set isLast = 0 where referralId = ?";
    
    public static final String updateInsIsLast = 
    	"update insurance set isLast = 1 where insuranceId = ?";

    public static final String createHoliday =
        "insert into holidayMap ( startDate, endDate, description ) values "+
        "(?, ?, ?)";
    
    public static final String deleteHoliday =
    	"delete from holidayMap where holidayMapId = ?";

    public static final String updateStdHolidayStatus =
    	"update holiday set isActive = ? where name = ?";

    public static final String logException =
    	"insert into exceptionLog (createDate, exception) values (now(), ?)";

    public static final String updateProviderInactiveBit =
    	"update provider set inactive = ?, inactiveDate = ? where providerId = ?";
    
    public static final String updatePatientDischargedBit =
    	"update referral set discharged = ?, dischargeDate = ? where referralId = ?";
    
    public static final String checkoffEvaluation =
    	"insert into evaluation (referralId, year, notes, checkoffDate, checkoffBy) values (?, ?, ?, ?, ?)";
    
    public static final String updateCancelByPatient =
    	"update canceledAppointment set isCancelByPatient = ?, isCancelByClinic = ? where apptId = ?";

    public static final String updateIsWnTwentyFourHrs =
    	"update canceledAppointment set isWnTwentyFourHrs = ? where apptId = ?";
    
}
