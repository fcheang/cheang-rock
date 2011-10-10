package com.suntek.scheduler.ui;

import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.suntek.scheduler.appsvcs.JDBCConnector;
import com.suntek.scheduler.appsvcs.persistence.Constant;

public class ScreenTimeoutThread extends Thread {

	private long interval = 5 * 60 * 1000; //check every 5 mins
	private long timeout =  15 * 60 * 1000; //timeout if no activity in 15 mins
	private volatile boolean canceled = false;
	
	private JFrame parent = null;
	
	public ScreenTimeoutThread(JFrame parent){
		this.parent = parent;
	}
	
	@Override
	public void run(){
		while (!canceled){
			//System.out.println("sleep for "+interval);
			try{
				Thread.sleep(interval);
			}catch(InterruptedException ie){				
			}
			Date now = new Date();
			long delta = now.getTime() - Constant.lastActive.getTime();
			//System.out.println("delta = "+delta);
			if (delta > timeout){
				//System.out.println("delta > timeout");
				if (relogin()){
					Constant.lastActive = new Date();
					continue;
				}else{
					JOptionPane.showMessageDialog(parent, "Invalid password, exiting scheduler!", "Invalid password", JOptionPane.ERROR_MESSAGE);
		            parent.dispose();
		            JDBCConnector.getInstance().destroy();
		            System.exit(0);					
				}
			}
		}
	}

	private boolean relogin(){
		
		int numRetry = 0;
		while (numRetry < 3){
			String pwd = null
			;		
			JTextField passwordField = new JPasswordField();
			Object[] obj = null;
			if (numRetry == 0){
				obj = new Object[] {"Screen locked. Please enter the password:\n\n", passwordField};
			}else{
				obj = new Object[] {"Invalid password! Please enter the password again:\n\n", passwordField};
			}
			Object stringArray[] = {"OK","Cancel"};
			if (JOptionPane.showOptionDialog(parent, obj, "Screen Lock", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, stringArray, obj) == JOptionPane.OK_OPTION){
				pwd = passwordField.getText();
			}
			
			if (pwd != null && pwd.equals(Constant.appPass)){
				return true;
			}
			//System.out.println("relogin faled, retry "+numRetry);			
			numRetry += 1;
		}
		return false;
	}
	
	public void cancel(){
		canceled = true;
	}
	
}
