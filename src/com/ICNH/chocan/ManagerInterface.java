package com.ICNH.chocan;

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

    // Create a provider report and save to ProviderReport.txt in reports directory
    public int providerReport() {
        int providerID;
        Scanner in = new Scanner(System.in);
        // Loop until user enters a valid provider ID
        Utilities.clearConsole();
        do {
            System.out.print("Enter member ID to validate: ");
            String input = in.nextLine();

            // member ID must be a positive int
            try {
                providerID = Integer.parseInt(input);
                if (providerID <= 0) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Member ID's are positive numerals.");
                    providerID = 0;
                }
            } catch (NumberFormatException ex) {
                Utilities.clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                providerID = 0;
            }
            // Check if provider ID is in database
            try {
                if(providerID != 0 && database.validateProvider(providerID) == false){
                    System.out.println("Provider ID " + providerID + " not found.");
                }
            }
            catch(SQLException ex){
                System.out.println("Error: SQL Exception thrown");
            }
        } while (providerID == 0);

        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter("reports/ProviderReport.txt"));
            ProviderRecord providerInfo = database.getProviderRecord(providerID);
            fileOut.write("Provider Name:    " + providerInfo.name + "Provider Number:  " + providerInfo.ID +
                    "Provider Address: " + providerInfo.address + "\n\t\t  " + providerInfo.city +
                    ", " + providerInfo.state + " " + providerInfo.zip);

            fileOut.write("Hello World!"); // TODO: Write services + total number of consultations + total fee for week
            fileOut.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Create a member report and save to MemberReport.txt in reports directory
    private int memberReport() {
        // TODO: implement this
        System.out.println("Member report not implemented yet!");
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
