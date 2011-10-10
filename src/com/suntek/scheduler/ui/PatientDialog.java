/**
 * 
 */
package com.suntek.scheduler.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import com.suntek.scheduler.appsvcs.ReadSvc;
import com.suntek.scheduler.appsvcs.WriteSvc;
import com.suntek.scheduler.appsvcs.persistence.Appointment;
import com.suntek.scheduler.appsvcs.persistence.Constant;
import com.suntek.scheduler.appsvcs.persistence.Evaluation;
import com.suntek.scheduler.appsvcs.persistence.Insurance;
import com.suntek.scheduler.appsvcs.persistence.Patient;
import com.suntek.scheduler.util.SecurityUtil;

/**
 * @author Steve Cheang
 *
 */
public class PatientDialog extends JDialog implements ActionListener {

	private PatientDialog patDialog = null;

	private int refId = -1; 		
	
	
	///////////////////
	// Insurance Tab //
	///////////////////
		
	private JTable insTable = null;
	private InsTableModel insTableModel = null;
	private JPanel insDetailPanel = null;

    private JLabel insProviderL = 		 new JLabel("Insurance company: ");
    private JLabel insAdmitDateL =       new JLabel("Admission date: ");
    private JLabel insAdmitDateFormatL = new JLabel("mm/dd/yyyy");    
    private JLabel esdL =         	     new JLabel("Elig eff date: ");
    private JLabel esdFormatL =          new JLabel("mm/dd/yyyy");
    private JLabel eedL =  		  		 new JLabel("Elig term date: ");
    private JLabel eedFormatL =          new JLabel("mm/dd/yyyy");
    private JLabel pspL =         		 new JLabel("Member Id/PSP number: ");
    private JLabel copayL =       		 new JLabel("Copay non-parity: ");
    private JLabel copayParityL = 		 new JLabel("Copay Parity:");
    private JLabel insPnL = 	  		 new JLabel("Phone number: ");
    private JLabel navForMDL = 		  	 new JLabel("MD # auth visit: ");
    private JLabel anForMDL = 		  	 new JLabel("Auth #: ");
    private JLabel navForMAL =   		 new JLabel("MA # auth visit: ");
    private JLabel anForMAL =    		 new JLabel("Auth #: ");    
    private JLabel medicalIdL =   		 new JLabel("Medi-Cal CIN #: ");
    private JLabel medIssueDateL = 		 new JLabel("Medi-Cal issue date: ");
    private JLabel medIssueDateFormatL = new JLabel("mm/dd/yyyy");
    private JLabel notesL =              new JLabel("Notes: ");
    
    private Spring shortStrut = Spring.constant(5);
    private Spring mediumStrut = Spring.constant(10);
    private Spring longStrut = Spring.constant(20);	    
	
	private int insId;
	private JComboBox  insCompanyCB = null;
	private JTextField insAdmitDateTF = null;
	private JTextField esdTF = null;
	private JTextField eedTF = null;
    private JTextField memberIdTF = null;
    private JTextField copayTF = null;
    private JTextField copayParityTF = null;
    private JTextField insPnTF = null;
    private JTextField navTF = null;
    private JTextField anTF = null;    
    private JTextField navParityTF = null;
    private JTextField anParityTF = null;
    private JTextField medicalIdTF = null;
    private JTextField medIssueDateTF = null;
    private JTextArea notesTA = null;
    private JScrollPane notesSP = null;
    
    private JButton addInsButton = null;
    private JButton saveInsButton = null;
    private JButton deleteInsButton = null;
    
    
    /////////////////////
    // Appointment Tab //
    /////////////////////
        
    private JTable apptTable = null;
    private ApptTableModel apptTableModel = null;
    private JPanel apptDetailPanel = null;
    private int curApptId;
    JLabel apptIdTF = null;
    JLabel apptStatusTF = null;
    JLabel apptDateTF = null;
    JLabel apptTimeTF = null;
    JLabel apptProvTF = null;
    JLabel apptNtsTF = null;
    JLabel apptCrTF = null;
    JLabel apptEligTF = null;
    JLabel apptWalkInTF = null;
    JTextArea apptNotesTF = null;
    JTextArea apptStatusNotesTF = null;
    

    /////////////////////
    // Canceled Appointment Tab //
    /////////////////////
    
    private JTable cancelApptTable = null;
    private CancelApptTableModel cancelApptTableModel = null;
    private JPanel cancelApptDetailPanel = null;
    private JScrollPane cancelApptDetailScroller = null; 
    private int curCancelApptId;
    JLabel cancelApptIdTF = null;
    JLabel cancelApptStatusTF = null;
    JLabel cancelApptCanceledByTF = null;

    JPanel cancelByPatientPanel = new JPanel(new FlowLayout());
    ButtonGroup cancelByPatientBtnGroup = new ButtonGroup();
    JRadioButton cancelByPatientYesBtn = new JRadioButton("Yes"); 
    JRadioButton cancelByPatientNoBtn = new JRadioButton("No");
    {
    	cancelByPatientBtnGroup.add(cancelByPatientYesBtn);
    	cancelByPatientBtnGroup.add(cancelByPatientNoBtn);
    	cancelByPatientPanel.add(cancelByPatientYesBtn);
    	cancelByPatientPanel.add(cancelByPatientNoBtn);
    	cancelByPatientYesBtn.setEnabled(false);
    	cancelByPatientNoBtn.setEnabled(false);
    }

    JPanel cancelByClinicPanel = new JPanel(new FlowLayout());
    ButtonGroup cancelByClinicBtnGroup = new ButtonGroup();
    JRadioButton cancelByClinicYesBtn = new JRadioButton("Yes"); 
    JRadioButton cancelByClinicNoBtn = new JRadioButton("No");
    {
    	cancelByClinicBtnGroup.add(cancelByClinicYesBtn);
    	cancelByClinicBtnGroup.add(cancelByClinicNoBtn);
    	cancelByClinicPanel.add(cancelByClinicYesBtn);
    	cancelByClinicPanel.add(cancelByClinicNoBtn);
    	cancelByClinicYesBtn.setEnabled(false);
    	cancelByClinicNoBtn.setEnabled(false);
    }
    
    JPanel cancelWnTwentyFourHrsPanel = new JPanel(new FlowLayout());
    ButtonGroup cancelWnTwentyFourHrsBtnGroup = new ButtonGroup();
    JRadioButton cancelWnTwentyFourHrsYesBtn = new JRadioButton("Yes"); 
    JRadioButton cancelWnTwentyFourHrsNoBtn = new JRadioButton("No");
    {
    	cancelWnTwentyFourHrsBtnGroup.add(cancelWnTwentyFourHrsYesBtn);
    	cancelWnTwentyFourHrsBtnGroup.add(cancelWnTwentyFourHrsNoBtn);
    	cancelWnTwentyFourHrsPanel.add(cancelWnTwentyFourHrsYesBtn);
    	cancelWnTwentyFourHrsPanel.add(cancelWnTwentyFourHrsNoBtn);
    	cancelWnTwentyFourHrsYesBtn.setEnabled(false);
    	cancelWnTwentyFourHrsNoBtn.setEnabled(false);
    }
    
    JComboBox cancelReasonCB = new JComboBox();
    JTextArea cancelOtherReasonTF = new JTextArea(5, 45);
    {
    	cancelReasonCB.setEnabled(false);
    	cancelOtherReasonTF.setEnabled(false);
    }
    JLabel cancelApptDateTF = null;
    JLabel cancelApptTimeTF = null;
    JLabel cancelApptProvTF = null;
    JLabel cancelApptNtsTF = null;
    JLabel cancelApptCrTF = null;
    JLabel cancelApptEligTF = null;
    JLabel cancelApptWalkInTF = null;
    JTextArea cancelApptNotesTF = null;
    JTextArea cancelApptStatusNotesTF = null;
    
    
    ///////////
    // Cache //
    ///////////
    
    private List insCompanyNames = null;


    /////////////////
    // Constructor //
    /////////////////
    
	public PatientDialog(Frame owner, int refId, int tabIndex, int locX, int locY, boolean model){
		super(owner, model);
		init(refId, tabIndex, locX, locY);
	}
	
	public PatientDialog(Dialog owner, int refId, int tabIndex, int locX, int locY, boolean model){
		super(owner, model);
		init(refId, tabIndex, locX, locY);
	}
	
