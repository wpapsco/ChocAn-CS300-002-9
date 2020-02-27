package com.ICNH.chocan.records;

import java.util.Calendar;

//Holds data for an instance of a service
public class ServiceRecord {
    public int serviceID;
    public int memberID;
    public int providerID;
    public String comments;
    public Calendar serviceDate;
    public Calendar currentDate;

    public ServiceRecord() {
    }

    public ServiceRecord(int serviceID, int memberID, int providerID, String comments, Calendar serviceDate) {
        currentDate = Calendar.getInstance();
        this.serviceID = serviceID;
        this.memberID = memberID;
        this.comments = comments;
        this.serviceDate = serviceDate;
    }
}
