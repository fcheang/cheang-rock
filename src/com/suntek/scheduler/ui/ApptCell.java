package com.suntek.scheduler.ui;

import java.util.*;
import com.suntek.scheduler.appsvcs.persistence.Appointment;

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
public class ApptCell {

    private Appointment appt = null;
    private Date apptDate = null;

    public ApptCell(Date apptDate, Appointment appt) {
        this.appt = appt;
        this.apptDate = apptDate;
    }

    public Appointment getAppointment(){
        return appt;
    }

    public Date getApptDate(){
        return apptDate;
    }

    public String getDiaplayText(){
        if (appt != null){
            return appt.getFirstSlotDisplayText();
        }else{
            return null;
        }
    }

    public String getApptDetail(){
        if (appt != null){
            return appt.getDetail();
        }else{
            return null;
        }
    }
}
