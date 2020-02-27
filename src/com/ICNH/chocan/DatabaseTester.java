package com.ICNH.chocan;

import com.ICNH.chocan.records.ServiceInfoRecord;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseTester {
    public static void main(String[] args) throws SQLException {
        DatabaseInterface dbinter = new DatabaseInterface();
        ArrayList<ServiceInfoRecord> records = dbinter.getServiceInfos();
        records.forEach(e -> System.out.println(e));
        System.out.println(dbinter.getServiceInfo(1));
    }
}