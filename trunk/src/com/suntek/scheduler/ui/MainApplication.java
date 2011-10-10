package com.suntek.scheduler.ui;

import com.suntek.scheduler.appsvcs.persistence.*;
import com.suntek.scheduler.appsvcs.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.net.InetAddress;


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
public class MainApplication implements ActionListener {
    boolean packFrame = false;

    JTextField userNameTF = null;
    JTextField passwordTF = null;
    JButton okButton = null;
    JButton cancelButton = null;
    JDialog loginDialog = null;
    JComboBox clinicCB = null;
    
    private boolean debugOn = false;

    /**
     * Construct and show the application.
     */
    public MainApplication() {
    	PSService.getService().connect();
        showLogin();
    }

    private void startApp(){
        Constant.selectedClinic = (String)clinicCB.getSelectedItem();
        Constant.appUser = userNameTF.getText();
        Constant.appPass = passwordTF.getText();
        Constant.appRole = ReadSvc.getInstance().getRoles(Constant.appUser);

        MainFrame frame = new MainFrame();
        Utils.centerFrame(frame);
        frame.setVisible(true);
        startScreenTimeoutThread(frame);
    }
    
    private void startScreenTimeoutThread(MainFrame frame){
    	ScreenTimeoutThread t = new ScreenTimeoutThread(frame);
    	t.start();
    }

    public void actionPerformed(ActionEvent ae){
        String cmd = ae.getActionCommand();
        String host = "";
        try{
            host = InetAddress.getLocalHost().getHostAddress();
        }catch(java.net.UnknownHostException e){
            e.printStackTrace();
        }
        if (cmd.equals("ok")){
            String user = userNameTF.getText();
            String pass = passwordTF.getText();
            String clinic = (String)clinicCB.getSelectedItem();
            if (clinic.equalsIgnoreCase("<Select Clinic>")){
                JOptionPane.showMessageDialog(
                    loginDialog, "Please select a clinic to login!",
                    "Missing clinic", JOptionPane.ERROR_MESSAGE);
            }else{
                if (PSService.getService().login(user, pass)) {
                    if (PSService.getService().allowConnect(user, host)) {
                        loginDialog.setVisible(false);
                        loginDialog.dispose();
                        startApp();
                    }
                    else {
                        JOptionPane.showMessageDialog(
                            loginDialog, "User \"" + user +
                            "\" is not allowed to login on this machine",
                            "Authorization Failure", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(
                        loginDialog, "Invalid User Name or password!",
                        "Login Failure",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if (cmd.equals("cancel")){
            if (loginDialog != null){
                loginDialog.setVisible(false);
                loginDialog.dispose();
            }
            JDBCConnector.getInstance().destroy();
            System.exit(0);
        }else if (cmd.equals("enterPassword")){
            String user = userNameTF.getText();
            String pass = passwordTF.getText();
            String clinic = (String)clinicCB.getSelectedItem();
            if (clinic.equalsIgnoreCase("<Select Clinic>")){
                JOptionPane.showMessageDialog(
                    loginDialog, "Please select a clinic to login!",
                    "Missing clinic", JOptionPane.ERROR_MESSAGE);
            }else{
                if (PSService.getService().login(user, pass)) {
                    if (PSService.getService().allowConnect(user, host)) {
                        loginDialog.setVisible(false);
                        loginDialog.dispose();
                        startApp();
                    }
                    else {
                        JOptionPane.showMessageDialog(
                            loginDialog, "User \"" + user +
                            "\" is not allowed to login on this machine",
                            "Authorization Failure", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(
                        loginDialog, "Invalid User Name or password!",
                        "Login Failure",
                        JOptionPane.ERROR_MESSAGE);
                    userNameTF.requestFocusInWindow();
                }
            }
        }else if (cmd.equals("enterUserName")){
            passwordTF.requestFocusInWindow();
        }
    }

    private void showLogin(){
        loginDialog = new JDialog();
        userNameTF = new JTextField(15);
        passwordTF = new JPasswordField(15);
        userNameTF.setActionCommand("enterUserName");
        userNameTF.addActionListener(this);
        passwordTF.setActionCommand("enterPassword");
        passwordTF.addActionListener(this);
        java.util.List allClinics = PSService.getService().getAllClinicName();
        allClinics.add(0, "<Select Clinic>");
        clinicCB = new JComboBox(allClinics.toArray());
        
        if (debugOn){
        	clinicCB.setSelectedIndex(1); //debug
        	userNameTF.setText("Administrator");
        	passwordTF.setText("p3t3r2008");
        }        
        
        okButton = new JButton("OK");
        okButton.setActionCommand("ok");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        Utils.centerFrame(loginDialog, 310, 280);
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loginDialog.setLayout(new BorderLayout());
        loginDialog.setResizable(false);
        loginDialog.setTitle("Appointment Scheduler Login");

        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        JLabel t1 = new JLabel("BHR Appointment Scheduler "+Constant.version, JLabel.CENTER);
        JLabel t2 = new JLabel("Supported and maintained by SuntekSystems", JLabel.CENTER);
        JLabel t3 = new JLabel("Last Updated: "+Constant.df_l.format(Constant.lastUpdated.getTime()));
        JLabel t4 = new JLabel();
        JPanel tp1 = new JPanel();
        JPanel tp2 = new JPanel();
        JPanel tp3 = new JPanel();
        JPanel tp4 = new JPanel();
        tp1.add(t1);
        tp2.add(t2);
        tp3.add(t3);
        tp4.add(t4);
        topPanel.add(tp1);
        topPanel.add(tp2);
        topPanel.add(tp3);
        topPanel.add(tp4);

        JPanel centerPanel = new JPanel(new SpringLayout());
        JLabel userNameL = new JLabel("User Name: ", JLabel.TRAILING);
        centerPanel.add(userNameL);
        centerPanel.add(userNameTF);
        JLabel passL = new JLabel("Password: ", JLabel.TRAILING);
        centerPanel.add(passL);
        centerPanel.add(passwordTF);
        JLabel clinicL = new JLabel("Clinic: ", JLabel.TRAILING);
        centerPanel.add(clinicL);
        centerPanel.add(clinicCB);
        SpringUtilities.makeCompactGrid(centerPanel, 3, 2, 6, 6, 7, 7);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        loginDialog.getContentPane().add(topPanel, BorderLayout.NORTH);
        loginDialog.getContentPane().add(centerPanel, BorderLayout.CENTER);
        loginDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        loginDialog.setVisible(true);
    }

    public void GUITest() {
    	new MainApplication();
    }

    
    /**
     * Application entry point.
     *
     * @param args String[]
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    MetalLookAndFeel.setCurrentTheme(new RubyTheme());
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                EventQueue waitQueue = new WaitCursorEventQueue(100);
                Toolkit.getDefaultToolkit().getSystemEventQueue().push(waitQueue);

                new MainApplication();
            }
        });
    }

}
