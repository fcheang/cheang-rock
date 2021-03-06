package com.suntek.scheduler.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;
import com.suntek.scheduler.appsvcs.persistence.Constant;
import com.suntek.scheduler.appsvcs.persistence.Patient;

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
public class EvalCellRenderer
    implements TableCellRenderer {

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
       String str = (String)value;
       JTextArea ta = new JTextArea(str);
       String status = (String)table.getModel().getValueAt(row, 6);
       if (status.equals(Patient.OVERDUE)){
    	   if (isSelected){
    		   ta.setBackground(Constant.OVERDUE_SELECTED_COLOR);
    	   }else{
    		   ta.setBackground(Constant.OVERDUE_COLOR);
    	   }
       }else{
    	   if (isSelected){
    		   ta.setBackground(Constant.OVERDUE_SELECTED_COLOR);
    	   }else{
    		   ta.setBackground(Color.white);
    	   }
       }
       return ta;
    }
}
