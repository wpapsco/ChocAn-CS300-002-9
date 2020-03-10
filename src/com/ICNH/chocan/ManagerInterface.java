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
                    break;
                case 2:
                    memberReport();
                    break;
                case 3:
                    editProvider();
                    break;
                case 4:
                    editMember();
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
    // Return 0 for success, -1 for failure/user quits
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
            // TODO: fileOut.write the services info (date, provider.name, and service.name for each service)
            fileOut.close();
        } catch (IOException | SQLException e) {
            System.out.println("Error: Failed to generate report");
            return -1;
        }
        return 0;
    }

    // Add, remove, or update member information
    private void editMember() {
        Scanner in = new Scanner(System.in);
        int selection = 0;
        Utilities.clearConsole();
        System.out.println(
                "1. Add new member." +
                "\n2. Update current member." +
                "\n3. Delete member." +
                "\n4. Go back." +
                "\nMake a selection: ");

        // get valid user choice
        boolean valid = false;
        do {
            if(!in.hasNextInt()){
                System.out.println("Invalid selection.\n");
                in.next();
                continue;
            }
            selection = in.nextInt();
            if(selection <= 0 || selection > 4){
                System.out.println("Invalid selection.\n");
                in.next();
            } else {
                valid = true;
            }
        } while(!valid);
        // enact user choice
        // TODO: Finish this
        switch(selection){
            case(1):
                // add member
                addnewMember();
                return;
            case(2):
                // edit member
                editMemberInfo(); // Incomplete
                return;
            case(3):
                // delete member
                deleteMember(); // Hasn't been written yet
                return;
            case(4): // fall through
            default: // fall through
        }
    }


    private void addnewMember(){
        Scanner sc = new Scanner(System.in);
        int memberID, memberStatus = -99;
        String name, address, city, zip, state;

        // Loop until user enters reasonable member ID
        Utilities.clearConsole();
        do {
            do {
                System.out.print("Enter member ID to validate: ");
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
            //Needs to check if memberID is valid/exists because a new member needs an ID that doesn't already exist.
            try {
                if(database.validateMember(memberID) == -1){
                    System.out.println("Member ID " + memberID + "is available. ");
                    memberStatus = -1;
                }
            }
             catch(SQLException ex){
                System.out.println("Error: SQL Exception thrown");
            }
            if(memberStatus != -1){
                System.out.println("The member ID you entered is not available. Please try again.");
            }
        }while(memberStatus != -1); // Will keep looping until valid NEW member ID entered

        //MemberID is valid new ID, continues with new member information.
        do {
            System.out.println("Enter the new member's name: ");
            name = sc.next();
            System.out.println("Enter address: ");
            address = sc.next();
            System.out.println("Enter city: ");
            city = sc.next();
            System.out.println("Enter state: ");
            state = sc.next();
            System.out.println("Enter  zip: ");
            zip = sc.next();

            System.out.println(" You've entered the following information: ");
            System.out.println("Name:" + name);
            System.out.println("Address:" + address);
            System.out.println(" City:" +city + "State:"+ state + " Zip:" + zip);
            System.out.println("Is this information correct?");
        //Checks to make sure new info member is correct before creating new member
        }while(Utilities.confirm());

       // **** Add new member record unfinished, unsure at the moment of  adding new member correctly. ********
        MemberRecord record;
        record = new MemberRecord(memberID, name, 1, address, city, state, zip);

        try {
            if(database.insertMember(record)){
                System.out.println("Member created. ");
            }
        }
        catch(SQLException ex) {
            System.out.println("Error: SQL Exception thrown");
        }
    }

    private void editMemberInfo(){
        Scanner sc = new Scanner(System.in);
        int memberID, memberStatus = -99;
        String name, address, city, zip, state;

        // Loop until user enters reasonable member ID and exists in database
        Utilities.clearConsole();
        do {
            do {
                System.out.print("Enter member ID to validate: ");
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
            //Needs to check if memberID is valid/exists because a new member needs an ID that doesn't already exist.
            try {
                if(database.validateMember(memberID) == -1){
                    System.out.println("Member ID does not exist. ");
                    memberStatus = -1;
                }
            }
            catch(SQLException ex){
                System.out.println("Error: SQL Exception thrown");
            }
        }while(memberStatus == -1); // Will continue to have user enter ID until valid member ID is entered

        //Ready to call edit function that hasn't been written yet *********

        //database.editMember(memberID);
    }
    private void deleteMember(){

    }

    // Add, remove, or update provider records
    private void editProvider() {
        Scanner in = new Scanner(System.in);
        int selection = 0;
        Utilities.clearConsole();
        System.out.println(
                "1. Add new provider." +
                        "\n2. Update current provider." +
                        "\n3. Delete provider." +
                        "\n4. Go back." +
                        "\nMake a selection: ");

        // get valid user choice
        boolean valid = false;
        do {
            if(!in.hasNextInt()){
                System.out.println("Invalid selection.\n");
                in.next();
                continue;
            }
            selection = in.nextInt();
            if(selection <= 0 || selection > 4){
                System.out.println("Invalid selection.\n");
                in.next();
            } else {
                valid = true;
            }
        } while(!valid);
        // enact user choice
        // TODO: Finish this
        switch(selection){
            case(1):
                // add provider
                addNewProvider();
                return;
            case(2):
                // edit provider
                editproviderInfo(); // Incomplete
                return;
            case(3):
                // delete provider
                deleteProvider(); // hasn't been written yet
                return;
            case(4): // fall through
            default: // fall through
        }
    }

    private void addNewProvider(){
        Scanner sc = new Scanner(System.in);
        int providerID;
        String name, address, city, state, zip;
        int providerStatus = -99;

        // Loop until user enters reasonable member ID
        Utilities.clearConsole();
        do {
            do {
                System.out.print("Enter member ID to validate: ");
                String input = sc.nextLine();

                // provider ID must be a positive int
                try {
                    providerID= Integer.parseInt(input);
                    if (providerID <= 0) {
                        Utilities.clearConsole();
                        System.out.println("Invalid Number. Provider ID's are positive numerals.");
                        providerID = 0;
                    }
                } catch (NumberFormatException ex) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Provider ID's are positive numerals.");
                    providerID = 0;
                }
            } while (providerID  == 0);

            try {
                if(database.validateProvider(providerID ) == false){
                    System.out.println("Provider ID " + providerID  + "is available. ");
                    providerStatus = -1;
                }
            }
            catch(SQLException ex){
                System.out.println("Error: SQL Exception thrown");
            }
            if(providerStatus != -1){
                System.out.println("The provider ID you entered is not available. Please try again.");
            }
        }while(providerStatus != -1); // Will keep looping until valid NEW member ID entered


            //ProviderID is valid new ID, continues with new member information.
            do {
                System.out.println("Enter the new provider's name: ");
                name = sc.next();
                System.out.println("Enter address: ");
                address = sc.next();
                System.out.println("Enter city: ");
                city = sc.next();
                System.out.println("Enter state: ");
                state = sc.next();
                System.out.println("Enter  zip: ");
                zip = sc.next();

                System.out.println(" You've entered the following information: ");
                System.out.println("Name:" + name);
                System.out.println("Address:" + address);
                System.out.println(" City:" +city + "State:"+ state + " Zip:" + zip);
                System.out.println("Is this information correct?");
                //Checks to make sure new info member is correct before creating new member
            }while(Utilities.confirm() != false);
            //Needs to check if providerID is valid/exists because a new provider needs an ID that doesn't already exist.

        ProviderRecord record;
        record = new ProviderRecord(providerID, name, address, city, state, zip);
        try {
            if( database.insertProvider(record) ==true){
                System.out.println("Provider created. ");
            }
        }
        catch(SQLException ex){
            System.out.println("Error: SQL Exception thrown");
        }
        return;

    }
    private void editproviderInfo(){
        Scanner sc = new Scanner(System.in);
        int providerID, providerStatus = -99;
        //String name, address, city, zip, state;

        // Loop until user enters reasonable member ID and exists in database
        Utilities.clearConsole();
        do {
            do {
                System.out.print("Enter provider ID to validate: ");
                String input = sc.nextLine();

                // member ID must be a positive int
                try {
                    providerID = Integer.parseInt(input);
                    if (providerID <= 0) {
                        Utilities.clearConsole();
                        System.out.println("Invalid Number. Provider ID's are positive numerals.");
                        providerID = 0;
                    }
                } catch (NumberFormatException ex) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. Provider ID's are positive numerals.");
                    providerID = 0;
                }
            } while (providerID== 0);
            //Needs to check if memberID is valid/exists because a new member needs an ID that doesn't already exist.
            try {
                if(database.validateProvider(providerID) == false){
                    System.out.println("Provider ID does not exist. ");
                    providerStatus = -1;
                }
            }
            catch(SQLException ex){
                System.out.println("Error: SQL Exception thrown");
            }
        }while(providerStatus == -1); // Will continue to have user enter ID until valid provider ID is entered

        //Ready to call edit function that hasn't been written yet *********

        //database.editProvider();
    }

    public void deleteProvider(){

    }
}
