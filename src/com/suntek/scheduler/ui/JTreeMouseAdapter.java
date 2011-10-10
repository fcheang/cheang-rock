package com.suntek.scheduler.ui;

import java.awt.event.*;
import javax.swing.JPopupMenu;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.suntek.scheduler.appsvcs.persistence.*;
import com.suntek.scheduler.appsvcs.*;

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
public class JTreeMouseAdapter
    extends MouseAdapter {

    MainFrame f = null;

    public JTreeMouseAdapter(MainFrame f) {
        this.f = f;
    }

    public void mousePressed(MouseEvent e) {
        //maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = null;

            // select popup type
            if (f.selectedNodeType == f.PROVIDER_FOLDER) {
                popup = f.providerFolderPU;
            }else if (f.selectedNodeType == f.PATIENT_FOLDER) {
                popup = f.patientFolderPU;
            }else if (f.selectedNodeType == f.CLINIC_FOLDER) {
                popup = f.clinicFolderPU;
            }else if (f.selectedNodeType == f.PROVIDER_REC){
                popup = f.providerPU;
            }else if (f.selectedNodeType == f.PATIENT_REC){
                popup = f.patientPU;
            }else if (f.selectedNodeType == f.INACT_PROVIDER_REC){
            	popup = f.providerPU2;
            }else if (f.selectedNodeType == f.DC_PATIENT_REC){
            	popup = f.patientPU2;
            }

            if (popup != null) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
        else if (e.getClickCount() == 2) {
            if (f.selectedDate != null) {
                // double click on a Provider or Patient
                if (f.selectedNodeType == f.PROVIDER_REC) {
                    populateApptWindow();
                }else if (f.selectedNodeType == f.PATIENT_REC) {
                    int patId = ((Patient) f.getSelectedPatNode().getUserObject()).getRefId();                	
                    showPatientDialog(0, patId);
                }else if (f.selectedNodeType == f.CLINIC_REC) {
                    showClinicDialog();
                }else if (f.selectedNodeType == f.DC_PATIENT_REC) {
                    int patId = ((Patient) f.getSelectedDcPatNode().getUserObject()).getRefId();                	
                	showPatientDialog(0, patId);
                }
            }
        }
    }

    private void showPatientDialog(int tabIndex, int patId) {
    	new PatientDialog(f, patId, tabIndex, f.getNextLocX(), f.getNextLocY(), false);
    }    	

    private void showClinicDialog() {
        String clinic = (String) f.getSelectedClinicNode().getUserObject();
        String dateStr = Constant.df_l.format(f.selectedDate.getTime());

        StringBuffer sb = new StringBuffer();
        //sb.append("<p align=\"center\">").
        sb.append(Constant.subTitleFont);
        sb.append("<b>Date: ").append(dateStr).append("</b><br>");
        sb.append("<b>Clinic: ").append(clinic).append("</b></font><br>");

        List appts = ReadSvc.getInstance().getApptForClinicAndDate(clinic,
            f.selectedDate.getTime());
        //sb.append("<table width=\"800\" border=\"1\">\n");
        sb.append("<table border=\"1\">\n");
        sb.append("<tr>\n");
        sb.append("<td>").append(Constant.thFont).append(
            "<b>Time</b></font></td>\n");
        sb.append("<td>").append(Constant.thFont).append(
            "<b>Patient</b></font></td>\n");
        sb.append("<td>").append(Constant.thFont).append(
            "<b>Provider</b></font></td>\n");
        sb.append("<td>").append(Constant.thFont).append(
            "<b>Appt. Type </b></font></td>\n");
        sb.append("<td>").append(Constant.thFont).append(
            "<b>Status</b></font></td>\n");
        sb.append("<td>").append(Constant.thFont).append(
            "<b>Contact</b></font></td>\n");
        sb.append("<td>").append(Constant.thFont).append(
            "<b>Notes</b></font></td>\n");
        sb.append("</tr>");

        Appointment appt;

        for (int i = 0; i < appts.size(); i++) {
            appt = (Appointment) appts.get(i);
            if (appt.getType().equals(Constant.Blocked)) {
                continue;
            }
            sb.append("<tr>\n");
            sb.append("<td>").append(Constant.thFont).append(appt.
                getStartTimeStr()).append(" - ").append(appt.getEndTimeStr()).
                append("</font></td>\n");
            sb.append("<td>").append(Constant.thFont).append(appt.getFullName()).
                append("</font></td>\n");
            sb.append("<td>").append(Constant.thFont).append(appt.getProvider()).
                append("</font></td>\n");
            sb.append("<td>").append(Constant.thFont).append(appt.getType()).
                append("</font></td>\n");
            sb.append("<td>").append(Constant.thFont).append(appt.getStatus()).
                append("</font></td>\n");
            sb.append("<td>").append(Constant.thFont).append(appt.
                getPhoneNumHTML()).append("</font></td>\n");
            sb.append("<td>").append(Constant.thFont).append(appt.getNotesHTML()).
                append("</font></td>\n");
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        //sb.append("</p>\n");

        final String finalInfo = sb.toString();
        final JEditorPane clinicInfo = new JEditorPane("text/html", finalInfo);
        clinicInfo.setFocusable(false);

        final JDialog clinicDialog = new JDialog(f, false);
        clinicDialog.setSize(1000, 500);
        clinicDialog.setTitle(clinic);
        //clinicDialog.setResizable(false);
        clinicDialog.setLocation(f.getNextLocClinicX(), f.getNextLocClinicY());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton printButton = new JButton("Print");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PrintUtils.printComponent(clinicInfo);
                //PrintUtils.printHTML(finalInfo);
            }
        }
        );
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clinicDialog.setVisible(false);
                clinicDialog.dispose();
            }
        }
        );

        actionPanel.add(closeButton);
        actionPanel.add(printButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(clinicInfo), BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        clinicDialog.add(panel);
        clinicDialog.setVisible(true);
    }

    private void populateApptWindow() {
        String name = f.getSelectedProviderNode().getUserObject().toString();
        java.util.Date apptDate = f.selectedDate.getTime();
        PSService.getService().createDailyApptTable(name, apptDate);
        f.isWorkAreaActive = true;
        f.updateStatusBar();
    }

}
