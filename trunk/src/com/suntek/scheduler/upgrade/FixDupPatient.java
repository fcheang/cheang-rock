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
public class FixDupPatient {

    public FixDupPatient() {
    }

    public static void main(String[] args){
        PSService.getService().connect();
        fixDupPatient();
    }

    public static void fixDupPatient(){
        String getDupEntry =
            "select count(*), lastname, firstname, birthdate "+
            "from referral "+
            "group by lastname, firstname, birthdate "+
            "having count(*) > 1 "+
            "order by lastname, firstname";

        String getRefId =
            "select referralId from referral where lastname = ? "+
            "and firstname = ? "+
            "and birthdate = ? "+
            "order by referralId";

        String mergeAppt = "update appointment set referralId = ? where referralId = ?";

        String delStatus = "delete from referralStatus where referralId = ? and status = 'Not Scheduled'";

        String mergeStatus = "update referralStatus set referralId = ? where referralId = ?";

        String delRef = "delete from referral where referralId = ?";

        String delIns = "delete from insurance where referralId = ?";

        List listOfDups = new ArrayList();
        int firstRef = 0;
        int dupRef = 0;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        Connection con = null;
        JDBCConnector connector = JDBCConnector.getInstance();
        try{
            con = connector.getConnectionFromPool();
            pstmt = con.prepareStatement(getDupEntry);
            rs = pstmt.executeQuery();
            int c=0;
            while (rs.next()){
                DupEntry de = new DupEntry();
                de.numDup = rs.getInt(1);
                de.lastName = rs.getString(2);
                de.firstName = rs.getString(3);
                de.dob = rs.getTimestamp(4);
                debug("Dup["+c+"] "+de.numDup+" "+de.lastName+" "+de.firstName+" "+de.dob);
                c += 1;
                listOfDups.add(de);
            }
            rs.close();
            pstmt.close();

            for (int i=0; i<listOfDups.size(); i++){
                DupEntry dup = (DupEntry)listOfDups.get(i);
                pstmt = con.prepareStatement(getRefId);
                pstmt.setString(1, dup.lastName);
                pstmt.setString(2, dup.firstName);
                pstmt.setTimestamp(3, dup.dob);
                rs = pstmt.executeQuery();

                rs.next();
                firstRef = rs.getInt(1);

                while (rs.next()){
                    dupRef = rs.getInt(1);

                    pstmt2 = con.prepareStatement(mergeAppt);
                    debug("mergeAppt: "+firstRef+", "+dupRef);
                    pstmt2.setInt(1, firstRef);
                    pstmt2.setInt(2, dupRef);
                    pstmt2.executeUpdate();
                    //con.commit();
                    pstmt2.close();

                    pstmt2 = con.prepareStatement(delStatus);
                    debug("delStatus: "+dupRef);
                    pstmt2.setInt(1, dupRef);
                    pstmt2.executeUpdate();
                    //con.commit();
                    pstmt2.close();

                    pstmt2 = con.prepareStatement(mergeStatus);
                    debug("mergeStatus: "+firstRef+", "+dupRef);
                    pstmt2.setInt(1, firstRef);
                    pstmt2.setInt(2, dupRef);
                    pstmt2.executeUpdate();
                    //con.commit();
                    pstmt2.close();

                    pstmt2 = con.prepareStatement(delRef);
                    debug("delRef: "+dupRef);
                    pstmt2.setInt(1, dupRef);
                    pstmt2.executeUpdate();
                    //con.commit();
                    pstmt2.close();

                    pstmt2 = con.prepareStatement(delIns);
                    debug("delIns: "+dupRef);
                    pstmt2.setInt(1, dupRef);
                    pstmt2.executeUpdate();
                    //con.commit();
                    pstmt2.close();
                }
                rs.close();
                pstmt.close();
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
