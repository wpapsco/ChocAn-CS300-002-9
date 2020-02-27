package com.ICNH.chocan.records;

public class ProviderRecord {
    public int ID;
    public String name;
    public String address;
    public String city;
    public String state;
    public String zip;

    public ProviderRecord() {}

    public ProviderRecord(int ID, String name, String address, String city, String state, String zip) {
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}