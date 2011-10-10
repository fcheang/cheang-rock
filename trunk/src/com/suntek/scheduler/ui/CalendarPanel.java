package  com.suntek.scheduler.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Panel containing a calendar to choose a date from.
 */
public class CalendarPanel extends JPanel {
    private Locale defaultLocale = Locale.getDefault();

    private ToggleBtnActionListener tglBtnActionListener = new ToggleBtnActionListener();

    private GregorianCalendar gregorianCalendar = new GregorianCalendar(defaultLocale);
    private int nSelectedYear = gregorianCalendar.get(Calendar.YEAR);
    private int nSelectedMonth = gregorianCalendar.get(Calendar.MONTH);
    private int nSelectedDay = gregorianCalendar.get(Calendar.DATE);
    private int nFirstDayOfWeek = gregorianCalendar.getFirstDayOfWeek();
    int nLocaleFirstDay     = nFirstDayOfWeek;

    private DateFormatSymbols dateSymbols = new DateFormatSymbols(defaultLocale);
    private String[] sMonths = dateSymbols.getMonths();
    private String[] dayStrings = dateSymbols.getShortWeekdays();

    private final int minDayOfMonth = gregorianCalendar.getMinimum(Calendar.DATE);
    private final int maxDayOfMonth = gregorianCalendar.getMaximum(Calendar.DATE);
    private final int minDayOfWeek = gregorianCalendar.getMinimum(Calendar.DAY_OF_WEEK);
    private final int maxDayOfWeek = gregorianCalendar.getMaximum(Calendar.DAY_OF_WEEK);
    private final int minMonth = gregorianCalendar.getMinimum(Calendar.MONTH);
    private final int maxMonth = gregorianCalendar.getMaximum(Calendar.MONTH);

    private boolean bIgnoreEvents = false;

    private JPanel panelCombo = new JPanel(true);
    private ComboMonth comboMonth = new ComboMonth();
    private ComboYear comboYear = new ComboYear();
    private JPanel panelMonth = new JPanel(true);
    private DateChooserButton[] buttonDays = new DateChooserButton[31];
    private ActionListener actionListener;
    private FocusListener focusListener;

    private static final String EMPTY_STRING = "";

