package com.ICNH.chocan;

import com.ICNH.chocan.records.ServiceInfoRecord;
import com.ICNH.chocan.records.ServiceRecord;
import com.mysql.cj.result.SqlDateValueFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

// Assumptions: - 0 is an invalid member ID
//              - member IDs are always positive integers

public class ProviderInterface {
    private DatabaseInterface database;
    private int ID;

    ProviderInterface(DatabaseInterface database, int ID) {
        this.database = database;
        this.ID = ID;
    }

    //Menu driver
    public void menu() {
        Scanner userIn = new Scanner(System.in);
        int selection;

        Utilities.clearConsole();
        while (true) {
            //validate user selection
            boolean valid = false;
            do {
                printMenu();
                while (!userIn.hasNextInt()) {
                    Utilities.clearConsole();
                    System.out.println("Invalid selection.\n");
                    userIn.next();
                    printMenu();
                }
                selection = userIn.nextInt();
                if (selection >= 1 && selection <= 5)
                    valid = true;
                else {
                    Utilities.clearConsole();
                    System.out.println("Invalid selection.\n");
                }
            } while (!valid);

            //process user selection
            switch (selection) {
                //still need to catch and process return values
                case 1:
                    int validMember = checkID();
                    if (validMember == -1 || validMember == 0)
                        System.out.println("Member ID not recognized.");
                    else if (validMember == -2)
                        System.out.println("Suspended.");
                    else
                        System.out.println("Validated.");
                    break;
                case 2:
                    if (logService()) {
                        Utilities.clearConsole();
                        System.out.println("\nService recorded.");
                    } else {
                        System.out.println("\nAborted service record.");
                    }
                    break;
                case 3:
                    checkProviderDirectory();
                    break;
                case 4:
                    if(!generateDirectoryReport()){
                        System.out.println("Unable to generate service directory");
                    }
                    break;
                case 5:
                    return;
                //should never reach default, if we do, something went wrong
                default:
                    assert false;
            }
        }
    }

    //print provider options menu to System.out
    private void printMenu() {
        System.out.println("1. Check member status.\n" +
                "2. Log a service.\n" +
                "3. Lookup service by name.\n" +
                "4. Generate service directory.\n" +
                "5. Logout.\n" +
                "Make a selection: ");
    }

    // Check member ID number for status (valid: member ID is returned, suspended: -2, not found: -1, user quits: 0)
    int checkID() {
        Scanner sc = new Scanner(System.in);
        int memberID;

        // Loop until user enters reasonable member ID
        Utilities.clearConsole();
        do {
            System.out.print("Enter member ID to validate, or enter 'x' to return: ");
                if(sc.hasNext("x")){
                    return 0;
                }
            String input = sc.nextLine();

            // member ID must be a positive int
            try {
                memberID = Integer.parseInt(input);
                if (memberID <= 0) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Member ID's are positive numerals.");
                    memberID = 0;
                }
            } catch (NumberFormatException ex) {
                Utilities.clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                memberID = 0;
            }
        } while (memberID == 0);

