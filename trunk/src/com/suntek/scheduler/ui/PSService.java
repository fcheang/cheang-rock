package com.suntek.scheduler.ui;

import com.suntek.scheduler.appsvcs.*;
import com.suntek.scheduler.appsvcs.persistence.*;
import com.suntek.scheduler.util.HolidayCalculator;

import javax.swing.tree.*;
import java.util.*;
import java.text.DateFormat;
import java.awt.BorderLayout;
import java.awt.Cursor;
import javax.swing.*;
import javax.swing.table.*;


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
public class PSService {

    private static PSService svc = new PSService();

    private MainFrame f = null;

    private static Date END_OF_TIME = null;
    private static DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

    private ReadSvcI readSvc;
    private WriteSvcI writeSvc;

    private ApptCellRenderer apptCellRenderer = new ApptCellRenderer();
    private DateCellRenderer dateCellRenderer = new DateCellRenderer();
    private ApptCellEditor apptCellEditor = null;
    private boolean init = false;
    private Object initLock = new Object();
    
    //cache
    private List<Patient> patientNodes = null;

    private PSService() {
    }

    public static PSService getService(){
        return svc;
    }

    public void setMainFrame(MainFrame aF){
        f = aF;
        apptCellEditor = new ApptCellEditor(f);
    }

    public void connect(){
    	if (!init){
    		synchronized (initLock){
    			if (!init){
			        JDBCConnector connector = JDBCConnector.getInstance();
			        String dbDriver = System.getProperty("dbDriver");
			        String dbHost = System.getProperty("dbHost");
			        String dbUsed = System.getProperty("dbUsed");
			        String dbUser = System.getProperty("dbUser");
			        String dbPassword = System.getProperty("dbPassword");
			        connector.init(dbDriver, dbHost, dbUsed, dbUser, dbPassword);
			        readSvc = ReadSvc.getInstance();
			        writeSvc = WriteSvc.getInstance();
			        init = true;
    			}
    		}
    	}    		
    }
    
    public void connect(String[] args){
    	if (!init){
    		synchronized (initLock){
    			if (!init){
			        JDBCConnector connector = JDBCConnector.getInstance();
			        String dbDriver = args[0];
			        String dbHost = args[1];
			        String dbUsed = args[2];
			        String dbUser = args[3];
			        String dbPassword = args[4];
			        connector.init(dbDriver, dbHost, dbUsed, dbUser, dbPassword);
			        readSvc = ReadSvc.getInstance();
			        writeSvc = WriteSvc.getInstance();
			        init = true;
    			}
    		}
    	}    		
    }
    
    public boolean login(String user, String pass){
        return readSvc.login(user, pass);
    }

    public TreeNode getRootNode()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root", true);

        List<ProviderNode> provNodes = readSvc.getAllProviderNode();
        DefaultMutableTreeNode provider = new DefaultMutableTreeNode("Providers ("+provNodes.size()+")" , true);
        for (int i=0; i<provNodes.size(); i++){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(provNodes.get(i), false);
            provider.add(node);
        }

        List<Patient> patNames = readSvc.getAllPatientNode();
        DefaultMutableTreeNode patient = new DefaultMutableTreeNode("Patients ("+patNames.size()+")", true);
        for (int i=0; i<patNames.size(); i++){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(patNames.get(i), false);
            patient.add(node);
        }

        List<String> clinicNames = readSvc.getAllClinicName();
        DefaultMutableTreeNode clinic = new DefaultMutableTreeNode("Clinics ("+clinicNames.size()+")", true);
        for (int i=0; i<clinicNames.size(); i++){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(clinicNames.get(i), false);
            clinic.add(node);
        }
        
        List<ProviderNode> inactProvNodes = readSvc.getInactiveProvider();
        DefaultMutableTreeNode inactProvider = new DefaultMutableTreeNode("Inactive Providers ("+inactProvNodes.size()+")" , true);
        for (int i=0; i<inactProvNodes.size(); i++){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(inactProvNodes.get(i), false);
            inactProvider.add(node);
        }
                