	private void init(int refId, int tabIndex, int locX, int locY){
		this.refId = refId;
		patDialog = this;
        final Patient patFull = ReadSvc.getInstance().getPatientById(refId);
        List appts = ReadSvc.getInstance().getApptForPatient(patFull.getRefId());
        List cancelAppts = ReadSvc.getInstance().getCancelApptForPatient(patFull.getRefId());
        insCompanyNames = ReadSvc.getInstance().getAllInsuranceCompanyName();

		setSize(740, 870);
        setTitle(" " + patFull.getFullName());
        //setResizable(false);
        setResizable(true);
        if (locX != -1 && locY != -1){
        	setLocation(locX, locY);
        }

        final PatientForm bean = new PatientForm();
        JPanel generalPanel = createGeneralPanel(patFull, bean);
        JPanel lgPanel = createLgPanel(patFull, bean);
        JPanel insPanel = createInsPanel(patFull, bean);
        JPanel medPanel = createMedPanel(patFull);
        JPanel apptPanel = createApptPanel(appts);
        JPanel cancelApptPanel = createCancelApptPanel(cancelAppts);
        JPanel remPanel = createRemPanel(patFull, bean);
        JPanel osBalPanel = createBalPanel(patFull, bean);
        JPanel evalPanel = createEvalPanel(patFull);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("General", generalPanel);
        tabbedPane.addTab("LegalGardian", lgPanel);
        tabbedPane.addTab("Insurance", insPanel);
        tabbedPane.addTab("Medical", medPanel);
        tabbedPane.addTab("Appointment", apptPanel);
        tabbedPane.addTab("Canceled Appointment", cancelApptPanel);
        tabbedPane.addTab("Reminder", remPanel);
        tabbedPane.addTab("Outstanding Balance", osBalPanel);
        tabbedPane.addTab("Evaluation", evalPanel);
        tabbedPane.setSelectedIndex(tabIndex);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // save modification
                if (hasChanged(patFull, bean)) {
                    int reply = JOptionPane.showConfirmDialog(patDialog,
                        "Do you want to save change to patient record?",
                        "Confirm save",
                        JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {

                    	// validation
                    	if (bean.msgTA.getText().length() > 200){ 
                            JOptionPane.showMessageDialog(patDialog, "Reminder is too long. Maximum size is 200!\nPlease delete some text and try to save again.",
                                    "Invalid Reminder", JOptionPane.ERROR_MESSAGE);
                            return;
                    	}
                    	if (bean.pnTF.getText().length() > 10){
                    		JOptionPane.showMessageDialog(patDialog, "Phone Number is too long. Maximum size is 10!\nPlease delete some text and try to save again.",
                                    "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);                    		
                    		return;
                    	}
                    	if (bean.emailTF.getText().length() > 30){
                    		JOptionPane.showMessageDialog(patDialog, "email is too long. Maximum size is 30!\nPlease delete some text and try to save again.",
                                    "Invalid email", JOptionPane.ERROR_MESSAGE);                    		
                    		return;
                    	}
                    	
                    	saveRefChange(patFull, bean);
                    	
                    }
                }
                patDialog.setVisible(false);
                patDialog.dispose();
            }
        }
        );

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                patDialog.setVisible(false);
                patDialog.dispose();
            }
        }
        );

        actionPanel.add(okButton);
        actionPanel.add(cancelButton);

        JPanel p = new JPanel(new BorderLayout());
        p.add(tabbedPane, BorderLayout.CENTER);
        p.add(actionPanel, BorderLayout.SOUTH);

        patDialog.add(p);
        patDialog.setVisible(true);
    }
	
	private JPanel createApptPanel(List appts){
    	JPanel apptPanel = new JPanel(new BorderLayout(15,15));
    	
    	// Table Summary
    	apptTableModel = new ApptTableModel(appts);
    	apptTable = new JTable(apptTableModel);
    	apptTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	apptTable.getColumn(ApptTableModel.APPT_ID).setPreferredWidth(50);
    	apptTable.getColumn(ApptTableModel.STATUS).setPreferredWidth(80);
    	apptTable.getColumn(ApptTableModel.DATE).setPreferredWidth(120);
    	apptTable.getColumn(ApptTableModel.TIME).setPreferredWidth(80);
    	apptTable.getColumn(ApptTableModel.DOCTOR).setPreferredWidth(180);
    	apptTable.getColumn(ApptTableModel.NTS).setPreferredWidth(50);
    	apptTable.getColumn(ApptTableModel.LANG).setPreferredWidth(80);
    	apptTable.getColumn(ApptTableModel.CR).setPreferredWidth(50);
    	apptTable.getColumn(ApptTableModel.ELIG).setPreferredWidth(50);
    	apptTable.getColumn(ApptTableModel.WALKIN).setPreferredWidth(50);
    	apptTable.getColumn(ApptTableModel.NOTES).setPreferredWidth(300);
    	apptTable.getColumn(ApptTableModel.STATUS_NOTES).setPreferredWidth(300);
    	apptTable.setAutoCreateRowSorter(true);
    	
    	apptTable.getSelectionModel().addListSelectionListener(
    			new ListSelectionListener() {
    				  public void valueChanged(ListSelectionEvent e){
    					  if (e.getValueIsAdjusting()){
    						  return;
    					  }
    					  ListSelectionModel lsm = (ListSelectionModel)e.getSource();
    					  if (lsm.isSelectionEmpty()){
    						  apptDetailPanel.setVisible(false);
    					  }else{
    			    	    	// set the detail panel
    			    	    	int viewRowIndex = apptTable.getSelectedRow();
    			    	    	int selectedRow = apptTable.convertRowIndexToModel(viewRowIndex);
    			    	    	if (selectedRow >= 0){
    			    	    		curApptId = Integer.parseInt((String)apptTableModel.getValueAt(selectedRow, 0));
    			    	    		apptIdTF.setText((String)apptTableModel.getValueAt(selectedRow, 0));
    			    	    		apptStatusTF.setText((String)apptTableModel.getValueAt(selectedRow, 1));
    			    	    		apptDateTF.setText((String)apptTableModel.getValueAt(selectedRow, 2));
    			    	    		apptTimeTF.setText((String)apptTableModel.getValueAt(selectedRow, 3));
    			    	    		apptProvTF.setText((String)apptTableModel.getValueAt(selectedRow, 4));
    			    	    		String ntsText = (String)apptTableModel.getValueAt(selectedRow, 5);
    			    	    		String langText = (String)apptTableModel.getValueAt(selectedRow, 6);
    			    	    		if (langText != null && !langText.equals("")){
    			    	    			ntsText = ntsText + " ("+langText+")";
    			    	    		}
    			    	    		apptNtsTF.setText(ntsText);
    			    	    		apptCrTF.setText((String)apptTableModel.getValueAt(selectedRow, 7));
    			    	    		apptEligTF.setText((String)apptTableModel.getValueAt(selectedRow, 8));
    			    	    		apptWalkInTF.setText((String)apptTableModel.getValueAt(selectedRow, 9));
    			    	    		apptNotesTF.setText((String)apptTableModel.getValueAt(selectedRow, 10));
    			    	    		apptStatusNotesTF.setText((String)apptTableModel.getValueAt(selectedRow, 11));
    			    	    		apptDetailPanel.setVisible(true);    
    			    	    	}
    					  }
    				  }
    			}
    	);

    	JScrollPane tableScroller = new JScrollPane(apptTable);
    	tableScroller.setPreferredSize(new Dimension(570, 200));
    	    	    	
        // Detail Panel
    	apptDetailPanel = createApptDetailPanel();
    	apptDetailPanel.setVisible(false);
    	
        apptPanel.add(tableScroller, BorderLayout.NORTH);
        apptPanel.add(apptDetailPanel, BorderLayout.CENTER);
        return apptPanel;
	}

	private JPanel createCancelApptPanel(List appts){
		cancelByClinicYesBtn.setActionCommand("cancelByClinicYesBtn");
		cancelByClinicNoBtn.setActionCommand("cancelByClinicNoBtn");
		cancelByPatientYesBtn.setActionCommand("cancelByPatientYesBtn");
		cancelByPatientNoBtn.setActionCommand("cancelByPatientNoBtn");
		cancelWnTwentyFourHrsYesBtn.setActionCommand("cancelWnTwentyFourHrsYesBtn");
		cancelWnTwentyFourHrsNoBtn.setActionCommand("cancelWnTwentyFourHrsNoBtn");
		
		cancelByClinicYesBtn.addActionListener(this);		
		cancelByClinicNoBtn.addActionListener(this);
		cancelByPatientYesBtn.addActionListener(this);
		cancelByPatientNoBtn.addActionListener(this);
		cancelWnTwentyFourHrsYesBtn.addActionListener(this);
		cancelWnTwentyFourHrsNoBtn.addActionListener(this);
		
    	JPanel apptPanel = new JPanel(new BorderLayout(15,15));
    	
    	// Table Summary
    	cancelApptTableModel = new CancelApptTableModel(appts);
    	cancelApptTable = new JTable(cancelApptTableModel);
    	cancelApptTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	cancelApptTable.getColumn(CancelApptTableModel.APPT_ID).setPreferredWidth(50);
    	cancelApptTable.getColumn(CancelApptTableModel.STATUS).setPreferredWidth(80);
    	cancelApptTable.getColumn(CancelApptTableModel.CANCELED_BY).setPreferredWidth(150);
    	cancelApptTable.getColumn(CancelApptTableModel.CANCEL_BY_PATIENT).setPreferredWidth(120);
    	cancelApptTable.getColumn(CancelApptTableModel.CANCEL_BY_CLINIC).setPreferredWidth(100);
    	cancelApptTable.getColumn(CancelApptTableModel.WN_TWENTY_FOUR_HRS).setPreferredWidth(100);
    	cancelApptTable.getColumn(CancelApptTableModel.CANCEL_REASON).setPreferredWidth(200);
    	cancelApptTable.getColumn(CancelApptTableModel.CANCEL_OTHER_REASON).setPreferredWidth(200);
    	cancelApptTable.getColumn(CancelApptTableModel.DATE).setPreferredWidth(120);
    	cancelApptTable.getColumn(CancelApptTableModel.TIME).setPreferredWidth(80);
    	cancelApptTable.getColumn(CancelApptTableModel.DOCTOR).setPreferredWidth(180);
    	cancelApptTable.getColumn(CancelApptTableModel.NTS).setPreferredWidth(50);
    	cancelApptTable.getColumn(CancelApptTableModel.LANG).setPreferredWidth(80);
    	cancelApptTable.getColumn(CancelApptTableModel.CR).setPreferredWidth(50);
    	cancelApptTable.getColumn(CancelApptTableModel.ELIG).setPreferredWidth(50);
    	cancelApptTable.getColumn(CancelApptTableModel.WALKIN).setPreferredWidth(50);
    	cancelApptTable.getColumn(CancelApptTableModel.NOTES).setPreferredWidth(300);
    	cancelApptTable.getColumn(CancelApptTableModel.STATUS_NOTES).setPreferredWidth(300);
    	cancelApptTable.setAutoCreateRowSorter(true);
    	
    	cancelApptTable.getSelectionModel().addListSelectionListener(
    			new ListSelectionListener() {
    				  public void valueChanged(ListSelectionEvent e){
    					  if (e.getValueIsAdjusting()){
    						  return;
    					  }
    					  ListSelectionModel lsm = (ListSelectionModel)e.getSource();
    					  if (lsm.isSelectionEmpty()){
    						  cancelApptDetailPanel.setVisible(false);
    					  }else{
    			    	    	// set the detail panel
    			    	    	int viewRowIndex = cancelApptTable.getSelectedRow();
    			    	    	int selectedRow = cancelApptTable.convertRowIndexToModel(viewRowIndex);
    			    	    	if (selectedRow >= 0){
    			    	    		curCancelApptId = Integer.parseInt((String)cancelApptTableModel.getValueAt(selectedRow, 0));
    			    	    		cancelApptIdTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 0));
    			    	    		cancelApptStatusTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 1));
    			    	    		cancelApptCanceledByTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 2));
    			    	    		
    			    	    		if (cancelApptTableModel.getValueAt(selectedRow, 3).equals("yes")){
    			    	    			cancelByPatientYesBtn.setSelected(true);
    			    	    		}else{
    			    	    			cancelByPatientNoBtn.setSelected(true);
    			    	    		}

    			    	    		if (cancelApptTableModel.getValueAt(selectedRow, 4).equals("yes")){
    			    	    			cancelByClinicYesBtn.setSelected(true);
    			    	    		}else{
    			    	    			cancelByClinicNoBtn.setSelected(true);
    			    	    		}

    			    	    		if (cancelApptTableModel.getValueAt(selectedRow, 5).equals("yes")){
    			    	    			cancelWnTwentyFourHrsYesBtn.setSelected(true);
    			    	    		}else{
    			    	    			cancelWnTwentyFourHrsNoBtn.setSelected(true);
    			    	    		}
    			    	    		
    			    	    		if (cancelByPatientYesBtn.isSelected()){
    			    	    			List rc = ReadSvc.getInstance().getCancelByPatientReasonCode();
    			    	    			cancelReasonCB.removeAllItems();
    			    	    			for (Object item : rc){
    			    	    				cancelReasonCB.addItem(item);
    			    	    			}
    			    	    		}else{
    			    	    			List rc = ReadSvc.getInstance().getCancelByClinicReasonCode();
    			    	    			cancelReasonCB.removeAllItems();
    			    	    			for (Object item : rc){
    			    	    				cancelReasonCB.addItem(item);
    			    	    			}    			    	    			
    			    	    		}
    			    	    		cancelReasonCB.setSelectedItem(cancelApptTableModel.getValueAt(selectedRow, 6));    			    	    		
    			    	    		
    			    	    		if (cancelApptTableModel.getValueAt(selectedRow, 7) == null){
    			    	    			cancelOtherReasonTF.setText("");
    			    	    		}else{
    			    	    			cancelOtherReasonTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 7));
    			    	    		}
    			    	    		
    			    	    		cancelApptDateTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 8));
    			    	    		cancelApptTimeTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 9));
    			    	    		cancelApptProvTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 10));
    			    	    		String ntsText = (String)cancelApptTableModel.getValueAt(selectedRow, 11);
    			    	    		String langText = (String)cancelApptTableModel.getValueAt(selectedRow, 12);
    			    	    		if (langText != null && !langText.equals("")){
    			    	    			ntsText = ntsText + " ("+langText+")";
    			    	    		}
    			    	    		cancelApptNtsTF.setText(ntsText);
    			    	    		cancelApptCrTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 13));
    			    	    		cancelApptEligTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 14));
    			    	    		cancelApptWalkInTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 15));
    			    	    		cancelApptNotesTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 16));
    			    	    		cancelApptStatusNotesTF.setText((String)cancelApptTableModel.getValueAt(selectedRow, 17));
    			    	    		cancelApptDetailPanel.setVisible(true);
    			    	    	}
    					  }
    				  }
    			}
    	);

    	JScrollPane tableScroller = new JScrollPane(cancelApptTable);
    	tableScroller.setPreferredSize(new Dimension(570, 200));
    	    	    	
        // Detail Panel
    	cancelApptDetailPanel = createCancelApptDetailPanel();
    	cancelApptDetailPanel.setVisible(false);
    	cancelApptDetailPanel.setPreferredSize(new Dimension(570, 700));
    	cancelApptDetailScroller = new JScrollPane(cancelApptDetailPanel);
    	
        apptPanel.add(tableScroller, BorderLayout.NORTH);
        apptPanel.add(cancelApptDetailScroller, BorderLayout.CENTER);
        return apptPanel;
	}
	
	private JPanel createApptDetailPanel(){		
        // 11 fields
        JLabel apptIdL = new JLabel("Confirmation#: ");
        JLabel apptStatusL = new JLabel("Status: ");
        JLabel apptDateL = new JLabel("Date: ");
        JLabel apptTimeL = new JLabel("Time: ");
        JLabel apptProvL = new JLabel("Doctor: ");
        JLabel apptNtsL = new JLabel("Need translation service: ");
        JLabel apptCrL = new JLabel("Colleteral received: ");
        JLabel apptEligL = new JLabel("Eligible for appt: ");
        JLabel apptWalkInL = new JLabel("Is WalkIn: ");
        JLabel apptNotesL = new JLabel("Notes: ");
        JLabel apptStatusNotesL = new JLabel("Show/NoShow notes: ");
        JPanel apptDetailPanel = new JPanel(new SpringLayout());
        SpringLayout l = new SpringLayout();
        apptDetailPanel.setLayout(l);

        apptIdTF = new JLabel("");
        apptStatusTF = new JLabel("");
        apptDateTF = new JLabel("");
        apptTimeTF = new JLabel("");
        apptProvTF = new JLabel("");
        apptNtsTF = new JLabel("");
        apptCrTF = new JLabel("");
        apptEligTF = new JLabel("");
        apptWalkInTF = new JLabel("");
        apptNotesTF = new JTextArea("", 5, 45);
        apptNotesTF.setLineWrap(true);
        apptNotesTF.setWrapStyleWord(true);
        apptNotesTF.setEditable(false);
        apptStatusNotesTF = new JTextArea("", 5, 45);
        apptStatusNotesTF.setLineWrap(true);
        apptStatusNotesTF.setWrapStyleWord(true);
        apptStatusNotesTF.setEditable(false);        

        apptDetailPanel.add(apptIdL);
        apptDetailPanel.add(apptIdTF);
        apptDetailPanel.add(apptStatusL);
        apptDetailPanel.add(apptStatusTF);
        apptDetailPanel.add(apptDateL);
        apptDetailPanel.add(apptDateTF);
        apptDetailPanel.add(apptTimeL);
        apptDetailPanel.add(apptTimeTF);
        apptDetailPanel.add(apptProvL);
        apptDetailPanel.add(apptProvTF);
        apptDetailPanel.add(apptNtsL);
        apptDetailPanel.add(apptNtsTF);
        apptDetailPanel.add(apptCrL);
        apptDetailPanel.add(apptCrTF);
        apptDetailPanel.add(apptEligL);
        apptDetailPanel.add(apptEligTF);
        apptDetailPanel.add(apptWalkInL);
        apptDetailPanel.add(apptWalkInTF);        
        apptDetailPanel.add(apptNotesL);
        apptDetailPanel.add(apptNotesTF);
        apptDetailPanel.add(apptStatusNotesL);
        apptDetailPanel.add(apptStatusNotesTF);
        

        Spring strut = Spring.constant(10);

        Spring labelsEast = Spring.sum(strut,
                                       l.getConstraints(apptNtsL).getWidth());

        // align first col
        l.putConstraint(SpringLayout.EAST, apptIdL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptStatusL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptDateL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptTimeL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptProvL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptNtsL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptCrL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptEligL, labelsEast,
                				SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptWalkInL, labelsEast,
								SpringLayout.WEST, apptDetailPanel);                
        l.putConstraint(SpringLayout.EAST, apptNotesL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptStatusNotesL,labelsEast, 
        						SpringLayout.WEST, apptDetailPanel);

        // align second col
        l.putConstraint(SpringLayout.WEST, apptIdTF, strut,
                        SpringLayout.EAST,
                        apptIdL);
        l.putConstraint(SpringLayout.WEST, apptStatusTF, strut,
                                SpringLayout.EAST,
                                apptStatusL);
        l.putConstraint(SpringLayout.WEST, apptDateTF, strut,
                                SpringLayout.EAST,
                                apptDateL);
        l.putConstraint(SpringLayout.WEST, apptTimeTF, strut,
                                SpringLayout.EAST,
                                apptTimeL);
        l.putConstraint(SpringLayout.WEST, apptProvTF, strut,
                                SpringLayout.EAST,
                                apptProvL);
        l.putConstraint(SpringLayout.WEST, apptNtsTF, strut,
                                SpringLayout.EAST,
                                apptNtsL);
        l.putConstraint(SpringLayout.WEST, apptCrTF, strut,
                                SpringLayout.EAST,
                                apptCrL);
        l.putConstraint(SpringLayout.WEST, apptEligTF, strut,
				                SpringLayout.EAST,
				                apptEligL);
        l.putConstraint(SpringLayout.WEST, apptWalkInTF, strut,
				                SpringLayout.EAST,
				                apptWalkInL);                
        l.putConstraint(SpringLayout.WEST, apptNotesTF, strut,
                                SpringLayout.EAST,
                                apptNotesL);
        l.putConstraint(SpringLayout.WEST, apptStatusNotesTF, strut,
                				SpringLayout.EAST,
            					apptStatusNotesL);


        // align row
        l.putConstraint(SpringLayout.NORTH, apptIdL, strut,
                        SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptIdTF, strut,
                        SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth1 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptIdL),
            l.getConstraint(SpringLayout.SOUTH, apptIdTF)));
        l.putConstraint(SpringLayout.NORTH, apptStatusL, rowNorth1,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptStatusTF, rowNorth1,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptStatusL),
            l.getConstraint(SpringLayout.SOUTH, apptStatusTF)));
        l.putConstraint(SpringLayout.NORTH, apptDateL, rowNorth2,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptDateTF, rowNorth2,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth3 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptDateL),
            l.getConstraint(SpringLayout.SOUTH, apptDateTF)));
        l.putConstraint(SpringLayout.NORTH, apptTimeL, rowNorth3,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptTimeTF, rowNorth3,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth4 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptTimeL),
            l.getConstraint(SpringLayout.SOUTH, apptTimeTF)));
        l.putConstraint(SpringLayout.NORTH, apptProvL, rowNorth4,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptProvTF, rowNorth4,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth5 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptProvL),
            l.getConstraint(SpringLayout.SOUTH, apptProvTF)));
        l.putConstraint(SpringLayout.NORTH, apptNtsL, rowNorth5,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptNtsTF, rowNorth5,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth6 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptNtsL),
            l.getConstraint(SpringLayout.SOUTH, apptNtsTF)));
        l.putConstraint(SpringLayout.NORTH, apptCrL, rowNorth6,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptCrTF, rowNorth6,
                                SpringLayout.NORTH, apptDetailPanel);
        
        Spring rowNorth7 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptCrL),
                l.getConstraint(SpringLayout.SOUTH, apptCrTF)));                
            l.putConstraint(SpringLayout.NORTH, apptEligL, rowNorth7,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, apptEligTF, rowNorth7,
                                    SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth8 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptEligL),
                l.getConstraint(SpringLayout.SOUTH, apptEligTF)));                
            l.putConstraint(SpringLayout.NORTH, apptWalkInL, rowNorth8,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, apptWalkInTF, rowNorth8,
                                    SpringLayout.NORTH, apptDetailPanel);
            
        Spring rowNorth9 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptWalkInL),
            l.getConstraint(SpringLayout.SOUTH, apptWalkInTF)));                
        l.putConstraint(SpringLayout.NORTH, apptNotesL, rowNorth9,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptNotesTF, rowNorth9,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth10 = Spring.sum(strut, Spring.max(
            	l.getConstraint(SpringLayout.SOUTH, apptNotesL),
            	l.getConstraint(SpringLayout.SOUTH, apptNotesTF)));
        l.putConstraint(SpringLayout.NORTH, apptStatusNotesL, rowNorth10,
                				SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, apptStatusNotesTF, rowNorth10,
                				SpringLayout.NORTH, apptDetailPanel);

        return apptDetailPanel;		
	}

	private JPanel createCancelApptDetailPanel(){		
        // 16 fields
        JLabel apptIdL = new JLabel("Confirmation#: ");
        JLabel apptStatusL = new JLabel("Status: ");
        JLabel apptCanceledByL = new JLabel("Canceled by: ");
        JLabel apptCancelByPatientL = new JLabel("Cancel by Patient: ");
        JLabel apptCancelByClinicL = new JLabel("Cancel by Clinic: ");
        JLabel apptWnTwentyFourHrsL = new JLabel("Is within 24 hours: ");
        JLabel apptCancelReasonL = new JLabel("Reason");
        JLabel apptCancelOtherReasonL = new JLabel("Other Reason");
        JLabel apptDateL = new JLabel("Date: ");
        JLabel apptTimeL = new JLabel("Time: ");
        JLabel apptProvL = new JLabel("Doctor: ");
        JLabel apptNtsL = new JLabel("Need translation service: ");
        JLabel apptCrL = new JLabel("Colleteral received: ");
        JLabel apptEligL = new JLabel("Eligible for appt: ");
        JLabel apptWalkInL = new JLabel("Is WalkIn: ");
        JLabel apptNotesL = new JLabel("Notes: ");
        JLabel apptStatusNotesL = new JLabel("Show/NoShow notes: ");
        JPanel apptDetailPanel = new JPanel(new SpringLayout());
        SpringLayout l = new SpringLayout();
        apptDetailPanel.setLayout(l);

        cancelApptIdTF = new JLabel("");
        cancelApptStatusTF = new JLabel("");
        cancelApptCanceledByTF = new JLabel("");
        cancelApptDateTF = new JLabel("");
        cancelApptTimeTF = new JLabel("");
        cancelApptProvTF = new JLabel("");
        cancelApptNtsTF = new JLabel("");
        cancelApptCrTF = new JLabel("");
        cancelApptEligTF = new JLabel("");
        cancelApptWalkInTF = new JLabel("");
        cancelApptNotesTF = new JTextArea("", 5, 45);
        cancelApptNotesTF.setLineWrap(true);
        cancelApptNotesTF.setWrapStyleWord(true);
        cancelApptNotesTF.setEditable(false);
        cancelApptStatusNotesTF = new JTextArea("", 5, 45);
        cancelApptStatusNotesTF.setLineWrap(true);
        cancelApptStatusNotesTF.setWrapStyleWord(true);
        cancelApptStatusNotesTF.setEditable(false);        

        apptDetailPanel.add(apptIdL);
        apptDetailPanel.add(cancelApptIdTF);
        apptDetailPanel.add(apptStatusL);
        apptDetailPanel.add(apptCanceledByL);
        apptDetailPanel.add(cancelApptCanceledByTF);
        apptDetailPanel.add(cancelApptStatusTF);
        apptDetailPanel.add(apptCancelByPatientL);
        apptDetailPanel.add(cancelByPatientPanel);
        apptDetailPanel.add(apptCancelByClinicL);
        apptDetailPanel.add(cancelByClinicPanel);
        apptDetailPanel.add(apptWnTwentyFourHrsL);
        apptDetailPanel.add(cancelWnTwentyFourHrsPanel);        
        apptDetailPanel.add(apptCancelReasonL);
        apptDetailPanel.add(cancelReasonCB);        
        apptDetailPanel.add(apptCancelOtherReasonL);
        apptDetailPanel.add(cancelOtherReasonTF);                
        apptDetailPanel.add(apptDateL);
        apptDetailPanel.add(cancelApptDateTF);
        apptDetailPanel.add(apptTimeL);
        apptDetailPanel.add(cancelApptTimeTF);
        apptDetailPanel.add(apptProvL);
        apptDetailPanel.add(cancelApptProvTF);
        apptDetailPanel.add(apptNtsL);
        apptDetailPanel.add(cancelApptNtsTF);
        apptDetailPanel.add(apptCrL);
        apptDetailPanel.add(cancelApptCrTF);
        apptDetailPanel.add(apptEligL);
        apptDetailPanel.add(cancelApptEligTF);
        apptDetailPanel.add(apptWalkInL);
        apptDetailPanel.add(cancelApptWalkInTF);        
        apptDetailPanel.add(apptNotesL);
        apptDetailPanel.add(cancelApptNotesTF);
        apptDetailPanel.add(apptStatusNotesL);
        apptDetailPanel.add(cancelApptStatusNotesTF);
        

        Spring strut = Spring.constant(10);

        Spring labelsEast = Spring.sum(strut,
                                       l.getConstraints(apptNtsL).getWidth());

        // align first col
        l.putConstraint(SpringLayout.EAST, apptIdL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptStatusL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptCanceledByL, labelsEast,
                				SpringLayout.WEST, apptDetailPanel);        
        l.putConstraint(SpringLayout.EAST, apptCancelByPatientL, labelsEast,
                				SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptCancelByClinicL, labelsEast,
                				SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptWnTwentyFourHrsL, labelsEast,
                				SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptCancelReasonL, labelsEast,
								SpringLayout.WEST, apptDetailPanel);                
        l.putConstraint(SpringLayout.EAST, apptCancelOtherReasonL, labelsEast,
								SpringLayout.WEST, apptDetailPanel);                
        l.putConstraint(SpringLayout.EAST, apptDateL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptTimeL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptProvL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptNtsL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptCrL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptEligL, labelsEast,
                				SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptWalkInL, labelsEast,
								SpringLayout.WEST, apptDetailPanel);                
        l.putConstraint(SpringLayout.EAST, apptNotesL, labelsEast,
                                SpringLayout.WEST, apptDetailPanel);
        l.putConstraint(SpringLayout.EAST, apptStatusNotesL,labelsEast, 
        						SpringLayout.WEST, apptDetailPanel);

        // align second col
        l.putConstraint(SpringLayout.WEST, cancelApptIdTF, strut,
		                        SpringLayout.EAST,
		                        apptIdL);
        l.putConstraint(SpringLayout.WEST, cancelApptStatusTF, strut,
                                SpringLayout.EAST,
                                apptStatusL);
        l.putConstraint(SpringLayout.WEST, cancelApptCanceledByTF, strut,
				                SpringLayout.EAST,
				                apptCanceledByL);
        l.putConstraint(SpringLayout.WEST, cancelByPatientPanel, strut,
				                SpringLayout.EAST,
				                apptCancelByPatientL);
        l.putConstraint(SpringLayout.WEST, cancelByClinicPanel, strut,
				                SpringLayout.EAST,
				                apptCancelByClinicL);
        l.putConstraint(SpringLayout.WEST, cancelWnTwentyFourHrsPanel, strut,
				                SpringLayout.EAST,
				                apptWnTwentyFourHrsL);
        l.putConstraint(SpringLayout.WEST, cancelReasonCB, strut,
				                SpringLayout.EAST,
				                apptCancelReasonL);        
        l.putConstraint(SpringLayout.WEST, cancelOtherReasonTF, strut,
				                SpringLayout.EAST,
				                apptCancelOtherReasonL);                
        l.putConstraint(SpringLayout.WEST, cancelApptDateTF, strut,
                                SpringLayout.EAST,
                                apptDateL);
        l.putConstraint(SpringLayout.WEST, cancelApptTimeTF, strut,
                                SpringLayout.EAST,
                                apptTimeL);
        l.putConstraint(SpringLayout.WEST, cancelApptProvTF, strut,
                                SpringLayout.EAST,
                                apptProvL);
        l.putConstraint(SpringLayout.WEST, cancelApptNtsTF, strut,
                                SpringLayout.EAST,
                                apptNtsL);
        l.putConstraint(SpringLayout.WEST, cancelApptCrTF, strut,
                                SpringLayout.EAST,
                                apptCrL);
        l.putConstraint(SpringLayout.WEST, cancelApptEligTF, strut,
				                SpringLayout.EAST,
				                apptEligL);
        l.putConstraint(SpringLayout.WEST, cancelApptWalkInTF, strut,
				                SpringLayout.EAST,
				                apptWalkInL);                
        l.putConstraint(SpringLayout.WEST, cancelApptNotesTF, strut,
                                SpringLayout.EAST,
                                apptNotesL);
        l.putConstraint(SpringLayout.WEST, cancelApptStatusNotesTF, strut,
                				SpringLayout.EAST,
            					apptStatusNotesL);


        // align row
        l.putConstraint(SpringLayout.NORTH, apptIdL, strut,
                        SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptIdTF, strut,
                        SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth1 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptIdL),
            l.getConstraint(SpringLayout.SOUTH, cancelApptIdTF)));
        l.putConstraint(SpringLayout.NORTH, apptStatusL, rowNorth1,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptStatusTF, rowNorth1,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptStatusL),
                l.getConstraint(SpringLayout.SOUTH, cancelApptStatusTF)));
            l.putConstraint(SpringLayout.NORTH, apptCanceledByL, rowNorth2,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, cancelApptCanceledByTF, rowNorth2,
                                    SpringLayout.NORTH, apptDetailPanel);
        
        Spring rowNorth3 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptCanceledByL),
                l.getConstraint(SpringLayout.SOUTH, cancelApptCanceledByTF)));
            l.putConstraint(SpringLayout.NORTH, apptCancelByPatientL, rowNorth3,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, cancelByPatientPanel, rowNorth3,
                                    SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth4 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptCancelByPatientL),
                l.getConstraint(SpringLayout.SOUTH, cancelByPatientPanel)));
            l.putConstraint(SpringLayout.NORTH, apptCancelByClinicL, rowNorth4,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, cancelByClinicPanel, rowNorth4,
                                    SpringLayout.NORTH, apptDetailPanel);

	    Spring rowNorth5 = Spring.sum(strut, Spring.max(
	            l.getConstraint(SpringLayout.SOUTH, apptCancelByClinicL),
	            l.getConstraint(SpringLayout.SOUTH, cancelByClinicPanel)));
	        l.putConstraint(SpringLayout.NORTH, apptWnTwentyFourHrsL, rowNorth5,
	                                SpringLayout.NORTH, apptDetailPanel);
	        l.putConstraint(SpringLayout.NORTH, cancelWnTwentyFourHrsPanel, rowNorth5,
	                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth6 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptWnTwentyFourHrsL),
                l.getConstraint(SpringLayout.SOUTH, cancelWnTwentyFourHrsPanel)));
            l.putConstraint(SpringLayout.NORTH, apptCancelReasonL, rowNorth6,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, cancelReasonCB, rowNorth6,
                                    SpringLayout.NORTH, apptDetailPanel);
            
        Spring rowNorth7 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptCancelReasonL),
                l.getConstraint(SpringLayout.SOUTH, cancelReasonCB)));
            l.putConstraint(SpringLayout.NORTH, apptCancelOtherReasonL, rowNorth7,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, cancelOtherReasonTF, rowNorth7,
                                    SpringLayout.NORTH, apptDetailPanel);
	        
        Spring rowNorth8 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptCancelOtherReasonL),
            l.getConstraint(SpringLayout.SOUTH, cancelOtherReasonTF)));
        l.putConstraint(SpringLayout.NORTH, apptDateL, rowNorth8,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptDateTF, rowNorth8,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth9 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptDateL),
            l.getConstraint(SpringLayout.SOUTH, cancelApptDateTF)));
        l.putConstraint(SpringLayout.NORTH, apptTimeL, rowNorth9,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptTimeTF, rowNorth9,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth10 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptTimeL),
            l.getConstraint(SpringLayout.SOUTH, cancelApptTimeTF)));
        l.putConstraint(SpringLayout.NORTH, apptProvL, rowNorth10,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptProvTF, rowNorth10,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth11 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptProvL),
            l.getConstraint(SpringLayout.SOUTH, cancelApptProvTF)));
        l.putConstraint(SpringLayout.NORTH, apptNtsL, rowNorth11,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptNtsTF, rowNorth11,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth12 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptNtsL),
            l.getConstraint(SpringLayout.SOUTH, cancelApptNtsTF)));
        l.putConstraint(SpringLayout.NORTH, apptCrL, rowNorth12,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptCrTF, rowNorth12,
                                SpringLayout.NORTH, apptDetailPanel);
        
        Spring rowNorth13 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptCrL),
                l.getConstraint(SpringLayout.SOUTH, cancelApptCrTF)));                
            l.putConstraint(SpringLayout.NORTH, apptEligL, rowNorth13,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, cancelApptEligTF, rowNorth13,
                                    SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth14 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptEligL),
                l.getConstraint(SpringLayout.SOUTH, cancelApptEligTF)));                
            l.putConstraint(SpringLayout.NORTH, apptWalkInL, rowNorth14,
                                    SpringLayout.NORTH, apptDetailPanel);
            l.putConstraint(SpringLayout.NORTH, cancelApptWalkInTF, rowNorth14,
                                    SpringLayout.NORTH, apptDetailPanel);
            
        Spring rowNorth15 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptWalkInL),
            l.getConstraint(SpringLayout.SOUTH, cancelApptWalkInTF)));                
        l.putConstraint(SpringLayout.NORTH, apptNotesL, rowNorth15,
                                SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptNotesTF, rowNorth15,
                                SpringLayout.NORTH, apptDetailPanel);

        Spring rowNorth16 = Spring.sum(strut, Spring.max(
            	l.getConstraint(SpringLayout.SOUTH, apptNotesL),
            	l.getConstraint(SpringLayout.SOUTH, cancelApptNotesTF)));
        l.putConstraint(SpringLayout.NORTH, apptStatusNotesL, rowNorth16,
                				SpringLayout.NORTH, apptDetailPanel);
        l.putConstraint(SpringLayout.NORTH, cancelApptStatusNotesTF, rowNorth16,
                				SpringLayout.NORTH, apptDetailPanel);

        return apptDetailPanel;		
	}
	
    private JPanel createMedPanel(Patient patFull) {
        // 14 fields
        JLabel medL = new JLabel("Medical History Information: ");
        JLabel prevPsyL = new JLabel("Previous psychiatrist: ");
        JLabel lastSeenL = new JLabel("Last seen: ");
        JLabel currMedL = new JLabel("Current medication: ");
        JLabel daysLeftL = new JLabel("How many days left: ");
        JLabel prevMedL = new JLabel("Previous medication: ");
        JLabel prevDxL = new JLabel("Previous diagnose: ");
        JLabel presProbL = new JLabel("Presenting problem: ");
        JLabel nmmsL = new JLabel("Need medical mgnt service: ");
        JLabel ntsL = new JLabel("Need therapy service: ");
        JLabel isUrgentL = new JLabel("Is urgent: ");
        JLabel clinicAcceptedL = new JLabel("Clinic accepted: ");
        JLabel commentsL = new JLabel("Comments: ");
        JLabel blankL = new JLabel(" ");

        JPanel medPanel = new JPanel(new SpringLayout());

        SpringLayout l = new SpringLayout();
        medPanel.setLayout(l);

        JLabel prevPsyTF = new JLabel(patFull.previousPsychiatrist);
        JLabel lastSeenTF = new JLabel(patFull.getLastSeen());
        JLabel currMedTF = new JLabel(patFull.currentMedications);
        JLabel daysLeftTF = new JLabel(patFull.getDaysLeft());
        JLabel prevMedTF = new JLabel(patFull.previousMedications);
        JLabel prevDxTF = new JLabel(patFull.previousMedications);
        JTextArea presProbTF = new JTextArea(patFull.presentingProblem, 10, 45);
        presProbTF.setEditable(false);
        presProbTF.setLineWrap(true);
        presProbTF.setWrapStyleWord(true);
        JLabel nmmsTF = new JLabel(patFull.getNeedMedMgntSvc());
        JLabel ntsTF = new JLabel(patFull.getNeedTherapy());
        JLabel isUrgentTF = new JLabel(patFull.isUrgent);
        JLabel clinicAcceptedTF = new JLabel(patFull.clinic);
        JTextArea commentsTF = new JTextArea(patFull.comments, 5, 45);
        commentsTF.setEditable(false);
        commentsTF.setLineWrap(true);     
        commentsTF.setWrapStyleWord(true);

        medPanel.add(medL);
        medPanel.add(blankL);
        medPanel.add(prevPsyL);
        medPanel.add(prevPsyTF);
        medPanel.add(lastSeenL);
        medPanel.add(lastSeenTF);
        medPanel.add(currMedL);
        medPanel.add(currMedTF);
        medPanel.add(daysLeftL);
        medPanel.add(daysLeftTF);
        medPanel.add(prevMedL);
        medPanel.add(prevMedTF);
        medPanel.add(prevDxL);
        medPanel.add(prevDxTF);
        medPanel.add(presProbL);
        medPanel.add(presProbTF);
        medPanel.add(nmmsL);
        medPanel.add(nmmsTF);
        medPanel.add(ntsL);
        medPanel.add(ntsTF);
        medPanel.add(isUrgentL);
        medPanel.add(isUrgentTF);
        medPanel.add(clinicAcceptedL);
        medPanel.add(clinicAcceptedTF);
        medPanel.add(commentsL);
        medPanel.add(commentsTF);

        Spring strut = Spring.constant(10);

        Spring labelsEast = Spring.sum(strut,
                                       l.getConstraints(nmmsL).getWidth());

        // align first col
        l.putConstraint(SpringLayout.EAST, prevPsyL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, lastSeenL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, currMedL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, daysLeftL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, prevMedL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, prevDxL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, presProbL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, nmmsL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, ntsL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, isUrgentL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, clinicAcceptedL, labelsEast,
                                SpringLayout.WEST, medPanel);
        l.putConstraint(SpringLayout.EAST, commentsL, labelsEast,
                                SpringLayout.WEST, medPanel);

        // align second col
        l.putConstraint(SpringLayout.WEST, blankL, strut,
                                SpringLayout.EAST,
                                medL);
        l.putConstraint(SpringLayout.WEST, prevPsyTF, strut,
                                SpringLayout.EAST,
                                prevPsyL);
        l.putConstraint(SpringLayout.WEST, lastSeenTF, strut,
                                SpringLayout.EAST,
                                lastSeenL);
        l.putConstraint(SpringLayout.WEST, currMedTF, strut,
                                SpringLayout.EAST,
                                currMedL);
        l.putConstraint(SpringLayout.WEST, daysLeftTF, strut,
                                SpringLayout.EAST,
                                daysLeftL);
        l.putConstraint(SpringLayout.WEST, prevMedTF, strut,
                                SpringLayout.EAST,
                                prevMedL);
        l.putConstraint(SpringLayout.WEST, prevDxTF, strut,
                                SpringLayout.EAST,
                                prevDxL);
        l.putConstraint(SpringLayout.WEST, presProbTF, strut,
                                SpringLayout.EAST,
                                presProbL);
        l.putConstraint(SpringLayout.WEST, nmmsTF, strut,
                                SpringLayout.EAST,
                                nmmsL);
        l.putConstraint(SpringLayout.WEST, ntsTF, strut,
                                SpringLayout.EAST,
                                ntsL);
        l.putConstraint(SpringLayout.WEST, isUrgentTF, strut,
                                SpringLayout.EAST,
                                isUrgentL);
        l.putConstraint(SpringLayout.WEST, clinicAcceptedTF, strut,
                                SpringLayout.EAST,
                                clinicAcceptedL);
        l.putConstraint(SpringLayout.WEST, commentsTF, strut,
                                SpringLayout.EAST,
                                commentsL);

        // align row
        l.putConstraint(SpringLayout.NORTH, medL, strut,
                                SpringLayout.NORTH,
                                medPanel);
        l.putConstraint(SpringLayout.NORTH, blankL, strut,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth1 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, medL),
            l.getConstraint(SpringLayout.SOUTH, blankL)));
        l.putConstraint(SpringLayout.NORTH, prevPsyL, rowNorth1,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, prevPsyTF, rowNorth1,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, prevPsyL),
            l.getConstraint(SpringLayout.SOUTH, prevPsyTF)));
        l.putConstraint(SpringLayout.NORTH, lastSeenL, rowNorth2,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, lastSeenTF, rowNorth2,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth3 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, lastSeenL),
            l.getConstraint(SpringLayout.SOUTH, lastSeenTF)));
        l.putConstraint(SpringLayout.NORTH, currMedL, rowNorth3,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, currMedTF, rowNorth3,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth4 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, currMedL),
            l.getConstraint(SpringLayout.SOUTH, currMedTF)));
        l.putConstraint(SpringLayout.NORTH, daysLeftL, rowNorth4,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, daysLeftTF, rowNorth4,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth5 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, daysLeftL),
            l.getConstraint(SpringLayout.SOUTH, daysLeftTF)));
        l.putConstraint(SpringLayout.NORTH, prevMedL, rowNorth5,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, prevMedTF, rowNorth5,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth6 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, prevMedL),
            l.getConstraint(SpringLayout.SOUTH, prevMedTF)));
        l.putConstraint(SpringLayout.NORTH, prevDxL, rowNorth6,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, prevDxTF, rowNorth6,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth7 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, prevDxL),
            l.getConstraint(SpringLayout.SOUTH, prevDxTF)));
        l.putConstraint(SpringLayout.NORTH, presProbL, rowNorth7,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, presProbTF, rowNorth7,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth8 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, presProbL),
            l.getConstraint(SpringLayout.SOUTH, presProbTF)));
        l.putConstraint(SpringLayout.NORTH, nmmsL, rowNorth8,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, nmmsTF, rowNorth8,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth9 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, nmmsL),
            l.getConstraint(SpringLayout.SOUTH, nmmsTF)));
        l.putConstraint(SpringLayout.NORTH, ntsL, rowNorth9,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, ntsTF, rowNorth9,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth10 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, ntsL),
            l.getConstraint(SpringLayout.SOUTH, ntsTF)));
        l.putConstraint(SpringLayout.NORTH, isUrgentL, rowNorth10,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, isUrgentTF, rowNorth10,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth11 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, isUrgentL),
            l.getConstraint(SpringLayout.SOUTH, isUrgentTF)));
        l.putConstraint(SpringLayout.NORTH, clinicAcceptedL, rowNorth11,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, clinicAcceptedTF, rowNorth11,
                                SpringLayout.NORTH, medPanel);

        Spring rowNorth12 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, clinicAcceptedL),
            l.getConstraint(SpringLayout.SOUTH, clinicAcceptedTF)));
        l.putConstraint(SpringLayout.NORTH, commentsL, rowNorth12,
                                SpringLayout.NORTH, medPanel);
        l.putConstraint(SpringLayout.NORTH, commentsTF, rowNorth12,
                                SpringLayout.NORTH, medPanel);

        return medPanel;
    }

    private JPanel createGeneralPanel(Patient patFull, PatientForm bean) {
        // 14 fields
        JLabel genL = new JLabel("General Information: ");
        JLabel adL = new JLabel("Referral date: ");
        JLabel fnL = new JLabel("First name: ");
        JLabel miL = new JLabel("Middle initial: ");
        JLabel lnL = new JLabel("Last name: ");
        JLabel genderL = new JLabel("Gender: ");
        JLabel ssnL = new JLabel("Social security number: ");
        JLabel dobL = new JLabel("Date of birth: ");
        JLabel streetL = new JLabel("Street address: ");
        JLabel apartL = new JLabel("Apartment #: ");
        JLabel cityL = new JLabel("City: ");
        JLabel stateL = new JLabel("State: ");
        JLabel zipcodeL = new JLabel("Zipcode: ");
        JLabel pnL = new JLabel("Phone number: ");
        JLabel emailL = new JLabel("Email: ");
        JLabel blankL = new JLabel(" ");

        JPanel generalPanel = new JPanel();
        SpringLayout generalSL = new SpringLayout();
        generalPanel.setLayout(generalSL);

        JLabel adTF = new JLabel(patFull.getCreateDateStr());
        JTextField fnTF = new JTextField(15);
        fnTF.setText(patFull.firstName);
        JTextField miTF = new JTextField(5);
        miTF.setText(patFull.mi);
        JTextField lnTF = new JTextField(15);
        lnTF.setText(patFull.lastName);
        JLabel genderTF = new JLabel(patFull.gender);
        JLabel ssnTF = new JLabel(patFull.ssn);
        JLabel dobTF = new JLabel(patFull.getDateOfBirth());
        JTextField streetTF = new JTextField(20);
        streetTF.setText(patFull.streetAddress);
        JTextField apartTF = new JTextField(20);
        apartTF.setText(patFull.apartmentNumber);
        JTextField cityTF = new JTextField(15);
        cityTF.setText(patFull.city);
        JTextField stateTF = new JTextField(5);
        stateTF.setText(patFull.state);
        JTextField zipTF = new JTextField(5);
        zipTF.setText(patFull.zipCode);
        JTextField pnTF = new JTextField(10);
        pnTF.setText(patFull.phoneNumber);
        JTextField emailTF = new JTextField(20);
        emailTF.setText(patFull.email);

        bean.fnTF = fnTF;
        bean.miTF = miTF;
        bean.lnTF = lnTF;
        bean.streetTF = streetTF;
        bean.apartTF = apartTF;
        bean.cityTF = cityTF;
        bean.stateTF = stateTF;
        bean.zipTF = zipTF;
        bean.pnTF = pnTF;
        bean.emailTF = emailTF;

        generalPanel.add(genL);
        generalPanel.add(blankL);
        generalPanel.add(adL);
        generalPanel.add(adTF);
        generalPanel.add(fnL);
        generalPanel.add(fnTF);
        generalPanel.add(miL);
        generalPanel.add(miTF);
        generalPanel.add(lnL);
        generalPanel.add(lnTF);
        generalPanel.add(genderL);
        generalPanel.add(genderTF);
        generalPanel.add(ssnL);
        generalPanel.add(ssnTF);
        generalPanel.add(dobL);
        generalPanel.add(dobTF);
        generalPanel.add(streetL);
        generalPanel.add(streetTF);
        generalPanel.add(apartL);
        generalPanel.add(apartTF);
        generalPanel.add(cityL);
        generalPanel.add(cityTF);
        generalPanel.add(stateL);
        generalPanel.add(stateTF);
        generalPanel.add(zipcodeL);
        generalPanel.add(zipTF);
        generalPanel.add(pnL);
        generalPanel.add(pnTF);
        generalPanel.add(emailL);
        generalPanel.add(emailTF);

        Spring strut = Spring.constant(10);

        Spring labelsEast = Spring.sum(strut,
                                       generalSL.getConstraints(ssnL).getWidth());

        // align first col
        generalSL.putConstraint(SpringLayout.EAST, adL, labelsEast,
                				SpringLayout.WEST, generalPanel);        
        generalSL.putConstraint(SpringLayout.EAST, fnL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, miL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, lnL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, genderL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, ssnL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, dobL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, streetL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, apartL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, cityL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, stateL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, zipcodeL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, pnL, labelsEast,
                                SpringLayout.WEST, generalPanel);
        generalSL.putConstraint(SpringLayout.EAST, emailL, labelsEast,
                                SpringLayout.WEST, generalPanel);

        // align second col
        generalSL.putConstraint(SpringLayout.WEST, blankL, strut,
                                SpringLayout.EAST,
                                genL);
        generalSL.putConstraint(SpringLayout.WEST, adTF, strut,
				                SpringLayout.EAST,
				                dobL);                
        generalSL.putConstraint(SpringLayout.WEST, fnTF, strut,
                                SpringLayout.EAST,
                                fnL);
        generalSL.putConstraint(SpringLayout.WEST, miTF, strut,
                                SpringLayout.EAST,
                                miL);
        generalSL.putConstraint(SpringLayout.WEST, lnTF, strut,
                                SpringLayout.EAST,
                                lnL);
        generalSL.putConstraint(SpringLayout.WEST, genderTF, strut,
                                SpringLayout.EAST,
                                genderL);
        generalSL.putConstraint(SpringLayout.WEST, ssnTF, strut,
                                SpringLayout.EAST,
                                ssnL);
        generalSL.putConstraint(SpringLayout.WEST, dobTF, strut,
                                SpringLayout.EAST,
                                dobL);
        generalSL.putConstraint(SpringLayout.WEST, streetTF, strut,
                                SpringLayout.EAST,
                                streetL);
        generalSL.putConstraint(SpringLayout.WEST, apartTF, strut,
                                SpringLayout.EAST,
                                apartL);
        generalSL.putConstraint(SpringLayout.WEST, cityTF, strut,
                                SpringLayout.EAST,
                                cityL);
        generalSL.putConstraint(SpringLayout.WEST, stateTF, strut,
                                SpringLayout.EAST,
                                stateL);
        generalSL.putConstraint(SpringLayout.WEST, zipTF, strut,
                                SpringLayout.EAST,
                                zipcodeL);
        generalSL.putConstraint(SpringLayout.WEST, pnTF, strut,
                                SpringLayout.EAST,
                                pnL);
        generalSL.putConstraint(SpringLayout.WEST, emailTF, strut,
                                SpringLayout.EAST,
                                emailL);

        // align row
        generalSL.putConstraint(SpringLayout.NORTH, genL, strut,
                                SpringLayout.NORTH,
                                generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, blankL, strut,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth0 = Spring.sum(strut, Spring.max(
                generalSL.getConstraint(SpringLayout.SOUTH, genL),
                generalSL.getConstraint(SpringLayout.SOUTH, blankL)));
            generalSL.putConstraint(SpringLayout.NORTH, adL, rowNorth0,
                                    SpringLayout.NORTH, generalPanel);
            generalSL.putConstraint(SpringLayout.NORTH, adTF, rowNorth0,
                                    SpringLayout.NORTH, generalPanel);
        
        Spring rowNorth1 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, adL),
            generalSL.getConstraint(SpringLayout.SOUTH, adTF)));
        generalSL.putConstraint(SpringLayout.NORTH, fnL, rowNorth1,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, fnTF, rowNorth1,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, fnL),
            generalSL.getConstraint(SpringLayout.SOUTH, fnTF)));
        generalSL.putConstraint(SpringLayout.NORTH, miL, rowNorth2,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, miTF, rowNorth2,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth3 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, miL),
            generalSL.getConstraint(SpringLayout.SOUTH, miTF)));
        generalSL.putConstraint(SpringLayout.NORTH, lnL, rowNorth3,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, lnTF, rowNorth3,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth4 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, lnL),
            generalSL.getConstraint(SpringLayout.SOUTH, lnTF)));
        generalSL.putConstraint(SpringLayout.NORTH, genderL, rowNorth4,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, genderTF, rowNorth4,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth5 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, genderL),
            generalSL.getConstraint(SpringLayout.SOUTH, genderTF)));
        generalSL.putConstraint(SpringLayout.NORTH, ssnL, rowNorth5,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, ssnTF, rowNorth5,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth6 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, ssnL),
            generalSL.getConstraint(SpringLayout.SOUTH, ssnTF)));
        generalSL.putConstraint(SpringLayout.NORTH, dobL, rowNorth6,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, dobTF, rowNorth6,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth7 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, dobL),
            generalSL.getConstraint(SpringLayout.SOUTH, dobTF)));
        generalSL.putConstraint(SpringLayout.NORTH, streetL, rowNorth7,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, streetTF, rowNorth7,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth8 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, streetL),
            generalSL.getConstraint(SpringLayout.SOUTH, streetTF)));
        generalSL.putConstraint(SpringLayout.NORTH, apartL, rowNorth8,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, apartTF, rowNorth8,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth9 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, apartL),
            generalSL.getConstraint(SpringLayout.SOUTH, apartTF)));
        generalSL.putConstraint(SpringLayout.NORTH, cityL, rowNorth9,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, cityTF, rowNorth9,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth10 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, cityL),
            generalSL.getConstraint(SpringLayout.SOUTH, cityTF)));
        generalSL.putConstraint(SpringLayout.NORTH, stateL, rowNorth10,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, stateTF, rowNorth10,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth11 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, stateL),
            generalSL.getConstraint(SpringLayout.SOUTH, stateTF)));
        generalSL.putConstraint(SpringLayout.NORTH, zipcodeL, rowNorth11,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, zipTF, rowNorth11,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth12 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, zipcodeL),
            generalSL.getConstraint(SpringLayout.SOUTH, zipTF)));
        generalSL.putConstraint(SpringLayout.NORTH, pnL, rowNorth12,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, pnTF, rowNorth12,
                                SpringLayout.NORTH, generalPanel);

        Spring rowNorth13 = Spring.sum(strut, Spring.max(
            generalSL.getConstraint(SpringLayout.SOUTH, pnL),
            generalSL.getConstraint(SpringLayout.SOUTH, pnTF)));
        generalSL.putConstraint(SpringLayout.NORTH, emailL, rowNorth13,
                                SpringLayout.NORTH, generalPanel);
        generalSL.putConstraint(SpringLayout.NORTH, emailTF, rowNorth13,
                                SpringLayout.NORTH, generalPanel);

        return generalPanel;
    }

    private JPanel createLgPanel(Patient patFull, PatientForm bean) {
        // 5 fields
        JLabel lgL = new JLabel("Legal Gardian Information: ");
        JLabel lgfnL = new JLabel("First name: ");
        JLabel lgmiL = new JLabel("Middle initial: ");
        JLabel lglnL = new JLabel("Last name: ");
        JLabel lgpnL = new JLabel("Phone number: ");
        JLabel blankL = new JLabel(" ");

        JPanel lgPanel = new JPanel(new SpringLayout());
        SpringLayout l = new SpringLayout();
        lgPanel.setLayout(l);

        JTextField lgFnTF = new JTextField(10);
        lgFnTF.setText(patFull.legalGardianFirstName);
        JTextField lgMiTF = new JTextField(5);
        lgMiTF.setText(patFull.legalGardianMiddleInitial);
        JTextField lgLnTF = new JTextField(15);
        lgLnTF.setText(patFull.legalGardianLastName);
        JTextField lgPnTF = new JTextField(10);
        lgPnTF.setText(patFull.legalGardianPhoneNumber);

        bean.lgFnTF = lgFnTF;
        bean.lgMiTF = lgMiTF;
        bean.lgLnTF = lgLnTF;
        bean.lgPnTF = lgPnTF;

        lgPanel.add(lgL);
        lgPanel.add(blankL);
        lgPanel.add(lgfnL);
        lgPanel.add(lgFnTF);
        lgPanel.add(lgmiL);
        lgPanel.add(lgMiTF);
        lgPanel.add(lglnL);
        lgPanel.add(lgLnTF);
        lgPanel.add(lgpnL);
        lgPanel.add(lgPnTF);

        Spring strut = Spring.constant(10);
        Spring labelsEast = Spring.sum(strut, l.getConstraints(lgpnL).getWidth());

        // align first col
        l.putConstraint(SpringLayout.EAST, lgfnL, labelsEast,
                        SpringLayout.WEST, lgPanel);
        l.putConstraint(SpringLayout.EAST, lgmiL, labelsEast,
                        SpringLayout.WEST, lgPanel);
        l.putConstraint(SpringLayout.EAST, lglnL, labelsEast,
                        SpringLayout.WEST, lgPanel);
        l.putConstraint(SpringLayout.EAST, lgpnL, labelsEast,
                        SpringLayout.WEST, lgPanel);

        // align second col
        l.putConstraint(SpringLayout.WEST, lgFnTF, strut,
                        SpringLayout.EAST,
                        lgfnL);
        l.putConstraint(SpringLayout.WEST, lgMiTF, strut,
                        SpringLayout.EAST,
                        lgmiL);
        l.putConstraint(SpringLayout.WEST, lgLnTF, strut,
                        SpringLayout.EAST,
                        lglnL);
        l.putConstraint(SpringLayout.WEST, lgPnTF, strut,
                        SpringLayout.EAST,
                        lgpnL);

        // align row
        l.putConstraint(SpringLayout.NORTH, lgL, strut,
                        SpringLayout.NORTH,
                        lgPanel);
        l.putConstraint(SpringLayout.NORTH, blankL, strut,
                        SpringLayout.NORTH, lgPanel);

        Spring rowNorth1 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, lgL),
            l.getConstraint(SpringLayout.SOUTH, blankL)));
        l.putConstraint(SpringLayout.NORTH, lgfnL, rowNorth1,
                        SpringLayout.NORTH, lgPanel);
        l.putConstraint(SpringLayout.NORTH, lgFnTF, rowNorth1,
                        SpringLayout.NORTH, lgPanel);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, lgfnL),
            l.getConstraint(SpringLayout.SOUTH, lgFnTF)));
        l.putConstraint(SpringLayout.NORTH, lgmiL, rowNorth2,
                        SpringLayout.NORTH, lgPanel);
        l.putConstraint(SpringLayout.NORTH, lgMiTF, rowNorth2,
                        SpringLayout.NORTH, lgPanel);

        Spring rowNorth3 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, lgmiL),
            l.getConstraint(SpringLayout.SOUTH, lgMiTF)));
        l.putConstraint(SpringLayout.NORTH, lglnL, rowNorth3,
                        SpringLayout.NORTH, lgPanel);
        l.putConstraint(SpringLayout.NORTH, lgLnTF, rowNorth3,
                        SpringLayout.NORTH, lgPanel);

        Spring rowNorth4 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, lglnL),
            l.getConstraint(SpringLayout.SOUTH, lgLnTF)));
        l.putConstraint(SpringLayout.NORTH, lgpnL, rowNorth4,
                        SpringLayout.NORTH, lgPanel);
        l.putConstraint(SpringLayout.NORTH, lgPnTF, rowNorth4,
                        SpringLayout.NORTH, lgPanel);

        return lgPanel;
    }

    private JPanel createInsPanel(Patient patFull, PatientForm bean) {
    	JPanel insPanel = new JPanel(new BorderLayout(15,15));

    	// Table Summary
    	insTableModel = new InsTableModel(patFull.getIns());
    	insTable = new JTable(insTableModel);
    	insTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	insTable.getColumn(InsTableModel.ID_COL).setPreferredWidth(50);
    	insTable.getColumn(InsTableModel.INS_COMPANY_COL).setPreferredWidth(180);
    	insTable.getColumn(InsTableModel.MEMBERID_COL).setPreferredWidth(80);
    	insTable.getColumn(InsTableModel.COPAY_COL).setPreferredWidth(50);
    	insTable.getColumn(InsTableModel.COPAY_PARITY_COL).setPreferredWidth(80);
    	insTable.getColumn(InsTableModel.PHONE_NUM_COL).setPreferredWidth(100);
    	insTable.getColumn(InsTableModel.AN_COL).setPreferredWidth(100);
    	insTable.getColumn(InsTableModel.NAV_COL).setPreferredWidth(100);
    	insTable.getColumn(InsTableModel.NAV_PARITY_COL).setPreferredWidth(130);
    	insTable.getColumn(InsTableModel.MEDI_CAL_ID_COL).setPreferredWidth(80);
    	insTable.getColumn(InsTableModel.MEDI_CAL_ISSUE_DATE_COL).setPreferredWidth(100);
    	insTable.setAutoCreateRowSorter(true);
    	
    	insTable.getSelectionModel().addListSelectionListener(
    			new ListSelectionListener() {
    				  public void valueChanged(ListSelectionEvent e){
    					  if (e.getValueIsAdjusting()){
    						  return;
    					  }
    					  ListSelectionModel lsm = (ListSelectionModel)e.getSource();
    					  if (lsm.isSelectionEmpty()){
    						  insDetailPanel.setVisible(false);
    						  deleteInsButton.setEnabled(false);
    						  saveInsButton.setEnabled(false);
    					  }else{
    			    	    	// set the detail panel
    			    	    	int viewRowIndex = insTable.getSelectedRow();
    			    	    	int selectedRow = insTable.convertRowIndexToModel(viewRowIndex);
    			    	    	if (selectedRow >= 0){
    			    	    		insId = Integer.parseInt((String)insTableModel.getValueAt(selectedRow, 0));
    			    	    		
    			    	    		insAdmitDateTF.setText((String)insTableModel.getValueAt(selectedRow, 1));
    			    	    		if (insAdmitDateTF.getText() == null || insAdmitDateTF.getText().trim().equals("")){
    			    	    			insAdmitDateTF.setEnabled(true);
    			    	    		}else{
    			    	    			insAdmitDateTF.setEnabled(false);
    			    	    		}
    			    	    		
    			    	    		esdTF.setText((String)insTableModel.getValueAt(selectedRow, 2));
    			    	    		esdTF.setEnabled(true);
    			    	    		
    			    	    		eedTF.setText((String)insTableModel.getValueAt(selectedRow, 3));
    			    	    		eedTF.setEnabled(true);
    			    	    		
    			    	    		setSelectedInsCompany((String)insTableModel.getValueAt(selectedRow, 4));
    			    	    		if (insId == -1){
    			    	    			insCompanyCB.setEnabled(true);
    			    	    		}else{
    			    	    			insCompanyCB.setEnabled(false);
    			    	    		}
    			    	    		
    			    	    		insPnTF.setText((String)insTableModel.getValueAt(selectedRow, 5));
    			    	    		insPnTF.setEnabled(true);
    			    	    		
    			    	    		memberIdTF.setText((String)insTableModel.getValueAt(selectedRow, 6));
    			    	    		memberIdTF.setEnabled(true);
    			    	    		
    			    	    		copayTF.setText((String)insTableModel.getValueAt(selectedRow, 7));
    			    	    		copayTF.setEnabled(true);
    			    	    		
    			    	    		copayParityTF.setText((String)insTableModel.getValueAt(selectedRow, 8));
    			    	    		copayParityTF.setEnabled(true);

    			    	    		navTF.setText((String)insTableModel.getValueAt(selectedRow, 9));
    			    	    		navTF.setEnabled(true);
    			    	    		
    			    	    		anTF.setText((String)insTableModel.getValueAt(selectedRow, 10));
    			    	    		anTF.setEnabled(true);
    			    	    		
    			    	    		navParityTF.setText((String)insTableModel.getValueAt(selectedRow, 11));
    			    	    		navParityTF.setEnabled(true);

    			    	    		anParityTF.setText((String)insTableModel.getValueAt(selectedRow, 12));
    			    	    		anParityTF.setEnabled(true);
    			    	    		
    			    	    		medicalIdTF.setText((String)insTableModel.getValueAt(selectedRow, 13));
    			    	    		medicalIdTF.setEnabled(true);
    			    	    		
    			    	    		medIssueDateTF.setText((String)insTableModel.getValueAt(selectedRow, 14));
    			    	    		medIssueDateTF.setEnabled(true);
    			    	    		
    			    	    		notesTA.setText((String)insTableModel.getValueAt(selectedRow, 15));
    			    	    		notesTA.setEnabled(true);
    			    	    		
    			    	    		addInsButton.setEnabled(true);
    			    	    		saveInsButton.setEnabled(false);
    			    	    		deleteInsButton.setEnabled(true);    			    	    		
    			    	    		insDetailPanel.setVisible(true);    
    			    	    	}
    					  }
    				  }
    			}
    	);

    	JScrollPane tableScroller = new JScrollPane(insTable);
    	tableScroller.setPreferredSize(new Dimension(570, 200));
    	    	    	
        // Detail Panel
    	insDetailPanel = createInsDetailPanel(patFull, bean);
    	insDetailPanel.setVisible(false);
    	
        // Action Panel
        JPanel actionPanel = createInsActionPanel();
        
        insPanel.add(tableScroller, BorderLayout.NORTH);
        insPanel.add(insDetailPanel, BorderLayout.CENTER);
        insPanel.add(actionPanel, BorderLayout.SOUTH);
        return insPanel;
    }
    
    private void enableAllTF(){
    	insAdmitDateTF.setEnabled(true);
    	esdTF.setEnabled(true);
    	eedTF.setEnabled(true);    	
		memberIdTF.setEnabled(true);
		copayTF.setEnabled(true);
		copayParityTF.setEnabled(true);	
		insPnTF.setEnabled(true);
		anTF.setEnabled(true);
		anParityTF.setEnabled(true);
		navTF.setEnabled(true);
		navParityTF.setEnabled(true);
		medicalIdTF.setEnabled(true);
		medIssueDateTF.setEnabled(true);
		notesTA.setEnabled(true);
    }
    
    private void clearAllTF(String insCompName){
    	insAdmitDateTF.setText("");
    	esdTF.setText("");
    	eedTF.setText("");
    	insCompanyCB.setSelectedItem(insCompName);
    	memberIdTF.setText("");
    	copayTF.setText("");
		copayParityTF.setText("");	
		insPnTF.setText("");
		navTF.setText("");		
		anTF.setText("");		
		navParityTF.setText("");
		anParityTF.setText("");
		medicalIdTF.setText("");
		medIssueDateTF.setText("");
		notesTA.setText("");
    }
    
    private JPanel createInsDetailPanel(Patient patFull, PatientForm bean){ 
        JPanel detailPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        detailPanel.setLayout(layout);

        initInsDetailPanelComponent();

        detailPanel.add(insAdmitDateL);
        detailPanel.add(insAdmitDateTF);
        detailPanel.add(insAdmitDateFormatL);        
        detailPanel.add(esdL);
        detailPanel.add(esdTF);
        detailPanel.add(esdFormatL);
        detailPanel.add(eedL); 
        detailPanel.add(eedTF);
        detailPanel.add(eedFormatL);
        detailPanel.add(insProviderL);
        detailPanel.add(insCompanyCB);  
        detailPanel.add(insPnL);
        detailPanel.add(insPnTF);
        detailPanel.add(pspL);
        detailPanel.add(memberIdTF);
        detailPanel.add(copayParityL);
        detailPanel.add(copayParityTF);        
        detailPanel.add(copayL);
        detailPanel.add(copayTF);
        detailPanel.add(anForMDL);
        detailPanel.add(anTF);
        detailPanel.add(navForMDL);
        detailPanel.add(navTF);        
        detailPanel.add(anForMAL);
        detailPanel.add(anParityTF);
        detailPanel.add(navForMAL);
        detailPanel.add(navParityTF);
        detailPanel.add(medicalIdL);
        detailPanel.add(medicalIdTF);
        detailPanel.add(medIssueDateL);
        detailPanel.add(medIssueDateTF);
        detailPanel.add(medIssueDateFormatL);
        detailPanel.add(notesL);
        notesSP = new JScrollPane(notesTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailPanel.add(notesSP);
       
        Spring leftmostStrut = Spring.sum(mediumStrut, layout.getConstraints(pspL).getWidth());
        
        alignRow1(detailPanel, layout, leftmostStrut);
        alignRow2(detailPanel, layout, leftmostStrut);                
        alignRow3(detailPanel, layout, leftmostStrut);
        alignRow4(detailPanel, layout, leftmostStrut);
        alignRow5(detailPanel, layout, leftmostStrut);
        alignRow6(detailPanel, layout, leftmostStrut);
        alignRow7(detailPanel, layout, leftmostStrut);
        alignRow8(detailPanel, layout, leftmostStrut);
        alignRow9(detailPanel, layout, leftmostStrut);
        alignRow10(detailPanel, layout, leftmostStrut);
        alignRow11(detailPanel, layout, leftmostStrut);
        alignRow12(detailPanel, layout, leftmostStrut);
                                        
        return detailPanel;
    }

    private void alignRow1(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, insAdmitDateL, leftmostStrut,
				 SpringLayout.WEST, detailPanel);        
    	layout.putConstraint(SpringLayout.WEST, insAdmitDateTF, shortStrut,
				 SpringLayout.EAST, insAdmitDateL);
    	layout.putConstraint(SpringLayout.WEST, insAdmitDateFormatL, shortStrut,
				 SpringLayout.EAST, insAdmitDateTF);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, insAdmitDateL, mediumStrut,
                SpringLayout.NORTH, detailPanel);
        layout.putConstraint(SpringLayout.NORTH, insAdmitDateTF, mediumStrut,
                SpringLayout.NORTH, detailPanel);
        layout.putConstraint(SpringLayout.NORTH, insAdmitDateFormatL, mediumStrut,
                SpringLayout.NORTH, detailPanel);
    }
    
    private void alignRow2(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, esdL, leftmostStrut,
				 SpringLayout.WEST, detailPanel);        
    	layout.putConstraint(SpringLayout.WEST, esdTF, shortStrut,
				 SpringLayout.EAST, esdL);
    	layout.putConstraint(SpringLayout.WEST, esdFormatL, shortStrut,
				 SpringLayout.EAST, esdTF);
    	layout.putConstraint(SpringLayout.WEST, eedL, longStrut,
				 SpringLayout.EAST, esdFormatL);
    	layout.putConstraint(SpringLayout.WEST, eedTF, shortStrut,
				 SpringLayout.EAST, eedL);
    	layout.putConstraint(SpringLayout.WEST, eedFormatL, shortStrut,
				 SpringLayout.EAST, eedTF);    	
    	// align row
        layout.putConstraint(SpringLayout.NORTH, esdL, mediumStrut,
                SpringLayout.SOUTH, insAdmitDateTF);
        layout.putConstraint(SpringLayout.NORTH, esdTF, mediumStrut,
                SpringLayout.SOUTH, insAdmitDateTF);
        layout.putConstraint(SpringLayout.NORTH, esdFormatL, mediumStrut,
                SpringLayout.SOUTH, insAdmitDateTF);
        layout.putConstraint(SpringLayout.NORTH, eedL, mediumStrut,
                SpringLayout.SOUTH, insAdmitDateTF);
        layout.putConstraint(SpringLayout.NORTH, eedTF, mediumStrut,
                SpringLayout.SOUTH, insAdmitDateTF);    	
        layout.putConstraint(SpringLayout.NORTH, eedFormatL, mediumStrut,
                SpringLayout.SOUTH, insAdmitDateTF);    	        
    }
    
    private void alignRow3(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
        layout.putConstraint(SpringLayout.EAST, insProviderL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
        layout.putConstraint(SpringLayout.WEST, insCompanyCB, shortStrut,
                SpringLayout.EAST, insProviderL);
        // align row        
        layout.putConstraint(SpringLayout.NORTH, insProviderL, mediumStrut,
                SpringLayout.SOUTH, esdTF);
        layout.putConstraint(SpringLayout.NORTH, insCompanyCB, mediumStrut,
                SpringLayout.SOUTH, esdTF);    	
    }
    
    private void alignRow4(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
        layout.putConstraint(SpringLayout.EAST, insPnL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
        layout.putConstraint(SpringLayout.WEST, insPnTF, shortStrut,
                SpringLayout.EAST, insPnL);
        // align row
        layout.putConstraint(SpringLayout.NORTH, insPnL, mediumStrut,
                SpringLayout.SOUTH, insCompanyCB);
        layout.putConstraint(SpringLayout.NORTH, insPnTF, mediumStrut,
                SpringLayout.SOUTH, insCompanyCB);    	            	
    }

    private void alignRow5(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {   
    	// align col
        layout.putConstraint(SpringLayout.EAST, pspL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
        layout.putConstraint(SpringLayout.WEST, memberIdTF, shortStrut,
                SpringLayout.EAST, pspL);
        // align row
        layout.putConstraint(SpringLayout.NORTH, pspL, mediumStrut,
                SpringLayout.SOUTH, insPnTF);
        layout.putConstraint(SpringLayout.NORTH, memberIdTF, mediumStrut,
                SpringLayout.SOUTH, insPnTF);    	            	                
    }

    private void alignRow6(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, copayParityL, leftmostStrut, 
				SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, copayParityTF, shortStrut,
        		SpringLayout.EAST, copayParityL);        
    	// align row
        layout.putConstraint(SpringLayout.NORTH, copayParityL, mediumStrut,
                SpringLayout.SOUTH, memberIdTF);
        layout.putConstraint(SpringLayout.NORTH, copayParityTF, mediumStrut,
                SpringLayout.SOUTH, memberIdTF);    	            	                    	    	
    }
    
    private void alignRow7(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, copayL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, copayTF, shortStrut,
                SpringLayout.EAST, copayL);    	    
    	// align row
        layout.putConstraint(SpringLayout.NORTH, copayL, mediumStrut,
                SpringLayout.SOUTH, copayParityTF);
        layout.putConstraint(SpringLayout.NORTH, copayTF, mediumStrut,
                SpringLayout.SOUTH, copayParityTF);    	            	                    	
    }

    private void alignRow8(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, navForMDL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, navTF, shortStrut,
                SpringLayout.EAST, navForMDL);
    	layout.putConstraint(SpringLayout.WEST, anForMDL, longStrut,
                SpringLayout.EAST, navTF);
    	layout.putConstraint(SpringLayout.WEST, anTF, shortStrut,
                SpringLayout.EAST, anForMDL);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, navForMDL, mediumStrut,
                SpringLayout.SOUTH, copayTF);
        layout.putConstraint(SpringLayout.NORTH, navTF, mediumStrut,
                SpringLayout.SOUTH, copayTF);    	            	                    	    	
        layout.putConstraint(SpringLayout.NORTH, anForMDL, mediumStrut,
                SpringLayout.SOUTH, copayTF);
        layout.putConstraint(SpringLayout.NORTH, anTF, mediumStrut,
                SpringLayout.SOUTH, copayTF);    	            	                    	    	    	    	
    }

    private void alignRow9(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, navForMAL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, navParityTF, shortStrut,
                SpringLayout.EAST, navForMDL);
    	layout.putConstraint(SpringLayout.WEST, anForMAL, longStrut,
                SpringLayout.EAST, navParityTF);
    	layout.putConstraint(SpringLayout.WEST, anParityTF, shortStrut,
                SpringLayout.EAST, anForMAL);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, navForMAL, mediumStrut,
                SpringLayout.SOUTH, navTF);
        layout.putConstraint(SpringLayout.NORTH, navParityTF, mediumStrut,
                SpringLayout.SOUTH, navTF);    	            	                    	    	
        layout.putConstraint(SpringLayout.NORTH, anForMAL, mediumStrut,
                SpringLayout.SOUTH, navTF);
        layout.putConstraint(SpringLayout.NORTH, anParityTF, mediumStrut,
                SpringLayout.SOUTH, navTF);    	            	                    	    	    	    	
    }

    private void alignRow10(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, medicalIdL, leftmostStrut, 
				SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, medicalIdTF, shortStrut,
        		SpringLayout.EAST, medicalIdL);        
    	// align row
        layout.putConstraint(SpringLayout.NORTH, medicalIdL, mediumStrut,
                SpringLayout.SOUTH, navParityTF);
        layout.putConstraint(SpringLayout.NORTH,medicalIdTF , mediumStrut,
                SpringLayout.SOUTH, navParityTF );    	            	                    	    	            	
    }

    private void alignRow11(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, medIssueDateL, leftmostStrut, 
				SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, medIssueDateTF, shortStrut,
        		SpringLayout.EAST, medIssueDateL);    
    	layout.putConstraint(SpringLayout.WEST, medIssueDateFormatL, shortStrut,
        		SpringLayout.EAST, medIssueDateTF);        	
    	// align row
        layout.putConstraint(SpringLayout.NORTH, medIssueDateL, mediumStrut,
                SpringLayout.SOUTH, medicalIdTF);
        layout.putConstraint(SpringLayout.NORTH, medIssueDateTF, mediumStrut,
                SpringLayout.SOUTH, medicalIdTF);
        layout.putConstraint(SpringLayout.NORTH, medIssueDateFormatL, mediumStrut,
                SpringLayout.SOUTH, medicalIdTF);        
    }

    private void alignRow12(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST,notesL , leftmostStrut,
				SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, notesSP, shortStrut,
				SpringLayout.EAST, notesL);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, notesL, mediumStrut,
                SpringLayout.SOUTH, medIssueDateTF);
        layout.putConstraint(SpringLayout.NORTH, notesSP, mediumStrut,
                SpringLayout.SOUTH, medIssueDateTF);    	            	                    	    	            	    	    	
    }
    
    private void addDocumentListener(JTextComponent c){
    	c.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){
            	saveInsButton.setEnabled(true);
            }
            public void removeUpdate(DocumentEvent e){
            	saveInsButton.setEnabled(true);            	
            }
            public void changedUpdate(DocumentEvent e){
            	saveInsButton.setEnabled(true);            	
            }
        });
    }
    
    private void initInsDetailPanelComponent(){
    	insAdmitDateTF = new JTextField(10);
    	insAdmitDateTF.setEnabled(false); 
    	
    	esdTF = new JTextField(10);
    	esdTF.setEnabled(false);
    	
    	eedTF = new JTextField(10);
    	eedTF.setEnabled(false);
    	
    	insCompanyCB = new JComboBox();
        insCompanyCB.setEnabled(false);
                
        insPnTF = new JTextField(10);
        insPnTF.setEnabled(false);
        
        memberIdTF = new JTextField(20);
        memberIdTF.setEnabled(false);
        
        copayTF = new JTextField(5);
        copayTF.setEnabled(false);
        
        copayParityTF = new JTextField(5);
        copayParityTF.setEnabled(false);
        
        navTF = new JTextField(5);
        navTF.setEnabled(false);        
        
        anTF = new JTextField(20);
        anTF.setEnabled(false);

        navParityTF = new JTextField(5);
        navParityTF.setEnabled(false);
        
        anParityTF = new JTextField(20);
        anParityTF.setEnabled(false);               
        
        medicalIdTF = new JTextField(20);
        medicalIdTF.setEnabled(false);

        medIssueDateTF = new JTextField(10);
        medIssueDateTF.setEnabled(false);
        
        notesTA = new JTextArea(4, 45);
        notesTA.setLineWrap(true);
        notesTA.setEnabled(false);

        addDocumentListener(insAdmitDateTF);        
        addDocumentListener(esdTF);
        addDocumentListener(eedTF);
        addDocumentListener(insPnTF);
        addDocumentListener(memberIdTF);        
        addDocumentListener(copayTF);                                     
        addDocumentListener(copayParityTF);
        addDocumentListener(navTF);
        addDocumentListener(anTF);
        addDocumentListener(navParityTF);
        addDocumentListener(anParityTF);
        addDocumentListener(medicalIdTF);
        addDocumentListener(medIssueDateTF);
        addDocumentListener(notesTA);        
    }
    
    private JPanel createInsActionPanel(){
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addInsButton = new JButton("Add");
        addInsButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		Insurance ins = new Insurance();        		
        		String insCompName = (String)insCompanyNames.get(0);
        		ins.setInsId(-1);
        		ins.setInsCompany(insCompName);        		
        		insTableModel.addIns(ins);
        		insId = -1;
        		setSelectedInsCompany(insCompName);
        		int lastRow = insTable.getRowCount();
        		insTable.setRowSelectionInterval(lastRow - 1, lastRow - 1);
        		insCompanyCB.setEnabled(true);
        		insDetailPanel.setVisible(true);
        		enableAllTF();
        		clearAllTF(insCompName);
        		addInsButton.setEnabled(false);
        	}
        });

        saveInsButton = new JButton("Save");
        saveInsButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (insId == -1){
        			if (createInsurance()){
                		saveInsButton.setEnabled(false);
                		addInsButton.setEnabled(true);
                		refreshInsTable();
        			}else{
                		saveInsButton.setEnabled(true);        				
        			}
        		}else{
        			if (updateInsurance()){
                		saveInsButton.setEnabled(false);
                		addInsButton.setEnabled(true);
                		refreshInsTable();
        			}else{
                		saveInsButton.setEnabled(true);        				
        			}
        		}
        		saveInsButton.setEnabled(false);
        	}
        });
        saveInsButton.setEnabled(false);

        deleteInsButton = new JButton("Delete");
        deleteInsButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (insId == -1){
        			clearInsurance();
        			addInsButton.setEnabled(true);
        		}else{
        			Object insCompany = insCompanyCB.getSelectedItem();
                    int reply = JOptionPane.showConfirmDialog(patDialog,
                            "Are you sure you want to delete the insurance \""+insCompany+"\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION){
                    	if (insTable.getRowCount() == 1){
                    		JOptionPane.showMessageDialog(patDialog, "Delete insurance is not allowed because Patient must have at least one Insurance!");
                    	}else{                    	
                    		deleteInsurance();
                    		refreshInsTable();
                    	}
                		addInsButton.setEnabled(true);                    	
                    }
        		}
        	}
        });
        deleteInsButton.setEnabled(false);
        
        actionPanel.add(addInsButton);
        actionPanel.add(saveInsButton);
        actionPanel.add(deleteInsButton);
        return actionPanel;                	
    }

    private JPanel createBalPanel(Patient patFull, PatientForm bean) {
        // 5 fields
        JLabel titleBlankL = new JLabel("", JLabel.TRAILING);
        JLabel titleL = new JLabel("Outstanding Balance for Patient: ");
        JLabel amtL   = new JLabel("Amount: ", JLabel.TRAILING);
        JLabel cbBlankL = new JLabel("", JLabel.TRAILING);
        JLabel notesL = new JLabel("Notes: ", JLabel.TRAILING);

        JPanel p = new JPanel();
        SpringLayout l = new SpringLayout();
        p.setLayout(l);

        final JTextField amtTF = new JTextField(20);
        amtTF.setText(patFull.getBalanceStr());
        bean.amtTF = amtTF;
        JButton clearBalB = new JButton("Clear Outstanding Balance");
        clearBalB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                amtTF.setText("0");
            }
        });

        final JTextArea balNotesTA = new JTextArea(4, 45);
        JScrollPane balNotesSP = new JScrollPane(balNotesTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        balNotesTA.setText(patFull.getBalanceNotes());
        bean.balNotesTA = balNotesTA;
        
        if (Constant.appRole.contains(Constant.BILLING) || Constant.appRole.contains(Constant.ADMINISTRATOR)){
        	amtTF.setEnabled(true);
        	clearBalB.setEnabled(true); 
        	balNotesTA.setEnabled(true);
        }else{
        	amtTF.setEnabled(false);
        	clearBalB.setEnabled(false);
        	balNotesTA.setEnabled(false);
        }

        //4 x 2 matrix        
        p.add(titleL);
        p.add(titleBlankL);
        p.add(amtL);
        p.add(amtTF);        
        p.add(cbBlankL);
        p.add(clearBalB);
        p.add(notesL);
        p.add(balNotesSP);
        
        Spring strut = Spring.constant(10);
        Spring labelsEast = Spring.sum(strut, l.getConstraints(amtL).getWidth());

        // align first col
        l.putConstraint(SpringLayout.EAST, amtL, labelsEast,
                        SpringLayout.WEST, p);
        l.putConstraint(SpringLayout.EAST, cbBlankL, labelsEast,
                        SpringLayout.WEST, p);
        l.putConstraint(SpringLayout.EAST, notesL, labelsEast, 
        				SpringLayout.WEST, p);

        // align second col
        l.putConstraint(SpringLayout.WEST, amtTF, strut,
                        SpringLayout.EAST,
                        amtL);
        l.putConstraint(SpringLayout.WEST, clearBalB, strut,
                        SpringLayout.EAST,
                        cbBlankL);
        l.putConstraint(SpringLayout.WEST, balNotesSP, strut,
        				SpringLayout.EAST,
        				notesL);

        
        // align row
        l.putConstraint(SpringLayout.NORTH, titleL, strut,
                        SpringLayout.NORTH,
                        p);
        l.putConstraint(SpringLayout.NORTH, titleBlankL, strut,
                        SpringLayout.NORTH, p);

        Spring rowNorth1 = Spring.sum(strut, Spring.max(l.getConstraint(SpringLayout.SOUTH, titleL), l.getConstraint(SpringLayout.SOUTH, titleBlankL)));
        l.putConstraint(SpringLayout.NORTH, amtL, rowNorth1,
                        SpringLayout.NORTH, p);
        l.putConstraint(SpringLayout.NORTH, amtTF, rowNorth1,
                        SpringLayout.NORTH, p);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(l.getConstraint(SpringLayout.SOUTH, amtL), l.getConstraint(SpringLayout.SOUTH, amtTF)));
        l.putConstraint(SpringLayout.NORTH, notesL, rowNorth2,
                	    SpringLayout.NORTH, p);
        l.putConstraint(SpringLayout.NORTH, balNotesSP, rowNorth2,
                	    SpringLayout.NORTH, p);
        
        
        Spring rowNorth3 = Spring.sum(strut, Spring.max(l.getConstraint(SpringLayout.SOUTH, notesL), l.getConstraint(SpringLayout.SOUTH, balNotesSP)));
        l.putConstraint(SpringLayout.NORTH, cbBlankL, rowNorth3,
                        SpringLayout.NORTH, p);
        l.putConstraint(SpringLayout.NORTH, clearBalB, rowNorth3,
                        SpringLayout.NORTH, p);
        
        return p;
    }
    
    private void setSelectedInsCompany(String insCompName){
		int numItem = insCompanyCB.getItemCount();
    	if (numItem == 0){
    		for (int i=0;i<insCompanyNames.size();i++){
    			insCompanyCB.addItem(insCompanyNames.get(i));
    		}
    	}
    	insCompanyCB.setSelectedItem(insCompName);        		        	    	
    }
    
    private JPanel createRemPanel(Patient patFull, PatientForm bean) {
        // 5 fields
        JLabel titleL = new JLabel("Please enter reminder message for this patient in the following text area: ");
        JLabel remL   = new JLabel("Reminder: ");
        JLabel blankL = new JLabel(" ");
        JLabel blankL2 = new JLabel(" ");

        JPanel p = new JPanel(new SpringLayout());
        SpringLayout l = new SpringLayout();
        p.setLayout(l);

        final JTextArea msgTA = new JTextArea(5, 45);
        msgTA.setLineWrap(true);
        msgTA.setWrapStyleWord(true);
        msgTA.setText(patFull.getReminder());
        bean.msgTA = msgTA;
        JButton clearMsgB = new JButton("Clear message");
        clearMsgB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                msgTA.setText("");
            }
        }
        );
        JScrollPane sp = new JScrollPane(msgTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p.add(titleL);
        p.add(blankL);
        p.add(remL);
        p.add(sp);
        p.add(blankL2);
        p.add(clearMsgB);

        Spring strut = Spring.constant(10);
        Spring labelsEast = Spring.sum(strut, l.getConstraints(remL).getWidth());

        // align first col
        l.putConstraint(SpringLayout.EAST, remL, labelsEast,
                        SpringLayout.WEST, p);
        l.putConstraint(SpringLayout.EAST, blankL2, labelsEast,
                        SpringLayout.WEST, p);

        // align second col
        l.putConstraint(SpringLayout.WEST, sp, strut,
                        SpringLayout.EAST,
                        remL);
        l.putConstraint(SpringLayout.WEST, clearMsgB, strut,
                        SpringLayout.EAST,
                        blankL2);

        // align row
        l.putConstraint(SpringLayout.NORTH, titleL, strut,
                        SpringLayout.NORTH,
                        p);
        l.putConstraint(SpringLayout.NORTH, blankL, strut,
                        SpringLayout.NORTH, p);

        Spring rowNorth1 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, titleL),
            l.getConstraint(SpringLayout.SOUTH, blankL)));
        l.putConstraint(SpringLayout.NORTH, remL, rowNorth1,
                        SpringLayout.NORTH, p);
        l.putConstraint(SpringLayout.NORTH, sp, rowNorth1,
                        SpringLayout.NORTH, p);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, remL),
            l.getConstraint(SpringLayout.SOUTH, sp)));
        l.putConstraint(SpringLayout.NORTH, blankL2, rowNorth2,
                        SpringLayout.NORTH, p);
        l.putConstraint(SpringLayout.NORTH, clearMsgB, rowNorth2,
                        SpringLayout.NORTH, p);

        return p;
    }	

    private boolean hasChanged(Patient patFull, PatientForm bean) {
        if (!sameVal(patFull.firstName, bean.fnTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.mi, bean.miTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.lastName, bean.lnTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.streetAddress, bean.streetTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.apartmentNumber, bean.apartTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.city, bean.cityTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.state, bean.stateTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.zipCode, bean.zipTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.phoneNumber, bean.pnTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.email, bean.emailTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.legalGardianFirstName, bean.lgFnTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.legalGardianMiddleInitial, bean.lgMiTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.legalGardianLastName, bean.lgLnTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.legalGardianPhoneNumber, bean.lgPnTF.getText())) {
            return true;
        }
        if (!sameVal(patFull.getReminder(), bean.msgTA.getText().trim())){
            return true;
        }
        if (!sameVal(patFull.getBalanceStr(), bean.amtTF.getText().trim())){
        	return true;
        }        
        if (!sameVal(patFull.getBalanceNotes(), bean.balNotesTA.getText())){
        	return true;
        }
        return false;
    }

    private void saveRefChange(Patient patFull, PatientForm bean) {
        boolean dirty = false;
        boolean firstTime = true;
        String sql = "update referral set ";
        List args = new ArrayList();

        if (!sameVal(patFull.firstName, bean.fnTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "firstName = ? ";
            //args.add(SecurityUtil.encrypt(bean.fnTF.getText().trim()));
            args.add(bean.fnTF.getText().trim());
        }
        if (!sameVal(patFull.mi, bean.miTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "middleInitial = ? ";
            args.add(bean.miTF.getText().trim());
        }
        if (!sameVal(patFull.lastName, bean.lnTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "lastName = ? ";
            args.add(bean.lnTF.getText().trim());

        }
        if (!sameVal(patFull.streetAddress, bean.streetTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "streetAddress = ? ";
            args.add(bean.streetTF.getText().trim());

        }
        if (!sameVal(patFull.apartmentNumber, bean.apartTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "apartmentNumber = ? ";
            args.add(bean.apartTF.getText().trim());

        }
        if (!sameVal(patFull.city, bean.cityTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "city = ? ";
            args.add(bean.cityTF.getText().trim());

        }
        if (!sameVal(patFull.state, bean.stateTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "state = ? ";
            args.add(bean.stateTF.getText().trim());

        }
        if (!sameVal(patFull.zipCode, bean.zipTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "zipCode = ? ";
            args.add(bean.zipTF.getText().trim());

        }
        if (!sameVal(patFull.phoneNumber, bean.pnTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "phoneNumber = ? ";
            String pn = bean.pnTF.getText().trim();
            pn = Constant.clearPhoneNumFormatting(pn);
            args.add(pn);

        }
        if (!sameVal(patFull.email, bean.emailTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "email = ? ";
            args.add(bean.emailTF.getText().trim());
        }
        if (!sameVal(patFull.legalGardianFirstName, bean.lgFnTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "legalGardianFirstName = ? ";
            args.add(bean.lgFnTF.getText().trim());
        }
        if (!sameVal(patFull.legalGardianMiddleInitial, bean.lgMiTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "legalGardianMiddleInitial = ? ";
            args.add(bean.lgMiTF.getText().trim());
        }
        if (!sameVal(patFull.legalGardianLastName, bean.lgLnTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "legalGardianLastName = ? ";
            args.add(bean.lgLnTF.getText().trim());
        }
        if (!sameVal(patFull.legalGardianPhoneNumber, bean.lgPnTF.getText())) {
            dirty = true;
            if (!firstTime) {
                sql += ", ";
            }
            else {
                firstTime = false;
            }
            sql += "legalGardianPhoneNumber = ? ";
            String pn = bean.lgPnTF.getText().trim();
            pn = Constant.clearPhoneNumFormatting(pn);            
            args.add(pn);
        }
        if (!sameVal(patFull.getReminder(), bean.msgTA.getText())){
            dirty = true;
            if (!firstTime){
                sql += ", ";
            }else{
                firstTime = false;
            }
            sql += "reminder = ? ";
            args.add(bean.msgTA.getText().trim());
        }
        if (!sameVal(patFull.getBalanceStr(), bean.amtTF.getText().trim())){
        	dirty = true;
        	if (!firstTime){
        		sql += ", ";        		
        	}else{
        		firstTime = false;
        	}
        	sql += "balance = ? ";
        	BigDecimal bal = new BigDecimal(bean.amtTF.getText().trim()).setScale(2, BigDecimal.ROUND_HALF_UP);
        	args.add(bal);
        }
        if (!sameVal(patFull.getBalanceNotes(), bean.balNotesTA.getText())){
        	dirty = true;
        	if (!firstTime){
        		sql += ", ";        		
        	}else{
        		firstTime = false;
        	}
        	sql += "balanceNotes = ? ";
        	args.add(bean.balNotesTA.getText().trim());
        }

        if (dirty) {
            sql += "where referralId = " + patFull.refId;
            WriteSvc.getInstance().execDynamicSql(sql, args);
        }
    }
    
    private boolean sameVal(String v1, String v2) {
        if (v1 == null && v2 == null) {
            return true;
        }
        if (v1 != null && v2 == null) {
            if (v1.trim().equals("")) {
                return true;
            }
            else {
                return false;
            }
        }
        if (v1 == null && v2 != null) {
            if (v2.trim().equals("")) {
                return true;
            }
            else {
                return false;
            }
        }
        v1 = v1.trim();
        v2 = v2.trim();
        if (v1.equals(v2)) {
            return true;
        }
        else {
            return false;
        }
    }
     
    
    private boolean createInsurance(){
    	Insurance ins = new Insurance();
    	
    	ins.setRefId(refId);    	
    	if (populateInsFromForm(ins)){
            if (ins.getNotes() != null && ins.getNotes().length() > 250){
                JOptionPane.showMessageDialog(this, "Notes is too long. Maximum size is 250. Please delete some text in the Notes.",
                                              "Notes too long", JOptionPane.ERROR_MESSAGE);
                return false;
            }else{    	    		
            	WriteSvc.getInstance().createInsurance(ins);
            	return true;
            }
    	}else{
    		return false;
    	}    	
    }
    
    private boolean populateInsFromForm(Insurance ins){
    	String refDateStr = insAdmitDateTF.getText();
		if (refDateStr != null && !refDateStr.equals("")){
			Date refDate = validDateFromString(refDateStr, "Referral Date");
			if (refDate != null){
				ins.setRefDate(refDate);
			}else{
				insAdmitDateTF.grabFocus();
				return false;
			}
		}
    	
    	String esdStr = esdTF.getText();
		if (esdStr != null && !esdStr.equals("")){
			Date esd = validDateFromString(esdStr, "Elig Eff Date");
			if (esd != null){
				ins.setEffStartDate(esd);
			}else{
				esdTF.grabFocus();
				return false;
			}
		}

    	String eedStr = eedTF.getText();
		if (eedStr != null && !eedStr.equals("")){
			Date eed = validDateFromString(eedStr, "Elig Term Date");
			if (eed != null){
				ins.setEffEndDate(eed);
			}else{
				eedTF.grabFocus();
				return false;
			}
		}		

    	ins.setInsCompany((String)insCompanyCB.getSelectedItem());		
    	ins.setInsPhoneNum(insPnTF.getText());    	
    	ins.setMemberId(memberIdTF.getText());
    	
    	try{
    		String copayStr = copayTF.getText();
    		if (copayStr != null && !copayStr.equals("")){
    			ins.setCopay(new BigDecimal(copayTF.getText()));
    		}
    	}catch(Exception e){
    		JOptionPane.showMessageDialog(this, "Invalid value for Copay! Please use number only!",
    				"Invalid Value", JOptionPane.ERROR_MESSAGE);
    		copayTF.grabFocus();
    		return false;
    	}
    	
    	try{
    		String copayParityStr = copayParityTF.getText();
    		if (copayParityStr != null && !copayParityStr.equals("")){    		
    			ins.setCopayParity(new BigDecimal(copayParityTF.getText()));
    		}
    	}catch(Exception e){
    		JOptionPane.showMessageDialog(this, "Invalid value for Copay Parity! Please use number only!",
    				"Invalid Value", JOptionPane.ERROR_MESSAGE);
    		copayParityTF.grabFocus();
    		return false;
    	}

    	try{
    		if (navTF.getText() != null && ! navTF.getText().equals("")){
    			ins.setNumAuthVisitForMD(Integer.parseInt(navTF.getText()));
    		}
    	}catch(Exception e){
    		JOptionPane.showMessageDialog(this, "Invalid value for # authorized visit! Please use number only!",
    				"Invalid Value", JOptionPane.ERROR_MESSAGE);
    		navTF.grabFocus();
    		return false;
    	}
    	
    	ins.setAuthNumForMD(anTF.getText());    	

    	try{
    		if (navParityTF.getText() != null && ! navParityTF.getText().equals("")){
    			ins.setNumAuthVisitForMA(Integer.parseInt(navParityTF.getText()));
    		}
    	}catch(Exception e){
    		JOptionPane.showMessageDialog(this, "Invalid value for # authorized visit parity! Please use number only!",
    				"Invalid Value", JOptionPane.ERROR_MESSAGE);
    		navParityTF.grabFocus();
    		return false;
    	}
    	
    	ins.setAuthNumForMA(anParityTF.getText());
    	
    	ins.setMedicalId(medicalIdTF.getText());

    	String medIssueDateStr = medIssueDateTF.getText();
		if (medIssueDateStr != null && !medIssueDateStr.equals("")){
			Date medIssueDate = validDateFromString(medIssueDateStr, "Medi-Cal Issue Date");
			if (medIssueDate != null){
				ins.setMedIssueDate(medIssueDate);
			}else{
				medIssueDateTF.grabFocus();
				return false;
			}
		}
		
		ins.setNotes(notesTA.getText()); 
		
    	return true;    	
    }
    
    private Date validDateFromString(String dateStr, String fieldName){
    	try{
			if (!DateStringConverter.isDateStringValid(dateStr)){
				JOptionPane.showMessageDialog(this, "Invalid "+fieldName+" format! Valid format is (MM/DD/YYYY).",
						"Invalid Date Format", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			int month = DateStringConverter.getMonthFromDateStr(dateStr);
			int day = DateStringConverter.getDayFromDateStr(dateStr);
			int year = DateStringConverter.getYearFromDateStr(dateStr); 
			if (month < 1 || month > 12){
				JOptionPane.showMessageDialog(this, "Invalid "+fieldName+" Month! Month must be between 1 and 12.",
						"Invalid Value", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			if (day < 1 || day > 31){
				JOptionPane.showMessageDialog(this, "Invalid "+fieldName+" Date! Date must be between 1 and 31.",
						"Invalid Value", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			if (year < 1900 || year > 2200){    		
				JOptionPane.showMessageDialog(this, "Invalid "+fieldName+" Year! Year must be between 1900 and 2200.",
						"Invalid Value", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			Calendar cal = new GregorianCalendar(year, month-1, day);
			return cal.getTime();
    	}catch(Exception e){
			JOptionPane.showMessageDialog(this,"Invalid value for "+fieldName+"! Please use number only!",
					"Invalid Value", JOptionPane.ERROR_MESSAGE);
			return null;
    	}    	
    }
        
    private void refreshInsTable(){
    	List insList = ReadSvc.getInstance().getInsForRef(refId);
    	insTableModel.setIns(insList);
    	insTableModel.fireTableDataChanged();
    }
    
    private boolean updateInsurance(){    	
    	Insurance oldIns = insTableModel.getIns(insTable.getSelectedRow());
    	
    	// set value for newIns
    	Insurance newIns = new Insurance();
    	newIns.setInsId(insId);
    	newIns.setRefId(refId);    	
    	if (!populateInsFromForm(newIns)){
    		return false;
    	}
    	
    	// Validation
        if (newIns.getNotes() != null && newIns.getNotes().length() > 250){
            JOptionPane.showMessageDialog(this, "Notes is too long. Maximum size is 250. Please delete some text in the Notes.",
                                          "Notes too long", JOptionPane.ERROR_MESSAGE);
            return false;
        }    	
        if (newIns.getAuthNumForMA() != null && newIns.getAuthNumForMA().length() > 30){
            JOptionPane.showMessageDialog(this, "Auth # for MA is too long. Maximum size is 30.\nPlease delete some text and try to save again.",
                                          "Invalid Auth #", JOptionPane.ERROR_MESSAGE);
            return false;
        }    	
        if (newIns.getAuthNumForMD() != null && newIns.getAuthNumForMD().length() > 30){
            JOptionPane.showMessageDialog(this, "Auth # for MD is too long. Maximum size is 30.\nPlease delete some text and try to save again.",
                                          "Invalid Auth #", JOptionPane.ERROR_MESSAGE);
            return false;
        }    	
        if (newIns.getMemberId() != null && newIns.getMemberId().length() > 40){
            JOptionPane.showMessageDialog(this, "MemberId is too long. Maximum size is 40.\nPlease delete some text and try to save again.",
                                          "Invalid MemberId", JOptionPane.ERROR_MESSAGE);
            return false;
        }    	

        
    	List args = new ArrayList();
    	String sql = constructUpdateSQL(oldIns, newIns, args);
    	if (sql != null){
    		WriteSvc.getInstance().execDynamicSql(sql, args);
    	}
    	return true;    	
    }
    
    private String constructUpdateSQL(Insurance oldIns, Insurance newIns, List args){
    	boolean dirty = false;
    	String sql = "update insurance set ";
    	
		if ( (oldIns.getRefDate() != null && newIns.getRefDate() != null) ||
		     (oldIns.getRefDate() == null && newIns.getRefDate() != null)) 
		{
			sql += "referralDate = ?, ";
			args.add(newIns.getRefDate());
			dirty = true;
		} else if (oldIns.getRefDate() != null && newIns.getRefDate() == null){
			sql += "referralDate = null, ";
			dirty = true;
		}
    	
		if ( (oldIns.getEffStartDate() != null && newIns.getEffStartDate() != null) ||
			 (oldIns.getEffStartDate() == null && newIns.getEffStartDate() != null)) 
		{
			//String dateStr = newIns.getEffStartDateForSQL();
			//sql += "eligEffDate = '"+dateStr+"', ";
			sql += "eligEffDate = ?, ";
			args.add(newIns.getEffStartDate());
			dirty = true;
		} else if (oldIns.getEffStartDate() != null && newIns.getEffStartDate() == null){
			//sql += "eligEffDate = null, ";
			sql += "eligEffDate = null, ";
			dirty = true;
		}
        	
		if ( (oldIns.getEffEndDate() != null && newIns.getEffEndDate() != null) ||
			 (oldIns.getEffEndDate() == null && newIns.getEffEndDate() != null)) 
		{
			//String dateStr = newIns.getEffEndDateForSQL();
			//sql += "eligTermDate = '"+dateStr+"', ";
			sql += "eligTermDate = ?, ";
			args.add(newIns.getEffEndDate());
			dirty = true;
		} else if (oldIns.getEffEndDate() != null && newIns.getEffEndDate() == null){
			sql += "eligTermDate = null, ";
			dirty = true;
		}

		if (!oldIns.getInsCompany().equals(newIns.getInsCompany())){
			//sql += "insuranceCompany = \""+newIns.getInsCompany()+"\", ";
			sql += "insuranceCompany = ?, ";
			args.add(newIns.getInsCompany());
			dirty = true;
		}

		if (oldIns.getInsPhoneNum() != null && newIns.getInsPhoneNum() != null){
			if (!oldIns.getInsPhoneNum().equals(newIns.getInsPhoneNum())){
				//sql += "phoneNumber = \""+newIns.getInsPhoneNum()+"\", ";
				sql += "phoneNumber = ?, ";
				args.add(newIns.getInsPhoneNum());
				dirty = true;
			}			
		}else{
			if (oldIns.getInsPhoneNum() == null){
				//sql += "phoneNumber = \""+newIns.getInsPhoneNum()+"\", ";
				sql += "phoneNumber = ?, ";
				args.add(newIns.getInsPhoneNum());
				dirty = true;
			}else if (newIns.getInsPhoneNum() == null){
				sql += "phoneNumber = null, ";
				dirty = true;
			}
		}

		if (!oldIns.getMemberId().equals(newIns.getMemberId())){
			//sql += "memberId = \""+newIns.getMemberId()+"\", ";
			sql += "memberId = ?, ";
			args.add(newIns.getMemberId());
			dirty = true;
		}

		if (!oldIns.getCopayStr().trim().equals(
				newIns.getCopayStr())){
			//sql += "copay = \""+newIns.getCopayStr()+"\", ";
			sql += "copay = ?, ";
			args.add(newIns.getCopay());
			dirty = true;
		}

		if (!oldIns.getCopayParityStr().trim().equals(newIns.getCopayParityStr())){
			//sql += "copayParity = \""+newIns.getCopayParityStr()+"\", ";
			sql += "copayParity = ?, ";
			args.add(newIns.getCopayParity());
			dirty = true;
		}

		if (oldIns.getNumAuthVisitForMD() != newIns.getNumAuthVisitForMD()){
			//sql += "numAuthVisitForMD = "+newIns.getNumAuthVisitForMD()+", ";
			sql += "numAuthVisitForMD = ?, ";
			args.add(new Integer(newIns.getNumAuthVisitForMD()));
			dirty = true;
		}
		
		if (!oldIns.getAuthNumForMD().equals(
				newIns.getAuthNumForMD())){
			//sql += "authNumForMD = \""+newIns.getAuthNumForMD()+"\", ";
			sql += "authNumForMD = ?, ";
			args.add(newIns.getAuthNumForMD());
			dirty = true;
		}
		
		if (oldIns.getNumAuthVisitForMA() != newIns.getNumAuthVisitForMA()){
			//sql += "numAuthVisitForMA = "+newIns.getNumAuthVisitForMA()+", ";
			sql += "numAuthVisitForMA = ?, ";
			args.add(new Integer(newIns.getNumAuthVisitForMA()));
			dirty = true;
		}

		if (!oldIns.getAuthNumForMA().equals(
				newIns.getAuthNumForMA())){
			//sql += "authNumForMA = \""+newIns.getAuthNumForMA()+"\", ";
			sql += "authNumForMA = ?, ";
			args.add(newIns.getAuthNumForMA());
			dirty = true;
		}
		
		if (!oldIns.getMedicalId().equals(newIns.getMedicalId())){
			//sql += "medicalId = '"+newIns.getMedicalId()+"', ";
			sql += "medicalId = ?, ";
			args.add(newIns.getMedicalId());
			dirty = true;
		}
		
		if ( (oldIns.getMedIssueDate() != null && newIns.getMedIssueDate() != null) ||
			 (oldIns.getMedIssueDate() == null && newIns.getMedIssueDate() != null)) {
			//String dateStr = newIns.getMedIssueDateForSQL();
			//sql += "medicalIssueDate = '"+dateStr+"', ";
			sql += "medicalIssueDate = ?, ";
			args.add(newIns.getMedIssueDate());
			dirty = true;
		}else if (oldIns.getMedIssueDate() != null && newIns.getMedIssueDate() == null){
			sql += "medicalIssueDate = null, ";
			dirty = true;
		}

		if (!oldIns.getNotes().equals(newIns.getNotes())){
			//sql += "notes = '"+newIns.getNotes()+"', ";
			sql += "notes = ?, ";
			args.add(newIns.getNotes());
			dirty = true;
		}
		
		if (dirty){
			// remove last comma
			int lastComma = sql.lastIndexOf(",");
			sql = sql.substring(0, lastComma);
			sql += " where insuranceId = "+oldIns.getInsId();
			return sql;
    			
		}else{
			return null;
		}    	
    }
        
    private void clearInsurance(){
    	insTableModel.removeInsById(insId);
    	insTableModel.fireTableDataChanged();
    	clearAllFields();
    }
    
    private void clearAllFields(){
    	insId = -1;
    	anTF.setText("");
    	copayParityTF.setText("");
    	copayTF.setText("");
    	insPnTF.setText("");
    	memberIdTF.setText("");
    	navParityTF.setText("");
    	navTF.setText("");
    	saveInsButton.setEnabled(false);
    	deleteInsButton.setEnabled(false);
    }
    
    private void deleteInsurance(){
    	WriteSvc.getInstance().deleteInsurance(insId, refId);
    	insTableModel.removeInsById(insId);
    	insTableModel.fireTableDataChanged();
    	clearAllFields();
    }
    
    private JPanel createEvalPanel(Patient patFull) {
        JPanel p = new JPanel(new BorderLayout());
        
        List<Evaluation> evals = ReadSvc.getInstance().getAllEvaluation(patFull.refId);
        StringBuilder sb = new StringBuilder();
        for (Evaluation eval : evals){
        	sb.append(eval.toString());
        	sb.append("\n");
        	sb.append("\n");
        }
        List<Appointment> appts = ReadSvc.getInstance().getAllEvaluationAppt(refId);
        for (Appointment appt : appts){
        	sb.append(appt.toStringForEvaluation());
        	sb.append("\n");
        	sb.append("\n");
        }
                
        final JTextArea msgTA = new JTextArea(20, 30);
        msgTA.setLineWrap(true);
        msgTA.setWrapStyleWord(true);
        msgTA.setText(sb.toString());
        msgTA.setEditable(false);
        msgTA.setBackground(RubyTheme.lightGreen);
        JScrollPane sp = new JScrollPane(msgTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }	
    
    class PatientForm {
        // general
        JTextField fnTF;
        JTextField miTF;
        JTextField lnTF;
        JTextField streetTF;
        JTextField apartTF;
        JTextField cityTF;
        JTextField stateTF;
        JTextField zipTF;
        JTextField pnTF;
        JTextField emailTF;

        // legalGardian
        JTextField lgFnTF;
        JTextField lgMiTF;
        JTextField lgLnTF;
        JTextField lgPnTF;

        // reminder
        JTextArea msgTA;
        
        // outstanding balance
        JTextField amtTF;
        JTextArea balNotesTA;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancelByPatientYesBtn")){
	    	int viewRowIndex = cancelApptTable.getSelectedRow();
	    	int selectedRow = cancelApptTable.convertRowIndexToModel(viewRowIndex);
	    	Appointment appt = cancelApptTableModel.getAppt(selectedRow);
	    	appt.setCancelByPatient(true);
	    	appt.setCancelByClinic(false);
	    	cancelByClinicNoBtn.setSelected(true);
	    	cancelApptTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
	    	WriteSvc.getInstance().updateCanceledApptCancelBy(appt.getApptId(), true);
		}else if (e.getActionCommand().equals("cancelByPatientNoBtn")){
	    	int viewRowIndex = cancelApptTable.getSelectedRow();
	    	int selectedRow = cancelApptTable.convertRowIndexToModel(viewRowIndex);
	    	Appointment appt = cancelApptTableModel.getAppt(selectedRow);
	    	appt.setCancelByPatient(false);
	    	appt.setCancelByClinic(true);
	    	cancelByClinicYesBtn.setSelected(true);			
	    	cancelApptTableModel.fireTableRowsUpdated(selectedRow, selectedRow);	  
	    	WriteSvc.getInstance().updateCanceledApptCancelBy(appt.getApptId(), false);	    	
		}else if (e.getActionCommand().equals("cancelByClinicYesBtn")){
	    	int viewRowIndex = cancelApptTable.getSelectedRow();
	    	int selectedRow = cancelApptTable.convertRowIndexToModel(viewRowIndex);
	    	Appointment appt = cancelApptTableModel.getAppt(selectedRow);
	    	appt.setCancelByClinic(true);
	    	appt.setCancelByPatient(false);
	    	cancelByPatientNoBtn.setSelected(true);
	    	cancelApptTableModel.fireTableRowsUpdated(selectedRow, selectedRow);	 
	    	WriteSvc.getInstance().updateCanceledApptCancelBy(appt.getApptId(), false);	    	
		}else if (e.getActionCommand().equals("cancelByClinicNoBtn")){
	    	int viewRowIndex = cancelApptTable.getSelectedRow();
	    	int selectedRow = cancelApptTable.convertRowIndexToModel(viewRowIndex);
	    	Appointment appt = cancelApptTableModel.getAppt(selectedRow);
	    	appt.setCancelByClinic(false);
	    	appt.setCancelByPatient(true);
	    	cancelByPatientYesBtn.setSelected(true);				    		
	    	cancelApptTableModel.fireTableRowsUpdated(selectedRow, selectedRow);	
	    	WriteSvc.getInstance().updateCanceledApptCancelBy(appt.getApptId(), true);	    	
		}else if (e.getActionCommand().equals("cancelWnTwentyFourHrsYesBtn")){
	    	int viewRowIndex = cancelApptTable.getSelectedRow();
	    	int selectedRow = cancelApptTable.convertRowIndexToModel(viewRowIndex);
	    	Appointment appt = cancelApptTableModel.getAppt(selectedRow);
	    	appt.setWnTwentyFourHrs(true);						
	    	cancelApptTableModel.fireTableRowsUpdated(selectedRow, selectedRow);	
	    	WriteSvc.getInstance().updateCanceledApptIsWnTwentyFourHrs(appt.getApptId(), true);
		}else if (e.getActionCommand().equals("cancelWnTwentyFourHrsNoBtn")){
	    	int viewRowIndex = cancelApptTable.getSelectedRow();
	    	int selectedRow = cancelApptTable.convertRowIndexToModel(viewRowIndex);
	    	Appointment appt = cancelApptTableModel.getAppt(selectedRow);
	    	appt.setWnTwentyFourHrs(false);
	    	cancelApptTableModel.fireTableRowsUpdated(selectedRow, selectedRow);	    	
	    	WriteSvc.getInstance().updateCanceledApptIsWnTwentyFourHrs(appt.getApptId(), false);
		}
	}
    
    
}
