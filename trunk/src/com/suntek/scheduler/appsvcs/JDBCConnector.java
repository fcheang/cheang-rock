package com.suntek.scheduler.appsvcs;

import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.sql.Types;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Timestamp;

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
public class JDBCConnector {

    private static JDBCConnector connector = new JDBCConnector();
    private static int MAX_NUM_RETRY = 5;

    private String dbSource = null;

    //private ArrayList connectionPool = null;

    private Object poolLock = new Object();
    private long oneDayInMillis = 24 * 60 * 60 * 1000;

    private boolean inited = false;
    private Connection con = null;

    private JDBCConnector(){
    }

    public static JDBCConnector getInstance(){
        return connector;
    }

    public void init(
        String dbDriver,
        String dbHost,
        String dbUsed,
        String dbUser,
        String dbPassword)
    {
        dbSource = "jdbc:mysql://"+dbHost+"/"+dbUsed+"?user="+dbUser+"&password="+dbPassword
            +"&useCompression=true";
        //connectionPool = new ArrayList();

        try {
            Class.forName(dbDriver).newInstance();
        } catch (Exception ex) {
            System.out.println("Error: Problem initilize conneciton pool!");
            ex.printStackTrace();
        }
    }

    private Connection getConnection(){
    	int numRetry = 0;
    	while (!isConnectionValid(con)){
    		try {
				Thread.sleep(1000);
    		}catch(InterruptedException ie){    			
    		}
    		con = createConnection();
    		numRetry += 1;
    		if (numRetry >= MAX_NUM_RETRY){
    			break;        			
    		}
    	}
        return con;
    }
    
    private boolean isConnectionValid(Connection c){
    	boolean isValid = false;
    	try {
	    	if (c == null || c.isClosed()){
	    		isValid = false;
	    	}else{
	    		// test connection
	    		Statement stmt = c.createStatement();
	    		stmt.executeQuery("select 1 from version");
	    		isValid = true;
	    	}
    	} catch (SQLException e) {
    		isValid = false;
    	}
    	if (!isValid){
    		try{
    			if (c != null){
    				c.close();
    			}
    		}catch(Exception e1){    			
    		}    		
    	}
    	return isValid;
    }

    private Connection createConnection(){
    	Connection c = null;
        try{
            c = DriverManager.getConnection(dbSource);
            c.setAutoCommit(true); // InnoDB only works with autocommit on
        }catch(SQLException e){
            System.out.println("Error: Problem create new connection!");
            e.printStackTrace();
        }    
        return c;
    }
    
    public Connection getConnectionFromPool(){
        return getConnection();
    }

    public void releaseConnection(Connection con){
        // no-op
    }

    public void destroy(){
        if (con != null){
            try{
                con.close();
            }catch(Exception e){
                // ignore
            }
        }
    }

}
