package com.suntek.scheduler.ui;

import java.util.*;
import java.util.List;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import javax.swing.border.EtchedBorder;

import com.suntek.scheduler.appsvcs.persistence.*;
import com.suntek.scheduler.appsvcs.WriteSvc;
import com.suntek.scheduler.appsvcs.ReadSvc;

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
public class ApptCellEditor extends ApptCellRenderer implements TableCellEditor, ActionListener, DocumentListener {

	private static long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000; 
	private static long ONE_DAY_IN_MILLIS = 24 * ONE_HOUR_IN_MILLIS;
	private static long SIXTY_DAYS_IN_MILLIS = 60 * ONE_DAY_IN_MILLIS;
	
    private JTable apptTable = null;
    private MainFrame f = null;

    private JDialog apptDialog = null;

    private static DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

    // panels in the dialog
    JPanel dialogPanel = null;

    JPanel providerPanel = null;
    JPanel apptTypePanel = null;
    JPanel patCards = null;
    JPanel evalYearCards = null;
    JPanel evalYearPanel = null;
    JComboBox evalYearCB = null;
    JLabel evalYearLabel = new JLabel("Evaluation Year: ");
    JPanel patientPanel = null;
    JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
    JPanel startTimePanel = null;
    JPanel endTimePanel = null;
    JPanel actionPanel = null;
    JPanel actionPanel2 = null;

    JLabel providerLabel = new JLabel();

    ButtonGroup apptTypeButtonGroup = null;
    JRadioButton apptRadioButton = null;
    JRadioButton blockedButton = null;
    JRadioButton evalRadioButton = null;

    JLabel clinicLabel = new JLabel("Clinic: ");
    JComboBox clinicCB = null;
    JLabel needTranL = new JLabel("Need Translation Service? ");
    JLabel collReceived = new JLabel("Collateral Received? ");
    JLabel eligL = new JLabel("Eligible for appointment? ");
    JLabel walkInL = new JLabel("Walk-in? ");
    ButtonGroup needTranBG = null;
    JRadioButton ntYesB = null;
    JRadioButton ntNoB = null;
    JPanel langCards = null;
    JPanel langPanel = null;
    JPanel emptyPanel2 = null;
    JLabel langL = new JLabel("Language: ");
    JTextField langTF = null;
    ButtonGroup collReceivedBG = null;
    JRadioButton crYesB = null;
    JRadioButton crNoB = null;
    ButtonGroup eligBG = null;
    JRadioButton eligYesB = null;
    JRadioButton eligNoB = null;    
    ButtonGroup walkInBG = null;
    JRadioButton walkInYesB = null;
    JRadioButton walkInNoB = null;   
    JPanel authNumCards = null;
    JPanel authNumPanel = null;
    JPanel emptyPanel3 = null;
    JLabel authNumL = new JLabel("Authorization #: ");
    JTextField authNumTF = null;
    JPanel countyNumCards = null;
    JPanel countyNumPanel = null;
    JPanel emptyPanel4 = null;
    JLabel countyNumL = new JLabel("County #: ");
    JTextField countyNumTF = null;

    JLabel patientLabel = new JLabel("Patient: ");
    JTextField patientTextField = null;
    JButton selectPatientButton = null;
    JLabel startTimeLabel = null;
    JComboBox startTimeHourComboBox = null;
    JComboBox startTimeMinComboBox = null;
    JComboBox startTimeAMPMComboBox = null;
    JLabel endTimeLabel = null;
    JComboBox endTimeHourComboBox = null;
    JComboBox endTimeMinComboBox = null;
    JComboBox endTimeAMPMComboBox = null;
    JLabel notesLabel = new JLabel("  Appointment Notes: ");
    JLabel blankLabel = new JLabel("   ");
    JTextArea notesTextArea = null;
    JScrollPane notesSP = null;
    JButton closeButton = null;
    JButton undoButton = null;
    JButton deleteButton = null;
    JButton showupButton = null;
    JButton noShowsButton = null;
    JButton cancelByPatientButton = null;
    JButton cancelByClinicButton = null;

    JPanel buttonPanel = new JPanel(new FlowLayout());

    private EventListenerList listenerList = new EventListenerList();
    private ChangeEvent event = new ChangeEvent(this);

    private boolean isApptCellSelected = false;

    // Select Patient Dialog
    JLabel spDescLabel1 = new JLabel("  Enter full or partial name for patient.");
    JLabel spDescLabel2 = new JLabel("");

    JList patientList = null;
    DefaultListModel patientListModel = new DefaultListModel();

    JTextField enterPatient = null;

    JButton spOkButton = null;
    JButton spCancelButton = null;
    JDialog spDialog = null;

    Object[] hours = new Object[] { "8", "9", "10", "11", "12", "1", "2", "3", "4", "5" };
    Object[] mins  = new Object[] { "00", "10", "20", "30", "40", "50" };
    Object[] ampm = new Object[] { "AM", "PM" };

    int selectedRow = 0;
    int selectedCol = 0;

    JPanel clinicPanel = null;
    JPanel ntPanel = null;
    JPanel crPanel = null;
    JPanel eligPanel = null;
    JPanel walkInPanel = null;

    JEditorPane patInfoPane = null;

    // data
    ApptCell apptCell = null;
    Appointment appt = null;
    Patient pat = null;

    // showup Dialog
    JDialog showupDialog;
    JDialog noshowDialog;
    JTextArea showupNotesTA;
    JTextArea noshowNotesTA;
    JComboBox reasonCodeCB;
    JLabel l1 = new JLabel("Notes:  ");
    JLabel l2 = new JLabel("Reason: ");

    public ApptCellEditor(MainFrame f){
        this.f = f;
    }

    /**
     * Adds a listener to the list that's notified when the editor stops, or
     * cancels editing.
     *
     * @param l the CellEditorListener
     * @todo Implement this javax.swing.CellEditor method
     */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
     * Tells the editor to cancel editing and not accept any partially edited
     * value.
     *
     * @todo Implement this javax.swing.CellEditor method
     */
    public void cancelCellEditing() {
        // editing is cancelled, hide dialog
        apptDialog.setVisible(false);
        apptDialog.dispose();
    }

    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     * @todo Implement this javax.swing.CellEditor method
     */
    public Object getCellEditorValue() {
        return apptCell;
    }

