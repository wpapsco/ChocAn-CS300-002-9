import java.util.Calendar;

//Holds data for an instance of a service
class ServiceRecord {
    int serviceID;
    int memberID;
    int providerID;
    double fee;
    String comments;
    Calendar dateProvided;
    Calendar dateLogged;

    ServiceRecord(){dateProvided = Calendar.getInstance(); dateLogged = Calendar.getInstance();}
}
