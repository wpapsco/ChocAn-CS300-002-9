package com.ICNH.chocan.records;

public class MemberRecord {
    public int ID;
    public String name;
    public boolean valid; //false if suspended, true if valid
    public String address;
    public String city;
    public String state;
    public String zip;

    public MemberRecord(int memberID, String name, int i, String address, String city, String state, String zip) {
    }

    public MemberRecord(int ID, String name, boolean valid, String address, String city, String state, String zip) {
        this.ID = ID;
        this.name = name;
        this.valid = valid;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}