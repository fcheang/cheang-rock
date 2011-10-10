package com.suntek.scheduler.ui;

import javax.swing.table.*;
import java.util.*;
import com.suntek.scheduler.appsvcs.persistence.Appointment;
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
public class DailyApptTableModel
    extends AbstractTableModel {

    private boolean debugOn = Constant.debugOn;

    private String provider = null;
    private Date apptDate = new Date();
    private List appts = null;

    public DailyApptTableModel(String provider, Date aApptDate, List aAppts) {
    	this.provider = provider;
        apptDate = aApptDate;
        appts = aAppts;
    }

    public void setApptStatus(long apptId, String status){
        Appointment appt = null;
        for (int i=0; i<appts.size(); i++){
            appt = (Appointment)appts.get(i);
            if (appt.getApptId() == apptId){
                appt.setStatus(status);
            }
        }
    }

    public void setAppt(Date aApptDate, List aAppts){
        apptDate = aApptDate;
        appts = aAppts;
    }

    public void addAppt(Appointment appt){
        if (!appts.contains(appt)){
            appts.add(appt);
        }else{
            debug("xxx appt "+appt+" already exist ");
        }
    }

    public void updateAppt(Appointment oldAppt, Appointment newAppt){
        debug("updateAppt("+oldAppt+" | "+newAppt+")");
        appts.remove(oldAppt);
        appts.add(newAppt);
    }

    public void deleteAppt(Appointment oldAppt){
        appts.remove(oldAppt);
    }

    /**
     * Returns the number of columns in the model.
     *
     * @return the number of columns in the model
     * @todo Implement this javax.swing.table.TableModel method
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     * Returns the number of rows in the model.
     *
     * @return the number of rows in the model
     * @todo Implement this javax.swing.table.TableModel method
     */
    public int getRowCount() {
        return 6 * 10; // 10 minutes for 10 hour each side
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     * @todo Implement this javax.swing.table.TableModel method
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
       Date d = getDateForRowAndCol(rowIndex, columnIndex, apptDate);
       if (columnIndex == 0){
           return d;
       }else{
           Appointment appt = null;
           if (columnIndex == 1){
               appt = getAppt(d, Constant.Appointment);
           }else if (columnIndex == 2){
               appt = getAppt(d, Constant.DoubleBook);
           }
           return new ApptCell(d, appt);
       }
   }

    public static Date getDateForRowAndCol(int rowIndex, int columnIndex, Date apptDate){
      int hour = 8; // start at 8 am
      int min = 0;
      Calendar c = new GregorianCalendar();
      c.setTime(apptDate);

      hour += (rowIndex / 6);
      min = (min + (rowIndex % 6)) * 10;

      c.set(Calendar.HOUR_OF_DAY, hour);
      c.set(Calendar.MINUTE, min);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
      return c.getTime();
    }

    private Appointment getAppt(Date d, String type){
        Appointment appt = null;
        for (int i=0; i<appts.size(); i++){
            appt = (Appointment)appts.get(i);
            if (belongsTo(d, appt, type)){
                return appt;
            }
        }
        return null;
    }

    private boolean belongsTo(Date date, Appointment appt, String type){
        if ( (date.equals(appt.getStartDate()) || date.after(appt.getStartDate())) &&
             date.before(appt.getEndDate()) ){
            if (appt.getType().equals(Constant.Blocked) || appt.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public String getColumnName(int c){
      String colName = "";
      switch (c){
          case 0:
              colName = "Time";
              break;
          case 1:
              colName = "Appointment";
              break;
          case 2:
              colName = "Double Book";
              break;
      }
      return colName;

    }

    /**
     *  Returns false.  This is the default implementation for all cells.
     *
     *  @param  rowIndex  the row being queried
     *  @param  columnIndex the column being queried
     *  @return false
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     *  This empty implementation is provided so users don't have to implement
     *  this method if their data model is not editable.
     *
     *  @param  aValue   value to assign to cell
     *  @param  rowIndex   row of cell
     *  @param  columnIndex  column of cell
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // update the new appointment info to database
        debug("setValueAt("+aValue+", "+rowIndex+", "+columnIndex+")");
    }


    /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0){
            return Date.class;
        }else{
            return ApptCell.class;
        }

    }

    public void setProvider(String provider){
    	this.provider = provider;
    }
    
    public String getProvider(){
    	return provider;
    }
    
    private void debug(String msg){
        if (debugOn){
            System.out.println("[DailyApptTableModel]: "+msg);
        }
    }
}
