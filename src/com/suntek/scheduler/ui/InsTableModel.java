package com.suntek.scheduler.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.suntek.scheduler.appsvcs.persistence.Insurance;

public class InsTableModel extends AbstractTableModel {
	
	static final String ID_COL = "Id";
	static final String REF_DATE = "Ref date";
	static final String ELIG_EFF_DATE = "Elig eff date";
	static final String ELIG_TERM_DATE = "Elig term date";
	static final String INS_COMPANY_COL = "Insurance Company";
	static final String MEMBERID_COL = "MemberId";
	static final String COPAY_COL = "Copay";
	static final String COPAY_PARITY_COL = "Copay Parity";
	static final String PHONE_NUM_COL = "Phone #";
	static final String AN_COL = "Auth #";
	static final String AN_PARITY_COL = "Auth # Parity";
	static final String NAV_COL = "# Auth visit";
	static final String NAV_PARITY_COL = "# Auth visit Parity";	
	static final String MEDI_CAL_ID_COL = "Medi-Cal CIN #";
	static final String MEDI_CAL_ISSUE_DATE_COL = "Medi-Cal Issue Date";
	static final String NOTES = "Notes";
		
	private List insurances = null;
	
	public InsTableModel(List insList){
		insurances = insList;    		
	}
	
	public int getRowCount(){
		return insurances.size();
	}
	
	public int getColumnCount(){
		return 15;
	}

	public void addIns(Insurance ins){
		insurances.add(ins);
		fireTableDataChanged();    		
	}
	
	public void setIns(List insList){
		insurances = insList;
		fireTableDataChanged();
	}    	
	
	public void removeInsById(int insId){
		for (int i=0;i<insurances.size(); i++){
			Insurance ins = (Insurance)insurances.get(i);
			if (ins.getInsId() == insId){
				insurances.remove(i);
				break;
			}
		}
		fireTableDataChanged();
	}
	
	public Insurance getIns(int i){
		if (insurances.size() >= i+1){
			return (Insurance)insurances.get(i);
		}else{
			return null;
		}
	}
	
	public String getColumnName(int col){
		switch (col){    		
		case 0: return ID_COL;
		case 1: return REF_DATE;
		case 2: return ELIG_EFF_DATE;
		case 3: return ELIG_TERM_DATE;		
		case 4: return INS_COMPANY_COL;
		case 5: return PHONE_NUM_COL;		
		case 6: return MEMBERID_COL;
		case 7: return COPAY_COL;
		case 8: return COPAY_PARITY_COL;
		case 9: return NAV_COL;		
		case 10: return AN_COL;
		case 11: return NAV_PARITY_COL;
		case 12: return AN_PARITY_COL;
		case 13: return MEDI_CAL_ID_COL;
		case 14: return MEDI_CAL_ISSUE_DATE_COL;
		case 15: return NOTES;
		default: return "";
		}
	}    	
	
	public boolean isCellEditable(int row, int col){
		return false;
	}
	
	public Object getValueAt(int row, int col){		
		Insurance ins = (Insurance)insurances.get(row);
		switch (col){
		case 0: return ""+ins.getInsId();
		case 1: return ins.getRefDateStr();
		case 2: return ins.getEffStartDateStr();
		case 3: return ins.getEffEndDateStr();
		case 4: return ins.getInsCompany();
		case 5: return ins.getInsPhoneNumFormated();
		case 6: return ins.getMemberId();
		case 7: return ins.getCopayStr();
		case 8: return ins.getCopayParityStr();
		case 9: return ins.getNumAuthVisitForMDStr(); 
		case 10: return ins.getAuthNumForMD();
		case 11: return ins.getNumAuthVisitForMAStr();
		case 12: return ins.getAuthNumForMA();
		case 13: return ins.getMedicalId();
		case 14: return ins.getMedIssueDateStr();
		case 15: return ins.getNotes();
		default: return "Unknown";
		}    		
	}
	
	public void setValueAt(Object val, int row, int col){
		super.setValueAt(val, row, col);
		Insurance ins = (Insurance)insurances.get(row);
		String valStr = (String)val;
		switch (col){
		case 0: ins.setRefId(Integer.parseInt(valStr));
		case 1: ins.setRefDateStr(valStr);
		case 2: ins.setEffStartDateStr(valStr);
		case 3: ins.setEffEndDateStr(valStr);
		case 4: ins.setInsCompany(valStr);
		case 5: ins.setInsPhoneNum(valStr);		
		case 6: ins.setMemberId(valStr);
		case 7: ins.setCopay(valStr);
		case 8: ins.setCopayParity(valStr);
		case 9: ins.setNumAuthVisitForMD(Integer.parseInt(valStr));		
		case 10: ins.setAuthNumForMD(valStr);
		case 11: ins.setNumAuthVisitForMA(Integer.parseInt(valStr));
		case 12: ins.setAuthNumForMA(valStr);
		case 13: ins.setMedicalId(valStr);
		case 14: ins.setMedIssueDateStr(valStr);
		case 15: ins.setNotes(valStr);
		default: ;
		}    		
		super.fireTableCellUpdated(row, col);
	}
}    
