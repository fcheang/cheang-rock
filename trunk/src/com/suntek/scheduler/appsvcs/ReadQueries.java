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
public class ReadQueries {

    public static String isApptCompleted = "select 1 from referralStatus where apptId = ? and "+
        "( status = '"+Constant.Seen+"' or status = '"+Constant.NotSeen+"' )";

    public static String getPatientIdByName = "select referralId from referral where lastName = ? and firstName = ?";
    
    public static String getPatientBirthDate = "select birthDate from referral where referralId = ?";    

    public static String login = "select 1 from user where userId = ? and password = ?";

    public static String getRoles = "select roleName from userRole where userId = ?";

    public static String getAllProviderName = "select name from provider where inactive = 0 order by name";
    
    public static String getAllProviderNode = "select providerId, name from provider where inactive = 0 order by name";
    
    public static String getInactiveProviderNode = "select providerId, name from provider where inactive = 1 order by name";    

    public static String getAllReasonCode = "select reasonCode from reasonCodeType";
    
    public static String getCancelByPatientReasonCode = "select name from cancelByPatientReasonCode order by id";
    
    public static String getCancelByClinicReasonCode = "select name from cancelByClinicReasonCode order by id";

    public static String isRemoteUser = "select 1 from remoteUser where userId = ?";

    public static String isTrustedHost = "select 1 from trustedIP where host = ? or host = ?";

    public static String getAllClinicName = "select name from clinic";

    public static String getAllInsuranceCompanyName = "select name from insuranceProvider";
    
    public static String getAllPatientName = "select referralId, lastName, middleInitial, firstName from referral where discharged = 0 order by lastName, firstName";
    
    public static String getDischargedPatientName = "select referralId, lastName, middleInitial, firstName from referral where discharged = 1 order by lastName, firstName";    

    public static String getPatientById =
        "select "+
        "r.referralId, r.firstName, r.middleInitial, r.lastName, r.gender, "+
        "r.ssn, r.birthDate, r.streetAddress, r.apartmentNumber, r.city, "+
        "r.state, r.zipCode, r.phoneNumber, r.email, r.legalGardianFirstName, "+
        "r.legalGardianLastName, r.legalGardianMiddleInitial, r.legalGardianPhoneNumber, r.previousPsychiatrist, r.lastSeen, "+
        "r.currentMedications, r.daysLeft, r.previousMedications, r.previousDx, r.presentingProblem, "+
        "r.needMedicalMgntSvc, r.needTherapy, r.isUrgent, r.clinic, r.comments, "+
        "r.isChild, r.reminder, r.balance, r.balanceNotes "+
        "from referral r "+
        "where r.referralId = ?";
    
    public static String getReferralDate =
        "select createDate from referralStatus where status = 'Not Scheduled' and referralId = ?";
        
    public static String getInsByRefId =
    	"select i.insuranceId, i.referralDate, i.eligEffDate, i.eligTermDate, i.insuranceCompany, "+
    	"i.phoneNumber, i.memberId, i.copay, i.copayParity, i.numAuthVisitForMD, "+
    	"i.authNumForMD, i.numAuthVisitForMA, i.authNumForMA, "+
    	"i.medicalId, i.medicalIssueDate, i.notes "+
    	"from insurance i "+
    	"where i.referralId = ? "+
    	"order by i.insuranceId desc";

    public static String getAllOverlapAppt =
        "select a.apptId, a.referralId, r.lastName, r.firstName, a.provider, "+
        "a.appointmentDate, a.endTime, a.notes, a.type, a.clinicName, "+
        "a.translationSvcNeeded, a.language, a.collateralReceived, a.isEligible, r.phoneNumber, r.birthDate, a.userId, "+
        "a.isEvaluation, a.evaluationYear, a.isWalkIn, a.authNum, a.countyNum "+
        "from appointment a left join referral r on (a.referralId = r.referralId)"+
        "where "+
        "a.provider = ? and "+
        "a.appointmentDate < ? and a.endTime > ? "+
        "order by a.appointmentDate";

