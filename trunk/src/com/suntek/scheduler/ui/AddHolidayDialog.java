package com.suntek.scheduler.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import com.suntek.scheduler.appsvcs.ReadSvc;
import com.suntek.scheduler.appsvcs.WriteSvc;
import com.suntek.scheduler.appsvcs.persistence.Holiday;
import com.suntek.scheduler.appsvcs.persistence.Insurance;
import com.suntek.scheduler.appsvcs.persistence.StandardHoliday;

public class AddHolidayDialog extends JDialog {

	private AddHolidayDialog dialog = this;

	//////////////////////////
	// Standard Holiday Tab //
	//////////////////////////

	private JLabel label = new JLabel("Please select the holidays that will be observed by the company:");
	
	private JCheckBox newYearCB      = new JCheckBox(StandardHoliday.NEW_YEARS_DAY + "     (1/1)");
	private JCheckBox mlkCB          = new JCheckBox(StandardHoliday.MLK_DAY + "     (3rd Monday in January)");
	private JCheckBox presidentDayCB = new JCheckBox(StandardHoliday.PRESIDENTS_DAY + "     (3rd Monday in February)");
	private JCheckBox memorialDayCB  = new JCheckBox(StandardHoliday.MEMORIAL_DAY + "     (last Monday in May)");
	private JCheckBox indepDayCB     = new JCheckBox(StandardHoliday.INDEPENDENCE_DAY + "     (7/4)");
	private JCheckBox laborDayCB     = new JCheckBox(StandardHoliday.LABOR_DAY + "     (first Monday in September)");
	private JCheckBox columbusDayCB  = new JCheckBox(StandardHoliday.COLUMBUS_DAY + "     (seconday Monday in October)");
	private JCheckBox veteransDayCB  = new JCheckBox(StandardHoliday.VETERANS_DAY + "     (11/11)");
	private JCheckBox thanksGivingCB = new JCheckBox(StandardHoliday.THANKSGIVING_DAY + "     (fourth Thursday in November)");
	private JCheckBox christmasDayCB = new JCheckBox(StandardHoliday.CHRISTMAS_DAY + "     (12/25)"); 
	
	private boolean newYearActive = false;
	private boolean mlkActive = false;
	private boolean presidentDayActive = false;
	private boolean memorialDayActive = false;
	private boolean indepDayActive = false;
	private boolean laborDayActive = false;
	private boolean columbusDayActive = false;
	private boolean veteransDayActive = false;
	private boolean thanksGivingActive = false;
	private boolean christmasDayActive = false;
    
	
	
	////////////////////////
	// Custom Holiday Tab //
	////////////////////////
	
	private HolidayTableModel tableModel = null;
	private JTable table = null;
    private JButton addButton = null;
    private JButton saveButton = null;
    private JButton deleteButton = null;
    private JPanel detailPanel = null;
	
    private int holidayId = -1;
    private JTextField startDateTF = null;
    private JTextField endDateTF = null;
    private JTextArea descTA = null;
    
    private JLabel startDateL = new JLabel("Start Date: ");
    private JLabel startDateFormatL = new JLabel("mm/dd/yyyy");
    private JLabel endDateL = new JLabel("End Date: ");
    private JLabel endDateFormatL = new JLabel("mm/dd/yyyy");
    private JLabel descL = new JLabel("Description: ");
    private JScrollPane descSP = null;    
    
    private Spring shortStrut = Spring.constant(5);
    private Spring mediumStrut = Spring.constant(10);	    


    
	public AddHolidayDialog(Frame owner, int locX, int locY, boolean model){
		super(owner, model);
		init(locX, locY);
	}
	
	private void init(int locX, int locY){		
		dialog = this;
		setSize(500, 500);
        setTitle("Add Holiday");
        setResizable(true);
        if (locX != -1 && locY != -1){
        	setLocation(locX, locY);
        }

        initCheckbox();
        JPanel stdHolidayPanel = createStdHolidayPanel();        
        stdHolidayPanel.setBorder(BorderFactory.createEtchedBorder());
        
        JPanel custHolidayPanel = createCustomHolidayPanel();
        custHolidayPanel.setBorder(BorderFactory.createEtchedBorder());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Standard Holiday", stdHolidayPanel);
        tabbedPane.addTab("Custom Holiday", custHolidayPanel);		
                
        JPanel actionPanel = createGlobalActionPanel();
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(tabbedPane, BorderLayout.CENTER);
        p.add(actionPanel, BorderLayout.SOUTH);

        dialog.add(p);
        dialog.setVisible(true);        
	}
	