    /**
    * Initializes the Calendar
    */
    CalendarPanel() {
        gregorianCalendar.setLenient(false);
        initUI();

        actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!bIgnoreEvents) {
                    fixMonthDisplay();
                }
            }
        };

        comboYear.addActionListener(actionListener);
        comboMonth.addActionListener(actionListener);
    }

    public void destroy() {
        comboYear.removeActionListener(actionListener);
        comboMonth.removeActionListener(actionListener);
        removeFocusListener(focusListener);

        for (int i = 0; i < buttonDays.length; i++) {
            if (buttonDays[i] != null) {
                buttonDays[i].removeActionListener(tglBtnActionListener);
            }
        }
    }

    /**
    * Updates the Calendar with the specified date.
    *
    * @param newDate A date object representing the new date to be selected.
    */
    public void setDate(Date newDate) {
        assert(gregorianCalendar != null);

        if (newDate != null) {
          gregorianCalendar.setTime(newDate);
        }

        bIgnoreEvents = true;
        setYear(gregorianCalendar.get(Calendar.YEAR));
        setMonth(gregorianCalendar.get(Calendar.MONTH));
        bIgnoreEvents = false;
        setDay(gregorianCalendar.get(Calendar.DATE));
        fixMonthDisplay();
    }

    /**
     * Set the selected year.
     * Must be a valid, fully qualified 4 digit year.
     *
     * @param newYear New year to be set.
    */
    private void setYear(int newYear) {
        comboYear.setSelectedItem(new Integer(newYear));
    }

    /**
    * Set the selected month.
    *
    * 0 = January ... 11 = December
    *
    * @param newMonth New Month to be set.
    */
    private void setMonth(int newMonth) {
        comboMonth.setSelectedIndex(newMonth);
    }

    /**
    * Set the day of month.
    *
    * @param newDay New day to be set.
    */
    private void setDay(int newDay) {
        if ((newDay > 0) && (newDay < maxDayOfMonth)){
            if (nSelectedDay != newDay){
                buttonDays[newDay - 1].setSelected(false);
                buttonDays[nSelectedDay - 1].setSelected(true);
                nSelectedDay = newDay;
            }
        }
        gregorianCalendar.set(Calendar.DATE,nSelectedDay);
    }

    /**
    * Get the currently selected date.
    * @return a Date object representing the currently selected date.
    */
    public Date getDate() {
        return new Date(gregorianCalendar.getTime().getTime());
    }

    /**
     * initialize UI.
     */
    private void initUI() {
        panelMonth.setLayout(new GridLayout(7,7));
        Insets buttonMargins = new Insets(2,4,2,4);


        //add day labels
        for (int i = nFirstDayOfWeek; i < maxDayOfWeek + 1; i++) {
            JLabel dayLabel = new JLabel(dayStrings[i]);
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelMonth.add(dayLabel);
        }

        for (int i = minDayOfMonth ; i < nFirstDayOfWeek; i++) {
          JLabel dayLabel = new JLabel(dayStrings[i]);
          dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
          panelMonth.add(dayLabel);
        }

        //add day toggle buttons
        for (int i = minDayOfMonth - 1; i < maxDayOfMonth; i++) {
            buttonDays[i] = new DateChooserButton(Integer.toString(i+1),true);
            buttonDays[i].setMargin(buttonMargins);
            buttonDays[i].addActionListener(tglBtnActionListener);
            panelMonth.add(buttonDays[i]);
        }

        for (int i = minMonth; i < maxMonth; i++) {
            panelMonth.add(new JLabel(EMPTY_STRING));
        }
        panelCombo.add(comboMonth);
        panelCombo.add(comboYear);

        setLayout(new BorderLayout());
        add(panelCombo, BorderLayout.NORTH);
        add(panelMonth, BorderLayout.CENTER);

        bIgnoreEvents = true;
        for (int i = (nSelectedYear - 10); i < (nSelectedYear + 10); i++)
        {
            comboYear.addItem(new Integer(i));
        }
        comboYear.setSelectedIndex(10);

        for (int i = 0; i < sMonths.length - 1; i++)
        {
            comboMonth.addItem(sMonths[i]);
        }
        comboMonth.setSelectedIndex(nSelectedMonth);
        bIgnoreEvents = false;

        buttonDays[nSelectedDay-1].setSelected(false);
        fixMonthDisplay();

        focusListener = new FocusAdapter() {
          public void focusLost(FocusEvent e) {
            comboMonth.hidePopup();
            comboYear.hidePopup();
          }
        };
        this.addFocusListener(focusListener);
    }

    /**
     * Makes day buttons visible/invisible based on the year and month selected/set.
     */
    private void fixMonthDisplay()
    {
        int numDays;

        int curMonth = comboMonth.getSelectedIndex();
        int curYear;

        curYear = comboYear.getSelectedIndex() + nSelectedYear - 10;
        gregorianCalendar.set(curYear,curMonth,nSelectedDay);

        Calendar firstOfMonth = new GregorianCalendar(defaultLocale);
        firstOfMonth.set(curYear,curMonth,1);

        int dayOfWeek = firstOfMonth.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek < nLocaleFirstDay)
            dayOfWeek += 7;
        
        while (dayOfWeek < nFirstDayOfWeek) {
            panelMonth.remove(7);
            panelMonth.add(new JLabel(EMPTY_STRING),-1);
            --nFirstDayOfWeek;
        }
        
        while (dayOfWeek > nFirstDayOfWeek) {
            panelMonth.remove(48);
            panelMonth.add(new JLabel(EMPTY_STRING),7);
            ++nFirstDayOfWeek;
        }
        
        panelMonth.validate();
        nFirstDayOfWeek = dayOfWeek;

        switch (curMonth + 1) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numDays = 31;
                break;
            case 2:
                if (gregorianCalendar.isLeapYear(curYear))
                    numDays = 29;
                else
                    numDays = 28;
                    break;
            default:
                numDays = 30;
                break;
        }

        for (int i = numDays; i < maxDayOfMonth; i++) {
            buttonDays[i].setVisible(false);
        }
        for (int i = gregorianCalendar.getLeastMaximum(Calendar.DATE); i < numDays; i++) {
            buttonDays[i].setVisible(true);
        }
        if (nSelectedDay > numDays) setDay(numDays);
    }

    /**
     * Month Combo class calls fixMonthDisplay to adjust days buttons display
     * according to the month being selected.
     */
    private static class ComboMonth extends JComboBox {
        List itemListeners;

        public ComboMonth() {
            this.setEditable(false);
            this.setRequestFocusEnabled(false);
            setName("month_comboBox");
        }

        //this component should never get the focus.  The calendar panel has to
        //always have the focus for the escape key to work.
        public boolean isFocusTraversable() {
            return false;
        }

        public void destroy() {
            if (itemListeners != null) {
                for (int i = 0; i < itemListeners.size(); i++) {
                    // force the removal of this rude S.O.B.
                    removeItemListener((ItemListener)itemListeners.get(0));
                }
                itemListeners = null;
            }
        }

        // we trap the add/remove of ItemListeners as
        // swing internally calls this and causes a memory leak
        public void removeItemListener(ItemListener aListener) {
            if (itemListeners != null) {
                itemListeners.remove(aListener);
            }
            super.removeItemListener(aListener);
        }

        public void addItemListener(ItemListener aListener) {
            if (itemListeners == null) {
                itemListeners = new ArrayList();
            }
            itemListeners.add(aListener);
            super.addItemListener(aListener);
        }
    }

     /**
     * Year Combo class-calls fixMonthDisplay to adjust days buttons display
     * according to the year being selected.
     */
    private class ComboYear extends JComboBox {
        public ComboYear() {
            this.setEditable(false);
            this.setRequestFocusEnabled(false);
            setName("year_comboBox");
        }

        //this component should never get the focus.  The calendar panel has to
        //always have the focus for the escape key to work.
        public boolean isFocusTraversable() {
            return false;
        }
    }

    /**
     * Listens for the event generated when a day toggle button is selected, and fires
     * an event.
     */
    private class ToggleBtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt)
        {
            if (!bIgnoreEvents)
            {
                Object actionObject = evt.getSource();
                if (actionObject instanceof DateChooserButton)
                {
                    Integer daySelected = new Integer(evt.getActionCommand());

                    // check to see if they picked the current selection
                    if ((buttonDays[daySelected.intValue() - 1].isSelected()) &&
                        (daySelected.intValue() == nSelectedDay))
                    {
                        buttonDays[daySelected.intValue() - 1].setSelected(false);
                    }
                    else if (daySelected.intValue() != nSelectedDay)
                    {
                        buttonDays[nSelectedDay - 1].setSelected(true);
                        nSelectedDay = daySelected.intValue();
                        gregorianCalendar.set(Calendar.DATE,nSelectedDay);
                    }
                }
            }
        }
    }


    /**
     * Date Chooser Toggle button class
     */
    private class DateChooserButton extends JToggleButton {
        /**
         * constructor
         * @param text button label
         * @param selected if true button is selected
         */
        public DateChooserButton(String text, boolean selected) {
            super(text, selected);
            setName(text + "_dateChooser_toggleBtn");
        }

        /**
         * get look and feel class
         * @return name of the look and feel class for this button
         */
        public String getUIClassID() {
        	return super.getUIClassID();
        }
    }//class DateChooserButton
    
    public static void main(String[] args) {
        JFrame frame;

        CalendarPanel p = new CalendarPanel();

        frame = new JFrame();
        JPanel contentPane;
        contentPane = (JPanel) frame.getContentPane();
        contentPane.setLayout(new BorderLayout(8, 8));

        contentPane.add(p, BorderLayout.NORTH);

        frame.setSize(new Dimension(225, 255));
        frame.setTitle("Calendar Panel Test Window");
        frame.setVisible(true);
    }

} //end Calendar Panel class