    public static String getApptForDay =
        "select a.apptId, a.referralId, r.lastName, r.firstName, a.provider, "+
        "a.appointmentDate, a.endTime, a.notes, a.type, a.clinicName, "+
        "a.translationSvcNeeded, a.language, a.collateralReceived, a.isEligible, r.phoneNumber, r.birthDate, a.userId, "+
        "a.isEvaluation, a.evaluationYear, a.isWalkIn, a.authNum, a.countyNum "+
        "from appointment a left join referral r on (a.referralId = r.referralId)"+
        "where "+
        "a.provider = ? and "+
        "a.appointmentDate >= ? and a.appointmentDate < ? "+
        "order by appointmentDate";

    public static String getApptForClinicAndDate =
        "select a.apptId, a.referralId, r.lastName, r.firstName, a.provider, "+
        "a.appointmentDate, a.endTime, a.notes, a.type, a.clinicName, "+
        "a.translationSvcNeeded, a.language, a.collateralReceived, a.isEligible, r.phoneNumber, r.birthDate, a.userId, "+
        "a.isEvaluation, a.evaluationYear, a.isWalkIn, a.authNum, a.countyNum "+
        "from appointment a left join referral r on (a.referralId = r.referralId)"+
        "where "+
        "a.clinicName = ? and "+
        "a.appointmentDate >= ? and a.appointmentDate < ? "+
        "order by appointmentDate";

    public static String getApptForPatient =
        "select a.apptId, a.referralId, r.lastName, r.firstName, a.provider, "+
        "a.appointmentDate, a.endTime, a.notes, a.type, a.clinicName, "+
        "a.translationSvcNeeded, a.language, a.collateralReceived, a.isEligible, r.phoneNumber, r.birthDate, a.userId, "+
        "a.isEvaluation, a.evaluationYear, a.isWalkIn, a.authNum, a.countyNum "+
        "from appointment a, referral r "+
        "where a.referralId = r.referralId and "+
        "r.referralId = ? "+
        "order by appointmentDate desc";

    public static String getCancelApptForPatient =
        "select a.apptId, a.referralId, r.lastName, r.firstName, a.provider, "+
        "a.appointmentDate, a.endTime, a.notes, a.type, a.clinicName, "+
        "a.translationSvcNeeded, a.language, a.collateralReceived, a.isEligible, r.phoneNumber, r.birthDate, a.userId, "+
        "a.isEvaluation, a.evaluationYear, a.isWalkIn, a.authNum, a.countyNum, a.cancelBy, a.isCancelByPatient, a.isCancelByClinic, a.isWnTwentyFourHrs, "+
        "a.cancelReason, a.cancelOtherReason "+
        "from canceledAppointment a, referral r "+
        "where a.referralId = r.referralId and "+
        "r.referralId = ? "+
        "order by appointmentDate desc";
    
    public static String getApptForApptId =
        "select a.apptId, a.referralId, r.lastName, r.firstName, a.provider, "+
        "a.appointmentDate, a.endTime, a.notes, a.type, a.clinicName, "+
        "a.translationSvcNeeded, a.language, a.collateralReceived, a.isEligible, r.phoneNumber, r.birthDate, a.userId, "+
        "a.isEvaluation, a.evaluationYear, a.isWalkIn, a.authNum, a.countyNum "+
        "from appointment a, referral r "+
        "where a.referralId = r.referralId and "+
        "a.apptId = ? ";

    public static String getNextSeq =
        "select nextSeq from sequenceGenerator where tableName = ?";

    public static String updateNextSeq =
        "update sequenceGenerator set nextSeq = nextSeq + 1 where tableName = ?";

    public static String getApptStatus =
        "select rs.status, rs.reasonCode, i.memberId, rs.notes from referralStatus rs, insurance i "+
        "where rs.apptId = ? and rs.removeDate = '2200-01-01' and rs.referralId = i.referralId "+
        "and i.isLast = 1";

