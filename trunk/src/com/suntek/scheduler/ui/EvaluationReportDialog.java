package com.suntek.scheduler.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.JTable.PrintMode;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.suntek.scheduler.appsvcs.ReadSvc;
import com.suntek.scheduler.appsvcs.WriteSvc;
import com.suntek.scheduler.appsvcs.persistence.Constant;
import com.suntek.scheduler.appsvcs.persistence.Patient;

public class EvaluationReportDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private EvaluationReportDialog evalDialog = this;
	private int evalYear; 		
		
	private JTable evalTable = null;
	private EvaluationTableModel evalTableModel = null;
	private JPanel evalDetailPanel = null; 

	private final String ADMIT_DATE = "Admission Date: ";
	private final String FIRST_NAME = "First Name: ";
	private final String LAST_NAME = "Last Name: ";
	private final String PHONE_NUM = "Phone Number: ";
	private final String DOB = "Date of Birth: ";
	
    private JLabel admitDateL = new JLabel(ADMIT_DATE);
    private JLabel firstNameL = new JLabel(FIRST_NAME);
    private JLabel lastNameL = new JLabel(LAST_NAME);
    private JLabel phoneL = new JLabel(PHONE_NUM);
    private JLabel dobL = new JLabel(DOB);
    private JLabel blankL = new JLabel("");
    
    private JTextField admitDateTF = new JTextField(50);
    private JTextField firstNameTF = new JTextField(50);
    private JTextField lastNameTF = new JTextField(50);
    private JTextField phoneTF = new JTextField(50);
    private JTextField dobTF = new JTextField(50);    
    private JButton evalCheckoffButton = new JButton("Evaluation Checkoff");    
	
    private Spring shortStrut = Spring.constant(5);
    private Spring mediumStrut = Spring.constant(10);	    
	
    //private EvalCellRenderer evalCellRenderer = new EvalCellRenderer();
    
	public EvaluationReportDialog(Frame owner, int locX, int locY, boolean model, String year){
		super(owner, model);
		this.evalYear = Integer.parseInt(year);
		init(locX, locY);
	}
	
	private void init(int locX, int locY){		
		evalDialog = this;
		
		setSize(950, 750);
        setTitle(evalYear+" Contra Costa Access Evaluation Report");		
        setResizable(true);
        if (locX != -1 && locY != -1){
        	setLocation(locX, locY);
        }

        admitDateTF.setEditable(false);
        firstNameTF.setEditable(false);
        lastNameTF.setEditable(false);
        phoneTF.setEditable(false);
        dobTF.setEditable(false);        
        
        // Main
        List<Patient> patients = getEvaluationPatient();
        JPanel mainPanel = createMainPanel(patients);
        
        // Action
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton printButton = new JButton("Print");
        printButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		try {
        			 MessageFormat headerFormat = new MessageFormat(evalYear+" Contra Costa Access Evaluation Report");
        			 MessageFormat footerFormat = new MessageFormat("- {0} -");
        			evalTable.print(PrintMode.FIT_WIDTH, headerFormat, footerFormat, true, null, true);
        		} catch (PrinterException pe) {
        			JOptionPane.showMessageDialog(evalDialog, "Problem printing! "+pe.getLocalizedMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
				}
        	}
        });
        
        JButton closeButton = new JButton("Close");        
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	evalDialog.setVisible(false);
            	evalDialog.dispose();
            }
        }
        );
        
        actionPanel.add(printButton);
        actionPanel.add(closeButton);

        JPanel p = new JPanel(new BorderLayout());
        p.add(mainPanel, BorderLayout.CENTER);
        p.add(actionPanel, BorderLayout.SOUTH);

        add(p);                
        setVisible(true);     
        this.invalidate();
	}
	
	private List<Patient> filterCurrentYearAdmission(List<Patient> pats){
		Calendar cal = new GregorianCalendar();
		int curYear = cal.get(Calendar.YEAR);		

		List<Patient> retVals = new ArrayList<Patient>();
		for (Patient p: pats){
			if (p.getInsAdmitYear() < curYear){ //ignore current year and future year admission
				retVals.add(p);
			}
		}
		return retVals;
	}
	
	private List<Patient> getEvaluationPatient(){
		List<Patient> patients = filterCurrentYearAdmission(ReadSvc.getInstance().getEvalPatientByIns(Constant.CONTRA_COSTA_ACCESS));
		List<Integer> evaledOrScheduledRefIds = ReadSvc.getInstance().getEvaluatedRefId(evalYear);
		evaledOrScheduledRefIds.addAll(ReadSvc.getInstance().getRefIdWithEvalAppt(""+evalYear));
		
		List<Patient> retVals = new ArrayList<Patient>();
		
		Calendar cal = new GregorianCalendar();
		int curYear = cal.get(Calendar.YEAR);		
		if (evalYear < curYear){
			// show all overdue
			for (Patient p : patients){
				if (!evaledOrScheduledRefIds.contains(p.getRefId())){
					p.setEvalStatus(Patient.OVERDUE);
					retVals.add(p);
				}
			}
		}else if (evalYear == curYear){
			// show all overdue + next two month
			int curMonth = cal.get(Calendar.MONTH) + 1;
			int curDay = cal.get(Calendar.DAY_OF_MONTH);

			int twoMonthMonth = curMonth + 2;
			int twoMonthDay = curDay;
			if (twoMonthMonth > 12){
				twoMonthMonth = 12; 
				twoMonthDay = 31;
			}
					
			for (Patient p : patients){
				int admitMonth = p.getInsAdmitMonth();
				int admitDay = p.getInsAdmitDay();

				if (isBefore(admitMonth, admitDay, curMonth, curDay)){
					if (!evaledOrScheduledRefIds.contains(p.getRefId())){
						p.setEvalStatus(Patient.OVERDUE);
						retVals.add(p);
					}
					continue;
				}
				if (isBefore(admitMonth, admitDay, twoMonthMonth, twoMonthDay) || 
					(admitMonth == twoMonthMonth && admitDay == twoMonthDay)){
					if (!evaledOrScheduledRefIds.contains(p.getRefId())){
						p.setEvalStatus(Patient.DUE_IN_60_DAYS);					
						retVals.add(p);
					}
					continue;
				}
				break;
			}			
		}else if (evalYear > curYear){
			// shows first 2 months
			int twoMonthMonth = 3; 
			int twoMonthDay = 1;
			for (Patient p : patients){
				int admitMonth = p.getInsAdmitMonth();
				int admitDay = p.getInsAdmitDay();
				if (isBefore(admitMonth, admitDay, twoMonthMonth, twoMonthDay) || 
				    (admitMonth == twoMonthMonth && admitDay == twoMonthDay)){
					if (!evaledOrScheduledRefIds.contains(p.getRefId())){
						p.setEvalStatus(Patient.DUE_IN_60_DAYS);
						retVals.add(p);
					}
					continue;
				}
				break;
			}
		}
		return retVals;
	}
	
	//returns true if admitDate is before curDate
	private boolean isBefore(int admitMonth, int admitDay, int curMonth, int curDay){
		if (admitMonth < curMonth){
			return true;
		}else if (admitMonth == curMonth){
			if (admitDay < curDay){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

    private JPanel createMainPanel(List<Patient> patients) {
    	JPanel panel = new JPanel(new BorderLayout(15,15));

    	// Table Summary
    	evalTableModel = new EvaluationTableModel(patients);
    	evalTable = new JTable(evalTableModel);
    	//evalTable.setDefaultRenderer(Object.class, evalCellRenderer);
    	evalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	evalTable.setAutoCreateRowSorter(true);
    	evalTable.getColumn(EvaluationTableModel.ID).setPreferredWidth(75);
    	evalTable.getColumn(EvaluationTableModel.ADMIT_DATE).setPreferredWidth(150);
    	evalTable.getColumn(EvaluationTableModel.FIRST_NAME).setPreferredWidth(150);
    	evalTable.getColumn(EvaluationTableModel.LAST_NAME).setPreferredWidth(150);
    	evalTable.getColumn(EvaluationTableModel.PHONE).setPreferredWidth(150);
    	evalTable.getColumn(EvaluationTableModel.DOB).setPreferredWidth(150);
    	evalTable.getColumn(EvaluationTableModel.STATUS).setPreferredWidth(100);
    	
    	evalTable.getSelectionModel().addListSelectionListener(
    			new ListSelectionListener() {
    				  public void valueChanged(ListSelectionEvent e){
    					  if (e.getValueIsAdjusting()){
    						  return;
    					  }
    					  ListSelectionModel lsm = (ListSelectionModel)e.getSource();
    					  if (lsm.isSelectionEmpty()){
    						  evalDetailPanel.setVisible(false);
    					  }else{
    			    	    	// set the detail panel    						  
    			    	    	int selectedViewIndex = evalTable.getSelectedRow();
    			    	    	int selectedModelIndex = evalTable.convertRowIndexToModel(selectedViewIndex);
    			    	    	if (selectedModelIndex >= 0){
    			    	    		admitDateTF.setText((String)evalTableModel.getValueAt(selectedModelIndex, 1));    			    	    		
    			    	    		firstNameTF.setText((String)evalTableModel.getValueAt(selectedModelIndex, 2));
    			    	    		lastNameTF.setText((String)evalTableModel.getValueAt(selectedModelIndex, 3));
    			    	    		phoneTF.setText((String)evalTableModel.getValueAt(selectedModelIndex, 4));
    			    	    		dobTF.setText((String)evalTableModel.getValueAt(selectedModelIndex, 5));
    			    	    		evalDetailPanel.setVisible(true);    
    			    	    	}else{
    	    						  evalDetailPanel.setVisible(false);    			    	    		
    			    	    	}
    					  }
    				  }
    			}
    	);
    	evalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	JScrollPane tableScroller = new JScrollPane(evalTable);
    	tableScroller.setPreferredSize(new Dimension(570, 400));
    	    	    	
    	initEvalCheckoffButton();
    	
        // Detail Panel
    	evalDetailPanel = createDetailPanel();
    	evalDetailPanel.setVisible(false);    	
    	
        panel.add(tableScroller, BorderLayout.NORTH);
        panel.add(evalDetailPanel, BorderLayout.CENTER);
        return panel;
    }
	
    private JPanel createDetailPanel(){ 
        JPanel detailPanel = new JPanel();
        SpringLayout layout = new SpringLayout();        
        detailPanel.setLayout(layout);
        detailPanel.add(admitDateL);
        detailPanel.add(admitDateTF);
        detailPanel.add(firstNameL);
        detailPanel.add(firstNameTF);
        detailPanel.add(lastNameL);
        detailPanel.add(lastNameTF);
        detailPanel.add(phoneL);
        detailPanel.add(phoneTF);
        detailPanel.add(dobL);
        detailPanel.add(dobTF);
        detailPanel.add(blankL);
        detailPanel.add(evalCheckoffButton);

        Spring leftmostStrut = Spring.sum(mediumStrut, layout.getConstraints(admitDateL).getWidth());        
        alignRow1(detailPanel, layout, leftmostStrut);
        alignRow2(detailPanel, layout, leftmostStrut);                
        alignRow3(detailPanel, layout, leftmostStrut);
        alignRow4(detailPanel, layout, leftmostStrut);
        alignRow5(detailPanel, layout, leftmostStrut);
        alignRow6(detailPanel, layout, leftmostStrut);
        
        return detailPanel;
    }

    private void alignRow1(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, admitDateL, leftmostStrut,
				 SpringLayout.WEST, detailPanel);        
    	layout.putConstraint(SpringLayout.WEST, admitDateTF, shortStrut,
				 SpringLayout.EAST, admitDateL);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, admitDateL, mediumStrut,
                SpringLayout.NORTH, detailPanel);
        layout.putConstraint(SpringLayout.NORTH, admitDateTF, mediumStrut,
                SpringLayout.NORTH, detailPanel);
    }
    
    private void alignRow2(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, firstNameL, leftmostStrut,
				 SpringLayout.WEST, detailPanel);        
    	layout.putConstraint(SpringLayout.WEST, firstNameTF, shortStrut,
				 SpringLayout.EAST, firstNameL);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, firstNameL, mediumStrut,
                SpringLayout.SOUTH, admitDateTF);
        layout.putConstraint(SpringLayout.NORTH, firstNameTF, mediumStrut,
                SpringLayout.SOUTH, admitDateTF);
    }
    
    private void alignRow3(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
        layout.putConstraint(SpringLayout.EAST, lastNameL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
        layout.putConstraint(SpringLayout.WEST, lastNameTF, shortStrut,
                SpringLayout.EAST, lastNameL);
        // align row        
        layout.putConstraint(SpringLayout.NORTH, lastNameL, mediumStrut,
                SpringLayout.SOUTH, firstNameTF);
        layout.putConstraint(SpringLayout.NORTH, lastNameTF, mediumStrut,
                SpringLayout.SOUTH, firstNameTF);    	
    }
    
    private void alignRow4(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
        layout.putConstraint(SpringLayout.EAST, phoneL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
        layout.putConstraint(SpringLayout.WEST, phoneTF, shortStrut,
                SpringLayout.EAST, phoneL);
        // align row
        layout.putConstraint(SpringLayout.NORTH, phoneL, mediumStrut,
                SpringLayout.SOUTH, lastNameTF);
        layout.putConstraint(SpringLayout.NORTH, phoneTF, mediumStrut,
                SpringLayout.SOUTH, lastNameTF);    	            	
    }

    private void alignRow5(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {   
    	// align col
        layout.putConstraint(SpringLayout.EAST, dobL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
        layout.putConstraint(SpringLayout.WEST, dobTF, shortStrut,
                SpringLayout.EAST, dobL);
        // align row
        layout.putConstraint(SpringLayout.NORTH, dobL, mediumStrut,
                SpringLayout.SOUTH, phoneTF);
        layout.putConstraint(SpringLayout.NORTH, dobTF, mediumStrut,
                SpringLayout.SOUTH, phoneTF);    	            	                
    }

    private void alignRow6(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {   
    	// align col
        layout.putConstraint(SpringLayout.EAST, blankL, leftmostStrut,
                SpringLayout.WEST, detailPanel);
        layout.putConstraint(SpringLayout.WEST, evalCheckoffButton, shortStrut,
                SpringLayout.EAST, blankL);
        // align row
        layout.putConstraint(SpringLayout.NORTH, blankL, mediumStrut,
                SpringLayout.SOUTH, dobTF);
        layout.putConstraint(SpringLayout.NORTH, evalCheckoffButton, mediumStrut,
                SpringLayout.SOUTH, dobTF);    	            	                
    }
    
    private void initEvalCheckoffButton(){        
        evalCheckoffButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
    	    	int viewRowIndex = evalTable.getSelectedRow(); 
    	    	int selectedRow = evalTable.convertRowIndexToModel(viewRowIndex);
    	    	if (selectedRow >= 0){
        	    	final int refId = Integer.parseInt((String)evalTableModel.getValueAt(selectedRow, 0));        	    	
    	    		final String patient = evalTableModel.getValueAt(selectedRow, 2)+" "+evalTableModel.getValueAt(selectedRow, 3);
    	    		
    	            final JDialog checkoffDialog = new JDialog(evalDialog, "Evaluation Checkoff", false);
    	            checkoffDialog.setLayout(new BorderLayout());
    	            checkoffDialog.setSize(300, 200);
    	            checkoffDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    	            checkoffDialog.setResizable(false);
    	            checkoffDialog.setLocationRelativeTo(evalDialog);

    	            final JTextArea checkoffNotesTA = new JTextArea(5, 20);
    	            checkoffNotesTA.setWrapStyleWord(true);
    	            JScrollPane sp = new JScrollPane(checkoffNotesTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);        
    	            
    	            JButton checkoffButton = new JButton("Checkoff");
    	            checkoffButton.addActionListener(new ActionListener(){
    	            	public void actionPerformed(ActionEvent e){
    	            		String notes = checkoffNotesTA.getText();
                    		WriteSvc.getInstance().checkoffEvaluation(refId, evalYear, notes);                   			
                    		JOptionPane.showMessageDialog(evalDialog, "Successfully checkoff "+evalYear+" evaluation for \""+patient+"\".");
                    		evalTableModel.removePatientById(refId);
                    		evalTableModel.fireTableDataChanged();
    	            		checkoffDialog.setVisible(false);
    	            		checkoffDialog.dispose();
    	            	}
    	            });
    	            
    	            JButton cancelButton = new JButton("Cancel");
    	            cancelButton.addActionListener(new ActionListener(){
    	            	public void actionPerformed(ActionEvent e){
    	            		checkoffDialog.setVisible(false);
    	            		checkoffDialog.dispose();
    	            	}
    	            });
    	            JPanel topPanel = new JPanel();
    	            JPanel botPanel = new JPanel();
    	            topPanel.add(new JLabel("Notes:  "));
    	            topPanel.add(sp);
    	            botPanel.add(checkoffButton);
    	            botPanel.add(cancelButton);

    	            checkoffDialog.add(topPanel, BorderLayout.NORTH);
    	            checkoffDialog.add(botPanel, BorderLayout.CENTER);
    	            checkoffDialog.setVisible(true);    	    		
        		}
        	}
        });
    }

}
