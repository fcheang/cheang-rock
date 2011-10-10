package com.suntek.scheduler.appsvcs;

import java.sql.*;
import java.math.*;

/**
 * <p>Title: Appointment Scheduler</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Sunteksystems</p>
 * @author Steve Cheang
 * @version 1.0
 */

public class QuerySvc {

    boolean debugOn = Boolean.getBoolean("debugOn");

    public void close(ResultSet rs){
        try{
            if (rs != null){
                rs.close();
            }
        }catch(SQLException e){
        }
    }

    public void close(Statement stmt){
        try{
            if (stmt != null){
                stmt.close();
            }
        }catch(SQLException e){
        }
    }

    public void setObject(PreparedStatement pstmt, int param, Object obj)
        throws SQLException
    {
        debug("setObject("+param+", "+obj+")");
        if (obj instanceof String){
            setString(pstmt, param, (String)obj);
        }else if (obj instanceof Date){
            setDate(pstmt, param, (Date)obj);
        }else if (obj instanceof Integer){
            setInt(pstmt, param, ((Integer)obj).intValue());
        }else if (obj instanceof Long){
            setLong(pstmt, param, ((Long)obj).longValue());
        }else if (obj instanceof Boolean){
            setBoolean(pstmt, param, ((Boolean)obj).booleanValue());
        }else if (obj instanceof BigDecimal){
            setBigDecimal(pstmt, param, ((BigDecimal)obj));
        }else{
            pstmt.setObject(param, obj);
        }
    }

    public void setString(PreparedStatement pStmt, int param, String val)
        throws SQLException
    {
        debug("setString("+param+", "+val+")");
        if (val != null){
            pStmt.setString(param, val);
        }else{
            pStmt.setNull(param, Types.VARCHAR);
        }
    }

    public void setDate(PreparedStatement pstmt, int param, java.util.Date date)
    throws SQLException
    {
        debug("setTimestamp("+param+", "+date+")");
        if (date != null){
            pstmt.setTimestamp(param, new Timestamp(date.getTime()));
        }else{
            pstmt.setNull(param, Types.TIMESTAMP);
        }
    }

    public void setInt(PreparedStatement pstmt, int param, int val)
    throws SQLException
    {
        debug("setInt("+param+", "+val+")");
        pstmt.setInt(param, val);
    }

    public void setLong(PreparedStatement pstmt, int param, long val)
    throws SQLException
    {
        debug("setLong("+param+", "+val+")");
        pstmt.setLong(param, val);
    }

    public void setBoolean(PreparedStatement pstmt, int param, boolean val)
    throws SQLException
    {
        debug("setBoolean("+param+", "+val+")");
        pstmt.setBoolean(param, val);
    }

    public void setBigDecimal(PreparedStatement pstmt, int param, BigDecimal val)
    throws SQLException
    {
        debug("setBigDecimal("+param+", "+val+")");
        if (val != null){
            pstmt.setBigDecimal(param, val);
        }else{
            pstmt.setNull(param, Types.NUMERIC);
        }
    }

    public void executeUpdate(PreparedStatement stmt)
    throws SQLException
    {
        debug("executeUpdate");
        stmt.executeUpdate();
    }

    public ResultSet executeQuery(PreparedStatement stmt)
    throws SQLException
    {
        debug("executeQuery");
        return stmt.executeQuery();
    }

    protected void debug(String msg){
        if (debugOn){
            System.out.println("[QuerySvc]" + msg);
        }
    }
}