        // Lookup member in database, return status or no match
        try {
            int memberStatus = database.validateMember(memberID);
            switch (memberStatus) {
                case 1: // valid ID
                    return memberID;
                case 0: // suspended ID
                    return -2;
                case -1: // ID not found
                    return -1;
            }
        }
        catch(SQLException ex){
            System.out.println("Error: SQL Exception thrown");
        }
        return 0;
    }

    //log a service to the database
    boolean logService() {
        int member = checkID();
        //member ID not recognized
        if (member == -1 || member == 0) {
            Utilities.clearConsole();
            System.out.println("Member ID not recognized.");
            return false;
        }

        //Suspended member ID
        if (member == -2) {
            Utilities.clearConsole();
            System.out.println("Member is suspended.");
            return false;
        }

        //valid member ID
        Utilities.clearConsole();
        System.out.println("Validated.");
        ServiceRecord log = new ServiceRecord();
        Scanner in = new Scanner(System.in);
        log.memberID = member;
        log.providerID = ID;
        int serviceID;

        // please check out https://mkyong.com/java/how-to-convert-string-to-date-java/ probably a lot easier and less error-prone
        // will also save this file from 67 lines of code to read -will

        // got bored and did it myself, I highly recommend using this piece of code -will

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        formatter.setLenient(false); //disallows dates like 13-22-2019 -> 1/22/2020
        Date theDate = null;
        while (theDate == null) {
            System.out.print("Enter the date that the service was provided in MM-DD-YYYY format, or enter \"x\" to abort: ");
            // abort if they type x
            if (in.hasNext("x")) return false;
            try {
                //read and parse date
                theDate = formatter.parse(in.nextLine());
            } catch (ParseException e) {
                //format is incorrect, so we re-start the loop
                System.out.println("Invalid input. Format may be incorrect.");
                theDate = null;
                continue;
            }
            //check if date is in the future
            if (theDate.after(new Date())) {
                System.out.println("Date may not be in the future.");
                theDate = null;
                continue;
            }
            //confirm the date is correct
            System.out.println("Confirm that the following date is the date this service was provided: " + formatter.format(theDate));
            if (!Utilities.confirm()) {
                theDate = null;
            }
            log.serviceDate = theDate;
        }

        //get service code
        boolean gettingInput = true;
        String input = null;
        Utilities.clearConsole();
        while (gettingInput) {
            do {
                boolean done = false;
                while (!done) {
                    System.out.print("Enter the service code to continue, \"x\" to abort, or \"s\" to search for a service in the provider service directory: ");
                    if (in.hasNext("x"))
                        return false;
                    else if (in.hasNext("s")) {
                        in.nextLine();
                        while (!checkProviderDirectory()) {
                            System.out.print("A service with that name could not be found. Enter \"x\" to go back or any other key to search the directory again: ");
                            if (in.hasNext("x")) {
                                in.nextLine();
                                break;
                            } else {
                                in.nextLine();
                            }
                        }
                    } else {
                        //if(in.hasNext()) {
                        input = in.nextLine();
                        done = true;
                    }
                }

                // service code ID must be a positive int
                try {
                    serviceID = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Service codes are positive numerals.");
                    serviceID = 0;
                }
                if (serviceID <= 0) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Service codes are positive numerals.");
                    serviceID = 0;
                }
            } while (serviceID == 0);

            //check with database interface if service code is valid, if so, the database should print the service info and return true.
            if (checkProviderDirectory(serviceID)) {
                System.out.print("\nWas this the service provided? (Y/N): ");
                while (!in.hasNext("Y") && !in.hasNext("y") && !in.hasNext("N") && !in.hasNext("n")) {
                    in.nextLine();
                    System.out.print("\nPlease enter Y or N for yes or no: ");
                }
                if (in.hasNext("Y") || in.hasNext("y")) {
                    log.serviceID = serviceID;
                    gettingInput = false;
                    in.nextLine();
                } else if (in.hasNext("N") || in.hasNext("n")) {
                    Utilities.clearConsole();
                    in.nextLine();
                }
            } else {
                Utilities.clearConsole();
                System.out.println("Service code " + serviceID + " not found in directory.");
                serviceID = 0;  //set serviceID back to 0 when we loop through again
            }
        }   //Finished getting service code

        // get service comment
        System.out.print("Enter service comment (up to 100 characters): ");
        String buffer = in.nextLine();
        if(buffer.length() > 100){ // cut off extra characters if too long
            log.comments = buffer.substring(0, 100);
        } else {
            log.comments = buffer;
        }

        try{
            database.insertService(log);
        } catch(SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    // asks the user for the name of a service and prints the service name, code, and fee if found.
    boolean checkProviderDirectory() {
        // check out DatabaseInterface.getServicesByName
        Scanner sc = new Scanner(System.in);
        String service;
        ArrayList<ServiceInfoRecord> records = new ArrayList<>();

        Utilities.clearConsole();
        System.out.print("Enter the name of the service to search for: ");
        service = sc.nextLine();

        try {
            records = database.getServicesByName(service);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(records.isEmpty()){
            System.out.println("No results found matching: " + service + ". \nPress enter to return.");
            sc.nextLine();
            return false;
        }
        int listSize = records.size();
        ServiceInfoRecord[] matches = records.toArray(new ServiceInfoRecord[listSize]);
        for(int i = 0; i < listSize; i++) {
            System.out.println("Service: " + matches[i].name + "\nService code: " + matches[i].id + "\nDescription: " + matches[i].description + "\nFee: " + matches[i].fee + "\n\n");
        }
        System.out.println(listSize + " result(s) found for: " + service + ". \nPress enter to return.");
        sc.nextLine();
        return true;
    }

    //takes a service code as an argument and prints the service name then returns true if found. Else returns false
    boolean checkProviderDirectory(int serviceID) {
        try {
            ServiceInfoRecord service = database.getServiceInfo(serviceID);
            if(service == null) return false;
            System.out.println("Service Name: " + service.name);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // fails when SQL exception is thrown
    }

    // create "a Provider Directory, an alphabetically ordered list of service names and corresponding service codes and fees"
    // and save to Provider<providerID>Directory.txt> in reports directory
    private boolean generateDirectoryReport() {
        // check out DatabaseInterface.getServiceInfos
        try {
            ArrayList<ServiceInfoRecord> recordList = database.getServiceInfos();
            int listSize = recordList.size();
            ServiceInfoRecord[] records = recordList.toArray(new ServiceInfoRecord[listSize]);
            BufferedWriter fileOut = new BufferedWriter(new FileWriter("reports/Provider" + ID + "Directory.txt"));
            fileOut.write("Provider: " + ID + "\n\n");
            for(int i = 0; i < listSize; i++) {
                fileOut.write("Service: " + records[i].name + "\nService code: " + records[i].id + "\nDescription: " + records[i].description + "\nFee: " + records[i].fee + "\n\n");
            }
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utilities.clearConsole();
        return true;
    }
}
