package com.ICNH.chocan;//This file defines the com.ICNH.ChocAn.DatabaseInterface class which interfaces with the
//data repository

import com.ICNH.chocan.records.FullServiceRecord;
import com.ICNH.chocan.records.MemberRecord;
import com.ICNH.chocan.records.ProviderRecord;
import com.ICNH.chocan.records.ServiceInfoRecord;
import javafx.embed.swt.SWTFXUtils;

import java.sql.*;
import java.util.ArrayList;

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
        int id = results.getInt("id");
        int fee = results.getInt("fee");
        String name = results.getString("name");
        String description = results.getString("description");
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
        int id = results.getInt("id");
        String name = results.getString("name");
        String address = results.getString("address");
        String city = results.getString("city");
        String state = results.getString("state");
        String zip = results.getString("zip");
        boolean valid = results.getInt("is_valid") == 1;
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
        int id = results.getInt("id");
        String name = results.getString("name");
        String address = results.getString("address");
        String city = results.getString("city");
        String state = results.getString("state");
        String zip = results.getString("zip");
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
        return getMemberRecord(id) != null;
    }

    //feel free to stub out more methods like this where you need them, I will implement them
    public ArrayList<FullServiceRecord> getServicesByProvider(int id) throws SQLException {
        return new ArrayList<FullServiceRecord>(); //TODO
    }

    public ArrayList<FullServiceRecord> getServicesByMember(int id) throws SQLException {
        return new ArrayList<FullServiceRecord>(); //TODO
    }
}
