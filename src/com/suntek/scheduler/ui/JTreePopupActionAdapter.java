package com.suntek.scheduler.ui;

import java.awt.event.*;
import java.util.Date;
import javax.swing.tree.*;

import com.suntek.scheduler.appsvcs.WriteSvc;
import com.suntek.scheduler.appsvcs.persistence.Patient;
import com.suntek.scheduler.appsvcs.persistence.ProviderNode;

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
public class JTreePopupActionAdapter
    implements ActionListener {

    MainFrame f;

    public JTreePopupActionAdapter(MainFrame adaptee){
        f = adaptee;
    }

    private void refreshTree(){
        TreeNode root = PSService.getService().getRootNode();
        f.createLaunchPad(root);
        f.leftSplitPane.getTopComponent().invalidate();    	
    }
    
    /**
     * Invoked when an action occurs.
     *
     * @param e ActionEvent
     * @todo Implement this java.awt.event.ActionListener method
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("refreshProviderList") ||
            e.getActionCommand().equals("refreshPatientList") ||
            e.getActionCommand().equals("refreshClinicList"))
        {
        	refreshTree();
        	if (e.getActionCommand().equals("refreshPatientList")){
        		PSService.getService().clearPatientNodeCache();
        	}
        }else if (e.getActionCommand().equals("hideProvider")){
        	ProviderNode providerNode = (ProviderNode)f.getSelectedProviderNode().getUserObject();
        	WriteSvc.getInstance().updateProviderInactiveBit(providerNode.getProviderId(), true, new Date());
        	refreshTree();
        }else if (e.getActionCommand().equals("hidePatient")){
        	Patient patientNode = (Patient)f.getSelectedPatNode().getUserObject();
        	WriteSvc.getInstance().updatePatientDischargedBit(patientNode.refId, true, new Date());
        	refreshTree();
        }else if (e.getActionCommand().equals("unhideProvider")){
        	ProviderNode providerNode = (ProviderNode)f.getSelectedInactProviderNode().getUserObject();
        	WriteSvc.getInstance().updateProviderInactiveBit(providerNode.getProviderId(), false, new Date());
        	refreshTree();        	
        }else if (e.getActionCommand().equals("unhidePatient")){
        	Patient patientNode = (Patient)f.getSelectedDcPatNode().getUserObject();
        	WriteSvc.getInstance().updatePatientDischargedBit(patientNode.refId, false, new Date());
        	refreshTree();        	
        }


        /*
        // check which popup action is selected
        // and show the appt internal frame
        JMenuItem source = (JMenuItem)(e.getSource());
        debug("MenuItemSelected = "+source.getText());
        String action = source.getText();

        if (action.equals("Schedule Appointment")){
            if (f.selectedNodeType == f.PROVIDER_REC) {
                String name = f.selectedProviderdNode.getUserObject().toString();
                java.util.Date apptDate = f.selectedDate.getTime();
                if (f.apptTable == null){
                    JTable table = PSService.getService().getDailyAppt(name,
                        f.selectedDate.getTime());
                    f.apptTable = table;
                    f.apptTableModel = (DailyApptTableModel)table.getModel();
                    f.splitPane.setRightComponent(new JScrollPane(table));
                }else{
                    java.util.List appts = PSService.getService().getApptForDay(name, apptDate);
                    f.apptTableModel.setAppt(apptDate, appts);
                    f.apptTable.setVisible(false);
                    f.apptTable.invalidate();
                    f.apptTable.setVisible(true);
                }
            }else if (f.selectedNodeType == f.PATIENT_REC) {
                String name = f.selectedPatNode.getUserObject().toString();
                String info = "Patient = " + name + "\n" +
                    "Date = " + f.df.format(f.selectedDate.getTime());
                JTextArea ta = new JTextArea(info);
                ta.setName("patient=" + name);
                f.splitPane.setRightComponent(ta);
            }

            f.isWorkAreaActive = true;
        }else if (action.equals("Show Appointment")){
            String name = f.selectedProviderdNode.getUserObject().toString();
            String info = "Patient = "+name+"\n"+
                "Date = "+f.df.format(f.selectedDate.getTime());
            JTextArea ta = new JTextArea(info);
            ta.setName("patient="+name);
            ta.setBackground(new Color(102, 189, 176));
            f.splitPane.setRightComponent(ta);
            f.isWorkAreaActive = true;
        }else if (action.equals("Refresh Provider list") ||
                  action.equals("Refresh Patient list")){
            TreeNode root = PSService.getService().getRootNode();
            f.createLaunchPad(root);
            f.leftSplitPane.getTopComponent().invalidate();
        }
        */
    }

    private void debug(String str){
        if (f.debugOn){
            System.out.println("[JTreePopupActionAdapter]: "+str);
        }
    }
}
