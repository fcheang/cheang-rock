package com.suntek.scheduler.ui;

import java.beans.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

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
public class JCalendarPropertyChangeAdapter
    implements PropertyChangeListener {

    MainFrame f = null;

    public JCalendarPropertyChangeAdapter(MainFrame f){
        this.f = f;
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *   and the property that has changed.
     * @todo Implement this java.beans.PropertyChangeListener method
     */
    public void propertyChange(PropertyChangeEvent evt){
        if (evt.getPropertyName().equals("calendar")){
            f.selectedDate = (Calendar)evt.getNewValue();
            f.updateStatusBar();
            debug("selectedDate = "+f.df.format(f.selectedDate.getTime()));
            if (f.isWorkAreaActive){
                //if (f.selectedNodeType == f.PROVIDER_REC ||
                //    f.selectedNodeType == f.PATIENT_REC) {
                if (f.getSelectedProviderNode() != null){
                    String name = f.getSelectedProviderNode().getUserObject().toString();
                    Date apptDate = f.selectedDate.getTime();
                    PSService.getService().createDailyApptTable(name, apptDate);
                }
                /*
                else if (f.selectedNodeType == f.PATIENT_REC) {
                    String name = f.selectedNode.getUserObject().toString();
                    String info = "Patient = " + name + "\n" +
                        "Date = " + f.df.format(f.selectedDate.getTime());
                    JTextArea ta = new JTextArea(info);
                    ta.setName("patient=" + name);
                    f.splitPane.setRightComponent(ta);
                }
                */
            }
        }
    }

    private void debug(String msg){
        if (f.debugOn){
            System.out.println("[JCalendarPropertyChangeAdapter]: "+msg);
        }
    }
}