	////////////////////////
	// Custom Holiday Tab //
	////////////////////////
	
	
	private List getHolidays(){
		return ReadSvc.getInstance().getHolidays();
	}
	
	private JPanel createCustomHolidayPanel(){
    	JPanel mainPanel = new JPanel(new BorderLayout(15,15));

    	// Table Summary
    	tableModel = new HolidayTableModel(getHolidays());
    	table = new JTable(tableModel);
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	table.getColumn(HolidayTableModel.START_DATE_COL).setPreferredWidth(90);    	
    	table.getColumn(HolidayTableModel.END_DATE_COL).setPreferredWidth(90);    	
    	table.getColumn(HolidayTableModel.DESCRIPTION_COL).setPreferredWidth(300);
    	
    	table.getSelectionModel().addListSelectionListener(
    			new ListSelectionListener() {
    				  public void valueChanged(ListSelectionEvent e){
    					  if (e.getValueIsAdjusting()){
    						  return;
    					  }
    					  ListSelectionModel lsm = (ListSelectionModel)e.getSource();
    					  if (lsm.isSelectionEmpty()){
    						  detailPanel.setVisible(false);
    						  deleteButton.setEnabled(false);
    						  saveButton.setEnabled(false);
    					  }else{
    			    	    	// set the detail panel
    			    	    	int selectedRow = table.getSelectedRow();
    			    	    	if (selectedRow >= 0){
    			    	    		Holiday h = tableModel.getHoliday(selectedRow);
    			    	    		holidayId = h.getId();
    			    	    		
    			    	    		startDateTF.setText(h.getStartDateStr());
    			    	    		startDateTF.setEnabled(true);
    			    	    		
    			    	    		endDateTF.setText(h.getEndDateStr());
    			    	    		endDateTF.setEnabled(true);
    			    	    		
    			    	    		descTA.setText(h.getDesc());
    			    	    		descTA.setEnabled(true);
    			    	    		
    			    	    		addButton.setEnabled(true);
    			    	    		saveButton.setEnabled(false);
    			    	    		deleteButton.setEnabled(true);    			    	    		
    			    	    		detailPanel.setVisible(true);    
    			    	    	}
    					  }
    				  }
    			}
    	);

    	JScrollPane tableScroller = new JScrollPane(table);
    	tableScroller.setPreferredSize(new Dimension(270, 200));
    	    	    	
        // Detail Panel
    	detailPanel = createDetailPanel();
    	detailPanel.setVisible(false);
    	
        // Action Panel
        JPanel actionPanel = createActionPanel();
        
        mainPanel.add(tableScroller, BorderLayout.NORTH);
        mainPanel.add(detailPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        return mainPanel;
	}
	
    private void enableAllTF(){
    	startDateTF.setEnabled(true);
    	endDateTF.setEnabled(true);    	
		descTA.setEnabled(true);
    }
    
    private void clearAllTF(){
    	startDateTF.setText("");
    	endDateTF.setText("");
		descTA.setText("");
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
    
    private boolean populateFromForm(Holiday h){    	    	
    	String sdStr = startDateTF.getText();
    	Date sd = null;
		if (sdStr != null && !sdStr.equals("")){
			sd = validDateFromString(sdStr, "Start Date");
			if (sd != null){
				h.setStartDate(sd);
			}else{
				startDateTF.grabFocus();
				return false;
			}
		}else{
			JOptionPane.showMessageDialog(this, "Please specify a value for Start Date!",
					"Missing value", JOptionPane.ERROR_MESSAGE);
			startDateTF.grabFocus();
			return false;
		}

    	String edStr = endDateTF.getText();
    	if (edStr == null || edStr.equals("")){
    		edStr = startDateTF.getText();
    	}
		if (edStr != null && !edStr.equals("")){
			Date ed = validDateFromString(edStr, "End Date");
			if (ed.before(sd)){
				JOptionPane.showMessageDialog(this, "End Date must be equal to or after Start Date!",
						"Invalid value", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (ed != null){
				h.setEndDate(ed);
			}else{
				endDateTF.grabFocus();
				return false;
			}
		}		
		
		h.setDesc(descTA.getText()); 
		
    	return true;    	
    }    
    
    private boolean createHoliday(){
    	Holiday h = new Holiday();
    	
    	h.setId(holidayId);    	
    	if (populateFromForm(h)){
    		WriteSvc.getInstance().createHoliday(h);
    		return true;
    	}else{
    		return false;
    	}    	
    }
    
    private void refreshTable(){
    	List hList = ReadSvc.getInstance().getHolidays();
    	tableModel.setHolidays(hList);
    	tableModel.fireTableDataChanged();
    }    
    
    private boolean updateHoliday(){    	
    	Holiday oldH = tableModel.getHoliday(table.getSelectedRow());
    	
    	// set value for newIns
    	Holiday newH = new Holiday();
    	newH.setId(holidayId);    	
    	if (!populateFromForm(newH)){
    		return false;
    	}
    	
    	String sql = constructUpdateSQL(oldH, newH);
    	if (sql != null){
    		WriteSvc.getInstance().execDynamicSql(sql, null);
    	}
    	return true;    	
    }
    
    private String constructUpdateSQL(Holiday oldH, Holiday newH){
    	boolean dirty = false;
    	String sql = "update holidayMap set ";
    	
		if ( (oldH.getStartDate() != null && newH.getStartDate() != null) ||
			 (oldH.getStartDate() == null && newH.getStartDate() != null)) 
		{
			String dateStr = newH.getStartDateForSQL();
			sql += "startDate = '"+dateStr+"', ";
			dirty = true;
		} else if (oldH.getStartDate() != null && newH.getStartDate() == null){
			sql += "startDate = null, ";
			dirty = true;
		}
        	
		if ( (oldH.getEndDate() != null && newH.getEndDate() != null) ||
			 (oldH.getEndDate() == null && newH.getEndDate() != null)) 
		{
			String dateStr = newH.getEndDateForSQL();
			sql += "endDate = '"+dateStr+"', ";
			dirty = true;
		} else if (oldH.getEndDate() != null && newH.getEndDate() == null){
			sql += "endDate = null, ";
			dirty = true;
		}

		if (!oldH.getDesc().equals(newH.getDesc())){
			sql += "description = '"+newH.getDesc()+"', ";
			dirty = true;
		}
		
		if (dirty){
			// remove last comma
			int lastComma = sql.lastIndexOf(",");
			sql = sql.substring(0, lastComma);
			sql += " where holidayMapId = "+oldH.getId();
			return sql;
    			
		}else{
			return null;
		}    	
    }
    
    
    private JPanel createActionPanel(){
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		Holiday h = new Holiday();
        		h.setId(-1);
        		tableModel.addHoliday(h);
        		holidayId = -1;
        		int lastRow = table.getRowCount();
        		table.setRowSelectionInterval(lastRow - 1, lastRow - 1);
        		detailPanel.setVisible(true);
        		enableAllTF();
        		clearAllTF();
        		addButton.setEnabled(false);
        	}
        });

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (holidayId == -1){
        			if (createHoliday()){
                		saveButton.setEnabled(false);
                		addButton.setEnabled(true);
                		refreshTable();
        			}else{
                		saveButton.setEnabled(true);        				
        			}
        		}else{
        			if (updateHoliday()){
                		saveButton.setEnabled(false);
                		addButton.setEnabled(true);
                		refreshTable();
        			}else{
                		saveButton.setEnabled(true);        				
        			}
        		}
        		saveButton.setEnabled(false);
        	}
        });
        saveButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (holidayId == -1){
        			clearHoliday();
        			addButton.setEnabled(true);
        		}else{
                    int reply = JOptionPane.showConfirmDialog(dialog,
                            "Are you sure you want to delete the holiday?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION){
                    	deleteHoliday();
                    	refreshTable();
                		addButton.setEnabled(true);                    	
                    }
        		}
        	}
        });
        deleteButton.setEnabled(false);
        
        actionPanel.add(addButton);
        actionPanel.add(saveButton);
        actionPanel.add(deleteButton);
        return actionPanel;                	
    }
	
    private void deleteHoliday(){
    	WriteSvc.getInstance().deleteHoliday(holidayId);
    	tableModel.removeHolidayById(holidayId);
    	tableModel.fireTableDataChanged();
    	clearAllFields();
    }    
    
    private void clearHoliday(){
    	tableModel.removeHolidayById(holidayId);
    	tableModel.fireTableDataChanged();
    	clearAllFields();
    }    
    
    private void clearAllFields(){
    	holidayId = -1;
    	startDateTF.setText("");
    	endDateTF.setText("");
    	descTA.setText("");
    	saveButton.setEnabled(false);
    	deleteButton.setEnabled(false);
    }
    
	
    private void addDocumentListener(JTextComponent c){
    	c.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){
            	saveButton.setEnabled(true);
            }
            public void removeUpdate(DocumentEvent e){
            	saveButton.setEnabled(true);            	
            }
            public void changedUpdate(DocumentEvent e){
            	saveButton.setEnabled(true);            	
            }
        });
    }
	
	private void initDetailPanelComponent(){
    	startDateTF = new JTextField(10);
    	startDateTF.setEnabled(false);
    	
    	endDateTF = new JTextField(10);
    	endDateTF.setEnabled(false);
    	        
        descTA = new JTextArea(4, 25);
        descTA.setLineWrap(true);
        descTA.setEnabled(false);

        addDocumentListener(startDateTF);
        addDocumentListener(endDateTF);
        addDocumentListener(descTA);
	}

    private void alignRow1(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, startDateL, leftmostStrut,
				 SpringLayout.WEST, detailPanel);        
    	layout.putConstraint(SpringLayout.WEST, startDateTF, shortStrut,
				 SpringLayout.EAST, startDateL);
    	layout.putConstraint(SpringLayout.WEST, startDateFormatL, shortStrut,
				 SpringLayout.EAST, startDateTF);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, startDateL, mediumStrut,
                SpringLayout.NORTH, detailPanel);
        layout.putConstraint(SpringLayout.NORTH, startDateTF, mediumStrut,
                SpringLayout.NORTH, detailPanel);
        layout.putConstraint(SpringLayout.NORTH, startDateFormatL, mediumStrut,
                SpringLayout.NORTH, detailPanel);
    }

    private void alignRow2(JPanel detailPanel, SpringLayout layout, 
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST, endDateL, leftmostStrut,
				 SpringLayout.WEST, detailPanel);        
    	layout.putConstraint(SpringLayout.WEST, endDateTF, shortStrut,
				 SpringLayout.EAST, startDateL);
    	layout.putConstraint(SpringLayout.WEST, endDateFormatL, shortStrut,
				 SpringLayout.EAST, startDateTF);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, endDateL, mediumStrut,
                SpringLayout.SOUTH, startDateL);
        layout.putConstraint(SpringLayout.NORTH, endDateTF, mediumStrut,
                SpringLayout.SOUTH, startDateL);
        layout.putConstraint(SpringLayout.NORTH, endDateFormatL, mediumStrut,
                SpringLayout.SOUTH, startDateL);
    }
        
    private void alignRow3(JPanel detailPanel, SpringLayout layout,
    		Spring leftmostStrut)
    {
    	// align col
    	layout.putConstraint(SpringLayout.EAST,descL , leftmostStrut,
				SpringLayout.WEST, detailPanel);
    	layout.putConstraint(SpringLayout.WEST, descSP, shortStrut,
				SpringLayout.EAST, descL);
    	// align row
        layout.putConstraint(SpringLayout.NORTH, descL, mediumStrut,
                SpringLayout.SOUTH, endDateTF);
        layout.putConstraint(SpringLayout.NORTH, descSP, mediumStrut,
                SpringLayout.SOUTH, endDateTF);    	            	                    	    	            	    	    	
    }
	
	private JPanel createDetailPanel(){
        JPanel detailPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        detailPanel.setLayout(layout);

        initDetailPanelComponent();
                                
        detailPanel.add(startDateL);
        detailPanel.add(startDateTF);
        detailPanel.add(startDateFormatL);
        detailPanel.add(endDateL); 
        detailPanel.add(endDateTF);
        detailPanel.add(endDateFormatL);
        detailPanel.add(descL);
        descSP = new JScrollPane(descTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailPanel.add(descSP);
       
        Spring leftmostStrut = Spring.sum(mediumStrut, layout.getConstraints(descL).getWidth());
        
        alignRow1(detailPanel, layout, leftmostStrut);                
        alignRow2(detailPanel, layout, leftmostStrut);
        alignRow3(detailPanel, layout, leftmostStrut);
                                        
        return detailPanel;		
	}
	
	
	//////////////////////////
	// Standard Holiday Tab //
	//////////////////////////
	
	private void initCheckbox(){
		List hdays = ReadSvc.getInstance().getStdHolidays();
		for (int i=0; i<hdays.size(); i++){
			StandardHoliday h = (StandardHoliday)hdays.get(i);
			if (h.getName().equals(StandardHoliday.NEW_YEARS_DAY)){
				newYearCB.setSelected(h.isActive());			
				newYearActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.MLK_DAY)){
				mlkCB.setSelected(h.isActive());
				mlkActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.PRESIDENTS_DAY)){
				presidentDayCB.setSelected(h.isActive());
				presidentDayActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.MEMORIAL_DAY)){
				memorialDayCB.setSelected(h.isActive());
				memorialDayActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.INDEPENDENCE_DAY)){
				indepDayCB.setSelected(h.isActive());
				indepDayActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.LABOR_DAY)){
				laborDayCB.setSelected(h.isActive());
				laborDayActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.COLUMBUS_DAY)){
				columbusDayCB.setSelected(h.isActive());
				columbusDayActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.VETERANS_DAY)){
				veteransDayCB.setSelected(h.isActive());
				veteransDayActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.THANKSGIVING_DAY)){
				thanksGivingCB.setSelected(h.isActive());
				thanksGivingActive = h.isActive();
			}else if (h.getName().equals(StandardHoliday.CHRISTMAS_DAY)){
				christmasDayCB.setSelected(h.isActive());
				christmasDayActive = h.isActive();
			}
		}
	}	
	
	private JPanel createStdHolidayPanel(){
    	JPanel mainPanel = new JPanel(new BorderLayout(20,20));
    	JPanel titlePanel = new JPanel();
    	titlePanel.add(label);
    	JPanel cbPanel = new JPanel(new GridLayout(10, 1));

    	JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p1.add(newYearCB);
    	cbPanel.add(p1);
    	
    	JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p2.add(mlkCB);    	
    	cbPanel.add(p2);    	
    	
    	JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p3.add(presidentDayCB);    	
    	cbPanel.add(p3);    	

    	JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p4.add(memorialDayCB);    	
    	cbPanel.add(p4);    	

    	JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p5.add(indepDayCB);    	
    	cbPanel.add(p5);    	

    	JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p6.add(laborDayCB);    	
    	cbPanel.add(p6);    	

    	JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p7.add(columbusDayCB);    	
    	cbPanel.add(p7);    	

    	JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p8.add(veteransDayCB);    	
    	cbPanel.add(p2);    	

    	JPanel p9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p9.add(thanksGivingCB);    	
    	cbPanel.add(p9);    	

    	JPanel p10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p10.add(christmasDayCB);    	
    	cbPanel.add(p10);    	

    	mainPanel.add(titlePanel, BorderLayout.NORTH);
    	mainPanel.add(cbPanel, BorderLayout.CENTER);
    	return mainPanel;
	}
	
	private JPanel createGlobalActionPanel(){
		JPanel actionPanel = 
        new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // save modification
                if (hasChanged()) {
                    int reply = JOptionPane.showConfirmDialog(dialog,
                        "Do you want to save change to unsaved record?",
                        "Confirm save",
                        JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        saveChange();
                    }
                }
                dialog.setVisible(false);
                dialog.dispose();
            }
        }
        );

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }
        );

        actionPanel.add(okButton);
        actionPanel.add(cancelButton);
        return actionPanel;
	}		
	
	private boolean hasChanged(){
		if (newYearActive != newYearCB.isSelected()){
			return true;
		}
		if (mlkActive != mlkCB.isSelected()){
			return true;
		}
		if (presidentDayActive != presidentDayCB.isSelected()){
			return true;
		}
		if (memorialDayActive != memorialDayCB.isSelected()){
			return true;
		}
		if (indepDayActive != indepDayCB.isSelected()){
			return true;
		}
		if (laborDayActive != laborDayCB.isSelected()){
			return true;
		}
		if (columbusDayActive != columbusDayCB.isSelected()){
			return true;
		}		
		if (veteransDayActive != veteransDayCB.isSelected()){
			return true;
		}
		if (christmasDayActive != christmasDayCB.isSelected()){
			return true;
		}
		if (thanksGivingActive != thanksGivingCB.isSelected()){
			return true;
		}
		return false;
	}
	
	private void saveChange(){
		if (newYearActive != newYearCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.NEW_YEARS_DAY, newYearCB.isSelected());
		}
		if (mlkActive != mlkCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.MLK_DAY, mlkCB.isSelected());
		}
		if (presidentDayActive != presidentDayCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.PRESIDENTS_DAY, presidentDayCB.isSelected());
		}
		if (memorialDayActive != memorialDayCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.MEMORIAL_DAY, memorialDayCB.isSelected());
		}
		if (indepDayActive != indepDayCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.INDEPENDENCE_DAY, indepDayCB.isSelected());
		}
		if (laborDayActive != laborDayCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.LABOR_DAY, laborDayCB.isSelected());
		}
		if (columbusDayActive != columbusDayCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.COLUMBUS_DAY, columbusDayCB.isSelected());
		}		
		if (veteransDayActive != veteransDayCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.VETERANS_DAY, veteransDayCB.isSelected());
		}
		if (christmasDayActive != christmasDayCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.CHRISTMAS_DAY, christmasDayCB.isSelected());
		}
		if (thanksGivingActive != thanksGivingCB.isSelected()){
			WriteSvc.getInstance().updateStdHolidayStatus(StandardHoliday.THANKSGIVING_DAY, thanksGivingCB.isSelected());
		}		
	}	
		
}