        List<Patient> dcPatNames = readSvc.getDischargedPatient();
        DefaultMutableTreeNode dcPatient = new DefaultMutableTreeNode("Discharged Patients ("+dcPatNames.size()+")", true);
        for (int i=0; i<dcPatNames.size(); i++){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dcPatNames.get(i), false);
            dcPatient.add(node);
        }        

        root.add(provider);
        root.add(patient);
        root.add(clinic);
        root.add(inactProvider);
        root.add(dcPatient);
        return root;
    }

    public void clearPatientNodeCache(){
    	patientNodes = null;
    }
    
    public List<Patient> getAllPatientNode(){
    	if (patientNodes == null){
    		patientNodes = readSvc.getAllPatientNode();
    	}
    	return patientNodes;
    }

    public List getApptForDay(String provider, Date date){
        return readSvc.getApptForDay(provider, date);
    }

    public JTable getDailyAppt(String provider, Date date){
        List<Appointment> appts = getApptForDay(provider, date);
        DailyApptTableModel model = new DailyApptTableModel(provider, date, appts);
        JTable table = new JTable(model);
        table.setDefaultRenderer(Date.class, dateCellRenderer);
        table.setDefaultRenderer(ApptCell.class, apptCellRenderer);
        table.setDefaultEditor(ApptCell.class, apptCellEditor);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        int rowHeight = table.getRowHeight();
        table.setRowHeight((int)(rowHeight * 1.25));
        TableColumn column = null;
        for (int i = 0; i < 3; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setResizable(false);
                column.setMaxWidth(60);
                column.setMinWidth(60);
            }
        }
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(Constant.TITLE_BAR_COLOR);
        table.getTableHeader().setForeground(Constant.TITLE_BAR_TEXT_COLOR);
        table.setToolTipText("Double click on a cell to schedule appointment");
        return table;
    }

    public List getAllClinicName(){
        return readSvc.getAllClinicName();
    }

    public void createAppointment(Appointment appt){
        writeSvc.insertAppt(appt);
    }
    
    public boolean checkCredential(String provider, int refId){
    	if (refId == -1){
    		return true;
    	}
    	return readSvc.checkCredential(provider, refId);    	
    }    

    public int getPatientId(String lastName, String firstName){
        return readSvc.getPatientId(lastName, firstName);
    }

    /**
     * get List of overlapping Appointment objects with startTime and endTime
     */
    public List getOverlapAppt(String provider, Date startTime, Date endTime, String apptType){
        List retVal = new ArrayList();

        List appts = readSvc.getAllOverlapAppt(provider, startTime, endTime);
        for (int i=0; i<appts.size(); i++){
            Appointment appt = (Appointment)appts.get(i);
            if (appt.getType().equals(Constant.Blocked) || apptType.equals(Constant.Blocked) ||
                appt.getType().equals(apptType)){
                retVal.add(appt);
            }
        }
        return retVal;
    }

    public boolean allowConnect(String user, String host){
        if (readSvc.isRemoteUser(user)){
            return true;
        }else{
            if (readSvc.isTrustedIP(host)){
                return true;
            }
        }
        return false;
    }


    public JPanel getApptPanelForApptId(long apptId){
        Appointment appt = readSvc.getApptForApptId(apptId);
        JPanel panel = null;
        if (appt != null){
            panel = createApptPanel(appt);
        }else{
            panel = new JPanel();
            JLabel label = new JLabel("No Appointment found for confirmation# "+apptId);
            panel.add(label);
        }
        return panel;
    }

    public JPanel createApptPanel(Appointment appt) {
        // 10 fields
        JLabel apptL = new JLabel("Appointment Information for confirmation#: "+appt.getApptId());
        JLabel apptIdL = new JLabel("Patient: ");
        JLabel apptStatusL = new JLabel("Status: ");
        JLabel apptDateL = new JLabel("Date: ");
        JLabel apptTimeL = new JLabel("Time: ");
        JLabel apptProvL = new JLabel("Doctor: ");
        JLabel apptNtsL = new JLabel("Need translation service: ");
        JLabel apptCrL = new JLabel("Colleteral received: ");
        JLabel apptEligL = new JLabel("Eligible for appt: ");
        JLabel apptNotesL = new JLabel("Notes: ");
        JLabel blankL = new JLabel(" ");
        JPanel apptPanel = new JPanel(new SpringLayout());
        SpringLayout l = new SpringLayout();
        apptPanel.setLayout(l);

        JLabel apptIdTF = new JLabel(appt.getFullName());
        JLabel apptStatusTF = new JLabel(appt.getStatus());
        JLabel apptDateTF = new JLabel(appt.getApptDateStr());
        JLabel apptTimeTF = new JLabel(appt.getStartTimeStr());
        JLabel apptProvTF = new JLabel(appt.getProvider());
        JLabel apptNtsTF = new JLabel(appt.needTranSvcStr());
        JLabel apptCrTF = new JLabel(appt.collateralReceivedStr());
        JLabel apptEligTF = new JLabel(appt.isEligible());
        JTextArea apptNotesTF = new JTextArea(appt.getNotes(), 3, 20);
        apptNotesTF.setEditable(false);

        apptPanel.add(apptL);
        apptPanel.add(blankL);
        apptPanel.add(apptIdL);
        apptPanel.add(apptIdTF);
        apptPanel.add(apptStatusL);
        apptPanel.add(apptStatusTF);
        apptPanel.add(apptDateL);
        apptPanel.add(apptDateTF);
        apptPanel.add(apptTimeL);
        apptPanel.add(apptTimeTF);
        apptPanel.add(apptProvL);
        apptPanel.add(apptProvTF);
        apptPanel.add(apptNtsL);
        apptPanel.add(apptNtsTF);
        apptPanel.add(apptCrL);
        apptPanel.add(apptCrTF);
        apptPanel.add(apptEligL);
        apptPanel.add(apptEligTF);
        apptPanel.add(apptNotesL);
        apptPanel.add(apptNotesTF);

        Spring strut = Spring.constant(10);

        Spring labelsEast = Spring.sum(strut,
                                       l.getConstraints(apptNtsL).getWidth());

        // align first col
        l.putConstraint(SpringLayout.EAST, apptIdL, labelsEast,
                                SpringLayout.WEST, apptPanel);
        l.putConstraint(SpringLayout.EAST, apptStatusL, labelsEast,
                                SpringLayout.WEST, apptPanel);
        l.putConstraint(SpringLayout.EAST, apptDateL, labelsEast,
                                SpringLayout.WEST, apptPanel);
        l.putConstraint(SpringLayout.EAST, apptTimeL, labelsEast,
                                SpringLayout.WEST, apptPanel);
        l.putConstraint(SpringLayout.EAST, apptProvL, labelsEast,
                                SpringLayout.WEST, apptPanel);
        l.putConstraint(SpringLayout.EAST, apptNtsL, labelsEast,
                                SpringLayout.WEST, apptPanel);
        l.putConstraint(SpringLayout.EAST, apptCrL, labelsEast,
                                SpringLayout.WEST, apptPanel);
        l.putConstraint(SpringLayout.EAST, apptEligL, labelsEast,
                				SpringLayout.WEST, apptPanel);        
        l.putConstraint(SpringLayout.EAST, apptNotesL, labelsEast,
                                SpringLayout.WEST, apptPanel);

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
        l.putConstraint(SpringLayout.WEST, apptNotesTF, strut,
                                SpringLayout.EAST,
                                apptNotesL);

        // align row
        l.putConstraint(SpringLayout.NORTH, apptL, strut,
                                SpringLayout.NORTH,
                                apptPanel);
        l.putConstraint(SpringLayout.NORTH, blankL, strut,
                                SpringLayout.NORTH, apptPanel);

        Spring rowNorth0 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptL),
            l.getConstraint(SpringLayout.SOUTH, blankL)));
        l.putConstraint(SpringLayout.NORTH, apptIdL, rowNorth0,
                        SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptIdTF, rowNorth0,
                        SpringLayout.NORTH, apptPanel);

        Spring rowNorth1 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptIdL),
            l.getConstraint(SpringLayout.SOUTH, apptIdTF)));
        l.putConstraint(SpringLayout.NORTH, apptStatusL, rowNorth1,
                                SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptStatusTF, rowNorth1,
                                SpringLayout.NORTH, apptPanel);

        Spring rowNorth2 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptStatusL),
            l.getConstraint(SpringLayout.SOUTH, apptStatusTF)));
        l.putConstraint(SpringLayout.NORTH, apptDateL, rowNorth2,
                                SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptDateTF, rowNorth2,
                                SpringLayout.NORTH, apptPanel);

        Spring rowNorth3 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptDateL),
            l.getConstraint(SpringLayout.SOUTH, apptDateTF)));
        l.putConstraint(SpringLayout.NORTH, apptTimeL, rowNorth3,
                                SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptTimeTF, rowNorth3,
                                SpringLayout.NORTH, apptPanel);

        Spring rowNorth4 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptTimeL),
            l.getConstraint(SpringLayout.SOUTH, apptTimeTF)));
        l.putConstraint(SpringLayout.NORTH, apptProvL, rowNorth4,
                                SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptProvTF, rowNorth4,
                                SpringLayout.NORTH, apptPanel);

        Spring rowNorth5 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptProvL),
            l.getConstraint(SpringLayout.SOUTH, apptProvTF)));
        l.putConstraint(SpringLayout.NORTH, apptNtsL, rowNorth5,
                                SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptNtsTF, rowNorth5,
                                SpringLayout.NORTH, apptPanel);

        Spring rowNorth6 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptNtsL),
            l.getConstraint(SpringLayout.SOUTH, apptNtsTF)));
        l.putConstraint(SpringLayout.NORTH, apptCrL, rowNorth6,
                                SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptCrTF, rowNorth6,
                                SpringLayout.NORTH, apptPanel);

        Spring rowNorth7 = Spring.sum(strut, Spring.max(
            l.getConstraint(SpringLayout.SOUTH, apptCrL),
            l.getConstraint(SpringLayout.SOUTH, apptCrTF)));
        l.putConstraint(SpringLayout.NORTH, apptEligL, rowNorth7,
                                SpringLayout.NORTH, apptPanel);
        l.putConstraint(SpringLayout.NORTH, apptEligTF, rowNorth7,
                                SpringLayout.NORTH, apptPanel);
        
        Spring rowNorth8 = Spring.sum(strut, Spring.max(
                l.getConstraint(SpringLayout.SOUTH, apptEligL),
                l.getConstraint(SpringLayout.SOUTH, apptEligTF)));
            l.putConstraint(SpringLayout.NORTH, apptNotesL, rowNorth8,
                                    SpringLayout.NORTH, apptPanel);
            l.putConstraint(SpringLayout.NORTH, apptNotesTF, rowNorth8,
                                    SpringLayout.NORTH, apptPanel);
        
        return apptPanel;
    }

    public boolean hasDeletePermission(Appointment appt){
        boolean isAllow = false;
        if (appt.isOwner(Constant.appUser)){
            isAllow = true;
        }else{
            if (Constant.appRole.contains(Constant.ADMINISTRATOR) || Constant.appRole.contains(Constant.MANAGER)){
                isAllow = true;
            }
        }
        return isAllow;
    }

    public boolean hasUpdatePermission(Appointment appt){
        boolean isAllow = false;
        if (appt.isOwner(Constant.appUser)){
            isAllow = true;
        }else{
            if (Constant.appRole.contains(Constant.ADMINISTRATOR) || Constant.appRole.contains(Constant.MANAGER)){
                isAllow = true;
            }
        }
        return isAllow;
    }
    
    public boolean hasEligCheckPermission(){
        boolean isAllow = false;
        if (Constant.appRole.contains(Constant.ELIGIBLITY_CHECK)){
            isAllow = true;
        }
        return isAllow;
    }

	public void createDailyApptTable(String name, Date apptDate){
		MainFrame f = MainFrame.f;

        // check company holiday
		String stdHoliday = HolidayCalculator.getInstance().getHoliday(apptDate);
		String custHoliday = ReadSvc.getInstance().getHolidayDesc(apptDate);
		
        if (stdHoliday != null && ReadSvc.getInstance().isActiveHoliday(stdHoliday)){        	
        	DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
            Box box = Box.createVerticalBox();
            box.add(new JLabel("   "));
            box.add(new JLabel("   "));
            box.add(new JLabel("   "));            
            box.add(new JLabel(df.format(apptDate.getTime())));
            box.add(new JLabel(stdHoliday+" Holiday"));            
            box.add(new JLabel("Clinics are closed"));
            JPanel p = new JPanel();
            p.add(box);
            JPanel p1 = new JPanel(new BorderLayout());
            p1.add(p, BorderLayout.CENTER);
        	f.splitPane.setRightComponent(p1);
        }else if (custHoliday != null){
        	DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
            Box box = Box.createVerticalBox();
            box.add(new JLabel("   "));
            box.add(new JLabel("   "));
            box.add(new JLabel("   "));            
            box.add(new JLabel(df.format(apptDate.getTime())));
            box.add(new JLabel(custHoliday));            
            box.add(new JLabel("Clinics are closed"));
            JPanel p = new JPanel();
            p.add(box);
            JPanel p1 = new JPanel(new BorderLayout());
            p1.add(p, BorderLayout.CENTER);
        	f.splitPane.setRightComponent(p1);        	
        }else{			
	        if (f.apptTable == null) {
	            JTable table = PSService.getService().getDailyAppt(name,
	                f.selectedDate.getTime());
	            f.apptTable = table;
	            f.apptTableModel = (DailyApptTableModel) table.getModel();
	            Box box = Box.createVerticalBox();
	            box.add(f.statusBar1);
	            box.add(f.statusBar2);
	            box.add(table);
	            f.apptBox = box;
	            JScrollPane p = new JScrollPane(box);
	            f.splitPane.setRightComponent(p);
	        } else {
	        	
	            f.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	            f.apptTableModel.setAppt(apptDate,
	                                     PSService.getService().
	                                     getApptForDay(name, apptDate));
	            f.apptTableModel.setProvider(name);
	            f.apptTable.setVisible(false);
	            f.apptTable.invalidate();
	            f.apptTable.setVisible(true);
	            f.setCursor(null);
	            if (f.apptBox == null){
		            Box box = Box.createVerticalBox();
		            box.add(f.statusBar1);
		            box.add(f.statusBar2);
		            box.add(f.apptTable);
		            f.apptBox = box;		            
	            }	            
	            JScrollPane p = new JScrollPane(f.apptBox);
	            f.splitPane.setRightComponent(p);	            
	        }
        }
	}

	/**
	 * @return 0 if evaluation is done or the year where evaluation is missing.
	 */
	public int checkEvaluation(Patient evalPat, int apptYear, Date apptStartTime){
		Calendar adCal = new GregorianCalendar();
		adCal.setTime(evalPat.getInsAdmitDate());
		adCal.set(Calendar.YEAR, apptYear);
		Date adDate = adCal.getTime();
		if (adDate.after(apptStartTime)){
			// no need to check evaluation is already done if admit date is after appointment date
			return 0;  
		}
		
		// check evalution status
		List<Appointment> appts = readSvc.getEvaluationApptForPatient(evalPat.getRefId(), ""+apptYear);
		for (Appointment appt : appts){
			if (appt.getStatus().equals(Constant.Seen)){
				if (appt.getStartDate().before(apptStartTime)){
					return 0;
				}
			}
		}
		List<Evaluation> evals = readSvc.getAllEvaluation(evalPat.getRefId());
		for (Evaluation eval : evals){
			if (apptYear == eval.getYear()){
				return 0;
			}
		}
		
		// no evaluation done 
		return apptYear;
	}
	
	private void debug(String msg){
        System.out.println("[PSService]: "+msg);
    }

}
