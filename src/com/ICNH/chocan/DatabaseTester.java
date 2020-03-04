package com.ICNH.chocan;

import com.ICNH.chocan.records.FullServiceRecord;
import com.ICNH.chocan.records.ServiceInfoRecord;
import com.ICNH.chocan.records.ServiceRecord;

//import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseTester {
    public static void main(String[] args) throws SQLException {
        DatabaseInterface dbinter = new DatabaseInterface();
//        ArrayList<ServiceInfoRecord> records = dbinter.getServiceInfos();
//        records.forEach(System.out::println);
//        System.out.println(dbinter.getServiceInfo(1));

//        ArrayList<FullServiceRecord> srecords = dbinter.getServicesByProvider(1);
//        srecords.forEach(System.out::println);

        ServiceRecord record = new ServiceRecord();
        record.currentDate = new Date();
        record.memberID = 1;
        record.serviceID = 1;
        record.comments = "waow";
        record.providerID = 1;
        record.serviceDate = new Date();
        dbinter.insertService(record);
    }
}