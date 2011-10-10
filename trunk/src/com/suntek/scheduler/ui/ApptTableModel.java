package com.suntek.scheduler.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.suntek.scheduler.appsvcs.persistence.Appointment;

public class ApptTableModel extends AbstractTableModel {
	
	static final String APPT_ID = "#";
    static final String STATUS = "Status";
    static final String DATE = "Date";
    static final String TIME = "Time";
    static final String DOCTOR = "Doctor";
    static final String NTS = "NTS";
    static final String LANG = "Lang";
    static final String CR = "CR";
    static final String ELIG = "Elig";
    static final String WALKIN = "WalkIn";
    static final String NOTES = "Notes";
    static final String STATUS_NOTES = "Show/Noshow Notes";
	
	private List appts = null;
	
	public ApptTableModel(List apptList){
		appts = apptList;    		
	}
	
	public int getRowCount(){
		return appts.size();
	}
	
	public int getColumnCount(){
		return 12;
	}

	public void addAppt(Appointment appt){
		appts.add(appt);
		fireTableDataChanged();    		
	}
	
	public void setAppts(List apptList){
		appts = apptList;
		fireTableDataChanged();
	}    	
	
	public void removeApptById(int apptId){
		for (int i=0;i<appts.size(); i++){
			Appointment appt = (Appointment)appts.get(i);
			if (appt.getApptId() == apptId){
				appts.remove(i);
				break;
			}
		}
		fireTableDataChanged();
	}
	
	public Appointment getAppt(int i){
		if (appts.size() >= i+1){
			return (Appointment)appts.get(i);
		}else{
			return null;
		}
	}
	
	public String getColumnName(int col){
		switch (col){    		
		case 0: return APPT_ID;
		case 1: return STATUS;
		case 2: return DATE;
		case 3: return TIME;
		case 4: return DOCTOR;
		case 5: return NTS;
		case 6: return LANG;
		case 7: return CR;
		case 8: return ELIG;
		case 9: return WALKIN;
		case 10: return NOTES;
		case 11: return STATUS_NOTES;
		default: return "";
		}
	}    	
	
	public boolean isCellEditable(int row, int col){
		return false;
	}
	
	public Object getValueAt(int row, int col){		
		Appointment appt = (Appointment)appts.get(row);
		switch (col){
		case 0: return ""+appt.getApptId();
		case 1: return appt.getStatus();
		case 2: return appt.getApptDateStr();
		case 3: return appt.getStartTimeStr();
		case 4: return appt.getProvider();
		case 5: return appt.needTranSvcStr();
		case 6: return appt.getLang();
		case 7: return appt.collateralReceivedStr();
		case 8: return appt.isEligible();
		case 9: return appt.isWalkInStr();
		case 10: return appt.getNotes();
		case 11: return appt.getStatusNotes();
		default: return "Unknown";
		}    		
	}
	
	public void setValueAt(Object val, int row, int col){
		// not implemented
	}
}    
