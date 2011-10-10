package com.suntek.scheduler.appsvcs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.suntek.scheduler.appsvcs.persistence.Constant;
import com.suntek.scheduler.ui.MainFrame;


public class ExceptionLoggerProxy implements InvocationHandler {

    private Object obj;
    
    private Map<String, Integer> countMap = new HashMap<String, Integer>();
    private Map<String, Long> totalTimeMap = new HashMap<String, Long>();

    private boolean profile = false;
    
    public static Object newInstance(Object obj) {
    	return java.lang.reflect.Proxy.newProxyInstance(
    			obj.getClass().getClassLoader(),
    			obj.getClass().getInterfaces(),
    			new ExceptionLoggerProxy(obj));
    }

    private ExceptionLoggerProxy(Object obj) {
    	this.obj = obj;
    }

    public Object invoke(Object proxy, Method m, Object[] args)
	throws Throwable
	{
    	Constant.lastActive = new Date();
    	if (profile){
    		return invokeWithProfile(proxy, m, args);
    	}else{
    		return invokeInternal(proxy, m, args);
    	}
	}
    
    private Object invokeWithProfile(Object proxy, Method m, Object[] args)
	throws Throwable
    {    	
    	String mtKey = m.getName();
    	long startTime = System.currentTimeMillis();
    	
    	Object result = invokeInternal(proxy, m, args);
    	
    	long stopTime = System.currentTimeMillis();
    	long delta = stopTime - startTime;        	
    
        Integer c = countMap.get(mtKey);
        if (c == null){
        	c = 1;
        }else{
        	c += 1;
        }
    	countMap.put(mtKey, c);        
        
        Long t = totalTimeMap.get(mtKey);
        if (t == null){
        	t = delta;
        }else{
        	t += delta;
        }
    	totalTimeMap.put(mtKey, t);
        System.out.println(mtKey+" | "+c+" | "+delta+" | "+t);
        return result;
    }
    
    private Object invokeInternal(Object proxy, Method m, Object[] args)
    throws Throwable {
        Object result = null;
		try {
			result = m.invoke(obj, args);
        } catch (Throwable t) {
        	String msg = getStackTrace(m, args, t);
        	logException(msg);
        	showDialog(msg);
		}
        return result;
    }
    
    public void logException(String msg){    	
        PreparedStatement pstmt = null;
        Connection con = null;
        JDBCConnector connector = JDBCConnector.getInstance();
        
        String sql = null;
        try{
            con = connector.getConnectionFromPool();           
            sql = WriteQueries.logException;
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, msg);
            pstmt.executeUpdate();            
        }catch(SQLException e){
        	System.out.println("Problem executing: "+sql);
        	e.printStackTrace();        	
        }finally{
        	try{
        		pstmt.close();
        	}catch(Exception e){        		
        	}
        	connector.releaseConnection(con);
        }    	
    }
    
    private String getStackTrace(Method m, Object[] args, Throwable t){
    	Throwable exp = t;
    	while (exp.getCause() != null){
    		exp = exp.getCause();
    	}
        StringWriter sr = new StringWriter();
        exp.printStackTrace(new PrintWriter(sr));
        
        String argsStr = "";
        if (args != null){
	        for (int i=0; i<args.length; i++){
	        	argsStr = argsStr + "["+i+"]: "+args[i]+" ";
	        }
        }
        String msg = "Found programming error! Please send the following error message to the Administrator!!!"+
    	"\n\nException in method: "+m.getName()+"( "+ argsStr+" )\n"+
    	sr.toString();
        
        return msg;
    }
    
    private void showDialog(String msg){
    	JDialog errorLog = null;
    	if (MainFrame.f != null){
    		errorLog = new JDialog(MainFrame.f, true);
    	}else{
    		errorLog = new JDialog();
    	}
        errorLog.setTitle("Error Log");
        errorLog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        errorLog.setLayout(new BorderLayout());
        errorLog.setResizable(true);
        
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);        
        textArea.setText(msg);
        JScrollPane scrollPane = new JScrollPane(textArea);
        errorLog.setSize(new Dimension(700, 700));        
        errorLog.add(scrollPane, BorderLayout.CENTER);        
        errorLog.setVisible(true);    	
    }
}
