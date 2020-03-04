package com.ICNH.chocan;//This file defines the com.ICNH.ChocAn.DatabaseInterface class which interfaces with the
//data repository

import com.ICNH.chocan.records.*;
import javafx.embed.swt.SWTFXUtils;

import java.lang.reflect.Member;
import java.security.Provider;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseInterface {
    private final String username = "admin";
    private final String password = "password123"; //so secure
    private final String dburl = "jdbc:mysql://chocandb.c3jj5hlkz8gr.us-west-2.rds.amazonaws.com/mydb";
    private Connection connection;

    public DatabaseInterface() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dburl, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param results the ResultsSet to read from
     * @return a single ServiceInfoRecord from the ResultsSet
     * @throws SQLException
     */
    private ServiceInfoRecord readServiceInfoRecord(ResultSet results) throws SQLException {
        return readServiceInfoRecord(results, 1);
    }
    private ServiceInfoRecord readServiceInfoRecord(ResultSet results, int offset) throws SQLException {
        int i = offset;
        int id = results.getInt(i++);
        String name = results.getString(i++);
        String description = results.getString(i++);
        int fee = results.getInt(i++);
        return new ServiceInfoRecord(id, name, description, fee);
    }

    /**
     * @return all ServiceInfo records from the database
     * @throws SQLException
     */
    public ArrayList<ServiceInfoRecord> getServiceInfos() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM ServiceInfo;");
        ResultSet results = statement.executeQuery();
        ArrayList<ServiceInfoRecord> records = new ArrayList<>();
        while (results.next()) {
            records.add(readServiceInfoRecord(results));
        }
        return records;
    }

    /**
     * @param id the id of the ServiceInfo record to find
     * @return the matching ServiceInfo record; null if not found
     * @throws SQLException
     */
    public ServiceInfoRecord getServiceInfo(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM ServiceInfo WHERE id = ?;");
        statement.setInt(1, id);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return readServiceInfoRecord(results);
        } else return null;
    }

    /**
     * @param results the ResultsSet to read from
     * @return a single MemberRecord from the ResultsSet
     * @throws SQLException
     */
    private MemberRecord readMemberRecord(ResultSet results) throws SQLException {
        return readMemberRecord(results, 1);
    }
    private MemberRecord readMemberRecord(ResultSet results, int offset) throws SQLException {
        int i = offset;
        int id = results.getInt(i++);
        String name = results.getString(i++);
        String address = results.getString(i++);
        String city = results.getString(i++);
        String state = results.getString(i++);
        String zip = results.getString(i++);
        boolean valid = results.getInt(i++) == 1;
        return new MemberRecord(id, name, valid, address, city, state, zip);
    }

    /**
     * @return all Member records from the database
     * @throws SQLException
     */
    public ArrayList<MemberRecord> getMemberRecords() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Members;");
        ArrayList<MemberRecord> records = new ArrayList<>();
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            records.add(readMemberRecord(results));
        }
        return records;
    }

    /**
     * @param id the id of the Member record to find
     * @return the matching ServiceInfo record; null if not found
     * @throws SQLException
     */
    public MemberRecord getMemberRecord(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Members WHERE id = ?;");
        statement.setInt(1, id);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return readMemberRecord(results);
        } else return null;
    }

    /**
     * @param results the ResultsSet to read from
     * @return a single ProviderRecord from the ResultsSet
     * @throws SQLException
     */
    private ProviderRecord readProviderRecord(ResultSet results) throws SQLException {
        return readProviderRecord(results, 1);
    }
    private ProviderRecord readProviderRecord(ResultSet results, int offset) throws SQLException {
        int i = offset;
        int id = results.getInt(i++);
        String name = results.getString(i++);
        String address = results.getString(i++);
        String city = results.getString(i++);
        String state = results.getString(i++);
        String zip = results.getString(i++);
        return new ProviderRecord(id, name, address, city, state, zip);
    }

    /**
     * @return all Provider records from the database
     * @throws SQLException
     */
    public ArrayList<ProviderRecord> getProviderRecords() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Providers;");
        ResultSet results = statement.executeQuery();
        ArrayList<ProviderRecord> records = new ArrayList<>();
        while (results.next()) {
            records.add(readProviderRecord(results));
        }
        return records;
    }

    /**
     * @param id the id of the Provider record to find
     * @return the matching ServiceInfo record; null if not found
     * @throws SQLException
     */
    public ProviderRecord getProviderRecord(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Providers WHERE id = ?;");
        statement.setInt(1, id);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return readProviderRecord(results);
        } else return null;
    }

    /**
     * @param id the member id to validate
     * @return true if the id is valid, false if not
     * @throws SQLException
     */
    public boolean validateMember(int id) throws SQLException {
        MemberRecord record = getMemberRecord(id);
        return record != null && record.valid;
    }

    public boolean validateProvider(int id) throws SQLException {
        return getProviderRecord(id) != null;
    }

    private FullServiceRecord readFullServiceRecord(ResultSet results) throws SQLException {
        FullServiceRecord record = new FullServiceRecord();
        record.comments = results.getString(4);
        record.currentDate = results.getDate(5);
        record.serviceDate = results.getDate(6);
        record.serviceInfo = readServiceInfoRecord(results, 7);
        record.provider = readProviderRecord(results, 11);
        record.member = readMemberRecord(results, 17);
        return record;
    }

    private ArrayList<FullServiceRecord> getServices(int id, boolean provider) throws SQLException {
        ArrayList<FullServiceRecord> records = new ArrayList<FullServiceRecord>(); //TODO
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM (((Services " +
                        "INNER JOIN ServiceInfo ON Services.service_info = ServiceInfo.id) " +
                        "INNER JOIN Providers ON Services.provider = Providers.id) " +
                        "INNER JOIN Members ON Services.member = Members.id) " +
                        "WHERE Services." + (provider ? "provider" : "member") + " = ?;");
        statement.setInt(1, id);
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            records.add(readFullServiceRecord(results));
        }
        return records;
    }

    //feel free to stub out more methods like this where you need them, I will implement them
    public ArrayList<FullServiceRecord> getServicesByProvider(int id) throws SQLException {
        return getServices(id, true);
    }

    public ArrayList<FullServiceRecord> getServicesByMember(int id) throws SQLException {
        return getServices(id, false);
    }

    public boolean insertService(ServiceRecord record) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Services VALUES (?, ?, ?, ?, ?, ?);");
        statement.setInt(1, record.providerID);
        statement.setInt(2, record.memberID);
        statement.setInt(3, record.serviceID);
        statement.setString(4, record.comments);
        statement.setObject(5, new Timestamp(record.currentDate.getTime()));
        statement.setDate(6, new Date(record.serviceDate.getTime()));
        return statement.execute();
    }
}
