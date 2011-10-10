
java -classpath .\scheduler.jar;.\extlib\mysql-connector-java-3.1.10-bin.jar -DdbDriver=com.mysql.jdbc.Driver -DdbHost=localhost -DdbUsed=gold -DdbUser=steve -DdbPassword=st3v3 -DdebugOn=true com.suntek.scheduler.upgrade.UpdateAppointmentIsNewColumn
