package com.suntek.scheduler.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.suntek.scheduler.appsvcs.persistence.Patient;

public class EvaluationTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	static final String ID = "Id";
	static final String ADMIT_DATE = "Admission Date";
	static final String FIRST_NAME = "First Name";
	static final String LAST_NAME = "Last Name";
	static final String PHONE = "Phone Number";
	static final String DOB = "Date of Birth";
	static final String STATUS = "Status";
		
	private List<Patient> patients = null;
	
	public EvaluationTableModel(List<Patient> patList){
		patients = patList;    		
	}
	
	public int getRowCount(){
		return patients.size();
	}
	
	public int getColumnCount(){
		return 7;
	}

	public void addPatient(Patient patient){
		patients.add(patient);
		fireTableDataChanged();    		
	}
	
	public void setPatients(List<Patient> patients){
		this.patients = patients;
		fireTableDataChanged();
	}    	
	
	public void removePatientById(int refId){
		for (int i=0;i<patients.size(); i++){
			Patient pat = patients.get(i);
			if (pat.getRefId() == refId){
				patients.remove(i);
				break;
			}
		}
		fireTableDataChanged();
	}
	
	public Patient getPatient(int i){
		if (patients.size() >= i+1){
			return patients.get(i);
		}else{
			return null;
		}
	}
	
	public String getColumnName(int col){
		switch (col){    		
		case 0: return ID;
		case 1: return ADMIT_DATE;
		case 2: return FIRST_NAME;
		case 3: return LAST_NAME;		
		case 4: return PHONE;
		case 5: return DOB;		
		case 6: return STATUS;
		default: return "";
		}
	}    	
	
	public boolean isCellEditable(int row, int col){
		return false;
	}
	
	public Object getValueAt(int row, int col){		
		Patient pat = patients.get(row);
		switch (col){
		case 0: return ""+pat.getRefId();
		case 1: return pat.getInsAdmitDateStr();
		case 2: return pat.getFirstName();
		case 3: return pat.getLastName();
		case 4: return pat.getPhoneNum();
		case 5: return pat.getDateOfBirth();
		case 6: return pat.getEvalStatus();
		default: return "Unknown";
		}    		
	}
	
}    
