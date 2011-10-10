package com.suntek.scheduler.test;

import com.suntek.scheduler.ui.PSService;
import com.suntek.scheduler.appsvcs.*;
import com.suntek.scheduler.appsvcs.persistence.*;
import java.sql.*;
import java.util.*;
import java.util.Date;


/**
 * <p>Title: Appointment Scheduler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Sunteksystems</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class UnitTest {

    public static void main(String[] args){
        PSService.getService().connect();
        testConcurrentInsertAppt();
    }

    public static void testConcurrentInsertAppt(){
        System.setProperty("debugOn", "false");
        Date now = new java.util.Date();

        WriteSvcI ws = WriteSvc.getInstance();
        while (true){
            Appointment appt = new Appointment();
            appt.setApptId(ReadSvc.getInstance().getNextSeq(Constant.APPOINTMENT));
            appt.setStatus(Constant.Scheduled);
            appt.setNotes("testing");
            appt.setReferralId(1);
            appt.setClinic("Oakland");
            appt.setCollateralReceived(false);
            appt.setIsEligible("no");
            appt.setStartDate(now);
            appt.setEndDate(now);
            appt.setType(Constant.Blocked);
            debug("apptId = "+appt.getApptId());
            PSService.getService().createAppointment(appt);
        }
    }

    static void debug(String msg){
        System.out.println(msg);
    }

}
