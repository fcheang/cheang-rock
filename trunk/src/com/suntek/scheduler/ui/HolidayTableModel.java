package com.suntek.scheduler.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.suntek.scheduler.appsvcs.persistence.Holiday;
import com.suntek.scheduler.appsvcs.persistence.Insurance;

public class HolidayTableModel extends AbstractTableModel {

	static final String START_DATE_COL = "Start date";
	static final String END_DATE_COL = "End date";
	static final String DESCRIPTION_COL = "Description";

	private List holidays;
	
	public HolidayTableModel(List l){
		holidays = l;
	}
	
	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return holidays.size();
	}

	public Object getValueAt(int row, int col) {
		Holiday h = (Holiday)holidays.get(row);
		switch (col){
		case 0: return h.getStartDate();
		case 1: return h.getEndDate();
		case 2: return h.getDesc();
		default: return "Unknown";
		}    		
	}
	
	public void setValueAt(Object val, int row, int col){
		super.setValueAt(val, row, col);
		Holiday h = (Holiday)holidays.get(row);
		String valStr = (String)val;
		switch (col){
		case 0: h.setStartDateStr(valStr);		
		case 1: h.setEndDateStr(valStr);
		case 2: h.setDesc(valStr);
		default: ;
		}    		
		super.fireTableCellUpdated(row, col);
	}	
	
	public String getColumnName(int col){
		switch (col){    		
		case 0: return START_DATE_COL;
		case 1: return END_DATE_COL;
		case 2: return DESCRIPTION_COL;		
		default: return "";
		}
	}    		
	
	public boolean isCellEditable(int row, int col){
		return false;
	}	

	public Holiday getHoliday(int i){
		if (holidays.size() >= i+1){
			return (Holiday)holidays.get(i);
		}else{
			return null;
		} 
	}
	
	public void removeHolidayById(int id){
		for (int i=0;i<holidays.size(); i++){
			Holiday h = (Holiday)holidays.get(i);
			if (h.getId() == id){
				holidays.remove(i);
				break;
			}
		}
		fireTableDataChanged();
	}
	
	public void addHoliday(Holiday h){
		holidays.add(h);
		fireTableDataChanged();    		
	}
	
	public void setHolidays(List l){
		holidays = l;
		fireTableDataChanged();
	}    		
		
}
