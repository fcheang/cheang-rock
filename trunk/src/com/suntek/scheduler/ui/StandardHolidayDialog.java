package com.suntek.scheduler.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.suntek.scheduler.appsvcs.ReadSvc;
import com.suntek.scheduler.appsvcs.WriteSvc;
import com.suntek.scheduler.appsvcs.persistence.StandardHoliday;
import com.suntek.scheduler.util.HolidayCalculator;

public class StandardHolidayDialog extends JDialog {

	private JDialog dialog;
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
	
	public StandardHolidayDialog(Frame owner, int locX, int locY, boolean model){
		super(owner, model);
		init(locX, locY);
	}
	
	private void init(int locX, int locY){
		dialog = this;
		setSize(500, 500);
        setTitle("Standard Holiday");
        setResizable(true);
        if (locX != -1 && locY != -1){
        	setLocation(locX, locY);
        }
        
        initCheckbox();
        JPanel mainPanel = createMainPanel();        
        JPanel actionPanel = createGlobalActionPanel();
        mainPanel.setBorder(BorderFactory.createEtchedBorder()); 
        JPanel p = new JPanel(new BorderLayout());
        p.add(mainPanel, BorderLayout.CENTER);
        p.add(actionPanel, BorderLayout.SOUTH);

        dialog.add(p);
        dialog.setVisible(true);        
	}
	
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
	
	private JPanel createMainPanel(){
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
