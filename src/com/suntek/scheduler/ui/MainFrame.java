package com.suntek.scheduler.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import java.util.List;
import java.text.DateFormat;
import javax.swing.event.*;

import com.toedter.calendar.JCalendar;
import com.suntek.scheduler.appsvcs.persistence.Constant;
import com.suntek.scheduler.appsvcs.JDBCConnector;

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
public class MainFrame
    extends JFrame implements TreeSelectionListener{

    boolean debugOn = Boolean.getBoolean("debugOn");

    public static MainFrame f = null;

    DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);

    JPanel contentPane;

    BorderLayout borderLayout = new BorderLayout();
    FlowLayout flowLayout = new FlowLayout();
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
    JCalendar jCalendar = null;

    JMenuBar jMenuBar = new JMenuBar();
    JMenu jMenuFile = new JMenu();
    JMenuItem jMenuFileExit = new JMenuItem();
    JMenuItem jMenuPrintAppt = new JMenuItem();

    JMenu jMenuSearch = new JMenu();
    JMenuItem jMenuSearchAppt = new JMenuItem();

    JMenu jMenuAdmin = new JMenu();
    JMenuItem jMenuAddHoliday = new JMenuItem();

    JMenu jMenuReport = new JMenu();
    JMenuItem jMenuContraCostaEval = new JMenuItem();
    
    JMenu jMenuHelp = new JMenu();
    JMenuItem jMenuHelpAbout = new JMenuItem();

    JLabel statusBar1 = new JLabel();
    JLabel statusBar2 = new JLabel();
    JLabel bottomStatusBar = new JLabel();
    JTree launchPad = null;
    JPanel workArea = null;

    JPopupMenu providerFolderPU = null;
    JPopupMenu patientFolderPU = null;
    JPopupMenu clinicFolderPU = null;

    JPopupMenu providerPU = null;
    JPopupMenu patientPU = null;
    JPopupMenu providerPU2 = null;
    JPopupMenu patientPU2 = null;    

    static final int UNKNOWN = 0;
    static final int PROVIDER_FOLDER = 1;
    static final int PATIENT_FOLDER = 2;
    static final int PROVIDER_REC = 3;
    static final int PATIENT_REC = 4;    
    static final int CLINIC_FOLDER = 5;
    static final int CLINIC_REC = 6;
    static final int PATIENT_SUB_FOLDER = 7;
    static final int INACT_PROVIDER_REC = 8;
    static final int DC_PATIENT_REC = 9;
 

    int selectedNodeType = 0;

    PSService service = null;

    Calendar selectedDate = null;
    private DefaultMutableTreeNode selectedProviderNode = null;
    private DefaultMutableTreeNode selectedPatNode = null;
    private DefaultMutableTreeNode selectedClinicNode = null;
    private DefaultMutableTreeNode selectedInactProviderNode = null;
    private DefaultMutableTreeNode selectedDcPatNode = null;    

    DailyApptTableModel apptTableModel = null;
    JTable apptTable = null;
    Box apptBox = null;

    boolean isWorkAreaActive = false;

    int nextLocX = 220;
    int nextLocY = 110;
    int numPopup = 0;

    int nextLocClinicX = 110;
    int nextLocClinicY = 55;
    int numClinicPopup = 0;



    public MainFrame() {
        try {
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e){
                    int reply = JOptionPane.showConfirmDialog(f,
                        "Are you sure you want to exit the Scheduler?",
                        "Comfirm Exit",
                        JOptionPane.YES_NO_OPTION
                        );
                    if (reply == JOptionPane.YES_OPTION){
                        f.dispose();
                        JDBCConnector.getInstance().destroy();
                        System.exit(0);
                    }
                }
            });
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            jbInit();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int getNextLocX(){
        numPopup += 1;
        if (numPopup > 5){
            numPopup = 0;
            nextLocX = 220;
            nextLocY = 110;
        }
        nextLocX += 20;
        nextLocY += 20;
        return nextLocX;
    }

    public int getNextLocY(){
        return nextLocY;
    }

    public int getNextLocClinicX(){
        numClinicPopup += 1;
        if (numClinicPopup > 5){
            numClinicPopup = 0;
            nextLocClinicX = 110;
            nextLocClinicY = 55;
        }
        nextLocClinicX += 20;
        nextLocClinicY += 20;
        return nextLocClinicX;
    }

    public int getNextLocClinicY(){
        return nextLocClinicY;
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        f = this;

        service = PSService.getService();
        service.setMainFrame(this);

        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout);
        setSize(new Dimension(800, 600));
        setTitle("BHR Appointment Scheduler - "+Constant.selectedClinic+" Clinic, "+Constant.appUser);

        bottomStatusBar.setText(" ");

        jMenuFile.setText("File");
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new
                                        MainFrame_jMenuFileExit_ActionAdapter(this));
        jMenuPrintAppt.setText("Print Schedule ...");
        jMenuPrintAppt.addActionListener(new
                                     MainFrame_jMenuPrint_ActionAdapter(this));

        jMenuSearch.setText("Search");
        jMenuSearchAppt.setText("Confirmation# ...");
        jMenuSearchAppt.addActionListener(new
                                          MainFrame_jMenuSearchAppt_ActionAdapter(this));

        jMenuAdmin.setText("Admin");
        jMenuAddHoliday.setText("Add Holiday ...");
        jMenuAddHoliday.addActionListener(new 
        		MainFrame_jMenuAddHoliday_ActionAdapter(this));
        
        jMenuReport.setText("Report");
        jMenuContraCostaEval.setText("Contra Costa Access Annual Evaluation");
        jMenuContraCostaEval.addActionListener(new 
        		MainFrame_jMenuContraCostaEval_ActionAdapter(this));
        
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new
                                         MainFrame_jMenuHelpAbout_ActionAdapter(this));
        jMenuBar.add(jMenuFile);
        jMenuFile.add(jMenuPrintAppt);
        jMenuFile.add(jMenuFileExit);
        jMenuBar.add(jMenuSearch);
        jMenuSearch.add(jMenuSearchAppt);
        if (Constant.appRole.contains(Constant.ADMINISTRATOR) ||
        	Constant.appRole.contains(Constant.MANAGER)){
        	jMenuBar.add(jMenuAdmin);
        	jMenuAdmin.add(jMenuAddHoliday);
        }
        if (Constant.appRole.contains(Constant.ADMINISTRATOR) ||
        	Constant.appRole.contains(Constant.BILLING)){
        	jMenuBar.add(jMenuReport);
        	jMenuReport.add(jMenuContraCostaEval);
        }
        jMenuBar.add(jMenuHelp);
        jMenuHelp.add(jMenuHelpAbout);
        setJMenuBar(jMenuBar);

        JMenuItem menuItem;

        JTreePopupActionAdapter listener = new JTreePopupActionAdapter(this);

        //Create the popup menu.
        providerFolderPU = new JPopupMenu();
        menuItem = new JMenuItem("Refresh Provider list");
        menuItem.setActionCommand("refreshProviderList");
        menuItem.addActionListener(listener);
        providerFolderPU.add(menuItem);

        patientFolderPU = new JPopupMenu();
        menuItem = new JMenuItem("Refresh Patient list");
        menuItem.setActionCommand("refreshPatientList");
        menuItem.addActionListener(listener);
        patientFolderPU.add(menuItem);

        clinicFolderPU = new JPopupMenu();
        menuItem = new JMenuItem("Refresh Clinic list");
        menuItem.setActionCommand("refreshClinicList");
        menuItem.addActionListener(listener);
        clinicFolderPU.add(menuItem);

        providerPU = new JPopupMenu();
        menuItem = new JMenuItem("Move to Inactive list");
        menuItem.setActionCommand("hideProvider");
        menuItem.addActionListener(listener);
        providerPU.add(menuItem);
        //providerPU.addSeparator();
        //menuItem = new JMenuItem("Delete Provider");
        //menuItem.addActionListener(listener);
        //providerPU.add(menuItem);

        patientPU = new JPopupMenu();
        menuItem = new JMenuItem("Discharge Patient");
        menuItem.setActionCommand("hidePatient");
        menuItem.addActionListener(listener);
        patientPU.add(menuItem);
        //patientPU.addSeparator();
        //menuItem = new JMenuItem("Delete Patient");
        //menuItem.addActionListener(listener);
        //patientPU.add(menuItem);

        providerPU2 = new JPopupMenu();
        menuItem = new JMenuItem("Move to Active list");
        menuItem.setActionCommand("unhideProvider");
        menuItem.addActionListener(listener);
        providerPU2.add(menuItem);
                
        patientPU2 = new JPopupMenu();
        menuItem = new JMenuItem("Re-Admit Patient");
        menuItem.setActionCommand("unhidePatient");
        menuItem.addActionListener(listener);
        patientPU2.add(menuItem);
        
        TreeNode root = service.getRootNode();
        createLaunchPad(root);

        workArea = new JPanel();

        selectedDate = new GregorianCalendar();
        jCalendar = new JCalendar(selectedDate.getTime(), null, true, true);
        jCalendar.addPropertyChangeListener(new JCalendarPropertyChangeAdapter(this));
        jCalendar.setWeekOfYearVisible(false);
        updateStatusBar();
        JPanel calPanel = new JPanel();
        calPanel.add(jCalendar);
        leftSplitPane.setBottomComponent(calPanel);
        leftSplitPane.setDividerLocation(400);

        splitPane.setLeftComponent(leftSplitPane);
        splitPane.setRightComponent(workArea);
        splitPane.setOneTouchExpandable(false);
        splitPane.setAutoscrolls(false);
        splitPane.setDividerLocation(242);

        contentPane.add(splitPane, BorderLayout.CENTER);
        contentPane.add(bottomStatusBar, BorderLayout.SOUTH);
    }

    void createLaunchPad(TreeNode root){
        launchPad = new JTree(root, true);
        launchPad.putClientProperty("JTree.lineStyle", "Angled");
        launchPad.setRootVisible(false);
        launchPad.addTreeSelectionListener(this);
        launchPad.addMouseListener(new JTreeMouseAdapter(this));
        JScrollPane sp = new JScrollPane(launchPad);
        leftSplitPane.setDividerLocation(325);
        leftSplitPane.setTopComponent(sp);
    }

    /**
     * File | Exit action performed.
     *
     * @param actionEvent ActionEvent
     */
    void jMenuFileExit_actionPerformed(ActionEvent actionEvent) {
        int reply = JOptionPane.showConfirmDialog(f,
            "Are you sure you want to exit the Scheduler?",
            "Comfirm Exit",
            JOptionPane.YES_NO_OPTION
            );
        if (reply == JOptionPane.YES_OPTION){
            f.dispose();
            JDBCConnector.getInstance().destroy();
            System.exit(0);
        }
    }

    void printAppointmentTable(ActionEvent actionEvent){
        if (apptBox != null){
            PrintUtils.printComponent(apptBox);
        }else{
            JOptionPane.showMessageDialog(f,
                "No provider schedule was selected for printing!",
                "Print error",
                JOptionPane.WARNING_MESSAGE);
        }
    }


    /**
     * Help | About action performed.
     *
     * @param actionEvent ActionEvent
     */
    void jMenuHelpAbout_actionPerformed(ActionEvent actionEvent) {
        MainFrame_AboutBox dlg = new MainFrame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x,
                        (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    // listener for JTree
    public void valueChanged(TreeSelectionEvent event){
        TreePath path = launchPad.getSelectionPath();
        if (path == null){
            return;
        }
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
        if (selectedNode.getUserObject().toString().startsWith("Providers")){
            selectedNodeType = PROVIDER_FOLDER;
        }else if (selectedNode.getUserObject().toString().startsWith("Patients")){
            selectedNodeType = PATIENT_FOLDER;
        }else if (selectedNode.getUserObject().toString().startsWith("Clinics")){
            selectedNodeType = CLINIC_FOLDER;
        }else if (((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject().toString().startsWith("Providers")){
            selectedNodeType = PROVIDER_REC;
            setSelectedProviderNode(selectedNode);
        }else if (((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject().toString().startsWith("Patients")){
            selectedNodeType = PATIENT_REC;
            setSelectedPatNode(selectedNode);
        }else if (((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject().toString().startsWith("Clinics")){
            selectedNodeType = CLINIC_REC;
            setSelectedClinicNode(selectedNode);
        }else if (((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject().toString().startsWith("Inactive Providers")){
            selectedNodeType = INACT_PROVIDER_REC;
            setSelectedInactProviderNode(selectedNode);
        }else if (((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject().toString().startsWith("Discharged Patients")){
            selectedNodeType = DC_PATIENT_REC;
            setSelectedDcPatNode(selectedNode);
        }else{
            selectedNodeType = UNKNOWN;
        }
        debug("selectedNodeType = "+selectedNodeType);
        debug("selectedNode = "+selectedNode.getUserObject().toString());
    }

    public void updateStatusBar(){
        if (selectedDate != null){
            statusBar1.setText("Date:          "+df.format(selectedDate.getTime()));
        }
        if (getSelectedProviderNode() != null){
            statusBar2.setText("Provider:   "+getSelectedProviderNode().getUserObject().toString());
        }
    }

    private void debug(String msg){
        if (debugOn){
            System.out.println("[MainFrame]: "+msg);
        }
    }
    
    void setSelectedProviderNode(DefaultMutableTreeNode selectedProviderNode) {
		this.selectedProviderNode = selectedProviderNode;
	}

	DefaultMutableTreeNode getSelectedProviderNode() {
		return selectedProviderNode;
	}

	void setSelectedPatNode(DefaultMutableTreeNode selectedPatNode) {
		this.selectedPatNode = selectedPatNode;
	}

	DefaultMutableTreeNode getSelectedPatNode() {
		return selectedPatNode;
	}

	void setSelectedClinicNode(DefaultMutableTreeNode selectedClinicNode) {
		this.selectedClinicNode = selectedClinicNode;
	}

	DefaultMutableTreeNode getSelectedClinicNode() {
		return selectedClinicNode;
	}

	void setSelectedInactProviderNode(DefaultMutableTreeNode selectedInactProviderNode) {
		this.selectedInactProviderNode = selectedInactProviderNode;
	}

	DefaultMutableTreeNode getSelectedInactProviderNode() {
		return selectedInactProviderNode;
	}

	void setSelectedDcPatNode(DefaultMutableTreeNode selectedDcPatNode) {
		this.selectedDcPatNode = selectedDcPatNode;
	}

	DefaultMutableTreeNode getSelectedDcPatNode() {
		return selectedDcPatNode;
	}

	class MainFrame_jMenuAddHoliday_ActionAdapter
		implements ActionListener {
    	MainFrame adaptee;

    	MainFrame_jMenuAddHoliday_ActionAdapter(MainFrame adaptee){
    		this.adaptee = adaptee;
    	}
	
    	public void actionPerformed(ActionEvent actionEvent) {
    		new AddHolidayDialog(f, f.getNextLocX(), f.getNextLocY(), false);
    	}
    }
    
	class MainFrame_jMenuContraCostaEval_ActionAdapter
		implements ActionListener {
		MainFrame adaptee;

		MainFrame_jMenuContraCostaEval_ActionAdapter(MainFrame adaptee){
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent actionEvent) {
	        final JDialog selectEvalYearDialog = new JDialog(f);
	        
	        Calendar today = new GregorianCalendar();
	        int thisYear = today.get(Calendar.YEAR);
	        int lastYear = thisYear - 1;
	        int nextYear = thisYear + 1;
	        List<String> allYear = new ArrayList<String>();
	        allYear.add(String.valueOf(lastYear));
	        allYear.add(String.valueOf(thisYear));
	        allYear.add(String.valueOf(nextYear));
	        
	        final JComboBox yearCB = new JComboBox(allYear.toArray());
	        yearCB.setSelectedIndex(1);
	        
	        JButton okButton = new JButton("OK");
	        okButton.setActionCommand("ok");
	        okButton.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent actionEvent){
	        		selectEvalYearDialog.setVisible(false);
	        		selectEvalYearDialog.dispose();	        		
	        		String year = (String)yearCB.getSelectedItem();
	        		new EvaluationReportDialog(f, f.getNextLocX(), f.getNextLocY(), false, year);
	        	}
	        });

	        selectEvalYearDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        selectEvalYearDialog.setLayout(new BorderLayout());
	        selectEvalYearDialog.setResizable(true);
	        selectEvalYearDialog.setTitle("Evaluation Year Selection");
	        selectEvalYearDialog.setSize(280, 140);	   
	        selectEvalYearDialog.setModal(true);
	        selectEvalYearDialog.setLocation(f.getNextLocX(), f.getNextLocClinicY());

	        JPanel topPanel = new JPanel();
	        JLabel label = new JLabel("Please select a year for evaluation:");
	        topPanel.add(label);
	        
	        JPanel centerPanel = new JPanel();
	        JLabel yearL = new JLabel("Year: ", JLabel.TRAILING);
	        centerPanel.add(yearL);
	        centerPanel.add(yearCB);

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	        buttonPanel.add(okButton);

	        selectEvalYearDialog.getContentPane().add(topPanel, BorderLayout.NORTH);
	        selectEvalYearDialog.getContentPane().add(centerPanel, BorderLayout.CENTER);
	        selectEvalYearDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	        selectEvalYearDialog.setVisible(true);
		}
	}

	
}

class MainFrame_jMenuSearchAppt_ActionAdapter
    implements ActionListener {
    MainFrame adaptee;

    MainFrame_jMenuSearchAppt_ActionAdapter(MainFrame adaptee){
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String confirmNum = JOptionPane.showInputDialog(adaptee,
            "Please enter appointment confirmation#: ",
            "Find appointment",
            JOptionPane.PLAIN_MESSAGE);

        final JDialog apptDialog = new JDialog(MainFrame.f, "Confirmation#: "+confirmNum);

        apptDialog.setLayout(new BorderLayout());
        apptDialog.setSize(500, 335);
        apptDialog.setResizable(false);

        JPanel panel = null;
        try{
            panel = PSService.getService().getApptPanelForApptId(Long.parseLong(
                confirmNum));
        }catch(NumberFormatException e){
            return;
        }

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apptDialog.setVisible(false);
                apptDialog.dispose();
            }
        }
        );

        actionPanel.add(okButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        apptDialog.add(mainPanel);
        apptDialog.setVisible(true);
    }

}

class MainFrame_jMenuPrint_ActionAdapter
    implements ActionListener {
    MainFrame adaptee;

    MainFrame_jMenuPrint_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        this.adaptee.printAppointmentTable(actionEvent);
    }
}

class MainFrame_jMenuFileExit_ActionAdapter
    implements ActionListener {
    MainFrame adaptee;

    MainFrame_jMenuFileExit_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        adaptee.jMenuFileExit_actionPerformed(actionEvent);
    }
}

class MainFrame_jMenuHelpAbout_ActionAdapter
    implements ActionListener {
    MainFrame adaptee;

    MainFrame_jMenuHelpAbout_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        adaptee.jMenuHelpAbout_actionPerformed(actionEvent);
    }
}