    /**
     * Sets an initial <code>value</code> for the editor.
     *
     * @param table the <code>JTable</code> that is asking the editor to
     *   edit; can be <code>null</code>
     * @param value the value of the cell to be edited; it is up to the
     *   specific editor to interpret and draw the value. For example, if
     *   value is the string "true", it could be rendered as a string or it
     *   could be rendered as a check box that is checked. <code>null</code>
     *   is a valid value
     * @param isSelected true if the cell is to be rendered with highlighting
     * @param row the row of the cell being edited
     * @param column the column of the cell being edited
     * @return the component for editing
     * @todo Implement this javax.swing.table.TableCellEditor method
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row,
                                                 int column) {
        apptTable = table;
        selectedRow = row;
        selectedCol = column;
        if (column == 1 || column == 2 ){
            isApptCellSelected = true;
            /* This is where we get the current appt cell
               We store it in the dialog in case the user starts editing
             */
            apptCell = (ApptCell) value;
            appt = apptCell.getAppointment();            
            if (appt == null){
                    createNewApptDialog(null);
            }else{
                createApptDialog(appt);
                showReminder(appt);
            }
        }else{
            isApptCellSelected = false;
        }
        return super.getTableCellRendererComponent(table, value, isSelected,
            true, row, column);
    }

    private void showReminder(Appointment appt){
        if (appt != null && appt.shouldCheckReminder()){
            if (pat.getReminder() != null && !pat.getReminder().trim().equals("")){
                int reply = JOptionPane.showConfirmDialog(apptDialog,
                    pat.getReminder(),
                    "Reminder: "+pat.getFullName(),
                    JOptionPane.OK_CANCEL_OPTION);
                if (reply == JOptionPane.OK_OPTION) {
                    // delete reminder
                    WriteSvc.getInstance().clearReminder(pat.getRefId());
                }
            }
        }
    }

    private void createNewApptDialog(Appointment appt){
        initProviderPane(appt);
        initApptTypePane();
        initEvalYearPane();
        initPatEntryPane();
        initStartTimePane(true);
        initEndTimePane(true);
        initNotesPane(true);
        initClinicPane(true);
        initNtPane(true);
        initCrPane(true);
        initEligPane(true);
        initWalkInPane(true);
        initActionComponent();

        // setting default value
        apptRadioButton.setSelected(true);
        patientTextField.setText("");
        notesTextArea.setText("");
        clinicCB.setSelectedItem(Constant.selectedClinic);
        
        ntNoB.setSelected(true);
        CardLayout cl = (CardLayout)langCards.getLayout();
        cl.show(langCards, "emptyPanel");

        crYesB.setSelected(true);
        
        walkInNoB.setSelected(true);
        ((CardLayout)authNumCards.getLayout()).show(authNumCards, "authNumPanel");
        ((CardLayout)countyNumCards.getLayout()).show(countyNumCards, "countyNumPanel");                 
        
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
        actionPanel.add(closeButton);

        apptDialog = new JDialog(f, "Schedule Appointment", true);
        apptDialog.setSize(450, 700);
        //apptDialog.setResizable(false);
        apptDialog.setLocationRelativeTo(f);
        apptDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                fireEditingCanceled();
            }
        });

        dialogPanel = createNewApptPanel();
        
        apptDialog.add(dialogPanel);
    }
    
    private JPanel createNewApptPanel(){
    	JPanel mainPanel = new JPanel(new BorderLayout());
		
        JPanel subPanel1 = new JPanel(new GridLayout(14, 1));
        subPanel1.add(providerPanel);
        subPanel1.add(apptTypePanel);
        subPanel1.add(evalYearCards);
        subPanel1.add(patCards);
        subPanel1.add(startTimePanel);
        subPanel1.add(endTimePanel);
        subPanel1.add(clinicPanel);
        subPanel1.add(ntPanel);
        subPanel1.add(langCards);
        subPanel1.add(crPanel);
        subPanel1.add(eligPanel);
        subPanel1.add(walkInPanel);
        subPanel1.add(authNumCards);
        subPanel1.add(countyNumCards);

        JPanel subPanel2 = new JPanel(new BorderLayout());
        subPanel2.add(notesLabel, BorderLayout.NORTH);
        subPanel2.add(notesSP, BorderLayout.CENTER);
        subPanel2.add(blankLabel, BorderLayout.EAST);
        subPanel2.add(blankLabel, BorderLayout.WEST);
        
        JPanel subPanel3 = new JPanel(new GridLayout(2, 1));
        subPanel3.add(actionPanel);
        
        mainPanel.add(subPanel1, BorderLayout.NORTH);
        mainPanel.add(subPanel2, BorderLayout.CENTER);
        mainPanel.add(subPanel3, BorderLayout.SOUTH);
        return mainPanel;
    }

    private void createApptDialog(final Appointment appt){
        initPatInfoPane();
        initProviderPane(appt);
        initApptTypePane();
        initEvalYearPane();
        initPatNamePane();
        initStartTimePane(false);
        initEndTimePane(false);
        initNotesPane(appt.allowEdit());
        initClinicPane(appt.allowEdit());
        initNtPane(appt.allowEdit());
        initCrPane(appt.allowEdit());
        initEligPane(appt.allowEdit());
        initWalkInPane(appt.allowEdit());
        initActionComponent();

        // setting default value
        if (appt.getType().equals(Constant.Blocked)){
            blockedButton.setSelected(true);
            CardLayout cl = (CardLayout)patCards.getLayout();
            cl.show(patCards, "emptyPanel");
        }else{
            apptRadioButton.setSelected(true);
            CardLayout cl = (CardLayout)patCards.getLayout();
            cl.show(patCards, "patientPanel");
        }
        patientTextField.setText(appt.getFullName());
        clinicCB.setSelectedItem(appt.getClinic());
        if (appt.needTranSvc()){
            ntYesB.setSelected(true);            
            CardLayout cl = (CardLayout)langCards.getLayout();
            cl.show(langCards, "langPanel");
            langTF.setText(appt.getLang());
        }else{
            ntNoB.setSelected(true);
            CardLayout cl = (CardLayout)langCards.getLayout();
            cl.show(langCards, "emptyPanel");
            langTF.setText("");
        }
        if (appt.collateralReceived()){
            crYesB.setSelected(true);
        }else{
            crNoB.setSelected(true);
        }
        if (appt.isEligible().equals("yes")){
        	eligYesB.setSelected(true);
        }else if (appt.isEligible().equals("no")){
        	eligNoB.setSelected(true);
        }
        if (appt.isWalkIn()){
        	walkInYesB.setSelected(true);        	
            //((CardLayout)authNumCards.getLayout()).show(authNumCards, "authNumPanel");            
            //((CardLayout)countyNumCards.getLayout()).show(countyNumCards, "countyNumPanel");
        }else{
        	walkInNoB.setSelected(true);        	
            //((CardLayout)authNumCards.getLayout()).show(authNumCards, "emptyPanel");            
            //((CardLayout)countyNumCards.getLayout()).show(countyNumCards, "emptyPanel");        	
        }
        authNumTF.setText(appt.getAuthNum());
        countyNumTF.setText(appt.getCountyNum());
        
        notesTextArea.setText(appt.getNotes());
        
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
        if (appt != null && appt.allowEdit()){
            actionPanel.add(closeButton);        	
            if (!appt.isBlockTime()){
                actionPanel.add(cancelByPatientButton);
                actionPanel.add(cancelByClinicButton);
            }
            actionPanel.add(deleteButton);            
        }else{
            // for non-editable appt
            actionPanel.add(closeButton);
        }
        if (appt.allowUndo()){
            undoButton.setText("Undo "+appt.getLastAction());
            actionPanel.add(undoButton);
        }
        actionPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
        actionPanel2.add(showupButton);
        actionPanel2.add(noShowsButton);

        apptDialog = new JDialog(f, "Appointment Detail", true);
        apptDialog.setSize(450, 800);
        //apptDialog.setResizable(false);
        apptDialog.setLocationRelativeTo(f);
        apptDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                fireEditingCanceled();
            }
        });
        apptDialog.setLayout(new BorderLayout());

        dialogPanel = createApptPanel();
        
        if (!appt.getType().equals(Constant.Blocked)){
        	JPanel patInfoPanel = new JPanel(new BorderLayout());
        	patInfoPanel.add(patInfoPane, BorderLayout.NORTH);
        	JButton insButton = new JButton("Insurance");
        	insButton.addActionListener(new ActionListener(){
        		public void actionPerformed(ActionEvent e){
        	    	new PatientDialog(apptDialog, appt.getReferralId(), 2, f.getNextLocX(), f.getNextLocY(), true);        			
        		}
        	});
        	
        	JPanel insButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        	JLabel showInsLabel = new JLabel("Show Insurance: ");
        	insButtonPanel.add(showInsLabel);
        	insButtonPanel.add(insButton);
        	patInfoPanel.add(insButtonPanel, BorderLayout.CENTER);
        	apptDialog.add(patInfoPanel, BorderLayout.NORTH);
        }        
        apptDialog.add(dialogPanel, BorderLayout.CENTER);
    }
    
    private JPanel createApptPanel(){
    	JPanel mainPanel = new JPanel(new BorderLayout());
    		
        JPanel subPanel1 = new JPanel(new GridLayout(12, 1));
        subPanel1.add(providerPanel);
        subPanel1.add(patCards);
        subPanel1.add(startTimePanel);
        subPanel1.add(endTimePanel);
        subPanel1.add(clinicPanel);
        if (!appt.getType().equals(Constant.Blocked)){
            subPanel1.add(ntPanel);
            subPanel1.add(langCards);
            subPanel1.add(crPanel);
            subPanel1.add(eligPanel);
            subPanel1.add(walkInPanel);
            subPanel1.add(authNumCards);
            subPanel1.add(countyNumCards);
        }
        JPanel subPanel2 = new JPanel(new BorderLayout());
        subPanel2.add(notesLabel, BorderLayout.NORTH);
        subPanel2.add(notesSP, BorderLayout.CENTER);
        subPanel2.add(blankLabel, BorderLayout.EAST);
        subPanel2.add(blankLabel, BorderLayout.WEST);
        JPanel subPanel3 = new JPanel(new GridLayout(2, 1));
        subPanel3.add(actionPanel);
        if (shouldShowAction2()){
            subPanel3.add(actionPanel2);
        }
        mainPanel.add(subPanel1, BorderLayout.NORTH);
        mainPanel.add(subPanel2, BorderLayout.CENTER);
        mainPanel.add(subPanel3, BorderLayout.SOUTH);
        return mainPanel;
    }
    
    private boolean shouldShowAction2(){
        if (!appt.getType().equals(Constant.Blocked) &&
            !ReadSvc.getInstance().isApptCompleted(appt.getApptId())){
            return true;
        }else{
            return false;
        }
    }

    private String getHour(){
       int hour = 8;
       if (selectedCol == 1 || selectedCol == 2){
           hour += (selectedRow / 6);
           if (hour > 12){
               hour -= 12;
           }
       }
       return String.valueOf(hour);
   }

   private String getMin(){
       int min = (selectedRow % 6) * 10;
       if (min == 0){
           return "00";
       }else{
           String retVal = String.valueOf(min);
           return retVal;
       }
   }

   private String getAMPM(){
       if (selectedRow < (6*4)){
           return "AM";
       }else{
           return "PM";
       }
   }

   private String getHour(int h){
       int hour = h;
       if (hour > 12){
           hour -= 12;
       }
       return String.valueOf(hour);
   }

   private String getMin(int m){
       if (m == 0){
           return "00";
       }else{
           return String.valueOf(m);
       }
   }

   private String getAMPM(int ampm){
       switch (ampm) {
           case Calendar.AM:
               return "AM";
           case Calendar.PM:
               return "PM";
       }
       return "AM";
   }


    /**
     * Asks the editor if it can start editing using <code>anEvent</code>.
     *
     * @param anEvent the event the editor should use to consider whether to
     *   begin editing or not
     * @return true if editing can be started
     * @todo Implement this javax.swing.CellEditor method
     */
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent)anEvent).getClickCount() >= 2;
        }
        return true;
    }

    /**
     *
     * @param l the CellEditorListener
     * @todo Implement this javax.swing.CellEditor method
     */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Returns true if the editing cell should be selected, false otherwise.
     *
     * @param anEvent the event the editor should use to start editing
     * @return true if the editor would like the editing cell to be
     *   selected; otherwise returns false
     * @todo Implement this javax.swing.CellEditor method
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        if (isApptCellSelected){
            // start editing
            apptDialog.setVisible(true);
            // tell caller it is ok to select this cell
        }
        return false;
    }

    /**
     * Tells the editor to stop editing and accept any partially edited value
     * as the value of the editor.
     *
     * @return true if editing was stopped; false otherwise
     * @todo Implement this javax.swing.CellEditor method
     */
    public boolean stopCellEditing() {
        // editing is cancelled, hide dialog
        apptDialog.setVisible(false);
        apptDialog.dispose();
        return true;
    }

    private void cancelAppointment(final boolean isCancelyByPatient, final boolean is24Hrs, String cancelReason, String cancelOtherReason){
        final Appointment appt = apptCell.getAppointment();
        //if (PSService.getService().hasDeletePermission(appt)){       
        appt.setCancelByPatient(isCancelyByPatient);
        appt.setCancelByClinic(!isCancelyByPatient);
        appt.setWnTwentyFourHrs(is24Hrs);
        appt.setCancelReason(cancelReason);
        appt.setCancelOtherReason(cancelOtherReason);
        WriteSvc.getInstance().cancelAppt(appt);
        DailyApptTableModel model = (DailyApptTableModel) apptTable.getModel();
        model.deleteAppt(appt);
        refreshTable();
            /*
        }else{
            final JDialog passDiag = new JDialog(apptDialog, "Enter password", true);
            passDiag.setLayout(new BorderLayout());
            final JLabel label = new JLabel("Please enter password for "+appt.getOwner());
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            labelPanel.add(label);
            final JTextField passField = new JPasswordField(20);
            JPanel passPanel = new JPanel();
            passPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            passPanel.add(passField);
            passDiag.add(labelPanel, BorderLayout.NORTH);
            passDiag.add(passPanel, BorderLayout.CENTER);
            JButton okB = new JButton("Ok");
            JButton cancelB = new JButton("Cancel");
            JPanel bPanel = new JPanel();
            bPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            bPanel.add(okB);
            bPanel.add(cancelB);
            passDiag.add(bPanel, BorderLayout.SOUTH);
            
            okB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String pass = passField.getText();
                    if (PSService.getService().login(appt.getOwner(), pass)){
                        WriteSvc.getInstance().cancelAppt(appt, isCancelyByPatient, is24Hrs);
                        DailyApptTableModel model = (DailyApptTableModel) apptTable.getModel();
                        model.deleteAppt(appt);
                        passDiag.setVisible(false);
                        passDiag.dispose();
                        refreshTable();
                    }else{
                        JOptionPane.showMessageDialog(f, "Invalid password",
                            "Invalid Password", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            cancelB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    passDiag.setVisible(false);
                    passDiag.dispose();
                }
            });

            passDiag.setSize(270, 160);
            passDiag.setLocation(400, 200);
            passDiag.setVisible(true);
            passDiag.setResizable(false);
        }
		*/
    }

    private void deleteAppointment(){
        final Appointment appt = apptCell.getAppointment();
        if (PSService.getService().hasDeletePermission(appt)){
            WriteSvc.getInstance().deleteAppt(appt);
            DailyApptTableModel model = (DailyApptTableModel) apptTable.getModel();
            model.deleteAppt(appt);
            refreshTable();
        }else{
            final JDialog passDiag = new JDialog(apptDialog, "Enter password", true);
            passDiag.setLayout(new BorderLayout());
            final JLabel label = new JLabel("Please enter password for "+appt.getOwner());
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            labelPanel.add(label);
            final JTextField passField = new JPasswordField(20);
            JPanel passPanel = new JPanel();
            passPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            passPanel.add(passField);
            passDiag.add(labelPanel, BorderLayout.NORTH);
            passDiag.add(passPanel, BorderLayout.CENTER);
            JButton okB = new JButton("Ok");
            JButton cancelB = new JButton("Cancel");
            JPanel bPanel = new JPanel();
            bPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            bPanel.add(okB);
            bPanel.add(cancelB);
            passDiag.add(bPanel, BorderLayout.SOUTH);
            okB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String pass = passField.getText();
                    if (PSService.getService().login(appt.getOwner(), pass)){
                        WriteSvc.getInstance().deleteAppt(appt);
                        DailyApptTableModel model = (DailyApptTableModel) apptTable.getModel();
                        model.deleteAppt(appt);
                        passDiag.setVisible(false);
                        passDiag.dispose();
                        refreshTable();
                    }else{
                        JOptionPane.showMessageDialog(f, "Invalid password",
                            "Invalid Password", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            cancelB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    passDiag.setVisible(false);
                    passDiag.dispose();
                }
            });

            passDiag.setSize(270, 160);
            passDiag.setLocation(400, 200);
            passDiag.setVisible(true);
            passDiag.setResizable(false);
        }
    }

    private void createAppointment(){
        // referralId, clinicName, appointmentDate, provider,
        // translationSvcNeeded, collateralReceived, notes, userId, endTime, type

        Appointment appt = new Appointment();
        appt.setOwner(Constant.appUser);

        if (blockedButton.isSelected()){
            appt.setType(Constant.Blocked);
        }else{
            if (selectedCol == 1){
                appt.setType(Constant.Appointment);
            }else{
                appt.setType(Constant.DoubleBook);
            }
        }
        
        String pat = patientTextField.getText();        
        int refId = -1;
        if (!appt.isBlockTime()){
	        String firstName = null;
	        String lastName = null;
	        if (pat != null){
	            int index = pat.indexOf(", ");
	            if (index > 0){
	                lastName = pat.substring(0, index);
	                firstName = pat.substring(index+2);
	                appt.setFirstName(firstName);
	                appt.setLastName(lastName);	                	                
	                refId = PSService.getService().getPatientId(lastName, firstName);
	                Date d = ReadSvc.getInstance().getPatientBirthDate(refId);
	                appt.setBirthDate(d);
	            }
	        }
        }
        
        // check if patient is selected
        if ((apptRadioButton.isSelected() || evalRadioButton.isSelected()) && (refId == -1)){
            JOptionPane.showMessageDialog(apptDialog,
                                          "Cannot find patient "+pat+"\nPlease enter a valid patient name!",
                                          "Unknown Patient",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Calendar startTime = (Calendar)f.selectedDate.clone();
        int stYear = f.selectedDate.get(Calendar.YEAR);
        int stMonth = f.selectedDate.get(Calendar.MONTH);
        int stDay = f.selectedDate.get(Calendar.DAY_OF_MONTH);
        int stHour = Integer.parseInt((String)startTimeHourComboBox.getSelectedItem());
        int stMin = Integer.parseInt((String)startTimeMinComboBox.getSelectedItem());
        if (startTimeAMPMComboBox.getSelectedItem().equals("PM") && stHour != 12){
            stHour += 12;
        }
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.set(stYear, stMonth, stDay, stHour, stMin, 0);

        Calendar endTime = (Calendar)f.selectedDate.clone();
        int etYear = f.selectedDate.get(Calendar.YEAR);
        int etMonth = f.selectedDate.get(Calendar.MONTH);
        int etDay = f.selectedDate.get(Calendar.DAY_OF_MONTH);
        int etHour = Integer.parseInt((String)endTimeHourComboBox.getSelectedItem());
        int etMin = Integer.parseInt((String)endTimeMinComboBox.getSelectedItem());
        if (endTimeAMPMComboBox.getSelectedItem().equals("PM") && etHour != 12){
            etHour += 12;
        }
        endTime.set(Calendar.MILLISECOND, 0);
        endTime.set(etYear, etMonth, etDay, etHour, etMin, 0);
        
        // start time end time check
        if (startTime.equals(endTime)){
            JOptionPane.showMessageDialog(apptDialog,
                                          "Appointment must be at least 10 minutes long.\n"+
                                          "Please select a valid start time and end time for appointment!",
                                          "Invalid Appointment Time",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (startTime.after(endTime)){
            JOptionPane.showMessageDialog(apptDialog,
                                          "End Time cannot be before start time!\n"+
                                          "Please select a valid start time and end time for appointment!",
                                          "Invalid Appointment Time",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // check if overlap lunch break
        if (startTime.after(Constant.DEC_FIRST_CALENDAR) && isApptOverlapLunchBreak(startTime, endTime)){
            JOptionPane.showMessageDialog(apptDialog,
                    "Lunch Break for All Clinics 12:00PM ~ 12:30PM.\n"+                    
                    "Schedule appointment during lunch break is not allowed!\n",
                    "Invalid Appointment Time",
                    JOptionPane.ERROR_MESSAGE);        	
        	return;
        }
        
        // check evaluation
        if (evalRadioButton.isSelected()){
            // check eval hour > 60 min
        	long durInMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        	if (durInMillis < ONE_HOUR_IN_MILLIS){
                JOptionPane.showMessageDialog(apptDialog,
                        "Evaluation appointment must be at least 60 minutes long.\n Please select a valid start time and end time for appointment.",
                        "Invalid Evaluation Time",
                        JOptionPane.ERROR_MESSAGE);
                return;
        	}
        	        	
        	// check if eval already scheduled
        	String evalYear = (String)evalYearCB.getSelectedItem();
        	List<Appointment> evalAppts = ReadSvc.getInstance().getEvaluationApptForPatient(refId, evalYear);
        	if (evalAppts.size() > 0){
        		Appointment evalAppt = evalAppts.get(0);
                JOptionPane.showMessageDialog(apptDialog,
                        "Cannot schedule "+evalYear+" evaluation because\n"+
                        "evaluation appointment is already scheduled for "+pat+"\n"+
                        " with "+evalAppt.getProvider()+" on "+evalAppt.getApptDateStr()+ " "+evalAppt.getStartTimeStr(),
                        "Evaluation exist",
                        JOptionPane.ERROR_MESSAGE);
                return;        		
        	}
        	
        	// check if ins is contra costa
        	Patient patient = ReadSvc.getInstance().getPatientById(refId);
        	List<Insurance> insList = patient.getIns();
        	boolean isContraCosta = false;
        	for (Insurance ins : insList){
        		if (ins.getInsCompany().equals(Constant.CONTRA_COSTA_ACCESS)){
        			isContraCosta = true;
        			break;
        		}
        	}                            	
        	if (!isContraCosta){
                JOptionPane.showMessageDialog(apptDialog,
                        "Cannot schedule evaluation appointment because patient is not belong to "+Constant.CONTRA_COSTA_ACCESS,
                        "Invalid Evaluation",
                        JOptionPane.ERROR_MESSAGE);        		
                return;
        	}
        	
        	appt.setEval(true);
        	appt.setEvalYear(evalYear);        	
        }else{
        	appt.setEval(false);
        	appt.setEvalYear(null);        	
        }               
        
        appt.setReferralId(refId);
        
        appt.setClinic((String)clinicCB.getSelectedItem());
        
        appt.setCollateralReceived(crYesB.isSelected());
        
        if (eligYesB.isSelected()){
        	appt.setIsEligible("yes");
        }else if (eligNoB.isSelected()){
        	appt.setIsEligible("no");
        }
        
        if (ntYesB.isSelected()){
            appt.setNeedTranSvc(true);
            appt.setLang(langTF.getText());
        }else{
            appt.setNeedTranSvc(false);
            appt.setLang("");
        }

        // check authNum and countyNum for walkIn appt
        if (walkInYesB.isSelected()){
        	appt.setWalkIn(true);   
        	if (authNumTF.getText() == null || authNumTF.getText().trim().equals("")){
        		JOptionPane.showMessageDialog(apptDialog, "Please specify Authorization # for Walk-in appointment!",
        				"Missing Authoriztion #", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	if (countyNumTF.getText() == null || countyNumTF.getText().trim().equals("")){
        		JOptionPane.showMessageDialog(apptDialog, "Please specify County # for Walk-in appointment!",
        				"Missing County #", JOptionPane.ERROR_MESSAGE);
        		return;        	
        	}        	
        }else{
        	appt.setWalkIn(false);
        }
        
    	appt.setAuthNum(authNumTF.getText());
    	appt.setCountyNum(countyNumTF.getText());        
        
        appt.setStartDate(startTime.getTime());
        appt.setEndDate(endTime.getTime());

        appt.setNotes(notesTextArea.getText());
        appt.setProvider(f.apptTableModel.getProvider());

        // check overlap appt
        List overlapAppts = PSService.getService().getOverlapAppt(
            appt.getProvider(), appt.getStartDate(), appt.getEndDate(), appt.getType());
        if (overlapAppts.size() > 0){
            DailyApptTableModel model = (DailyApptTableModel)apptTable.getModel();
            for (int i=0; i<overlapAppts.size(); i++){
                model.addAppt((Appointment)overlapAppts.get(i));
            }
            refreshTable();
            JOptionPane.showMessageDialog(apptDialog,
                                          "Time conflict, "+
                                          "there is overlap between "+Constant.tf_s.format(appt.getStartDate())+" to "+
                                          Constant.tf_s.format(appt.getEndDate())+" with existing appointment!\n"+
                                          "Please select another time for the appointment.",
                                          "Time Conflict",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }

        // note size check
        if (appt.getNotes() != null && appt.getNotes().length() > 200){
            JOptionPane.showMessageDialog(apptDialog, "Appointment Notes is too long. Maximum size is 200. Please delete some text in the Notes.",
                                          "Notes too long", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (appt.isBlockTime()){
            appt.setReferralId(-1);
            appt.setFirstName("");
            appt.setLastName("");
            appt.setIsEligible("");
        }
        
        appt.setApptId(ReadSvc.getInstance().getNextSeq(Constant.APPOINTMENT));
        appt.setStatus(Constant.Scheduled);
        
        // outstanding balance check
        if (!appt.getType().equals(Constant.Blocked)){
        	BigDecimal bal = ReadSvc.getInstance().getBalForPatient(appt.getReferralId());
        	if (bal.compareTo(BigDecimal.ZERO) > 0){
        		bal = bal.setScale(2, BigDecimal.ROUND_HALF_UP);
        		JOptionPane.showMessageDialog(dialogPanel, 
        				"Appointment cannot be scheduled because patient has Outstanding Balance of $"+bal+".\n"+
        				"Please call the Billing Department to resolve the issue!", "Outstanding Balance Error",
        				JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        }
        
    	// credential check
    	String provider = f.apptTableModel.getProvider();
        if (!PSService.getService().checkCredential(provider, refId)){
            JOptionPane.showMessageDialog(apptDialog, provider+" does not have the credential to see patient "+pat);
            return;
        }
    	
        // check if evaluation already done
    	Patient evalPat = ReadSvc.getInstance().getEvalPatientByInsAndId(refId, Constant.CONTRA_COSTA_ACCESS);        
        if (!appt.isBlockTime() && !appt.isEval()){
        	if (evalPat != null){
		        if ((evalPat.getInsAdmitYear() < stYear)){

		        	int errYear = PSService.getService().checkEvaluation(evalPat, stYear - 1, startTime.getTime());		        	
		        	if (errYear != 0){
			            if (Constant.appRole.contains(Constant.ADMINISTRATOR) || Constant.appRole.contains(Constant.BILLING)){
			    			int val = JOptionPane.showConfirmDialog(dialogPanel,
			    					errYear+" evaluation is not done for "+pat+"\n"+
			    					"Do you still want to schedule the appointment?",
			        				"Evaluation warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			        			if (val == JOptionPane.NO_OPTION){
			        				return;
			        			}            
			            }else{        	
			            	JOptionPane.showMessageDialog(apptDialog, "Schedule appointment is not allowed for "+pat+"\n "+
			            			"because annual evaluation is not done for year "+errYear+".\nPlease contact the billing department to resolve the issue.");
			                return;
			            }
		        	}		        		
		        	
		        	errYear = PSService.getService().checkEvaluation(evalPat, stYear, startTime.getTime());		        	
		        	if (errYear != 0){
			            if (Constant.appRole.contains(Constant.ADMINISTRATOR) || Constant.appRole.contains(Constant.BILLING)){
			    			int val = JOptionPane.showConfirmDialog(dialogPanel,
			    					errYear+" evaluation is not done for "+pat+"\n"+
			    					"Do you still want to schedule the appointment?",
			        				"Evaluation warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			        			if (val == JOptionPane.NO_OPTION){
			        				return;
			        			}            
			            }else{        	
			            	JOptionPane.showMessageDialog(apptDialog, "Schedule appointment is not allowed for "+pat+"\n "+
			            			"because annual evaluation is not done for year "+errYear+".\nPlease contact the billing department to resolve the issue.");
			                return;
			            }
		        	}
		        }
        	}
        }
        
        // check eligibility
        if (!appt.isBlockTime() && !appt.isEval()){
	        int numInElig = ReadSvc.getInstance().getNumInEligibleAppt(refId);
	        if (numInElig > 0){
	        	JOptionPane.showMessageDialog(apptDialog, "Schedule appointment is not allowed for "+pat+"\n "+
	        			"because patient has "+numInElig+" appointment with \"no\" eligibility check.\n"+
	        			"Please contact billing to resolve the issue.", "Patient not eligible", JOptionPane.ERROR_MESSAGE);
	            return;        	
	        }
        }
        
    	// check if HealthPac ins        
        if (!appt.isBlockTime()){
        	Patient patient = ReadSvc.getInstance().getPatientById(refId);
        	List<Insurance> insList = patient.getIns();
        	boolean isHealthPac = false;
        	for (Insurance ins : insList){
        		if (ins.getInsCompany().startsWith(Constant.HEALTHPAC_PREFIX)){
        			isHealthPac = true;
        			break;
        		}
        	}                            	
        	if (isHealthPac){
                JOptionPane.showMessageDialog(apptDialog,
                        "This is a HealthPac client, contact PTW case manager at 510-872-2422",
                        "Reminder",
                        JOptionPane.INFORMATION_MESSAGE);        		
        	}        	
        }        
        
        // create new appointment
        try{	            	
            PSService.getService().createAppointment(appt);
            DailyApptTableModel model = (DailyApptTableModel)apptTable.getModel();
            model.addAppt(appt);
        }catch(Exception e){
            JOptionPane.showMessageDialog(apptDialog, "Resource busy, please retry to schedule the appointment again.");
        }                       
        
        needTranslationAlert(appt);
        
        if (!appt.isBlockTime() && !appt.isEval()){
        	if (evalPat != null){
        		evaluationAlert(evalPat);
        	}
        }
        
        refreshTable();
    }
    
    private boolean isApptOverlapLunchBreak(Calendar startTime, Calendar endTime){
    	//check if time is overlap 12pm and 12:30pm
    	
    	Calendar lunchBreakStartTime = new GregorianCalendar();
    	lunchBreakStartTime.setTime(startTime.getTime());
    	lunchBreakStartTime.set(Calendar.HOUR_OF_DAY, 12);
    	lunchBreakStartTime.set(Calendar.MINUTE, 0);
    	lunchBreakStartTime.set(Calendar.SECOND, 0);
    	lunchBreakStartTime.set(Calendar.MILLISECOND, 0);

    	Calendar lunchBreakEndTime = new GregorianCalendar();
    	lunchBreakEndTime.setTime(startTime.getTime());
    	lunchBreakEndTime.set(Calendar.HOUR_OF_DAY, 12);
    	lunchBreakEndTime.set(Calendar.MINUTE, 30);
    	lunchBreakEndTime.set(Calendar.SECOND, 0);
    	lunchBreakEndTime.set(Calendar.MILLISECOND, 0);

    	if (startTime.before(lunchBreakEndTime) && endTime.after(lunchBreakStartTime)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    private void needTranslationAlert(Appointment appt){
        if (appt.getType().equals(Constant.Blocked)){
        	return;
        }
        if (appt.needTranSvc()){
            JOptionPane.showMessageDialog(apptDialog,
            		"Patient requires translation services for appointment.\n"+
            		"Please contact Language Line Solutions for On Demand \n"+
            		"over the phone interpreting.  \n"+
            		"Phone:  866.874.3972. Client ID: 298506.",
                    "Need Translation Service Alert",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void evaluationAlert(Patient pat){
		Calendar today = new GregorianCalendar();
		Calendar admitDate = new GregorianCalendar();
		admitDate.setTime(pat.getInsAdmitDate());
		admitDate.set(Calendar.YEAR, today.get(Calendar.YEAR));
		
		if (today.after(admitDate)){
			admitDate.set(Calendar.YEAR, admitDate.get(Calendar.YEAR)+1);
		}
		
		long deltaInMillis = admitDate.getTimeInMillis() - today.getTimeInMillis();
		if (deltaInMillis <= SIXTY_DAYS_IN_MILLIS){
			int numDays = (int)(deltaInMillis / ONE_DAY_IN_MILLIS)+1;
            JOptionPane.showMessageDialog(apptDialog,
                    "Annual Evaluation is due in "+numDays+" days.\n"+
                    "Please schedule a 60 minute annual evaluation for "+pat.getLastName()+", "+pat.getFirstName()+"!",
                    "Annual Evaluation Alert",
                    JOptionPane.INFORMATION_MESSAGE);    			
		}
    }
    
    private void refreshTable(){
        fireEditingStopped();
        apptTable.setVisible(false);
        apptTable.setVisible(true);
        apptDialog.setVisible(false);
        apptDialog.dispose();
    }

    private Appointment constructNewAppt(Appointment oldAppt){
        Appointment appt = new Appointment();
        appt.copyValue(oldAppt);
        
        int refId = -1;
        String pat = patientTextField.getText();
        String fName = null;
        String lName = null;
        if (pat != null) {
            int index = pat.indexOf(", ");
            if (index > 0) {
                lName = pat.substring(0, index);
                fName = pat.substring(index + 2);
                refId = PSService.getService().getPatientId(lName, fName);
            }
        }
        if (apptRadioButton.isSelected() && (refId == -1)) {
            JOptionPane.showMessageDialog(apptDialog,
                                          "Cannot find patient " + pat +
                                          "\nPlease enter a valid patient name!",
                                          "Unknown Patient",
                                          JOptionPane.ERROR_MESSAGE);
            return null;
        }else {
            appt.setReferralId(refId);
            appt.setClinic( (String) clinicCB.getSelectedItem());
            appt.setCollateralReceived(crYesB.isSelected());
            
            if (eligYesB.isSelected()){
            	appt.setIsEligible("yes");
            }else if (eligNoB.isSelected()){
            	appt.setIsEligible("no");
            }
            
            if (ntYesB.isSelected()) {
                appt.setNeedTranSvc(true);
                appt.setLang(langTF.getText());
            }
            else {
                appt.setNeedTranSvc(false);
                appt.setLang("");
            }

            if (walkInYesB.isSelected()){
            	appt.setWalkIn(true);
            }else{
            	appt.setWalkIn(false);
            }
            
        	appt.setAuthNum(authNumTF.getText());
        	appt.setCountyNum(countyNumTF.getText());            
            
            Calendar startTime = (Calendar) f.selectedDate.clone();
            int stYear = f.selectedDate.get(Calendar.YEAR);
            int stMonth = f.selectedDate.get(Calendar.MONTH);
            int stDay = f.selectedDate.get(Calendar.DAY_OF_MONTH);
            int stHour = Integer.parseInt( (String) startTimeHourComboBox.getSelectedItem());
            int stMin = Integer.parseInt( (String) startTimeMinComboBox.getSelectedItem());
            if (startTimeAMPMComboBox.getSelectedItem().equals("PM") && stHour != 12) {
                stHour += 12;
            }
            startTime.set(Calendar.MILLISECOND, 0);
            startTime.set(stYear, stMonth, stDay, stHour, stMin, 0);
            appt.setStartDate(startTime.getTime());

            Calendar endTime = (Calendar) f.selectedDate.clone();
            int etYear = f.selectedDate.get(Calendar.YEAR);
            int etMonth = f.selectedDate.get(Calendar.MONTH);
            int etDay = f.selectedDate.get(Calendar.DAY_OF_MONTH);
            int etHour = Integer.parseInt( (String) endTimeHourComboBox.getSelectedItem());
            int etMin = Integer.parseInt( (String) endTimeMinComboBox.getSelectedItem());
            if (endTimeAMPMComboBox.getSelectedItem().equals("PM") && etHour != 12) {
                etHour += 12;
            }
            endTime.set(Calendar.MILLISECOND, 0);
            endTime.set(etYear, etMonth, etDay, etHour, etMin, 0);
            appt.setEndDate(endTime.getTime());

            appt.setFirstName(fName);
            appt.setLastName(lName);
            appt.setNotes(notesTextArea.getText());
            appt.setProvider(f.apptTableModel.getProvider());
            if (blockedButton.isSelected()) {
                appt.setType(Constant.Blocked);
                appt.setReferralId(-1);
                appt.setFirstName(null);
                appt.setLastName(null);
            }else {
                if (selectedCol == 1) {
                    appt.setType(Constant.Appointment);
                }
                else {
                    appt.setType(Constant.DoubleBook);
                }
            }

            if (startTime.equals(endTime)) {
                JOptionPane.showMessageDialog(apptDialog,
                                              "Appointment must be at least 10 minutes long\n" +
                                              "Please select a valid start time and end time for appointment!",
                                              "Invalid Appointment Time",
                                              JOptionPane.ERROR_MESSAGE);
                return null;
            }
            if (startTime.after(endTime)) {
                JOptionPane.showMessageDialog(apptDialog,
                                              "End Time cannot be before start time!\n" +
                                              "Please select a valid start time and end time for appointment!",
                                              "Invalid Appointment Time",
                                              JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return appt;
    }

    private void updateAppointment(){
        Appointment oldAppt = apptCell.getAppointment();
        Appointment newAppt = constructNewAppt(oldAppt);
        if (newAppt == null){
            return;
        }

        // update from old appt
        newAppt.setApptId(oldAppt.getApptId());
        newAppt.setStatus(oldAppt.getStatus());

        if (oldAppt.noChange(newAppt)){
            // no change to appt
            refreshTable();
            return;
        }

        List overlapAppts = PSService.getService().getOverlapAppt(
        newAppt.getProvider(), newAppt.getStartDate(), newAppt.getEndDate(), newAppt.getType());
        overlapAppts.remove(oldAppt);
        if (overlapAppts.size() > 0){
            DailyApptTableModel model = (DailyApptTableModel)apptTable.getModel();
            for (int i=0; i<overlapAppts.size(); i++){
                model.addAppt((Appointment)overlapAppts.get(i));
            }
            refreshTable();
            JOptionPane.showMessageDialog(apptDialog,
                                          "Time conflict, "+
                                          "there is overlap between "+Constant.tf_s.format(newAppt.getStartDate())+" to "+
                                          Constant.tf_s.format(newAppt.getEndDate())+" with existing appointment!\n"+
                                          "Please select another time for the appointment.",
                                          "Time Conflict",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newAppt.getNotes() != null && newAppt.getNotes().length() > 200){
            JOptionPane.showMessageDialog(apptDialog, "Appointment Notes is too long. Maximum size is 200. Please delete some text in the Notes.",
                                          "Notes too long", JOptionPane.ERROR_MESSAGE);
            return;
        }        

        if (newAppt.isWalkIn()){
        	if (newAppt.getAuthNum() == null || newAppt.getAuthNum().trim().equals("")){
        		JOptionPane.showMessageDialog(apptDialog, "Please specify Authorization # for Walk-in appointment!",
        				"Missing Authoriztion #", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	if (newAppt.getCountyNum() == null || newAppt.getCountyNum().trim().equals("")){
        		JOptionPane.showMessageDialog(apptDialog, "Please specify County # for Walk-in appointment!",
        				"Missing County #", JOptionPane.ERROR_MESSAGE);
        		return;        	
        	}        	
        }
        
        WriteSvc.getInstance().updateAppt(oldAppt, newAppt);

        DailyApptTableModel model = (DailyApptTableModel)apptTable.getModel();
        model.updateAppt(oldAppt, newAppt);

        if (newAppt.needTranSvc() && !oldAppt.needTranSvc()){
        	needTranslationAlert(newAppt);
        }
        
        refreshTable();
    }

    protected void fireEditingStopped(){
        Object[] listeners = listenerList.getListenerList();
       for (int i=listeners.length-2; i>=0; i-=2){
           ( (CellEditorListener) listeners[i+1]).editingCanceled(event);
       }
    }

    protected void fireEditingCanceled(){
        Object[] listeners = listenerList.getListenerList();
        for (int i=listeners.length-2; i>=0; i-=2){
            ((CellEditorListener)listeners[i+1]).editingCanceled(event);
        }
    }

    // Document Listener
    public void changedUpdate(DocumentEvent e){
        String text = "";
        try{
            text = e.getDocument().getText(0, e.getDocument().getLength());
        }catch(Exception exp){
            exp.printStackTrace();
        }
        //debug("changedUpdate = "+text);
        selectPatientInList(text);
    }

    public void insertUpdate(DocumentEvent e){
        String text = "";
        try{
            text = e.getDocument().getText(0, e.getDocument().getLength());
        }catch(Exception exp){
            exp.printStackTrace();
        }
        //debug("insertUpdate = "+text);
        selectPatientInList(text);
    }

    public void removeUpdate(DocumentEvent e){
        String text = "";
        try{
            text = e.getDocument().getText(0, e.getDocument().getLength());
        }catch(Exception exp){
            exp.printStackTrace();
        }
        //debug("removeUpdate = "+text);
        selectPatientInList(text);
    }

    private void selectPatientInList(String text){
        String textLc = text.toLowerCase();

        int size = patientListModel.size();
        String pat = null;
        int selectedIndex = 0;
        for (int i=0; i<size; i++){
            pat = ((Patient)patientListModel.getElementAt(i)).toString().toLowerCase();
            if (textLc != "" && pat.startsWith(textLc)){
                selectedIndex = i;
                break;
            }
        }
        patientList.setSelectedIndex(selectedIndex);
        patientList.ensureIndexIsVisible(selectedIndex);
        patientList.invalidate();
    }

    private void initPatInfoPane(){
        if (!appt.getType().equals(Constant.Blocked)){
            pat = ReadSvc.getInstance().getPatientById(appt.getReferralId());
            String patInfo =
                "<font color=black><b>Confirmation#:</b> "+appt.getApptId()+"<br>"+
                "<b>Appointment Status:</b> "+appt.getStatus()+"<br>";
            if (appt.getStatus().equals(Constant.NotSeen)){
                patInfo += "<b>NoShow Notes:</b> "+appt.getStatusNotes()+"<br>";
            }
            if (appt.getStatus().equals(Constant.Seen)){
                patInfo += "<b>Showup Notes:</b> "+appt.getStatusNotes()+"<br>";
                patInfo += "<b>Reason Code:</b> "+appt.getReasonCode()+"<br>";
            }
            patInfo +=
                "<b>Patient Id:</b> "+pat.getRefId()+"<br>" +
                "<b>Contact number:</b> "+pat.getPhoneNum()+"<br>"+
                "<b>Address: </b> "+pat.getFullAddressInHTML()+
                "<b>Previous Psychiatrist: </b>"+pat.getPreviousPsychiatrist()+"<br>"+
                "</font>";

            patInfoPane = new JEditorPane("text/html", patInfo);
            patInfoPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
            patInfoPane.setFocusable(false);
            patInfoPane.setBackground(RubyTheme.lightGreen);
        }
    }

    private void initProviderPane(Appointment appt){
    	//String providerText = "Provider:   "+f.getSelectedProviderNode().getUserObject().toString();
    	String providerText = "Provider:   "+f.apptTableModel.getProvider();
    	if (appt != null){
    		if (appt.isEval()){
    			providerText = providerText + "   (Annual Evaluation for "+appt.getEvalYear()+")";
    		}else{
    			providerText = providerText + "   ("+appt.getType()+")";    		
    		}
    	}
        providerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        providerLabel.setText(providerText);
        providerPanel.add(providerLabel);
    }

    private void initApptTypePane(){
        apptTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        apptTypeButtonGroup = new ButtonGroup();
        apptRadioButton = new JRadioButton("appointment");
        blockedButton = new JRadioButton("blocked");
        evalRadioButton = new JRadioButton("evaluation");

        apptTypeButtonGroup.add(apptRadioButton);
        apptTypeButtonGroup.add(blockedButton);
        apptTypeButtonGroup.add(evalRadioButton);
        apptRadioButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                CardLayout cl = (CardLayout)patCards.getLayout();
                cl.show(patCards, "patientPanel");
                CardLayout cl2 = (CardLayout) evalYearCards.getLayout();
                cl2.show(evalYearCards, "emptyPanel");                                
            }
        });
        blockedButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) patCards.getLayout();
                cl.show(patCards, "emptyPanel");
                CardLayout cl2 = (CardLayout) evalYearCards.getLayout();
                cl2.show(evalYearCards, "emptyPanel");                
            }
        });
        evalRadioButton.addActionListener( new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		CardLayout cl = (CardLayout) patCards.getLayout();
        		cl.show(patCards, "patientPanel");
                CardLayout cl2 = (CardLayout) evalYearCards.getLayout();
                cl2.show(evalYearCards, "evalYearPanel");                        		
        	}
        });

        apptTypePanel.add(apptRadioButton);
        apptTypePanel.add(blockedButton);
        apptTypePanel.add(evalRadioButton);
    }

    private void initEvalYearPane(){
    	evalYearCards = new JPanel(new CardLayout());
    	evalYearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
    	emptyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
    	GregorianCalendar cal = new GregorianCalendar();
    	int year = cal.get(Calendar.YEAR);
    	List<String> years = new ArrayList<String>();
    	years.add(""+(year - 1));
    	years.add(""+year);
    	years.add(""+(year + 1));
    	evalYearCB = new JComboBox(years.toArray());
    	evalYearCB.setSelectedIndex(1);
    	
    	evalYearPanel.add(evalYearLabel);
    	evalYearPanel.add(evalYearCB);
    	
    	evalYearCards.add(evalYearPanel, "evalYearPanel");
    	evalYearCards.add(emptyPanel, "emptyPanel");
    	((CardLayout)evalYearCards.getLayout()).show(evalYearCards, "emptyPanel");
    }
    
    private void initPatNamePane(){
        patCards = new JPanel(new CardLayout());
        patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        emptyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        patientTextField = new JTextField("", 20);
        patientPanel.add(patientLabel);
        patientTextField.setEditable(false);
        patientPanel.add(patientTextField);
        
        patCards.add(patientPanel, "patientPanel");
        patCards.add(emptyPanel, "emptyPanel");        
    }

    private void initPatEntryPane(){
        patCards = new JPanel(new CardLayout());
        patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        emptyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        patientTextField = new JTextField("", 20);
        selectPatientButton = new JButton("Select Patient");
        selectPatientButton.setActionCommand("selectPatient");
        selectPatientButton.addActionListener(this);
        patientPanel.add(patientLabel);
        patientTextField.setEditable(true);
        patientPanel.add(patientTextField);
        patientPanel.add(selectPatientButton);
        
        patCards.add(patientPanel, "patientPanel");
        patCards.add(emptyPanel, "emptyPanel");
    }

    private void initStartTimePane(boolean editable){
        startTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        startTimeLabel = new JLabel("Start Time:  "+ df.format(f.selectedDate.getTime())+"   ");
        startTimeHourComboBox = new JComboBox(hours);
        startTimeMinComboBox = new JComboBox(mins);
        startTimeAMPMComboBox = new JComboBox(ampm);

        if (apptCell != null && apptCell.getAppointment() != null){
            Appointment appt = apptCell.getAppointment();
            Calendar cal = new GregorianCalendar();
            cal.setTime(appt.getStartDate());
            startTimeHourComboBox.setSelectedItem(getHour(cal.get(Calendar.HOUR_OF_DAY)));
            startTimeMinComboBox.setSelectedItem(getMin(cal.get(Calendar.MINUTE)));
            startTimeAMPMComboBox.setSelectedItem(getAMPM(cal.get(Calendar.AM_PM)));
        }else{
            startTimeHourComboBox.setSelectedItem(getHour());
            startTimeMinComboBox.setSelectedItem(getMin());
            startTimeAMPMComboBox.setSelectedItem(getAMPM());
        }
        startTimeHourComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if (startTimeHourComboBox.getSelectedIndex() < 4){
                    startTimeAMPMComboBox.setSelectedIndex(0);
                }else{
                    startTimeAMPMComboBox.setSelectedIndex(1);
                }
                startTimeAMPMComboBox.invalidate();
            }
        });

        if (!editable){
            startTimeHourComboBox.setEnabled(false);
            startTimeMinComboBox.setEnabled(false);
            startTimeAMPMComboBox.setEnabled(false);
        }
        startTimePanel.add(startTimeLabel);
        startTimePanel.add(startTimeHourComboBox);
        startTimePanel.add(new JLabel(":"));
        startTimePanel.add(startTimeMinComboBox);
        startTimePanel.add(startTimeAMPMComboBox);
    }

    private void initEndTimePane(boolean editable){
        endTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        endTimeLabel = new JLabel(  "End Time:    "+ df.format(f.selectedDate.getTime())+"   ");
        endTimeHourComboBox = new JComboBox(hours);
        endTimeMinComboBox = new JComboBox(mins);
        endTimeAMPMComboBox = new JComboBox(ampm);

        if (apptCell != null && apptCell.getAppointment() != null){
            Appointment appt = apptCell.getAppointment();
            Calendar cal = new GregorianCalendar();
            cal.setTime(appt.getEndDate());
            endTimeHourComboBox.setSelectedItem(getHour(cal.get(Calendar.HOUR_OF_DAY)));
            endTimeMinComboBox.setSelectedItem(getMin(cal.get(Calendar.MINUTE)));
            endTimeAMPMComboBox.setSelectedItem(getAMPM(cal.get(Calendar.AM_PM)));
        }else{
            endTimeHourComboBox.setSelectedItem(getHour());
            endTimeMinComboBox.setSelectedItem(getMin());
            endTimeAMPMComboBox.setSelectedItem(getAMPM());
        }

        endTimeHourComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if (endTimeHourComboBox.getSelectedIndex() < 4){
                    endTimeAMPMComboBox.setSelectedIndex(0);
                }else{
                    endTimeAMPMComboBox.setSelectedIndex(1);
                }
                endTimeAMPMComboBox.invalidate();
            }
        });

        if (!editable){
            endTimeHourComboBox.setEnabled(false);
            endTimeMinComboBox.setEnabled(false);
            endTimeAMPMComboBox.setEnabled(false);
        }
        endTimePanel.add(endTimeLabel);
        endTimePanel.add(endTimeHourComboBox);
        endTimePanel.add(new JLabel(":"));
        endTimePanel.add(endTimeMinComboBox);
        endTimePanel.add(endTimeAMPMComboBox);
    }

    private void initNotesPane(boolean editable){
        notesTextArea = new JTextArea(5, 20);
        notesTextArea.setWrapStyleWord(true);
        notesSP = new JScrollPane(notesTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);        
        if (!editable){
            notesTextArea.setEditable(false);
        }
    }

    private void initClinicPane(boolean editable){
        clinicPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        List clinics = PSService.getService().getAllClinicName();
        clinicCB = new JComboBox(clinics.toArray());

        if (!editable){
            clinicCB.setEnabled(false);
        }
        clinicPanel.add(clinicLabel);
        clinicPanel.add(clinicCB);
    }

    private void initNtPane(boolean editable){    	
        needTranBG = new ButtonGroup();
        ntYesB = new JRadioButton("Yes");
        ntYesB.setActionCommand("ntYes");
        ntNoB = new JRadioButton("No");
        ntNoB.setActionCommand("ntNo");
        ntPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);

        needTranBG.add(ntYesB);
        needTranBG.add(ntNoB);

        langCards = new JPanel(new CardLayout());
        langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        emptyPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        langTF = new JTextField("", 20);

        langCards.add(langPanel, "langPanel");
        langCards.add(emptyPanel2, "emptyPanel");
        langPanel.add(langL);
        langTF.setEditable(true);
        langPanel.add(langTF);

        if (!editable){
            ntYesB.setEnabled(false);
            ntNoB.setEnabled(false);
            langTF.setEnabled(false);
        }
        
        ntYesB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                CardLayout cl = (CardLayout)langCards.getLayout();
                cl.show(langCards, "langPanel");                
            }
        });
        ntNoB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) langCards.getLayout();
                cl.show(langCards, "emptyPanel");
                langTF.setText("");
            }
        });
        
        ntPanel.add(needTranL);
        ntPanel.add(ntYesB);
        ntPanel.add(ntNoB);
    }

    private void initCrPane(boolean editable){
        crPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        collReceivedBG = new ButtonGroup();
        crYesB = new JRadioButton("Yes");
        crYesB.setActionCommand("crYes");
        crNoB = new JRadioButton("No");
        crNoB.setActionCommand("crNO");

        collReceivedBG.add(crYesB);
        collReceivedBG.add(crNoB);

        if (!editable){
            crYesB.setEnabled(false);
            crNoB.setEnabled(false);
        }
        crPanel.add(collReceived);
        crPanel.add(crYesB);
        crPanel.add(crNoB);
    }

    private void initEligPane(boolean editable){
        eligPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        eligBG = new ButtonGroup();
        eligYesB = new JRadioButton("Yes");
        eligNoB = new JRadioButton("No");

        eligBG.add(eligYesB);
        eligBG.add(eligNoB);

        if (!editable || !PSService.getService().hasEligCheckPermission()){
        	eligYesB.setEnabled(false);
        	eligNoB.setEnabled(false);
        }
        eligPanel.add(eligL);
        eligPanel.add(eligYesB);
        eligPanel.add(eligNoB);
    }
    
    private void initWalkInPane(boolean editable){
        walkInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        walkInBG = new ButtonGroup();
        walkInYesB = new JRadioButton("Yes");
        walkInNoB = new JRadioButton("No");

        walkInBG.add(walkInYesB);
        walkInBG.add(walkInNoB);

        authNumCards = new JPanel(new CardLayout());
        authNumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        emptyPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        authNumTF = new JTextField("", 20);
        authNumCards.add(authNumPanel, "authNumPanel");
        authNumCards.add(emptyPanel3, "emptyPanel");
        authNumPanel.add(authNumL);
        authNumTF.setEditable(true);
        authNumPanel.add(authNumTF);

        countyNumCards = new JPanel(new CardLayout());
        countyNumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        emptyPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT), false);
        countyNumTF = new JTextField("", 20);
        countyNumCards.add(countyNumPanel, "countyNumPanel");
        countyNumCards.add(emptyPanel4, "emptyPanel");
        countyNumPanel.add(countyNumL);
        countyNumTF.setEditable(true);
        countyNumPanel.add(countyNumTF);
        
        /*
        walkInYesB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                ((CardLayout)authNumCards.getLayout()).show(authNumCards, "authNumPanel");
                ((CardLayout)countyNumCards.getLayout()).show(countyNumCards, "countyNumPanel");                
            }
        });
        walkInNoB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) authNumCards.getLayout()).show(authNumCards, "emptyPanel");
                authNumTF.setText("");
                
                ((CardLayout) countyNumCards.getLayout()).show(countyNumCards, "emptyPanel");
                countyNumTF.setText("");                
            }
        });
        */
        
        ntPanel.add(needTranL);
        ntPanel.add(ntYesB);
        ntPanel.add(ntNoB);
        
        if (!editable){
        	walkInYesB.setEnabled(false);
        	walkInNoB.setEnabled(false);
            authNumTF.setEnabled(false);
            countyNumTF.setEnabled(false);
        }
        walkInPanel.add(walkInL);
        walkInPanel.add(walkInYesB);
        walkInPanel.add(walkInNoB);
    }
    
    private void initActionComponent(){
        cancelByPatientButton = new JButton("Cancel by Patient");
        cancelByPatientButton.setActionCommand("cancelByPatient");
        cancelByPatientButton.addActionListener(this);    	

        cancelByClinicButton = new JButton("Cancel by Clinic");
        cancelByClinicButton.setActionCommand("cancelByClinic");
        cancelByClinicButton.addActionListener(this);    	

        deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this);

        showupButton = new JButton("Showup");
        showupButton.setActionCommand("showup");
        showupButton.addActionListener(this);

        noShowsButton = new JButton("No Show / No Call");
        noShowsButton.setActionCommand("noshows");
        noShowsButton.addActionListener(this);
        
        closeButton = new JButton("Close");
        closeButton.setActionCommand("saveAndClose");
        closeButton.addActionListener(this);

        undoButton = new JButton("Undo");
        undoButton.setActionCommand("undo");
        undoButton.addActionListener(this);
    }

    private void debug(String msg){
        if (f.debugOn){
            System.out.println("[ApptCellEditor]: "+msg);
        }
    }
    
    /**
     * Listener for Save and Delete button
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("close")){

            refreshTable();
        }
        if (e.getActionCommand().equals("saveAndClose")){
            // Update the appt if necessary
            debug("SaveAndClose button selected");
            if (apptCell.getAppointment() == null){
                // new appointment
                createAppointment();
            }else{
                updateAppointment();
            }
        }else if (e.getActionCommand().equals("cancelByPatient")){
            debug("Cancel button selected");
            int reply = JOptionPane.showConfirmDialog(apptDialog,
                "Warning: Are you sure you want to CANCEL the scheduled appointment?",
                "Cancel appointment",
                JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION){
            	int is24Hrs = JOptionPane.showConfirmDialog(apptDialog,
            			"Is cancel within 24 hours?",
            			"Cancel appointment",
            			JOptionPane.YES_NO_OPTION);
            	final boolean isCancelByPatient = true;
            	final boolean isWn24hrs = is24Hrs == JOptionPane.YES_OPTION;

            	final JDialog cancelReasonDialog = new JDialog(apptDialog, "Cancel Reason Dialog", false);
                cancelReasonDialog.setLayout(new BorderLayout());
                cancelReasonDialog.setSize(300, 200);
                cancelReasonDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                cancelReasonDialog.setResizable(false);
                cancelReasonDialog.setLocationRelativeTo(apptDialog);

                final JComboBox cancelReasonCB = new JComboBox(ReadSvc.getInstance().getCancelByPatientReasonCode().toArray());
                final JTextArea otherReasonTA = new JTextArea(5, 20);                
                otherReasonTA.setWrapStyleWord(true);
                otherReasonTA.setVisible(false);
                JScrollPane otherReasonSP = new JScrollPane(otherReasonTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);                        
                JButton okButton = new JButton("OK");
                JButton cancelButton = new JButton("Canel");
                JPanel topPanel = new JPanel();
                JPanel midPanel = new JPanel();
                JPanel botPanel = new JPanel();
                JLabel reasonL = new JLabel("Reason: ");
                JLabel otherReasonL = new JLabel("Other:  ");
                topPanel.add(reasonL);
                topPanel.add(cancelReasonCB);
                midPanel.add(otherReasonL);
                midPanel.add(otherReasonSP);                
                botPanel.add(okButton);
                botPanel.add(cancelButton);

                cancelReasonDialog.add(topPanel, BorderLayout.NORTH);
                cancelReasonDialog.add(midPanel, BorderLayout.CENTER);
                cancelReasonDialog.add(botPanel, BorderLayout.SOUTH);
                cancelReasonDialog.setVisible(true);

                cancelReasonCB.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if (cancelReasonCB.getSelectedItem().equals("Other")){
							otherReasonTA.setVisible(true);
							otherReasonTA.requestFocusInWindow();
						}else{
							otherReasonTA.setVisible(false);
						}
					}                	
                });
                
                okButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						String otherReason = null;
						if (cancelReasonCB.getSelectedItem().equals("Other")){
							otherReason = otherReasonTA.getText();
							if (otherReason == null || otherReason.trim().equals("")){
								JOptionPane.showMessageDialog(cancelReasonDialog, "Please enter a reason in the text area", "Missing reason text", JOptionPane.ERROR_MESSAGE);
								otherReasonTA.requestFocusInWindow();
								return;
							}
						}
						cancelAppointment(isCancelByPatient, isWn24hrs, (String)cancelReasonCB.getSelectedItem(), otherReason);
						cancelReasonDialog.setVisible(false);
						cancelReasonDialog.dispose();
					}                	
                });          
                
                cancelButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						cancelReasonDialog.setVisible(false);
						cancelReasonDialog.dispose();
					}                	
                });
            }
        }else if (e.getActionCommand().equals("cancelByClinic")){
            debug("Cancel button selected");
            int reply = JOptionPane.showConfirmDialog(apptDialog,
                "Warning: Are you sure you want to CANCEL the scheduled appointment?",
                "Cancel appointment",
                JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION){
            	final boolean isCancelByPatient = false;

            	final JDialog cancelReasonDialog = new JDialog(apptDialog, "Cancel Reason Dialog", false);
                cancelReasonDialog.setLayout(new BorderLayout());
                cancelReasonDialog.setSize(300, 200);
                cancelReasonDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                cancelReasonDialog.setResizable(false);
                cancelReasonDialog.setLocationRelativeTo(apptDialog);

                final JComboBox cancelReasonCB = new JComboBox(ReadSvc.getInstance().getCancelByClinicReasonCode().toArray());
                final JTextArea otherReasonTA = new JTextArea(5, 20);                
                otherReasonTA.setWrapStyleWord(true);
                otherReasonTA.setVisible(false);
                JScrollPane otherReasonSP = new JScrollPane(otherReasonTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);                        
                JButton okButton = new JButton("OK");
                JButton cancelButton = new JButton("Canel");                
                JPanel topPanel = new JPanel();
                JPanel midPanel = new JPanel();
                JPanel botPanel = new JPanel();
                JLabel reasonL = new JLabel("Reason: ");
                JLabel otherReasonL = new JLabel("Other:  ");
                topPanel.add(reasonL);
                topPanel.add(cancelReasonCB);
                midPanel.add(otherReasonL);
                midPanel.add(otherReasonSP);                
                botPanel.add(okButton);
                botPanel.add(cancelButton);

                cancelReasonDialog.add(topPanel, BorderLayout.NORTH);
                cancelReasonDialog.add(midPanel, BorderLayout.CENTER);
                cancelReasonDialog.add(botPanel, BorderLayout.SOUTH);
                cancelReasonDialog.setVisible(true);

                cancelReasonCB.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if (cancelReasonCB.getSelectedItem().equals("Other")){
							otherReasonTA.setVisible(true);
							otherReasonTA.requestFocusInWindow();
						}else{
							otherReasonTA.setVisible(false);
						}
					}                	
                });
                
                okButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {		
						String otherReason = null;
						if (cancelReasonCB.getSelectedItem().equals("Other")){
							otherReason = otherReasonTA.getText();
							if (otherReason == null || otherReason.trim().equals("")){
								JOptionPane.showMessageDialog(cancelReasonDialog, "Please enter a reason in the text area", "Missing reason text", JOptionPane.ERROR_MESSAGE);
								otherReasonTA.requestFocusInWindow();
								return;
							}
						}						
						cancelAppointment(isCancelByPatient, false, (String)cancelReasonCB.getSelectedItem(), otherReason);
						cancelReasonDialog.setVisible(false);
						cancelReasonDialog.dispose();						
					}                	
                });
                
                cancelButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						cancelReasonDialog.setVisible(false);
						cancelReasonDialog.dispose();
					}                	
                });                
            }            
        }else if (e.getActionCommand().equals("delete")){
            // Delete the current appointment
            debug("delete button selected");
            boolean shouldDelete = true;
            if (!appt.isBlockTime()){
	            int reply = JOptionPane.showConfirmDialog(apptDialog,
	                "Warning: Are you sure you want to PERMANENTLY delete the appointment record?",
	                "Confirm Delete",
	                JOptionPane.YES_NO_OPTION);
	            if (reply == JOptionPane.YES_OPTION){
	            	shouldDelete = true;
	            }else{
	            	shouldDelete = false;
	            }
            }
            if (shouldDelete){
                deleteAppointment();
            }
        }else if (e.getActionCommand().equals("selectPatient")){
            // Select Patient
            spOkButton = new JButton("OK");
            spCancelButton = new JButton("Cancel");
            spDialog = new JDialog(apptDialog, "Select Patient", true);
            spDialog.setSize(350, 350);
            spDialog.setResizable(true);
            spDialog.setLocationRelativeTo(apptDialog);

            List patients = PSService.getService().getAllPatientNode();
            patientListModel = new DefaultListModel();
            for (int i=0; i<patients.size(); i++){
                patientListModel.addElement(patients.get(i));
            }
            patientList = new JList(patientListModel);
            patientList.setSelectedIndex(0);
            patientList.setVisibleRowCount(10);
            patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane listScroll = new JScrollPane(patientList);

            enterPatient = new JTextField(20);
            enterPatient.setActionCommand("spEnterPatient");
            enterPatient.getDocument().addDocumentListener(this);

            spOkButton.setActionCommand("spOk");
            spOkButton.addActionListener(this);
            spCancelButton.setActionCommand("spCancel");
            spCancelButton.addActionListener(this);

            JPanel topPanel = new JPanel(new GridLayout(2, 1));
            topPanel.add(spDescLabel1);
            topPanel.add(spDescLabel2);

            JPanel centerPanel = new JPanel();

            centerPanel.add(listScroll);

            JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
            JPanel textPanel = new JPanel();
            textPanel.add(enterPatient);
            bottomPanel.add(textPanel);
            JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
            bPanel.add(spOkButton);
            bPanel.add(spCancelButton);
            bottomPanel.add(bPanel);

            JPanel spPanel = new JPanel(new BorderLayout());
            spPanel.add(topPanel, BorderLayout.NORTH);
            spPanel.add(centerPanel, BorderLayout.CENTER);
            spPanel.add(bottomPanel, BorderLayout.SOUTH);

            spDialog.getContentPane().add(spPanel);
            spDialog.pack();
            spDialog.setVisible(true);
            enterPatient.requestFocusInWindow();
        }else if (e.getActionCommand().equals("spOk")){
            debug("click OK Button");
            int index = patientList.getSelectedIndex();
            String selectedPat = patientListModel.getElementAt(index).toString();
            patientTextField.setText(selectedPat);

            String firstName = null;
            String lastName = null;
            if (selectedPat != null){
                int i = selectedPat.indexOf(", ");
                if (i > 0){
                    lastName = selectedPat.substring(0, i);
                    firstName = selectedPat.substring(i+2);
                    boolean nts = ReadSvc.getInstance().getNtsForPatient(lastName, firstName);
                    if (nts){
                    	ntYesB.setSelected(true);
                    	ntNoB.setSelected(false);
                    }else{
                    	ntYesB.setSelected(false);
                    	ntNoB.setSelected(true);
                    }
                }
            }                        
            spDialog.setVisible(false);
            spDialog.dispose();
        }else if (e.getActionCommand().equals("spCancel")){
            //debug("click Cancel Button");
            if (spDialog != null){
                spDialog.setVisible(false);
                spDialog.dispose();
            }
        }else if (e.getActionCommand().equals("showup")){
            showupDialog = new JDialog(apptDialog, "Showup Dialog", false);
            showupDialog.setLayout(new BorderLayout());
            showupDialog.setSize(300, 200);
            showupDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            showupDialog.setResizable(false);
            showupDialog.setLocationRelativeTo(apptDialog);

            showupNotesTA = new JTextArea(5, 20);
            showupNotesTA.setWrapStyleWord(true);
            JScrollPane sp = new JScrollPane(showupNotesTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);        

            reasonCodeCB = new JComboBox(ReadSvc.getInstance().getAllReasonCode().toArray());
            JButton okButton = new JButton("OK");
            okButton.setActionCommand("showupOk");
            okButton.addActionListener(this);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setActionCommand("showupCancel");
            cancelButton.addActionListener(this);
            JPanel topPanel = new JPanel();
            JPanel midPanel = new JPanel();
            JPanel botPanel = new JPanel();
            topPanel.add(l1);
            topPanel.add(sp);
            midPanel.add(l2);
            midPanel.add(reasonCodeCB);
            botPanel.add(okButton);
            botPanel.add(cancelButton);

            showupDialog.add(topPanel, BorderLayout.NORTH);
            showupDialog.add(midPanel, BorderLayout.CENTER);
            showupDialog.add(botPanel, BorderLayout.SOUTH);
            showupDialog.setVisible(true);
        }else if (e.getActionCommand().equals("showupOk")){
            String notes = showupNotesTA.getText();
            if (notes == null){
                notes = "";
            }
            if (notes != null && notes.length() > 200){
                JOptionPane.showMessageDialog(apptDialog, "Notes is too long. Maximum size is 200. Please delete some text in the Notes.",
                                              "Notes too long", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String reasonCode = (String)reasonCodeCB.getSelectedItem();
            WriteSvc.getInstance().showup(appt.getApptId(), appt.getReferralId(), notes, reasonCode);
            appt.setStatusNotes(notes);
            appt.setReasonCode(reasonCode);

            String patInfo =
                "<font color=black><b>Appointment Status:</b> "+appt.getStatus()+"<br>" +
                "<b>Showup Notes:</b> "+appt.getStatusNotes()+"<br>"+
                "<b>Reason Code:</b> "+appt.getReasonCode()+"<br>"+
                "<b>Patient Id:</b> "+pat.getRefId()+"<br>" +
                "<b>Contact number:</b> "+pat.getPhoneNum()+"<br>"+
                "<b>Address: </b> "+pat.getFullAddressInHTML()+
                "<b>Insurance: </b>"+pat.getInsuranceCompany(0)+"<br>" +
                "<b>Copay: </b>"+pat.getCopayStr(0)+"<br>"+
                "<b>Insurance PhoneNumber: </b>"+pat.getInsPhoneNumber(0)+"<br>"+
                "<b>Previous Psychiatrist: </b>"+pat.getPreviousPsychiatrist()+"<br>"+
                "</font>";

            patInfoPane.setText(patInfo);
            actionPanel2.setVisible(false);
            dialogPanel.remove(actionPanel2);

            f.apptTableModel.setApptStatus(appt.getApptId(), Constant.Seen);
            showupDialog.setVisible(false);
            showupDialog.dispose();

            apptDialog.setVisible(false);
            apptDialog.dispose();
            fireEditingCanceled();
        }else if (e.getActionCommand().equals("showupCancel")){
            showupDialog.setVisible(false);
            showupDialog.dispose();
        }else if (e.getActionCommand().equals("noshows")){
            noshowDialog = new JDialog(apptDialog, "NoShow Dialog", false);
            noshowDialog.setLayout(new BorderLayout());
            noshowDialog.setSize(300, 200);
            noshowDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            noshowDialog.setResizable(false);
            noshowDialog.setLocationRelativeTo(apptDialog);

            noshowNotesTA = new JTextArea(5, 20);
            noshowNotesTA.setWrapStyleWord(true);
            JScrollPane sp = new JScrollPane(noshowNotesTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);        
            
            JButton okButton = new JButton("OK");
            okButton.setActionCommand("noshowOk");
            okButton.addActionListener(this);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setActionCommand("noshowCancel");
            cancelButton.addActionListener(this);
            JPanel topPanel = new JPanel();
            JPanel botPanel = new JPanel();
            topPanel.add(l1);
            topPanel.add(sp);
            botPanel.add(okButton);
            botPanel.add(cancelButton);

            noshowDialog.add(topPanel, BorderLayout.NORTH);
            noshowDialog.add(botPanel, BorderLayout.CENTER);
            noshowDialog.setVisible(true);
        }else if (e.getActionCommand().equals("noshowOk")){
            String notes = noshowNotesTA.getText();        	
            if (notes == null){
                notes = "";
            }
            if (notes != null && notes.length() > 200){
                JOptionPane.showMessageDialog(apptDialog, "Notes is too long. Maximum size is 200. Please delete some text in the Notes.",
                                              "Notes too long", JOptionPane.ERROR_MESSAGE);
                return;
            }            
            WriteSvc.getInstance().noShows(appt.getApptId(), appt.getReferralId(), notes);
            appt.setStatusNotes(notes);
            String patInfo =
                "<font color=black><b>Appointment Status:</b> "+appt.getStatus()+"<br>" +
                "<b>NoShow Notes:</b> "+appt.getStatusNotes()+"<br>"+
                "<b>Patient Id:</b> "+pat.getRefId()+"<br>" +
                "<b>Contact number:</b> "+pat.getPhoneNum()+"<br>"+
                "<b>Address: </b> "+pat.getFullAddressInHTML()+
                "<b>Insurance: </b>"+pat.getInsuranceCompany(0)+"<br>" +
                "<b>Copay: </b>"+pat.getCopayStr(0)+"<br>"+
                "<b>Insurance PhoneNumber: </b>"+pat.getInsPhoneNumber(0)+"<br>"+
                "<b>Previous Psychiatrist: </b>"+pat.getPreviousPsychiatrist()+"<br>"+
                "</font>";

            patInfoPane.setText(patInfo);
            actionPanel2.setVisible(false);
            dialogPanel.remove(actionPanel2);
            appt.setStatus(Constant.NotSeen);
            f.apptTableModel.setApptStatus(appt.getApptId(), Constant.NotSeen);
            apptDialog.setVisible(false);
            apptDialog.dispose();
            fireEditingCanceled();                
        }else if (e.getActionCommand().equals("noshowCancel")){
            noshowDialog.setVisible(false);
            noshowDialog.dispose();            
        }else if (e.getActionCommand().equals("undo")){
            debug("undo button selected");
            int reply = JOptionPane.showConfirmDialog(apptDialog,
                "Warning: Are you sure you want to Undo "+appt.getLastAction()+" action?",
                "Confirm Undo",
                JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION){
                long apptId = appt.getApptId();
                WriteSvc.getInstance().undoStatusChange(apptId);
                appt.setStatus(Constant.Scheduled);
                refreshTable();
            }
        }else{
            debug("Warning: unknow actionCommand "+e.getActionCommand());
        }
    }

    
}
