package com.ICNH.chocan.records;

public class ServiceInfoRecord {
    public int id;
    public String name;
    public String description;
    public int fee;

    public ServiceInfoRecord() {}

    public ServiceInfoRecord(int id, String name, String description, int fee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "id: " + id + " name: " + name + " description: " + description + " fee: " + fee;
    }
}
