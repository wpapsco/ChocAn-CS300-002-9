package com.ICNH.chocan;

import com.ICNH.chocan.records.MemberRecord;
import com.ICNH.chocan.records.ProviderRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class ManagerInterface {
    private DatabaseInterface database;

    ManagerInterface(DatabaseInterface database) {
        this.database = database;
    }

    //Menu interface
    public void menu() {
        Scanner userIn = new Scanner(System.in);
        int selection = 0;

        while (true) {
            Utilities.clearConsole();
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
                // TODO: still need to catch and process return values
                case 1:
                    providerReport();
                    valid = true;
                    break;
                case 2:
                    memberReport();
                    valid = true;
                    break;
                case 3:
                    editProvider();
                    valid = true;
                    break;
                case 4:
                    editMember();
                    valid = true;
                    break;
                case 5:
                    return;
                //should never reach default, if we do, something went wrong
                default:
                    assert false;
            }
        }
    }

    //print manager options menu to System.out
    private void printMenu() {
        System.out.println(
                "1. Compile a service report for each provider.\n" +
                        "2. Compile a service report for each member.\n" +
                        "3. Add, remove, or update provider info.\n" +
                        "4. Add, remove, or update member info.\n" +
                        "5. Logout.\n" +
                        "Make a selection: ");
    }

    // Create a provider report and save to Provider<providerID>Report.txt in reports directory
    public int providerReport() {
        int providerID;
        Scanner in = new Scanner(System.in);
        // Loop until user enters a valid provider ID that exists in the database
        Utilities.clearConsole();
        do {
            System.out.print("Enter member ID to validate: ");
            if(in.hasNext("x")){
                return -1;
            }
            String input = in.nextLine();
            // member ID must be a positive int
            try {
                providerID = Integer.parseInt(input);
                if (providerID <= 0) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Member ID's are positive numerals.");
                    providerID = 0;
                    continue;
                }
            } catch (NumberFormatException ex) {
                Utilities.clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                providerID = 0;
                continue;
            }
            // Check if provider ID is in database
            try {
                if(!database.validateProvider(providerID)){
                    System.out.println("Provider ID " + providerID + " not found.");
                }
            }
            catch(SQLException ex){
                System.out.println("Error: SQL Exception thrown");
                return -1;
            }
        } while (providerID == 0);

        // Write info to a provider-unique text file
        try {
            // Provider report must include provider info (ProviderRecord), all services for the past week,
            // total number of consultations for that week, and total fees for that week
            BufferedWriter fileOut = new BufferedWriter(new FileWriter("reports/Provider" + providerID + "Report.txt"));
            ProviderRecord providerInfo = database.getProviderRecord(providerID);
            fileOut.write("Provider Name:    " + providerInfo.name + "\nProvider Number:  " + providerInfo.ID +
                    "\nAddress:          " + providerInfo.address + "\n\t\t  " + providerInfo.city +
                    ", " + providerInfo.state + " " + providerInfo.zip);

            // TODO: fileOut.write services + total number of consultations + total fee for week. Requires new database functionality
            fileOut.close();
        } catch (IOException | SQLException e) {
            System.out.println("Error: Failed to generate report");
            return -1;
        }
        return 0;
    }

    // Create a member report and save to Member<memberID>Report.txt in reports directory
    // Return 0 for success, -1 for failure/user quits
    private int memberReport() {
        int memberID = 0;
        Scanner in = new Scanner(System.in);
        // Loop until user enters a valid member ID that exists in the database
        Utilities.clearConsole();
        do {
            System.out.print("Enter member ID to validate or \"x\" to quit: ");
            if(in.hasNext("x")){
                return -1;
            }
            String input = in.nextLine();
            try {

                memberID = Integer.parseInt(input);
                if (memberID <= 0) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Member ID's are positive numerals.");
                    memberID = 0;
                    continue;
                }
            } catch (NumberFormatException ex) {
                Utilities.clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                memberID = 0;
                continue;
            }
            // Check if member ID is in database
            try {
                if(database.validateMember(memberID) == -1){
                    System.out.println("Member ID " + memberID + " not found.");
                }
            }
            catch(SQLException ex){
                System.out.println("Error: SQL Exception thrown");
                return -1;
            }
        } while (memberID == 0);

        try {
            // Member report must include member info (MemberRecord) + (for each service provided) date, provider name, and service name
            BufferedWriter fileOut = new BufferedWriter(new FileWriter("reports/Member" + memberID + "Report.txt"));
            MemberRecord memberInfo = database.getMemberRecord(memberID);
            fileOut.write("Member Name:      " + memberInfo.name + "\nMember Number:    " + memberInfo.ID + "\nAddress:          " +
                    memberInfo.address + "\n\t\t  " + memberInfo.city + ", " + memberInfo.state + " " + memberInfo.zip);
            fileOut.close();
        } catch (IOException | SQLException e) {
            System.out.println("Error: Failed to generate report");
            return -1;
        }
        return 0;
    }

    // Add, remove, or update member information
    private int editMember() {
        // TODO: implement this
        System.out.println("Edit member not implemented yet!");
        return 0;
    }

    // Add, remove, or update provider records
    private int editProvider() {
        // TODO: implement this
        System.out.println("Edit provider not implemented yet!");
        return 0;
    }
}