    public static String getLastRemoveDate =
        "select max(removeDate) from referralStatus where referralId = ?";
    
    public static String getBalForPatient = 
    	"select balance from referral where referralId = ?";
    
    public static String getMaxInsId =
    	"select insuranceId from insurance where referralId = ? order by insuranceId desc limit 1";
    
    public static String getNtsForPatient =
    	"select translationSvcNeeded from referral where lastName = ? and firstName = ?";
    
    public static String isHoliday = "select description from holidayMap where ? between startDate and endDate";
    
    public static String getHolidays = "select holidayMapId, startDate, endDate, description from holidayMap order by startDate desc";
    
    public static String getStdHolidays = "select name, isActive from holiday";
    
    public static String getIsActiveHoliday = "select isActive from holiday where name = ?";
    
    public static String checkCredential = 
    	"select count(*) from credential c, provider p, insuranceProvider ip, insurance i "+
    	"where c.providerId = p.providerId "+
    	"and p.name = ? "+
    	"and c.insuranceProviderId = ip.insuranceProviderId "+
    	"and ip.name = i.insuranceCompany "+
    	"and i.referralId = ?";

    public static String getEvalPatientByIns = 
    	"select r.referralId, i.referralDate, year(i.referralDate) admitYear, month(i.referralDate) admitMonth, "+
    	"dayofmonth(i.referralDate) admitDay, r.firstName, r.lastName, r.phoneNumber, r.birthDate "+
    	"from referral r, insurance i "+
    	"where r.referralId = i.referralId "+
    	"and i.insuranceCompany = ? "+
    	"and i.referralDate is not null "+
    	"and r.discharged = 0 "+
    	"order by admitMonth, admitDay, i.referralDate";    	

    public static String getEvalPatientByInsAndId = 
    	"select r.referralId, i.referralDate, year(i.referralDate) admitYear, month(i.referralDate) admitMonth, "+
    	"dayofmonth(i.referralDate) admitDay, r.firstName, r.lastName, r.phoneNumber, r.birthDate "+
    	"from referral r, insurance i "+
    	"where r.referralId = i.referralId "+
    	"and i.insuranceCompany = ? "+
    	"and i.referralDate is not null "+
    	"and r.discharged = 0 "+
    	"and r.referralId = ? "+
    	"order by admitMonth, admitDay, i.referralDate";    	
    
    public static String getEvaluatedRefId =
    	"select referralId from evaluation where year = ?";
    
    public static String getAllEvaluation =
    	"select referralId, year, notes, checkoffDate, checkoffBy from evaluation where referralId = ? order by year desc";
    
    public static String getEvaluationApptForPatient =
    	"SELECT a.provider, a.appointmentDate, rs.status FROM appointment a, referralStatus rs "+
    	"where a.apptId = rs.apptId and rs.removeDate = '2200-01-01' "+
    	"and a.referralId = ? and a.isEvaluation = true and a.evaluationYear = ? "+
    	"and (status = 'Scheduled' or status = 'Seen')";
    
    public static String getAllEvaluationAppt =
    	"SELECT a.provider, a.appointmentDate, rs.status, a.evaluationYear FROM appointment a, referralStatus rs "+
    	"where a.apptId = rs.apptId and rs.removeDate = '2200-01-01' "+
    	"and a.referralId = ? and a.isEvaluation = true order by a.evaluationYear";    	
    
    public static String getRefIdWithEvalAppt =
    	"SELECT distinct a.referralId "+
    	"FROM appointment a, referralStatus rs "+
    	"where a.apptId = rs.apptId and rs.removeDate = '2200-01-01' "+
    	"and a.isEvaluation = true and a.evaluationYear = ? "+
    	"and (rs.status = 'Scheduled' or rs.status = 'Seen')";
    
    public static String getNumApptByEligibility =
    	"select count(1) from appointment where referralId = ? and isEligible = ?";
}
