package com.suntek.scheduler.upgrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.suntek.scheduler.appsvcs.JDBCConnector;
import com.suntek.scheduler.ui.PSService;
import com.suntek.scheduler.util.SecurityUtil;

public class EncryptSensitiveData {

    public EncryptSensitiveData() {
    }

    public static void main(String[] args){
        PSService.getService().connect();
        encryptData();
    }
    
    private static void encryptData(){
    	encryptPassword();
    	encryptReferral();
    }
    
    private static void encryptPassword(){
        String getUser = "select userId, password from user";
        String updateUser = "update user set password = ? where userId = ?";

        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        Connection con = null;
        JDBCConnector connector = JDBCConnector.getInstance();
        String userId = null;
        String password = null;
        int numBatch = 0;
        try{
            con = connector.getConnectionFromPool();
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(getUser);
            pstmt2 = con.prepareStatement(updateUser);            
            rs1 = pstmt1.executeQuery();
            while (rs1.next()){
                userId = rs1.getString(1);
                password = rs1.getString(2);
                password = SecurityUtil.encrypt(password);
                pstmt2.setString(1, password);
                pstmt2.setString(2, userId);
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
    
    private static void encryptReferral(){
        String getRef = "select referralId, firstName, ssn from referral";        
        String updateRef = "update referral set firstName = ?, ssn = ? where referralId = ?";
        long refId = 0;
        String firstName = null;
        String ssn = null;
    	    	
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
                ssn = rs1.getString(3);
                firstName = SecurityUtil.encrypt(firstName);
                ssn = SecurityUtil.encrypt(ssn);
                pstmt2.setString(1, firstName);
                pstmt2.setString(2, ssn);
                pstmt2.setLong(3, refId);
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
