package com.suntek.scheduler.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.suntek.scheduler.appsvcs.*;
import com.suntek.scheduler.appsvcs.persistence.*;


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
public class ApptCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private JPanel panel = new JPanel();

    /**
     * Returns the component used for drawing the cell.
     *
     * @param table the <code>JTable</code> that is asking the renderer to
     *   draw; can be <code>null</code>
     * @param value the value of the cell to be rendered. It is up to the
     *   specific renderer to interpret and draw the value. For example, if
     *   <code>value</code> is the string "true", it could be rendered as a
     *   string or it could be rendered as a check box that is checked.
     *   <code>null</code> is a valid value
     * @param isSelected true if the cell is to be rendered with the
     *   selection highlighted; otherwise false
     * @param hasFocus if true, render cell appropriately. For example, put
     *   a special border on the cell, if the cell can be edited, render in
     *   the color used to indicate editing
     * @param row the row index of the cell being drawn. When drawing the
     *   header, the value of <code>row</code> is -1
     * @param column the column index of the cell being drawn
     * @return Component
     * @todo Implement this javax.swing.table.TableCellRenderer method
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column)
    {
        // sets the color lightblue if there is an appointment scheduled
        // sets the appointment text
        ApptCell cell = (ApptCell)value;
        Appointment appt = cell.getAppointment();

        JPanel p = new JPanel();

        if (appt == null){
            // no appt at this time
            p.setBackground(Color.WHITE);
        }else{
            if (appt.getType().equals(Constant.Blocked)) {
                p.setBackground(Constant.BLOCK_COLOR);
            }else if (appt.getType().equals(Constant.Appointment) ||
                      appt.getType().equals(Constant.DoubleBook)){
                // there is appt
                if (appt.getStatus().equals(Constant.Scheduled)) {
                	if (appt.isEligible() != null && appt.isEligible().equals("no")){
                		p.setBackground(Constant.NOT_ELIGIBLE_COLOR);                		
                	}else{
                		p.setBackground(Constant.SCHEDULED_COLOR);
                	}
                }
                else if (appt.getStatus().equals(Constant.Seen)) {
                    p.setBackground(Constant.SEEN_COLOR);
                }
                else if (appt.getStatus().equals(Constant.NotSeen)) {
                    p.setBackground(Constant.NOT_SEEN_COLOR);
                }
            }
            Date cellDate =
                DailyApptTableModel.getDateForRowAndCol(row, column, MainFrame.f.selectedDate.getTime());
            if (sameHourAndMin(appt.getStartDate(), cellDate)){
                // only display the appt text for the first row
                p.add(new JLabel(appt.getFirstSlotDisplayText(), JLabel.CENTER));
            }else if (isSecondSlot(appt.getStartDate(), cellDate)){
                // display phonenum, age group and psp number for the second row
                p.add(new JLabel(appt.getSecondSlotDisplayText(), JLabel.CENTER));
            }else{
                p.add(new JLabel("/", JLabel.CENTER));
            }
        }
        return p;
    }

    private boolean isSecondSlot(Date d1, Date d2){
        // second slot is 10 min after the first slot
        Calendar ad = new GregorianCalendar();
        ad.setTime(d1);
        Calendar cd = new GregorianCalendar();
        cd.setTime(d2);
        ad.add(Calendar.MINUTE, 10);
        if (ad.get(Calendar.HOUR_OF_DAY) == cd.get(Calendar.HOUR_OF_DAY) &&
            ad.get(Calendar.MINUTE) == cd.get(Calendar.MINUTE)){
            return true;
        }else{
            return false;
        }
    }

    private boolean sameHourAndMin(Date d1, Date d2){
        Calendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        Calendar c2 = new GregorianCalendar();
        c2.setTime(d2);
        if (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY) &&
            c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)){
            return true;
        }else{
            return false;
        }
    }

    private void debug(String msg){
        if (Constant.debugOn){
            System.out.println("[ApptCellRenderer]: "+msg);
        }
    }
}
