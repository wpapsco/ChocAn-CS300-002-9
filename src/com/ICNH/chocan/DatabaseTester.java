package com.ICNH.chocan;

import com.ICNH.chocan.records.FullServiceRecord;
import com.ICNH.chocan.records.MemberRecord;
import com.ICNH.chocan.records.ServiceInfoRecord;
import com.ICNH.chocan.records.ServiceRecord;

//import java.sql.*;
import java.lang.reflect.Member;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseTester {
    public static void main(String[] args) throws SQLException {
        DatabaseInterface dbinter = new DatabaseInterface();
//        ArrayList<ServiceInfoRecord> records = dbinter.getServiceInfos();
//        records.forEach(System.out::println);
//        System.out.println(dbinter.getServiceInfo(1));
//
//        ArrayList<FullServiceRecord> srecords = dbinter.getServicesByProvider(1);
//        srecords.forEach(System.out::println);

        MemberRecord record = new MemberRecord();
        record.address = "123 fake st waow";
        record.city = "portland";
        record.name = "tester";
        record.state = "or";
        record.valid = true;
        record.zip = "90210";
        int id = dbinter.insertMember(record);
        System.out.println(id);
    }
}