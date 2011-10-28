package com.suntek.scheduler.appsvcs;

import java.util.Date;
import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

import com.suntek.scheduler.appsvcs.persistence.*;
import com.suntek.scheduler.util.SecurityUtil;

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
public class ReadSvc extends QuerySvc implements ReadSvcI {

    private boolean debugOn = Boolean.getBoolean("debugOn");
    private static ReadSvcI readSvc = (ReadSvcI)ExceptionLoggerProxy.newInstance(new ReadSvc());
    private JDBCConnector connector = JDBCConnector.getInstance();

    List cancelByPatientReasonCode = null;
    List cancelByClinicReasonCode = null;
    
    private ReadSvc() {
    }

    public static ReadSvcI getInstance(){
    	return readSvc;
    }    

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#login(java.lang.String, java.lang.String)
	 */
    public boolean login(String user, String pass){
        debug("login("+user+", "+pass+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.login;
        pass = SecurityUtil.encrypt(pass);
        boolean success = false;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            rs = pstmt.executeQuery();
            if (rs.next()){
                success = true;
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return success;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getRoles(java.lang.String)
	 */
    public List<String> getRoles(String user){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getRoles;
        List<String> roles = new ArrayList<String>();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user);
            rs = pstmt.executeQuery();
            while (rs.next()){
                roles.add(rs.getString(1));
            }
        }catch(SQLException e){
        	throw new RuntimeException(e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return roles;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getAllProviderName()
	 */
    public List getAllProviderName(){
        debug("calling getAllProviderName()");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllProviderName;
        List retVal = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                retVal.add(rs.getString(1));
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;
    }
    
    
	public List<ProviderNode> getInactiveProvider(){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getInactiveProviderNode;
        List<ProviderNode> retVal = new ArrayList<ProviderNode>();
        ProviderNode node = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
            	node = new ProviderNode();
            	node.setProviderId(rs.getInt(1));
            	node.setName(rs.getString(2));
                retVal.add(node);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;				
	}
	
	public List<ProviderNode> getAllProviderNode(){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllProviderNode;
        List<ProviderNode> retVal = new ArrayList<ProviderNode>();
        ProviderNode node = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
            	node = new ProviderNode();
            	node.setProviderId(rs.getInt(1));
            	node.setName(rs.getString(2));
                retVal.add(node);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;		
	}

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getAllReasonCode()
	 */
    public List getAllReasonCode(){
        debug("calling getAllReasonCode()");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllReasonCode;
        List retVal = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                retVal.add(rs.getString(1));
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;
    }
    
	public List getCancelByPatientReasonCode(){
		if (cancelByPatientReasonCode == null){
			cancelByPatientReasonCode = new ArrayList();
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        Connection con = null;
	        String sql = ReadQueries.getCancelByPatientReasonCode;
	        try{
	            con = connector.getConnectionFromPool();
	            pstmt = con.prepareStatement(sql);
	            rs = pstmt.executeQuery();
	            while (rs.next()){
	            	cancelByPatientReasonCode.add(rs.getString(1));
	            }
	        }catch(SQLException e){
	            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
	        }finally{
	            close(rs);
	            close(pstmt);
	            connector.releaseConnection(con);
	        }
		}
        return cancelByPatientReasonCode;		
	}
	
	public List getCancelByClinicReasonCode(){
		if (cancelByClinicReasonCode == null){
			cancelByClinicReasonCode = new ArrayList();		
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        Connection con = null;
	        String sql = ReadQueries.getCancelByClinicReasonCode;
	        try{
	            con = connector.getConnectionFromPool();
	            pstmt = con.prepareStatement(sql);
	            rs = pstmt.executeQuery();
	            while (rs.next()){
	            	cancelByClinicReasonCode.add(rs.getString(1));
	            }
	        }catch(SQLException e){
	            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
	        }finally{
	            close(rs);
	            close(pstmt);
	            connector.releaseConnection(con);
	        }
		}
		return cancelByClinicReasonCode;	       
	}
    

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#isRemoteUser(java.lang.String)
	 */
    public boolean isRemoteUser(String user){
        debug("calling isRemoteUser("+user+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.isRemoteUser;
        boolean isRemote = false;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user);
            rs = pstmt.executeQuery();
            if (rs.next()){
                isRemote = true;
            }
        }catch(SQLException e){        	
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return isRemote;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#isTrustedIP(java.lang.String)
	 */
    public boolean isTrustedIP(String host){
        debug("calling isTrustedIP("+host+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.isTrustedHost;
        String domain = getDomain(host);
        boolean isAllow = false;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, host);
            pstmt.setString(2, domain);
            rs = pstmt.executeQuery();
            if (rs.next()){
                isAllow = true;
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return isAllow;
    }

    private String getDomain(String host){
        // domain is the first three section plus a star
        StringBuffer sb = new StringBuffer("");
        try{
            StringTokenizer stk = new StringTokenizer(host, ".");
            sb.append(stk.nextToken());
            sb.append(".");
            sb.append(stk.nextToken());
            sb.append(".");
            sb.append(stk.nextToken());
            sb.append(".");
            sb.append("*");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    
    public List<Patient> getDischargedPatient(){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getDischargedPatientName;
        List retVal = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Patient pat = new Patient();
                pat.setRefId(rs.getInt(1));
                pat.setLastName(rs.getString(2));
                pat.setMi(rs.getString(3));
                //pat.setFirstName(SecurityUtil.decrypt(rs.getString(4)));
                pat.setFirstName(rs.getString(4));
                retVal.add(pat);
            }
            Collections.sort(retVal);
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;    	
    }

    public List<Patient> getAllPatientNode(){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllPatientName;
        List retVal = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Patient pat = new Patient();
                pat.setRefId(rs.getInt(1));
                pat.setLastName(rs.getString(2));
                pat.setMi(rs.getString(3));
                //pat.setFirstName(SecurityUtil.decrypt(rs.getString(4)));
                pat.setFirstName(rs.getString(4));
                retVal.add(pat);
            }
            Collections.sort(retVal);
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;
    }
        
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getInsForRef(int)
	 */
    public List getInsForRef(int refId){
        debug("calling getInsForRef("+refId+")");
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getInsByRefId;
        List insList = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = pstmt.executeQuery();            
            while (rs.next()){
            	Insurance ins = new Insurance();
            	ins.setInsId(rs.getInt(1));
            	ins.setRefId(refId);
            	ins.setRefDate(rs.getDate(2));
            	ins.setEffStartDate(rs.getDate(3));
            	ins.setEffEndDate(rs.getDate(4));
            	ins.setInsCompany(rs.getString(5));
            	ins.setInsPhoneNum(rs.getString(6));            	
            	ins.setMemberId(rs.getString(7));            	
            	ins.setCopay(rs.getBigDecimal(8));
            	ins.setCopayParity(rs.getBigDecimal(9));
            	ins.setNumAuthVisitForMD(rs.getInt(10));            	
            	ins.setAuthNumForMD(rs.getString(11));
            	ins.setNumAuthVisitForMA(rs.getInt(12));
            	ins.setAuthNumForMA(rs.getString(13));            	
            	ins.setMedicalId(rs.getString(14));
            	ins.setMedIssueDate(rs.getDate(15));
            	ins.setNotes(rs.getString(16));
            	insList.add(ins);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return insList;    	
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getPatientById(int)
	 */
    public Patient getPatientById(int refId){
        debug("calling getPatientById("+refId+")");
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getPatientById;
        Patient pat = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                pat = new Patient();
                setValueForPatient(pat, rs);
            }
            close(rs);
            close(pstmt);
            
            sql = ReadQueries.getReferralDate;
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	pat.setCreateDate(rs.getDate(1));
            }
            close(rs);
            close(pstmt);
            
            sql = ReadQueries.getInsByRefId;
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = pstmt.executeQuery();            
            while (rs.next()){
            	addInsForPatient(pat, rs);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return pat;
    }

	public Patient getEvalPatientByInsAndId(int refId, String ins){
        debug("calling getEvalPatientByInsAndId("+refId+", "+ins+")");
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getEvalPatientByInsAndId;
        Patient pat = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, ins);
            setInt(pstmt, 2, refId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                pat = new Patient();
                setValueForEvalPatient(pat, rs);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return pat;    			
	}
    
    public List<Patient> getEvalPatientByIns(String ins){
        debug("calling getEvalPatientByClinic("+ins+")");
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getEvalPatientByIns;
        List<Patient> pats = new ArrayList<Patient>();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, ins);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Patient pat = new Patient();
                setValueForEvalPatient(pat, rs);
                pats.add(pat);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return pats;    	
    }
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#checkCredential(java.lang.String, int)
	 */
    public boolean checkCredential(String provider, int refId){
        debug("calling checkCredential("+provider+", "+refId+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.checkCredential;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, provider);
            setInt(pstmt, 2, refId);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	int count = rs.getInt(1);
            	if (count > 0){
            		return true;
            	}
            }
        }catch(SQLException e){
            System.out.println("Error: Problem executing: "+sql);
            e.printStackTrace();
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return false;
    }    

    private void setValueForPatient(Patient pat, ResultSet rs)
        throws SQLException
    {
        //r.referralId, r.firstName, r.middleInitial, r.lastName, r.gender,
        //r.ssn, r.birthDate, r.streetAddress, r.apartmentNumber, r.city,
        //r.state, r.zipCode, r.phoneNumber, r.email, r.legalGardianFirstName,
        //r.legalGardianLastName, r.legalGardianMiddleInitial, r.legalGardianPhoneNumber, r.previousPsychiatrist, r.lastSeen,
        //r.currentMedications, r.daysLeft, r.previousMedications, r.previousDx, r.presentingProblem,
        //r.needMedicalMgntSvc, r.needTherapy, r.isUrgent, r.clinic, r.comments,
        //r.isChild, r.reminder, r.balance, r.balanceNotes

        pat.refId = rs.getInt(1);
        //pat.firstName = SecurityUtil.decrypt(rs.getString(2));
        pat.firstName = rs.getString(2);
        pat.mi = rs.getString(3);
        pat.lastName = rs.getString(4);
        pat.gender = rs.getString(5);
        pat.ssn = SecurityUtil.decrypt(rs.getString(6));
        pat.setBirthDate(rs.getTimestamp(7));
        pat.streetAddress = rs.getString(8);
        pat.apartmentNumber = rs.getString(9);
        pat.city = rs.getString(10);
        pat.state = rs.getString(11);
        pat.zipCode = rs.getString(12);
        pat.phoneNumber = rs.getString(13);
        pat.email = rs.getString(14);
        pat.legalGardianFirstName = rs.getString(15);
        pat.legalGardianLastName = rs.getString(16);
        pat.legalGardianMiddleInitial = rs.getString(17);
        pat.legalGardianPhoneNumber = rs.getString(18);
        pat.previousPsychiatrist = rs.getString(19);
        pat.lastSeen = rs.getTimestamp(20);
        pat.currentMedications = rs.getString(21);
        pat.daysLeft = rs.getInt(22);
        pat.previousMedications = rs.getString(23);
        pat.previousDx = rs.getString(24);
        pat.presentingProblem = rs.getString(25);
        pat.needMedicalMgntSvc = rs.getBoolean(26);
        pat.needTherapy = rs.getBoolean(27);
        pat.isUrgent = rs.getString(28);
        pat.clinic = rs.getString(29);
        pat.comments = rs.getString(30);
        //pat.isChild = rs.getBoolean(31);
        pat.setReminder(rs.getString(32));
        pat.setBalance(rs.getBigDecimal(33));
        pat.setBalanceNotes(rs.getString(34));
    }

    private void setValueForEvalPatient(Patient pat, ResultSet rs)
    throws SQLException
    {
    	//select r.referralId, rs.createDate, month(rs.createDate) admitMonth,
    	//dayofmonth(rs.createDate) admitDay, r.firstName, r.lastName, r.phoneNumber, r.birthDate
	
	    pat.refId = rs.getInt(1);
	    pat.setInsAdmitDate(rs.getDate(2));
	    pat.setInsAdmitYear(rs.getInt(3));
	    pat.setInsAdmitMonth(rs.getInt(4));
	    pat.setInsAdmitDay(rs.getInt(5));
	    //pat.firstName = SecurityUtil.decrypt(rs.getString(6));
	    pat.firstName = rs.getString(6);
	    pat.lastName = rs.getString(7);
	    pat.phoneNumber = rs.getString(8);
	    pat.setBirthDate(rs.getDate(9));
	}
    
    private void addInsForPatient(Patient pat, ResultSet rs)
    	throws SQLException
    {
    	Insurance ins = new Insurance();
    	ins.setRefId(pat.getRefId());
    	
    	ins.setInsId(rs.getInt(1));
    	ins.setRefDate(rs.getDate(2));
    	ins.setEffStartDate(rs.getDate(3));
    	ins.setEffEndDate(rs.getDate(4));
    	ins.setInsCompany(rs.getString(5));
    	ins.setInsPhoneNum(rs.getString(6));    	
    	ins.setMemberId(rs.getString(7));
    	ins.setCopay(rs.getBigDecimal(8));
    	ins.setCopayParity(rs.getBigDecimal(9));
    	ins.setNumAuthVisitForMD(rs.getInt(10));
    	ins.setAuthNumForMD(rs.getString(11));
    	ins.setNumAuthVisitForMA(rs.getInt(12));
    	ins.setAuthNumForMA(rs.getString(13));    	
    	ins.setMedicalId(rs.getString(14));
    	ins.setMedIssueDate(rs.getDate(15));
    	ins.setNotes(rs.getString(16));
    	pat.addIns(ins);
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#isApptCompleted(long)
	 */
    public boolean isApptCompleted(long apptId){
        debug("calling isApptCompleted("+apptId+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.isApptCompleted;
        boolean apptCompleted = false;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setLong(pstmt, 1, apptId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                apptCompleted = true;
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return apptCompleted;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getPatientId(java.lang.String, java.lang.String)
	 */
    public int getPatientId(String lastName, String firstName){
        debug("calling getPatientId("+lastName+", "+firstName+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getPatientIdByName;
        int refId = -1;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, lastName);
            //setString(pstmt, 2, SecurityUtil.encrypt(firstName));
            setString(pstmt, 2, firstName);
            rs = pstmt.executeQuery();
            if (rs.next()){
                refId = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return refId;
    }

    public Date getPatientBirthDate(int refId){
        debug("calling getPatientBirthDate("+refId+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getPatientBirthDate;
        Date birthDate = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                birthDate = rs.getTimestamp(1);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return birthDate;    	
    }
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getAllClinicName()
	 */
    public List<String> getAllClinicName(){
        debug("calling getAllClinicName()");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllClinicName;
        List<String> retVal = new ArrayList<String>();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                retVal.add(rs.getString(1));
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);            
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getAllInsuranceCompanyName()
	 */
    public List getAllInsuranceCompanyName(){
        debug("calling getAllInsuranceCompanyName()");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllInsuranceCompanyName;
        List retVal = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                retVal.add(rs.getString(1));
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);            
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return retVal;
    }
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getApptForDay(java.lang.String, java.util.Date)
	 */
    public List<Appointment> getApptForDay(String provider, Date aDate){
        Calendar cal = new GregorianCalendar();
        cal.setTime(aDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date firstDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDate = cal.getTime();
        debug("getApptForDay("+provider+", "+firstDate+", "+nextDate+")");

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getApptForDay;
        List<Appointment> appts = new ArrayList<Appointment>();
        Appointment appt = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, provider);
            setDate(pstmt, 2, firstDate);
            setDate(pstmt, 3, nextDate);
            rs = executeQuery(pstmt);

            while (rs.next()){
                appt = new Appointment();
                setValueForAppt(con, rs, appt);
                appts.add(appt);
            }
            if (aDate.after(Constant.DEC_FIRST_DATE)){
            	appts.add(getLunchBreakAppt(provider, aDate));
            }
            
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appts;
    }

    private Appointment getLunchBreakAppt(String provider, Date date){
    	Calendar start = new GregorianCalendar();
    	start.setTime(date);
    	start.set(Calendar.HOUR_OF_DAY, 12);
    	start.set(Calendar.MINUTE, 0);
    	start.set(Calendar.SECOND, 0);
    	start.set(Calendar.MILLISECOND, 0);

    	Calendar end = new GregorianCalendar();
    	end.setTime(date);
    	end.set(Calendar.HOUR_OF_DAY, 12);
    	end.set(Calendar.MINUTE, 30);
    	end.set(Calendar.SECOND, 0);
    	end.set(Calendar.MILLISECOND, 0);
    	
    	Appointment appt = new Appointment();
    	appt.setStartDate(start.getTime());
    	appt.setEndDate(end.getTime());
    	appt.setType(Constant.Blocked);
    	appt.setReferralId(-1);
    	appt.setApptId(0);
    	appt.setProvider(provider);
    	appt.setClinic("");
    	appt.setNotes("Lunch Break for All Clinics");
    	return appt;
    }    
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getApptForClinicAndDate(java.lang.String, java.util.Date)
	 */
    public List<Appointment> getApptForClinicAndDate(String clinic, Date aDate){
        Calendar cal = new GregorianCalendar();
        cal.setTime(aDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date firstDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDate = cal.getTime();
        debug("getApptForClinicAndDay("+clinic+", "+firstDate+", "+nextDate+")");

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getApptForClinicAndDate;
        List<Appointment> appts = new ArrayList<Appointment>();
        Appointment appt = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, clinic);
            setDate(pstmt, 2, firstDate);
            setDate(pstmt, 3, nextDate);
            rs = executeQuery(pstmt);

            while (rs.next()){
                appt = new Appointment();
                setValueForAppt(con, rs, appt);
                appts.add(appt);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appts;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getApptForPatient(int)
	 */
    public List<Appointment> getApptForPatient(int refId){
        debug("getApptForPatient("+refId+")");

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getApptForPatient;
        List<Appointment> appts = new ArrayList<Appointment>();
        Appointment appt = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = executeQuery(pstmt);

            while (rs.next()){
                appt = new Appointment();
                setValueForAppt(con, rs, appt);
                appts.add(appt);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appts;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getApptForApptId(long)
	 */
    public Appointment getApptForApptId(long apptId){
        debug("getApptForApptId("+apptId+")");

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getApptForApptId;
        Appointment appt = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setLong(pstmt, 1, apptId);
            rs = executeQuery(pstmt);

            if (rs.next()){
                appt = new Appointment();
                setValueForAppt(con, rs, appt);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appt;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getAllOverlapAppt(java.lang.String, java.util.Date, java.util.Date)
	 */
    public List<Appointment> getAllOverlapAppt(String provider, Date startTime, Date endTime){
        debug("getAllOverlapAppt("+provider+", "+Constant.tf_s.format(startTime)+", "+
              Constant.tf_s.format(endTime)+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllOverlapAppt;
        List<Appointment> appts = new ArrayList<Appointment>();
        Appointment appt = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, provider);
            setDate(pstmt, 2, endTime);
            setDate(pstmt, 3, startTime);

            rs = executeQuery(pstmt);

            while (rs.next()){
                appt = new Appointment();
                setValueForAppt(con, rs, appt);
                appts.add(appt);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appts;
    }

    private void setValueForCanceledAppt(Connection con, ResultSet rs, Appointment appt) throws SQLException
    {
    	setValueForAppt(con, rs, appt, true);
    }

    private void setValueForAppt(Connection con, ResultSet rs, Appointment appt) throws SQLException
    {
    	setValueForAppt(con, rs, appt, false);
    }
    
    private void setValueForAppt(Connection con, ResultSet rs, Appointment appt, boolean isCancelAppt)
        throws SQLException
    {
        appt.setApptId(rs.getLong(1));
        appt.setReferralId(rs.getInt(2));
        appt.setLastName(rs.getString(3));
        //appt.setFirstName(SecurityUtil.decrypt(rs.getString(4)));
        appt.setFirstName(rs.getString(4));
        appt.setProvider( rs.getString(5));
        Timestamp ts = rs.getTimestamp(6);
        Date startDate = null;
        if (ts != null){
            startDate = new Date(ts.getTime());
        }
        appt.setStartDate( startDate );
        ts = rs.getTimestamp(7);
        Date endDate = null;
        if (ts != null){
            endDate = new Date(ts.getTime());
        }
        appt.setEndDate(endDate);
        appt.setNotes(rs.getString(8));
        appt.setType(rs.getString(9));
        appt.setClinic(rs.getString(10));
        appt.setNeedTranSvc(rs.getBoolean(11));
        appt.setLang(rs.getString(12));
        appt.setCollateralReceived(rs.getBoolean(13));
        appt.setIsEligible(rs.getString(14));
        appt.setPhoneNum(rs.getString(15));
        appt.setBirthDate(rs.getTimestamp(16));
        appt.setOwner(rs.getString(17));
        appt.setEval(rs.getBoolean(18));
        appt.setEvalYear(rs.getString(19));
        appt.setWalkIn(rs.getBoolean(20));
        appt.setAuthNum(rs.getString(21));
        appt.setCountyNum(rs.getString(22));
        if (isCancelAppt){
        	appt.setCanceledBy(rs.getString(23));
        	appt.setCancelByPatient(rs.getBoolean(24));
        	appt.setCancelByClinic(rs.getBoolean(25));
        	appt.setWnTwentyFourHrs(rs.getBoolean(26));
        	appt.setCancelReason(rs.getString(27));
        	appt.setCancelOtherReason(rs.getString(28));
        	appt.setStatus(Constant.CANCELED);;
        }else{
	        if (!appt.getType().equals(Constant.Blocked)){
	            setApptStatus(con, appt.getApptId(), appt);
	        }
        }
    }

    private void setApptStatus(Connection con, long apptId, Appointment appt){
        debug("calling getApptStatus("+apptId+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = ReadQueries.getApptStatus;
        String status = "";
        String reason = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setLong(pstmt, 1, apptId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                status = rs.getString(1);
                appt.setStatus(status);
                reason = rs.getString(2);
                if (reason != null && !reason.equalsIgnoreCase("")){
                    appt.setReasonCode(reason);
                }
                appt.setMemberId(rs.getString(3));
                appt.setStatusNotes(rs.getString(4));
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#acquireSeqLock(java.lang.String)
	 */
    public void acquireSeqLock(String lockName){
        String deleteSeq = "delete from seqLock where lockName = '"+lockName+"'";
        Connection con = connector.getConnectionFromPool();
        Statement stmt = null;
        int num = 0;
        try{
            while (num == 0){
                try{
                    Thread.sleep(100);
                }catch(InterruptedException ie){
                }
                stmt = con.createStatement();
                num = stmt.executeUpdate(deleteSeq);
            }
            // acquired lock
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+deleteSeq, e);
        }finally{
            close(stmt);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#releaseSeqLock(java.lang.String)
	 */
    public void releaseSeqLock(String lockName){
        String insertSeq = "insert into seqLock values ( '"+lockName+"' )";
        Connection con = connector.getConnectionFromPool();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            stmt.executeUpdate(insertSeq);
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+insertSeq, e);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getNextSeq(java.lang.String)
	 */
    public long getNextSeq(String tableName){
        debug("calling getNextSeq("+tableName+")");

        acquireSeqLock(tableName);

        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getNextSeq;
        long nextSeq = 0;
        try{
            con = connector.getConnectionFromPool();
            pstmt1 = con.prepareStatement(sql);
            setString(pstmt1, 1, tableName);
            rs = pstmt1.executeQuery();
            if (rs.next()){
                nextSeq = rs.getLong(1);
            }

            sql = ReadQueries.updateNextSeq;
            pstmt2 = con.prepareStatement(sql);
            setString(pstmt2, 1, tableName);
            pstmt2.executeUpdate();

            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt1);
            close(pstmt2);
            connector.releaseConnection(con);
            releaseSeqLock(tableName);
        }
        return nextSeq;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getBalForPatient(int)
	 */
    public BigDecimal getBalForPatient(int refId){
        debug("calling getBalForPatient("+refId+")");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getBalForPatient;
        BigDecimal bal = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	bal = rs.getBigDecimal(1);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return bal;
    }
        
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getNtsForPatient(java.lang.String, java.lang.String)
	 */
    public boolean getNtsForPatient(String lastName, String firstName){
    	boolean nts = false;
    	
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getNtsForPatient;        
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setString(pstmt, 1, lastName);
            //setString(pstmt, 2, SecurityUtil.encrypt(firstName));
            setString(pstmt, 2, firstName);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	nts = rs.getBoolean(1);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return nts;
    	
    	
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getHolidayDesc(java.util.Date)
	 */
    public String getHolidayDesc(Date date){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.isHoliday;
        String desc = null;
        
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            rs = pstmt.executeQuery();
            if (rs.next()){
            	desc = rs.getString(1);
            	if (desc == null){
            		desc = "";
            	}
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return desc;
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getHolidays()
	 */
    public List getHolidays(){
        Statement stmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getHolidays;
        List hList = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);            
            while (rs.next()){
            	Holiday h = new Holiday();
            	h.setId(rs.getInt(1));
            	h.setStartDate(rs.getDate(2));
            	h.setEndDate(rs.getDate(3));
            	h.setDesc(rs.getString(4));
            	hList.add(h);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(stmt);
            connector.releaseConnection(con);
        }
        return hList;    	
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#getStdHolidays()
	 */
    public List getStdHolidays(){
        Statement stmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getStdHolidays;
        List hList = new ArrayList();
        try{
            con = connector.getConnectionFromPool();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);            
            while (rs.next()){
            	String name = rs.getString(1);
            	boolean active = rs.getBoolean(2);
            	StandardHoliday h = new StandardHoliday(name, active);
            	hList.add(h);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(stmt);
            connector.releaseConnection(con);
        }
        return hList;    	
    }
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.ReadSvcI#isActiveHoliday(java.lang.String)
	 */
    public boolean isActiveHoliday(String holiday){
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getIsActiveHoliday;
        boolean isActive = false;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, holiday);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	isActive = rs.getBoolean(1);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return isActive;    	    	
    }
    
    public List<Integer> getRefIdWithEvalAppt(String year){
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getRefIdWithEvalAppt;
        List<Integer> refIdList = new ArrayList<Integer>();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, year);
            rs = pstmt.executeQuery();            
            while (rs.next()){
            	int refId = rs.getInt(1);
            	refIdList.add(refId);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return refIdList;    	    	
    }
    
    public List<Integer> getEvaluatedRefId(int year){
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getEvaluatedRefId;
        List<Integer> refIdList = new ArrayList<Integer>();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, year);
            rs = pstmt.executeQuery();            
            while (rs.next()){
            	int refId = rs.getInt(1);
            	refIdList.add(refId);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return refIdList;    	
    }

    public List<Evaluation> getAllEvaluation(int refId){
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllEvaluation;
        List<Evaluation> evalList = new ArrayList<Evaluation>();
        Evaluation eval = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, refId);
            rs = pstmt.executeQuery();            
            while (rs.next()){
            	eval = new Evaluation();
            	eval.setRefId(rs.getInt(1));
            	eval.setYear(rs.getInt(2));
            	eval.setNotes(rs.getString(3));
            	eval.setCheckoffDate(rs.getDate(4));
            	eval.setCheckoffBy(rs.getString(5));
            	evalList.add(eval);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return evalList;    	
    }

    public List<Appointment> getEvaluationApptForPatient(int refId, String evalYear){
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getEvaluationApptForPatient;
        List<Appointment> appts = new ArrayList<Appointment>();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, refId);
            pstmt.setString(2, evalYear);
            rs = pstmt.executeQuery();
            while (rs.next()){   
            	Appointment appt = new Appointment();
            	String provider = rs.getString(1);
            	Timestamp apptDate = rs.getTimestamp(2);
            	String status = rs.getString(3);
            	appt.setProvider(provider);
                appt.setStartDate(new Date(apptDate.getTime()));       
                appt.setStatus(status);
                appts.add(appt);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appts;    	    	
    }
    
    public List<Appointment> getAllEvaluationAppt(int refId){
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getAllEvaluationAppt;
        List<Appointment> appts = new ArrayList<Appointment>();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, refId);
            rs = pstmt.executeQuery();
            while (rs.next()){   
            	Appointment appt = new Appointment();
            	String provider = rs.getString(1);
            	Timestamp apptDate = rs.getTimestamp(2);
            	String status = rs.getString(3);
            	String evalYear = rs.getString(4);
            	
            	appt.setProvider(provider);
                appt.setStartDate(new Date(apptDate.getTime()));       
                appt.setStatus(status);
                appt.setEvalYear(evalYear);

                appts.add(appt);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appts;    	    	
    }
    
    public int getNumInEligibleAppt(int refId){
        PreparedStatement pstmt = null;        
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getNumApptByEligibility;
        int num = 0;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, refId);
            pstmt.setString(2, "no");
            rs = pstmt.executeQuery();
            if (rs.next()){
            	num = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return num;    	    	    	
    }

    protected void debug(String msg){
        if (Constant.debugOn){
            System.out.println("[ReadSvc]: "+msg);
        }
    }

	@Override
	public List<Appointment> getCancelApptForPatient(int refId) {
        debug("getCancelApptForPatient("+refId+")");

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getCancelApptForPatient;
        List<Appointment> appts = new ArrayList<Appointment>();
        Appointment appt = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            rs = executeQuery(pstmt);

            while (rs.next()){
                appt = new Appointment();
                setValueForCanceledAppt(con, rs, appt);
                appts.add(appt);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return appts;
    }

	@Override
	public List<Patient> getPatientByIns(String insName) {
        debug("getPatientByIns("+insName+")");

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = ReadQueries.getPatientByIns;
        List<Patient> pats = new ArrayList<Patient>();
        Patient pat = null;
        Insurance ins = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);            
            setString(pstmt, 1, insName);
            rs = executeQuery(pstmt);
            
            /*
				r.firstName, r.lastName, r.gender, r.ssn, r.birthDate, r.streetAddress,
				r.apartmentNumber, r.city, r.state, r.zipCode, r.phoneNumber, r.email,
				i.eligEffDate, i.eligTermDate, i.insuranceCompany, i.memberId, i.copayParity,
				i.medicalId, i.medicalIssueDate 
             */
            
            while (rs.next()){            	
                pat = new Patient();
                ins = new Insurance();
                pat.setFirstName(rs.getString(1));
                pat.setLastName(rs.getString(2));
                pat.gender = rs.getString(3);
                pat.ssn = SecurityUtil.decrypt(rs.getString(4));
                pat.setBirthDate(rs.getDate(5));
                pat.streetAddress = rs.getString(6);
                pat.apartmentNumber = rs.getString(7);
                pat.city = rs.getString(8);
                pat.state = rs.getString(9);
                pat.zipCode = rs.getString(10);
                pat.phoneNumber = rs.getString(11);
                pat.email = rs.getString(12);
                
                ins.setEffStartDate(rs.getDate(13));
                ins.setEffEndDate(rs.getDate(14));
                ins.setInsCompany(rs.getString(15));
                ins.setMemberId(rs.getString(16));
                ins.setCopayParity(rs.getBigDecimal(17));
                ins.setMedicalId(rs.getString(18));
                ins.setMedIssueDate(rs.getDate(19));
                
                pat.addIns(ins);
                
                pats.add(pat);
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(rs);
            close(pstmt);
            connector.releaseConnection(con);
        }
        return pats;
	}    
    
}
