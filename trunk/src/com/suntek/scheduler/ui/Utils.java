package com.suntek.scheduler.ui;

import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.suntek.scheduler.appsvcs.ReadSvc;

public class Utils {
	
	public static void centerFrame(Window f, int width, int height){
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int locX = (((screenWidth / width) / 2) * width) - (width / 2);
		int locY = (((screenHeight / height) / 2) * height) - (height /4 );
		f.setSize(width, height);
		f.setLocation(locX, locY);
	}

	public static void centerFrame(Window f){
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		f.setSize( (screenWidth / 10)* 9, (screenHeight / 10) * 9);
		f.setLocation(screenWidth/16, screenHeight/16);
	}
		
}
