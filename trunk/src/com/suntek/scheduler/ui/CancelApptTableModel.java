package com.suntek.scheduler.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.suntek.scheduler.appsvcs.persistence.Appointment;

public class CancelApptTableModel extends AbstractTableModel {
	
	static final String APPT_ID = "#";
    static final String STATUS = "Status";
    static final String CANCELED_BY = "Canceled by";
    static final String CANCEL_BY_PATIENT = "Cancel by Patient";
    static final String CANCEL_BY_CLINIC = "Cancel by Clinic";
    static final String WN_TWENTY_FOUR_HRS = "Within 24 hours";
    static final String CANCEL_REASON = "Cancel Reason";
    static final String CANCEL_OTHER_REASON = "Other Reason";
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
	
	public CancelApptTableModel(List apptList){
		appts = apptList;    		
	}
	
	public int getRowCount(){
		return appts.size();
	}
	
	public int getColumnCount(){
		return 18;
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
		case 2: return CANCELED_BY;
		case 3: return CANCEL_BY_PATIENT;
		case 4: return CANCEL_BY_CLINIC;
		case 5: return WN_TWENTY_FOUR_HRS;
		case 6: return CANCEL_REASON;
		case 7: return CANCEL_OTHER_REASON;
		case 8: return DATE;
		case 9: return TIME;
		case 10: return DOCTOR;
		case 11: return NTS;
		case 12: return LANG;
		case 13: return CR;
		case 14: return ELIG;
		case 15: return WALKIN;
		case 16: return NOTES;
		case 17: return STATUS_NOTES;
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
		case 2: return appt.getCanceledBy();
		case 3: return appt.isCancelByPatientStr();
		case 4: return appt.isCancelByClinicStr();
		case 5: return appt.isWnTwentyFourHrsStr();
		case 6: return appt.getCancelReason();
		case 7: return appt.getCancelOtherReason();
		case 8: return appt.getApptDateStr();
		case 9: return appt.getStartTimeStr();
		case 10: return appt.getProvider();
		case 11: return appt.needTranSvcStr();
		case 12: return appt.getLang();
		case 13: return appt.collateralReceivedStr();
		case 14: return appt.isEligible();
		case 15: return appt.isWalkInStr();
		case 16: return appt.getNotes();
		case 17: return appt.getStatusNotes();
		default: return "Unknown";
		}    		
	}
	
	public void setValueAt(Object val, int row, int col){
		// not implemented
	}
}    
