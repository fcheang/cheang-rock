package com.suntek.scheduler.appsvcs;

import com.suntek.scheduler.appsvcs.persistence.*;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

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
public class WriteSvc extends QuerySvc implements WriteSvcI {

    private boolean debugOn = Boolean.getBoolean("debugOn");
    private static WriteSvcI writeSvc = (WriteSvcI)ExceptionLoggerProxy.newInstance(new WriteSvc());
    private JDBCConnector connector = JDBCConnector.getInstance();

    private WriteSvc() {
    }

    public static WriteSvcI getInstance(){
    	return writeSvc;
    }

    public void updateAppt(Appointment oldAppt, Appointment newAppt){
        debug("updateAppt("+oldAppt+", "+newAppt+")");
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();
            // startDate
            if (!oldAppt.getStartDate().equals(newAppt.getStartDate())){
                sql = WriteQueries.updateApptStartTime;
                pstmt = con.prepareStatement(sql);
                setDate(pstmt, 1, newAppt.getStartDate());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
            // endDate
            if (!oldAppt.getEndDate().equals(newAppt.getEndDate())){
                sql = WriteQueries.updateApptEndTime;
                pstmt = con.prepareStatement(sql);
                setDate(pstmt, 1, newAppt.getEndDate());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
            // notes
            if (!oldAppt.getNotes().equals(newAppt.getNotes())){
                sql = WriteQueries.updateApptNotes;
                pstmt = con.prepareStatement(sql);
                setString(pstmt, 1, newAppt.getNotes());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
            // clinic
            if (!oldAppt.getClinic().equals(newAppt.getClinic())){
                sql = WriteQueries.updateApptClinic;
                pstmt = con.prepareStatement(sql);
                setString(pstmt, 1, newAppt.getClinic());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
            // nts
            if (! (oldAppt.needTranSvc() == newAppt.needTranSvc()) ){
                sql = WriteQueries.updateApptNts;
                pstmt = con.prepareStatement(sql);
                setBoolean(pstmt, 1, newAppt.needTranSvc());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
            if (!oldAppt.getLang().equals(newAppt.getLang())){
            	sql = WriteQueries.updateApptLang;
                pstmt = con.prepareStatement(sql);
                setString(pstmt, 1, newAppt.getLang());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);            	
            }
            // collRcv
            if (! (oldAppt.collateralReceived() == newAppt.collateralReceived()) ){
                sql = WriteQueries.updateApptCollRcv;
                pstmt = con.prepareStatement(sql);
                setBoolean(pstmt, 1, newAppt.collateralReceived());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
            // isEligible
            if (! oldAppt.isEligible().equals(newAppt.isEligible())){
                sql = WriteQueries.updateApptIsEligible;
                pstmt = con.prepareStatement(sql);
                setString(pstmt, 1, newAppt.isEligible());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
            // isWalkIn
            if (oldAppt.isWalkIn() != newAppt.isWalkIn()){
            	sql = WriteQueries.updateApptIsWalkIn;
                pstmt = con.prepareStatement(sql);
                setBoolean(pstmt, 1, newAppt.isWalkIn());
                setLong(pstmt, 2, oldAppt.getApptId());
                executeUpdate(pstmt);
                //con.commit();
            }
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#deleteAppt(com.suntek.scheduler.appsvcs.persistence.Appointment)
	 */
    public void deleteAppt(Appointment oldAppt){
        debug("deleteAppt("+oldAppt+")");
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();

            // delete old appt
            sql = WriteQueries.deleteAppt;
            pstmt1 = con.prepareStatement(sql);
            setLong(pstmt1, 1, oldAppt.getApptId());
            executeUpdate(pstmt1);

            // delete old status
            sql = WriteQueries.deleteApptStatus;
            pstmt2 = con.prepareStatement(sql);
            setLong(pstmt2, 1, oldAppt.getApptId());
            executeUpdate(pstmt2);

            // update lastStatus to EOT
            Date lastRmDate = Constant.END_OF_TIME;
            sql = ReadQueries.getLastRemoveDate;
            pstmt3 = con.prepareStatement(sql);
            setLong(pstmt3, 1, oldAppt.getReferralId());
            rs = executeQuery(pstmt3);
            if (rs.next()){
                lastRmDate = rs.getTimestamp(1);
            }

            sql = WriteQueries.updateLastApptStatusToEOT;
            pstmt4 = con.prepareStatement(sql);
            setLong(pstmt4, 1, oldAppt.getReferralId());
            setDate(pstmt4, 2, lastRmDate);
            executeUpdate(pstmt4);

            // audit deleted appt
            sql = WriteQueries.insertDeletedAppt;
            pstmt5 = con.prepareStatement(sql);
            setLong(pstmt5, 1, oldAppt.getApptId());
            setInt(pstmt5, 2, oldAppt.getReferralId());
            setString(pstmt5, 3, oldAppt.getClinic());
            setDate(pstmt5, 4, oldAppt.getStartDate());
            setString(pstmt5, 5, oldAppt.getProvider());
            setBoolean(pstmt5, 6, oldAppt.needTranSvc());
            setBoolean(pstmt5, 7, oldAppt.collateralReceived());
            setString(pstmt5, 8, oldAppt.isEligible());            
            setString(pstmt5, 9, oldAppt.getNotes());
            setString(pstmt5, 10, oldAppt.getOwner());
            setDate(pstmt5, 11, oldAppt.getEndDate());
            setString(pstmt5, 12, oldAppt.getType());
            setString(pstmt5, 13, Constant.appUser);
            setDate(pstmt5, 14, new java.util.Date());
            setBoolean(pstmt5, 15, oldAppt.isEval());
            setString(pstmt5, 16, oldAppt.getEvalYear());
            setBoolean(pstmt5, 17, oldAppt.isWalkIn());
            executeUpdate(pstmt5);

            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(pstmt1);
            close(pstmt2);
            close(pstmt3);
            close(pstmt4);
            close(pstmt5);
            connector.releaseConnection(con);        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#cancelAppt(com.suntek.scheduler.appsvcs.persistence.Appointment)
	 */
    public void cancelAppt(Appointment oldAppt){
        debug("cancelAppt("+oldAppt+")");
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();

            // delete old appt
            sql = WriteQueries.deleteAppt;
            pstmt1 = con.prepareStatement(sql);
            setLong(pstmt1, 1, oldAppt.getApptId());
            executeUpdate(pstmt1);

            // delete old status
            sql = WriteQueries.deleteApptStatus;
            pstmt2 = con.prepareStatement(sql);
            setLong(pstmt2, 1, oldAppt.getApptId());
            executeUpdate(pstmt2);

            // update lastStatus to EOT
            Date lastRmDate = Constant.END_OF_TIME;
            sql = ReadQueries.getLastRemoveDate;
            pstmt3 = con.prepareStatement(sql);
            setLong(pstmt3, 1, oldAppt.getReferralId());
            rs = executeQuery(pstmt3);
            if (rs.next()){
                lastRmDate = rs.getTimestamp(1);
            }

            sql = WriteQueries.updateLastApptStatusToEOT;
            pstmt4 = con.prepareStatement(sql);
            setLong(pstmt4, 1, oldAppt.getReferralId());
            setDate(pstmt4, 2, lastRmDate);
            executeUpdate(pstmt4);

            // audit deleted appt
            sql = WriteQueries.insertCanceledAppt;
            pstmt5 = con.prepareStatement(sql);
            setLong(pstmt5, 1, oldAppt.getApptId());
            setInt(pstmt5, 2, oldAppt.getReferralId());
            setString(pstmt5, 3, oldAppt.getClinic());
            setDate(pstmt5, 4, oldAppt.getStartDate());
            setString(pstmt5, 5, oldAppt.getProvider());
            setBoolean(pstmt5, 6, oldAppt.needTranSvc());
            setBoolean(pstmt5, 7, oldAppt.collateralReceived());
            setString(pstmt5, 8, oldAppt.isEligible());
            setString(pstmt5, 9, oldAppt.getNotes());
            setString(pstmt5, 10, oldAppt.getOwner());
            setDate(pstmt5, 11, oldAppt.getEndDate());
            setString(pstmt5, 12, oldAppt.getType());
            setString(pstmt5, 13, Constant.appUser);
            setDate(pstmt5, 14, new java.util.Date());
            setBoolean(pstmt5, 15, oldAppt.isEval());
            setString(pstmt5, 16, oldAppt.getEvalYear());
            setBoolean(pstmt5, 17, oldAppt.isWalkIn());
            setBoolean(pstmt5, 18, oldAppt.isCancelByPatient());
            setBoolean(pstmt5, 19, oldAppt.isCancelByClinic());
            setBoolean(pstmt5, 20, oldAppt.isWnTwentyFourHrs());
            setString(pstmt5, 21, oldAppt.getCancelReason());
            setString(pstmt5, 22, oldAppt.getCancelOtherReason());
            executeUpdate(pstmt5);

            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt1);
            close(pstmt2);
            close(pstmt3);
            close(pstmt4);
            close(pstmt5);
            connector.releaseConnection(con);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#insertAppt(com.suntek.scheduler.appsvcs.persistence.Appointment)
	 */
    public void insertAppt(Appointment appt){
        // referralId, clinicName, appointmentDate, provider,
        // translationSvcNeeded, collateralReceived, notes, userId, endTime, type, reason
        // modifyBy, modificationDate
        debug("calling insertAppt("+appt+")");
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        Connection con = null;
        String sql = WriteQueries.insertAppt;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            setLong(pstmt, 1, appt.getApptId());
            setInt(pstmt, 2, appt.getReferralId());
            setString(pstmt, 3, appt.getClinic());
            setDate(pstmt, 4, appt.getStartDate());
            setString(pstmt, 5, appt.getProvider());
            setBoolean(pstmt, 6, appt.needTranSvc());
            setString(pstmt, 7, appt.getLang());
            setBoolean(pstmt, 8, appt.collateralReceived());
            setString(pstmt, 9, appt.isEligible());
            setString(pstmt, 10, appt.getNotes());
            setString(pstmt, 11, appt.getOwner());
            setDate(pstmt, 12, appt.getEndDate());
            setString(pstmt, 13, appt.getType());
            setString(pstmt, 14, Constant.appUser);
            setDate(pstmt, 15, new java.util.Date());
            setBoolean(pstmt, 16, appt.isEval());
            setString(pstmt, 17, appt.getEvalYear());
            setBoolean(pstmt, 18, appt.isWalkIn());
            executeUpdate(pstmt);

            if (!appt.getType().equals(Constant.Blocked)){
                //removeDate = ? and referralId = ?
                sql = WriteQueries.endDatePriorNotScheduledOrWaitlistStatus;
                pstmt3 = con.prepareStatement(sql);
                setInt(pstmt3, 1, appt.getReferralId());
                executeUpdate(pstmt3);

                sql = WriteQueries.insertApptStatus;
                pstmt2 = con.prepareStatement(sql);
                //referralId, apptId, status, createDate, removeDate, isActive, notes, reason
                setInt(pstmt2, 1, appt.getReferralId());
                setLong(pstmt2, 2, appt.getApptId());
                setString(pstmt2, 3, Constant.Scheduled);
                setBoolean(pstmt2, 4, true);
                pstmt2.setNull(5, java.sql.Types.VARCHAR);
                pstmt2.setNull(6, java.sql.Types.VARCHAR);
                executeUpdate(pstmt2);
            }

            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            close(pstmt2);
            close(pstmt3);
            connector.releaseConnection(con);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#showup(long, int, java.lang.String, java.lang.String)
	 */
    public void showup(long apptId, int refId, String notes, String reason){
        insertApptStatus(apptId, refId, Constant.Seen, notes, reason);
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#noShows(long, int, java.lang.String)
	 */
    public void noShows(long apptId, int refId, String notes){
        insertApptStatus(apptId, refId, Constant.NotSeen, notes, null);
    }

    private void insertApptStatus(long apptId, int refId, String status, String notes, String reason){
        // referralId, clinicName, appointmentDate, provider,
        // translationSvcNeeded, collateralReceived, notes, userId, endTime, type, reason
        debug("calling insertApptStatus("+apptId+", "+status+")");
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        Connection con = null;
        String sql = WriteQueries.endDatePriorScheduledStatus;
        try{
            con = connector.getConnectionFromPool();
            pstmt1 = con.prepareStatement(sql);
            setLong(pstmt1, 1, apptId);
            executeUpdate(pstmt1);

            sql = WriteQueries.insertApptStatus;
            pstmt2 = con.prepareStatement(sql);
            //referralId, apptId, status, isActive, notes, reason
            setInt(pstmt2, 1, refId);
            setLong(pstmt2, 2, apptId);
            setString(pstmt2, 3, status);
            setBoolean(pstmt2, 4, true);
            setString(pstmt2, 5, notes);
            setString(pstmt2, 6, reason);
            executeUpdate(pstmt2);

            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt1);
            close(pstmt2);
            connector.releaseConnection(con);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#execDynamicSql(java.lang.String, java.util.List)
	 */
    public void execDynamicSql(String sql, List args){
        debug("calling execUpdateQuery("+sql+", "+args+")");
        PreparedStatement pstmt = null;
        Connection con = null;
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(sql);
            if (args != null){
	            for (int i=1; i<=args.size(); i++){
	                Object obj = args.get(i-1);
	                setObject(pstmt, i, obj);
	            }
            }
            executeUpdate(pstmt);
            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#undoStatusChange(long)
	 */
    public void undoStatusChange(long apptId){
        debug("undoStatusChange("+apptId+")");
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();

            // delete last status
            sql = WriteQueries.deleteLastStatus;
            pstmt1 = con.prepareStatement(sql);
            setLong(pstmt1, 1, apptId);
            executeUpdate(pstmt1);

            // update prior status to EOT
            sql = WriteQueries.setLastStatusToEOT;
            pstmt2 = con.prepareStatement(sql);
            setLong(pstmt2, 1, apptId);
            executeUpdate(pstmt2);

            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt1);
            close(pstmt2);
            connector.releaseConnection(con);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#clearReminder(long)
	 */
    public void clearReminder(long apptId){
        debug("clearReminder("+apptId+")");
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();
            sql = WriteQueries.clearReminder;
            pstmt = con.prepareStatement(sql);
            setLong(pstmt, 1, apptId);
            executeUpdate(pstmt);
            //con.commit();
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#createInsurance(com.suntek.scheduler.appsvcs.persistence.Insurance)
	 */
    public void createInsurance(Insurance ins){    	
        debug("calling createInsurance("+ins+")");
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        Connection con = null;
        
        String sql = null;
        try{
            con = connector.getConnectionFromPool();
            
            // reset all isLast to zero
            sql = WriteQueries.resetIsLastForIns;
            pstmt1 = con.prepareStatement(sql);
            setInt(pstmt1, 1, ins.getRefId());
            executeUpdate(pstmt1);

            // insert new insurance
            sql = WriteQueries.createInsurance;
            pstmt2 = con.prepareStatement(sql);
            setInt(pstmt2, 1, ins.getRefId());
            setDate(pstmt2, 2, ins.getRefDate());            
            setDate(pstmt2, 3, ins.getEffStartDate());
            setDate(pstmt2, 4, ins.getEffEndDate());            
            setString(pstmt2, 5, ins.getInsCompany());
            setString(pstmt2, 6, ins.getInsPhoneNum());            
            setString(pstmt2, 7, ins.getMemberId());
            setBigDecimal(pstmt2, 8, ins.getCopay());
            setBigDecimal(pstmt2, 9, ins.getCopayParity());
            setInt(pstmt2, 10, ins.getNumAuthVisitForMD());            
            setString(pstmt2, 11, ins.getAuthNumForMD());
            setInt(pstmt2, 12, ins.getNumAuthVisitForMA());
            setString(pstmt2, 13, ins.getAuthNumForMA());
            setString(pstmt2, 14, ins.getMedicalId());
            setDate(pstmt2, 15, ins.getMedIssueDate());
            setString(pstmt2, 16, ins.getNotes());
            setInt(pstmt2, 17, 1);
            executeUpdate(pstmt2);
            
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt1);
            close(pstmt2);
            connector.releaseConnection(con);
        }    	
    }
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#deleteInsurance(int, int)
	 */
    public void deleteInsurance(int insId, int refId){
        debug("calling deleteInsurance("+insId+")");
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;        
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();
            
            // delete insurance
            sql = WriteQueries.deleteInsurance;
            pstmt1 = con.prepareStatement(sql);
            setInt(pstmt1, 1, insId);
            executeUpdate(pstmt1);
            
            // set isLast = 1 for last record
            sql = ReadQueries.getMaxInsId;
            pstmt2 = con.prepareStatement(sql);
            setInt(pstmt2, 1, refId);
            ResultSet rs = executeQuery(pstmt2);
            if (rs.next()){
            	int maxId = rs.getInt(1);
            	sql = WriteQueries.updateInsIsLast;            	
            	pstmt3 = con.prepareStatement(sql);
            	setInt(pstmt3, 1, maxId);
            	executeUpdate(pstmt3);
            }            
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt1);
            close(pstmt2);
            close(pstmt3);
            connector.releaseConnection(con);
        }    	    	
    }
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#createHoliday(com.suntek.scheduler.appsvcs.persistence.Holiday)
	 */
    public void createHoliday(Holiday h){    	
        PreparedStatement pstmt = null;
        Connection con = null;
        
        String sql = null;
        try{
            con = connector.getConnectionFromPool();           
            sql = WriteQueries.createHoliday;
            pstmt = con.prepareStatement(sql);
            setDate(pstmt, 1, h.getStartDate());
            setDate(pstmt, 2, h.getEndDate());            
            setString(pstmt, 3, h.getDesc());
            executeUpdate(pstmt);            
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	
    }
    
    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#deleteHoliday(int)
	 */
    public void deleteHoliday(int id){
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();
            
            // delete insurance
            sql = WriteQueries.deleteHoliday;
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, id);
            executeUpdate(pstmt);            
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	    	
    }

    /* (non-Javadoc)
	 * @see com.suntek.scheduler.appsvcs.WriteSvcI#updateStdHolidayStatus(java.lang.String, boolean)
	 */
    public void updateStdHolidayStatus(String name, boolean isActive){
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();
            
            // delete insurance
            sql = WriteQueries.updateStdHolidayStatus;
            pstmt = con.prepareStatement(sql);
            setBoolean(pstmt, 1, isActive);
            setString(pstmt, 2, name);
            executeUpdate(pstmt);            
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	    	
    }
    
	public void updateProviderInactiveBit(int providerId, boolean isArchive, Date archiveDate){
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();
            sql = WriteQueries.updateProviderInactiveBit;
            pstmt = con.prepareStatement(sql);
            setBoolean(pstmt, 1, isArchive);
            setDate(pstmt, 2, archiveDate);
            setInt(pstmt, 3, providerId);
            executeUpdate(pstmt);            
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	    			
	}
	
	public void updatePatientDischargedBit(int refId, boolean isArchive, Date archiveDate){
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();            
            sql = WriteQueries.updatePatientDischargedBit;
            pstmt = con.prepareStatement(sql);
            setBoolean(pstmt, 1, isArchive);
            setDate(pstmt, 2, archiveDate);
            setInt(pstmt, 3, refId);
            executeUpdate(pstmt);   
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);        	
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	    					
	}

	public void checkoffEvaluation(int refId, int evalYear, String notes){
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();            
            sql = WriteQueries.checkoffEvaluation;
            pstmt = con.prepareStatement(sql);
            setInt(pstmt, 1, refId);
            setInt(pstmt, 2, evalYear);
            setString(pstmt, 3, notes);
            setDate(pstmt, 4, new Date());
            setString(pstmt, 5, Constant.appUser);
            executeUpdate(pstmt);
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	    					 
	}

	public void updateCanceledApptCancelBy(long apptId, boolean isCancelByPatient){
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();            
            sql = WriteQueries.updateCancelByPatient;
            pstmt = con.prepareStatement(sql);
            setBoolean(pstmt, 1, isCancelByPatient);
            setBoolean(pstmt, 2, !isCancelByPatient);
            setLong(pstmt, 3, apptId);
            executeUpdate(pstmt);
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	    					 		
	}
	
	public void updateCanceledApptIsWnTwentyFourHrs(long apptId, boolean isWnTwentyFourHrs){
        PreparedStatement pstmt = null;
        Connection con = null;
        String sql = null;
        try{
            con = connector.getConnectionFromPool();            
            sql = WriteQueries.updateIsWnTwentyFourHrs;
            pstmt = con.prepareStatement(sql);
            setBoolean(pstmt, 1, isWnTwentyFourHrs);
            setLong(pstmt, 2, apptId);
            executeUpdate(pstmt);
        }catch(SQLException e){
            throw new RuntimeException("Error: Problem executing: "+sql, e);
        }finally{
            close(pstmt);
            connector.releaseConnection(con);
        }    	    					 				
	}
	
    protected void debug(String msg){
        if (debugOn)
            System.out.println("[WriteSvc]: "+msg);
    }
}
