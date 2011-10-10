package com.suntek.scheduler.upgrade;

import com.suntek.scheduler.ui.PSService;
import com.suntek.scheduler.appsvcs.*;
import java.sql.*;
import java.util.*;

/**
 * <p>Title: Appointment Scheduler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Sunteksystems</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class UpdateAppointmentIsNewColumn {
    public UpdateAppointmentIsNewColumn() {
    }

    public static void main(String[] args){
        PSService.getService().connect();
        updateIsNewCol();
    }

    public static void updateIsNewCol(){
        String getAllRefIdForAppt = "select distinct referralId from appointment";

        String getFirstAppIdForRef = "select apptId from referralStatus where referralId = ? "+
            "and status = 'Scheduled' " +
            "order by createDate";

        String updateApptIsNewCol = "update appointment set isNew = 1 where apptId = ?";

        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        Connection con = null;
        JDBCConnector connector = JDBCConnector.getInstance();
        long refId = -1;
        try{
            con = connector.getConnectionFromPool();
            pstmt1 = con.prepareStatement(getAllRefIdForAppt);
            pstmt2 = con.prepareStatement(getFirstAppIdForRef);
            pstmt3 = con.prepareStatement(updateApptIsNewCol);

            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()){
                refId = rs1.getLong(1);
                pstmt2.setLong(1, refId);
                ResultSet rs2 = pstmt2.executeQuery();
                if (rs2.next()){
                    long apptId = rs2.getLong(1);
                    pstmt3.setLong(1, apptId);
                    debug("update apptId "+apptId+" to new");
                    pstmt3.executeUpdate();
                }else{
                    debug("Warning: referralId "+refId+" has no status!");
                }
                rs2.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            connector.releaseConnection(con);
        }
    }

    static void debug(String msg){
        System.out.println(msg);
    }

    static class DupEntry {
        int numDup;
        String lastName;
        String firstName;
        Timestamp dob;
    }
}
