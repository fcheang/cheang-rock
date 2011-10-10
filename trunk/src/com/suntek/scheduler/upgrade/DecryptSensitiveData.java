package com.suntek.scheduler.upgrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.suntek.scheduler.appsvcs.JDBCConnector;
import com.suntek.scheduler.ui.PSService;
import com.suntek.scheduler.util.SecurityUtil;

public class DecryptSensitiveData {

    public static void main(String[] args){
        PSService.getService().connect();
        decryptData();
    }
    
    private static void decryptData(){
    	decryptReferral();
    }
    
    private static void decryptReferral(){
        String getRef = "select referralId, firstName from referral";        
        String updateRef = "update referral set firstName = ? where referralId = ?";
        long refId = 0;
        String firstName = null;
    	    	
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        Connection con = null;
        JDBCConnector connector = JDBCConnector.getInstance();
        int numBatch = 0;
        try{
            con = connector.getConnectionFromPool();
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(getRef);
            pstmt2 = con.prepareStatement(updateRef);            
            rs1 = pstmt1.executeQuery();
            while (rs1.next()){
                refId = rs1.getLong(1);
                firstName = rs1.getString(2);
                firstName = SecurityUtil.decrypt(firstName);
                pstmt2.setString(1, firstName);
                pstmt2.setLong(2, refId);
                pstmt2.addBatch();
                numBatch += 1;
                if (numBatch >= 1000){
                	pstmt2.executeBatch();
                	debug("processed "+numBatch+" row");
                	numBatch = 0;
                }
            }
            if (numBatch > 0){
            	pstmt2.executeBatch();
            	debug("processed "+numBatch+" row");
            }
            con.commit();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
        	try{
        		rs1.close();
        		pstmt1.close();
        		pstmt2.close();
        	}catch (Exception e){
        		e.printStackTrace();
        	}
            connector.releaseConnection(con);
        }    	
    }
    
    private static void debug(String msg){
        System.out.println(msg);
    }
    
}
