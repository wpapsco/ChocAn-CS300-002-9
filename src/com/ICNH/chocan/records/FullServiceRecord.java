package com.ICNH.chocan.records;

import java.sql.Date;
import java.util.Calendar;

public class FullServiceRecord {
    public MemberRecord member;
    public ProviderRecord provider;
    public ServiceInfoRecord serviceInfo;
    public String comments;
    public Date currentDate;
    public Date serviceDate;

    @Override
    public String toString() {
//        currentDate.
        return member.toString() + "\n" +
                provider.toString() + "\n" +
                serviceInfo.toString() + "\n" +
                "comments: " + comments + " logged date: " + currentDate.toString() + " service date: " + serviceDate.toString();
    }
}
