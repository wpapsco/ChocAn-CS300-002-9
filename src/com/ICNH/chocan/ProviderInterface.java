package com.ICNH.chocan;

import com.ICNH.chocan.records.ServiceRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
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
        int selection = 0;

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
                    if (validMember == -1)
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
                    serviceReport();
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
                "4. Generate service report(last 7 days).\n" +
                "5. Logout.\n" +
                "Make a selection: ");
    }

    // Check member ID number for status (valid: member ID is returned, suspended: -2, or invalid: -1)
    private int checkID() {
        Scanner sc = new Scanner(System.in);
        int memberID = 0;

        // Loop until user enters reasonable member ID
        Utilities.clearConsole();
        do {
            System.out.print("Enter member ID to validate: ");
            String input = sc.nextLine();

            // member ID must be a positive int
            try {
                memberID = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                Utilities.clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                memberID = 0;
            }
            if (memberID <= 0) {
                Utilities.clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                memberID = 0;
            }
        } while (memberID == 0);

        /*
        // Lookup member in database, return status or no match
        int databaseRetValue = callDatabaseFunction(memberID);
        switch(databaseRetValue) {
            case 1:
                return 1;
                break;
            case 2:
                System.out.println("Member suspended.");
                return 2;
                break;
            case 3: return 3;
                break;
        }*/
        //return ID if valid, this return value is a placeholder until the database is integrated.
        return memberID;
    }

    //log a service to the database
    private boolean logService() {
        int member = checkID();
        //member ID not recognized
        if (member == -1) {
            Utilities.clearConsole();
            System.out.println("Member ID not recognized.");
            return false;
        }

        //Suspended member ID
        else if (member == -2) {
            Utilities.clearConsole();
            System.out.println("Member is suspended.");
            return false;
        }

        //valid member ID
        else {
            Utilities.clearConsole();
            System.out.println("Validated.");
            ServiceRecord log = new ServiceRecord();
            Scanner in = new Scanner(System.in);
            log.memberID = member;
            log.providerID = ID;
            int serviceID = 0;
            int month = 0;
            int day = 0;
            int year = 0;


            // please check out https://mkyong.com/java/how-to-convert-string-to-date-java/ probably a lot easier and less error-prone
            // will also save this file from 67 lines of code to read -will

            // got bored and did it myself, I highly recommend using this piece of code -will

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            formatter.setLenient(false); //disallows dates like 13-22-2019 -> 1/22/2020
            Date theDate = null;
            while (theDate == null) {
                System.out.print("Enter the date that the service was provided in MM-DD-YYYY format, or enter \"x\" to abort: ");
                if (in.hasNext("x")) return false;
                try {
                    //read and parse date
                    theDate = formatter.parse(in.nextLine());
                } catch (ParseException e) {
                    //format is incorrect, so we re-start the loop
                    System.out.println("Invalid input. Format may be incorrect.");
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

            //get comments on the service from user, up to 100 chars.
            return true;
        }
    }

    // asks the user for the name of a service and prints the service name, code, and fee if found.
    private boolean checkProviderDirectory() {
        return true;
    }

    //takes a service code as an argument and prints the service name then returns true if found. Else returns false
    private boolean checkProviderDirectory(int serviceID) {
        return true;
    }

    //generates service report for the past 7 days
    //returns true on success
    private boolean serviceReport() {
        return true;
    }
}
