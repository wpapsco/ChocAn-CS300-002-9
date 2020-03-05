package com.ICNH.chocan.records;

import java.util.Calendar;
import java.util.Date;

//Holds data for an instance of a service
public class ServiceRecord {
    public int serviceID;
    public int memberID;
    public int providerID;
    public String comments;
    public Date serviceDate;
    public Date currentDate;

    public ServiceRecord() {
    }

    public ServiceRecord(int serviceID, int memberID, int providerID, String comments, Date serviceDate) {
        currentDate = new Date();
        this.serviceID = serviceID;
        this.memberID = memberID;
        this.comments = comments;
        this.serviceDate = serviceDate;
    }
}
