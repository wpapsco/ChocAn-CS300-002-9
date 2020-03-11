package com.ICNH.chocan;

import com.ICNH.chocan.records.FullServiceRecord;
import com.ICNH.chocan.records.MemberRecord;
import com.ICNH.chocan.records.ProviderRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;

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
    //report contains all services logged by the provider in the last 7 days
    public void providerReport() {
        int providerID = getValidProvider();
        ProviderRecord provider = new ProviderRecord();

        //if user cancelled
        if(providerID == -1)
            return;
        try {
            provider = database.getProviderRecord(providerID);
        } catch(SQLException e){
            e.printStackTrace();
        }

        try {
            // Member report must include member info (ProviderRecord) + (for each service provided) date, provider name, and service name
            BufferedWriter fileOut = new BufferedWriter(new FileWriter("reports/Provider" + providerID + "Report.txt"));
            fileOut.write("Provider Name: " + provider.name + "\nProvider Number: " + provider.ID + "\nAddress: " +
                    provider.address + ", " + provider.city + " " + provider.state + ", " + provider.zip);
            ArrayList<FullServiceRecord> services = database.getServicesByProvider(providerID);
            int listSize = services.size();
            FullServiceRecord[] records = services.toArray(new FullServiceRecord[listSize]);
            for(int i = 0; i < listSize; i++) {
                fileOut.write("Service: " + records[i].serviceInfo.name + "\nMember: " + records[i].member.name + "\nDate: "
                        + records[i].serviceDate.toString() + "\nFee: $" + records[i].serviceInfo.fee + "\nComments: " + records[i].comments + "\n\n");
            }
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create a member report and save to Member<memberID>Report.txt in reports directory
    private void memberReport() {
        int memberID = getValidMember();
        MemberRecord memberInfo = new MemberRecord();

        //if user cancelled
        if(memberID == -1)
            return;
        try {
            memberInfo = database.getMemberRecord(memberID);
        } catch(SQLException e){
            e.printStackTrace();
        }

        try {
            // Member report must include member info (MemberRecord) + (for each service provided) date, provider name, and service name
            BufferedWriter fileOut = new BufferedWriter(new FileWriter("reports/Member" + memberID + "Report.txt"));
            fileOut.write("Member Name: " + memberInfo.name + "\nMember Number: " + memberInfo.ID + "\nAddress: " +
                    memberInfo.address + ", " + memberInfo.city + " " + memberInfo.state + ", " + memberInfo.zip);
            ArrayList<FullServiceRecord> services = new ArrayList<>();
            services = database.getServicesByMember(memberID);
            int listSize = services.size();
            FullServiceRecord[] records = services.toArray(new FullServiceRecord[listSize]);
            for(int i = 0; i < listSize; i++) {
                fileOut.write("Service: " + records[i].serviceInfo.name + "\nProvider: " + records[i].provider.name + "\nDate: "
                        + records[i].serviceDate.toString() + "\nFee: $" + records[i].serviceInfo.fee + "\nComments: " + records[i].comments + "\n\n");
            }
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
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
        switch(selection){
            case(1):
                // add member
                addNewMember();
                return;
            case(2):
                // edit member
                editMemberInfo(); // Needs database helper func. and testing. otherwise complete
                return;
            case(3):
                // delete member
                deleteMember(); // Needs database helper func. and testing. otherwise complete
                return;
            case(4): // fall through
            default: // fall through
        }
    }


    private void addNewMember(){
        Scanner sc = new Scanner(System.in);
        int memberID, memberStatus = -99;
        String name, address, city, zip, state;

        // Loop until user enters reasonable member ID
        Utilities.clearConsole();
//        do {
//            do {
//                System.out.print("Enter member ID to validate: ");
//                String input = sc.nextLine();
//
//                // member ID must be a positive int
//                try {
//                    memberID = Integer.parseInt(input);
//                    if (memberID <= 0) {
//                        Utilities.clearConsole();
//                        System.out.println("Invalid Number. Member ID's are positive numerals.");
//                        memberID = 0;
//                    }
//                } catch (NumberFormatException ex) {
//                    Utilities.clearConsole();
//                    System.out.println("Invalid Number. Member ID's are positive numerals.");
//                    memberID = 0;
//                }
//            } while (memberID == 0);
//            //Needs to check if memberID is valid/exists because a new member needs an ID that doesn't already exist.
//            try {
//                if(database.validateMember(memberID) == -1){
//                    System.out.println("Member ID " + memberID + "is available. ");
//                    memberStatus = -1;
//                }
//            }
//             catch(SQLException ex){
//                System.out.println("Error: SQL Exception thrown");
//            }
//            if(memberStatus != -1){
//                System.out.println("The member ID you entered is not available. Please try again.");
//            }
//        }while(memberStatus != -1); // Will keep looping until valid NEW member ID entered

        //MemberID is valid new ID, continues with new member information.
        do {
            System.out.println("Enter the new member's name: ");
            name = sc.nextLine();
            System.out.println("Enter address: ");
            address = sc.nextLine();
            System.out.println("Enter city: ");
            city = sc.nextLine();
            System.out.println("Enter state: ");
            state = sc.nextLine();
            System.out.println("Enter zip: ");
            zip = sc.nextLine();

            System.out.println(" You've entered the following information: ");
            System.out.println("Name: " + name);
            System.out.println("Address: " + address);
            System.out.println("City: " +city + ", State: "+ state + ", Zip: " + zip);
            System.out.println("Is this information correct?");
            //Checks to make sure new info member is correct before creating new member
        }while(!Utilities.confirm());

        // **** Add new member record unfinished, unsure at the moment of  adding new member correctly. ********
        MemberRecord record = new MemberRecord(0, name, true, address, city, state, zip);

        try {
            int memberId = database.insertMember(record);
            System.out.println("Member (id " + memberId + ") created. ");
        }
        catch(SQLException ex) {
            System.out.println("Error: SQL Exception thrown");
        }
    }

    //change one or more fields for an existing provider
    private void editMemberInfo(){
        Scanner sc = new Scanner(System.in);
        int memberID = getValidMember();
        MemberRecord toChange = new MemberRecord();

        //user chose to return from getValidProvider
        if(memberID == -1)
            return;
        try {
            toChange = database.getMemberRecord(memberID);
        } catch (SQLException e){
            e.printStackTrace();
        }

        Utilities.clearConsole();
        System.out.println("Name: " + toChange.name + "\nAddress: " + toChange.address + ", " + toChange.city + " " + toChange.state + " " + toChange.zip + "\nMember ID: " + toChange.ID);
        System.out.println("Edit this Member?");
        if(Utilities.confirm()) {
            do {
                int selection = 0;
                Utilities.clearConsole();
                System.out.println(
                        "Which field would you like to change? " +
                                "\n1. Name" +
                                "\n2. Address" +
                                "\n3. City" +
                                "\n4. State" +
                                "\n5. Go back, aborting changes" +
                                "\n6. Submit changes" +
                                "\nMake a selection: ");

                // get valid user choice
                boolean valid = false;
                do {
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid selection.\n");
                        sc.next();
                        continue;
                    }
                    selection = sc.nextInt();
                    if (selection <= 0 || selection > 6) {
                        System.out.println("Invalid selection.\n");
                        sc.next();
                    } else {
                        valid = true;
                    }
                } while (!valid);
                // enact user choice

                sc.nextLine();
                switch (selection) {
                    case (1):
                        // change name
                        String newName;
                        do {
                            System.out.println("Enter the new name, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newName = sc.nextLine();
                            if(newName.length() > 45){ // cut off extra characters if too long
                                newName = newName.substring(0, 45);
                            }
                            System.out.println("Change " + toChange.name + " to " + newName + "?");
                        } while(!Utilities.confirm());
                        toChange.name = newName;
                        break;
                    case (2):
                        // change address
                        String newAddress;
                        do {
                            System.out.println("Enter the new street address, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newAddress = sc.nextLine();
                            if(newAddress.length() > 45){ // cut off extra characters if too long
                                newAddress = newAddress.substring(0, 45);
                            }
                            System.out.println("Change " + toChange.address + " to " + newAddress + "?");
                        } while(!Utilities.confirm());
                        toChange.address = newAddress;
                        break;
                    case (3):
                        // change city
                        String newCity;
                        do {
                            System.out.println("Enter the new city, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newCity = sc.nextLine();
                            if(newCity.length() > 45){ // cut off extra characters if too long
                                newCity = newCity.substring(0, 45);
                            }
                            System.out.println("Change " + toChange.city+ " to " + newCity + "?");
                        } while(!Utilities.confirm());
                        toChange.city = newCity;
                        break;
                    case (4): // change state
                        String newState;
                        do {
                            System.out.println("Enter the new state, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newState = sc.nextLine();
                            if(newState.length() > 2){ // cut off extra characters if too long
                                newState= newState.substring(0, 2);
                            }
                            System.out.println("Change " + toChange.state + " to " + newState + "?");
                        } while(!Utilities.confirm());
                        toChange.state = newState;
                        break;
                    case (5): // go back, aborting all changes
                        return;
                    case (6): // go back, aborting all changes
                        //Ready to call replace function that hasn't been written yet *********
                        //database.replaceProvider(providerID, toChange);
                        System.out.println("Changes saved! Press enter to continue.");
                        sc.nextLine();
                        return;
                    default: // fall through
                }
                System.out.println("Change another field?");
            } while (true);
        }
    }

    private void deleteMember(){
        Scanner sc = new Scanner(System.in);
        int memberID;
        MemberRecord toDelete = new MemberRecord();
        System.out.println("WARNING! Deleting a member will remove all services logged to the member from the system. Ensure all bills are paid before removal.");
        System.out.println("Press enter to continue.");
        sc.nextLine();

        memberID = getValidMember();
        if(memberID == -1)
            return;
        try {
            toDelete = database.getMemberRecord(memberID);
        } catch (SQLException e){
            e.printStackTrace();
        }

        Utilities.clearConsole();
        System.out.println("Name: " + toDelete.name + "\nAddress: " + toDelete.address + ", " + toDelete.city + " " + toDelete.state + " " + toDelete.zip + "\nMember ID: " + toDelete.ID);
        System.out.println("Delete this member?");
        if(Utilities.confirm()){
            // TODO call database remove method for member. Method should also remove any services that link to the member.
            //database.removeMember(memberID);
            System.out.println("Member removed.");
        }
        else {
            System.out.println("Aborted");
        }
        System.out.println("Press enter to continue.");
        sc.nextLine();
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
        switch(selection){
            case(1):
                // add provider
                addNewProvider();
                return;
            case(2):
                // edit provider
                editProviderInfo(); // Needs database helper func. and testing. otherwise complete
                return;
            case(3):
                // delete provider
                deleteProvider(); // Needs database helper func. and testing. otherwise complete
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

        // Loop until user enters reasonable provider ID
//        Utilities.clearConsole();
//        do {
//            do {
//                System.out.print("Enter provider ID to validate, or enter 'x' to return: ");
//                if(sc.hasNext("x")){
//                    return;
//                }
//                String input = sc.nextLine();
//
//                // provider ID must be a positive int
//                try {
//                    providerID= Integer.parseInt(input);
//                    if (providerID <= 0) {
//                        Utilities.clearConsole();
//                        System.out.println("Invalid Number. Provider ID's are positive numerals.");
//                        providerID = 0;
//                    }
//                } catch (NumberFormatException ex) {
//                    Utilities.clearConsole();
//                    System.out.println("Invalid Number. Provider ID's are positive numerals.");
//                    providerID = 0;
//                }
//            } while (providerID  == 0);
//
//            try {
//                if(database.validateProvider(providerID)){
//                    System.out.println("Provider ID " + providerID  + "is available. ");
//                    providerStatus = -1;
//                }
//            }
//            catch(SQLException ex){
//                System.out.println("Error: SQL Exception thrown");
//            }
//            if(providerStatus != -1){
//                System.out.println("The provider ID you entered is not available. Please try again.");
//            }
//        }while(providerStatus != -1); // Will keep looping until valid NEW member ID entered


        //ProviderID is valid new ID, continues with new member information.
        do {
            System.out.println("Enter the new provider's name: ");
            name = sc.nextLine();
            System.out.println("Enter address: ");
            address = sc.nextLine();
            System.out.println("Enter city: ");
            city = sc.nextLine();
            System.out.println("Enter state: ");
            state = sc.nextLine();
            System.out.println("Enter zip: ");
            zip = sc.nextLine();

            System.out.println(" You've entered the following information: ");
            System.out.println("Name: " + name);
            System.out.println("Address: " + address);
            System.out.println("City: " + city + ", State: "+ state + ", Zip: " + zip);
            System.out.println("Is this information correct?");
            //Checks to make sure new info member is correct before creating new member
        }while(!Utilities.confirm());

        ProviderRecord record = new ProviderRecord(0, name, address, city, state, zip);
        try {
            int providerId = database.insertProvider(record);
            System.out.println("Provider (id " + providerId + ") created. Press enter to continue.");
            sc.nextLine();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    //change one or more fields for an existing provider
    private void editProviderInfo(){
        Scanner sc = new Scanner(System.in);
        int providerID = getValidProvider();
        ProviderRecord toChange = new ProviderRecord();

        //user chose to return from getValidProvider
        if(providerID == -1)
            return;
        try {
            toChange = database.getProviderRecord(providerID);
        } catch (SQLException e){
            e.printStackTrace();
        }

        Utilities.clearConsole();
        System.out.println("Name: " + toChange.name + "\nAddress: " + toChange.address + ", " + toChange.city + " " + toChange.state + " " + toChange.zip + "\nProvider ID: " + toChange.ID);
        System.out.println("Edit this provider?");
        if(Utilities.confirm()) {
            do {
                int selection = 0;
                Utilities.clearConsole();
                System.out.println(
                        "Which field would you like to change? " +
                                "\n1. Name" +
                                "\n2. Address" +
                                "\n3. City" +
                                "\n4. State" +
                                "\n5. Go back, aborting changes" +
                                "\n6. Submit changes" +
                                "\nMake a selection: ");

                // get valid user choice
                boolean valid = false;
                do {
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid selection.\n");
                        sc.next();
                        continue;
                    }
                    selection = sc.nextInt();
                    if (selection <= 0 || selection > 6) {
                        System.out.println("Invalid selection.\n");
                        sc.next();
                    } else {
                        valid = true;
                    }
                } while (!valid);
                // enact user choice
                sc.nextLine();
                switch (selection) {
                    case (1):
                        // change name
                        String newName;
                        do {
                            System.out.println("Enter the new name, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newName = sc.nextLine();
                            if(newName.length() > 45){ // cut off extra characters if too long
                                newName = newName.substring(0, 45);
                            }
                            System.out.println("Change " + toChange.name + " to " + newName + "?");
                        } while(!Utilities.confirm());
                        toChange.name = newName;
                        break;
                    case (2):
                        // change address
                        String newAddress;
                        do {
                            System.out.println("Enter the new street address, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newAddress = sc.nextLine();
                            if(newAddress.length() > 45){ // cut off extra characters if too long
                                newAddress = newAddress.substring(0, 45);
                            }
                            System.out.println("Change " + toChange.address + " to " + newAddress + "?");
                        } while(!Utilities.confirm());
                        toChange.address = newAddress;
                        break;
                    case (3):
                        // change city
                        String newCity;
                        do {
                            System.out.println("Enter the new city, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newCity = sc.nextLine();
                            if(newCity.length() > 45){ // cut off extra characters if too long
                                newCity = newCity.substring(0, 45);
                            }
                            System.out.println("Change " + toChange.city+ " to " + newCity + "?");
                        } while(!Utilities.confirm());
                        toChange.city = newCity;
                        break;
                    case (4): // change state
                        String newState;
                        do {
                            System.out.println("Enter the new state, or enter 'x' to cancel: ");
                            if(sc.hasNext("x"))
                                return;
                            newState = sc.nextLine();
                            if(newState.length() > 2){ // cut off extra characters if too long
                                newState= newState.substring(0, 2);
                            }
                            System.out.println("Change " + toChange.state + " to " + newState + "?");
                        } while(!Utilities.confirm());
                        toChange.state = newState;
                        break;
                    case (5): // go back, aborting all changes
                        return;
                    case (6): // go back, aborting all changes
                        //Ready to call replace function that hasn't been written yet *********
                        //database.replaceProvider(providerID, toChange);
                        System.out.println("Changes saved! Press enter to continue.");
                        sc.nextLine();
                        return;
                    default: // fall through
                }
                System.out.println("Change another field?");
            } while (true);
        }
    }

    private void deleteProvider(){
        Scanner sc = new Scanner(System.in);
        int providerID;
        ProviderRecord toDelete = new ProviderRecord();
        System.out.println("WARNING! Deleting a provider will remove all services logged by the provider from the system. Ensure all bills are paid before removal.");
        System.out.println("Press enter to continue.");
        sc.nextLine();

        providerID = getValidProvider();
        if(providerID == -1)
            return;
        try {
            toDelete = database.getProviderRecord(providerID);
        } catch (SQLException e){
            e.printStackTrace();
        }

        Utilities.clearConsole();
        System.out.println("Name: " + toDelete.name + "\nAddress: " + toDelete.address + ", " + toDelete.city + " " + toDelete.state + " " + toDelete.zip + "\nProvider ID: " + toDelete.ID);
        System.out.println("Delete this provider?");
        if(Utilities.confirm()){
            // TODO call database remove method for provider. Method should also remove any services that link to the provider.
            //database.removeProvider(providerID);
            System.out.println("Provider removed.");
        }
        else {
            System.out.println("Aborted");
        }
        System.out.println("Press enter to continue.");
        sc.nextLine();
    }

    //gets valid provider ID from the user and returns the ID, or returns -1 if user cancels.
    private int getValidProvider(){
        Scanner sc = new Scanner(System.in);
        int providerID, providerStatus = -1;

        Utilities.clearConsole();
        do {
            do {
                System.out.print("Enter provider ID, or enter 'x' to return: ");
                if(sc.hasNext("x"))
                    return -1;
                String input = sc.nextLine();

                // provider ID must be a positive int
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
            } while (providerID == 0);

            try {
                if(database.validateProvider(providerID)){
                    providerStatus = 0;
                }
                else {
                    Utilities.clearConsole();
                    System.out.println("Provider ID not recognized.");
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }while(providerStatus == -1); // Will continue to have user enter ID until valid provider ID is entered

        return providerID;
    }
    //gets valid provider ID from the user and returns the ID, or returns -1 if user cancels.
    private int getValidMember(){
        Scanner sc = new Scanner(System.in);
        int memberID, memberStatus = -1;

        Utilities.clearConsole();
        do {
            do {
                System.out.print("Enter member ID, or enter 'x' to return: ");
                if(sc.hasNext("x"))
                    return -1;
                String input = sc.nextLine();

                // member ID must be a positive int
                try {
                    memberID = Integer.parseInt(input);
                    if (memberID <= 0) {
                        Utilities.clearConsole();
                        System.out.println("Invalid Number. member ID's are positive numerals.");
                        memberID = 0;
                    }
                } catch (NumberFormatException ex) {
                    Utilities.clearConsole();
                    System.out.println("Invalid Number. member ID's are positive numerals.");
                    memberID = 0;
                }
            } while (memberID == 0);

            try {
                if(database.validateMember(memberID) != -1){
                    memberStatus = 0;
                }
                else {
                    Utilities.clearConsole();
                    System.out.println("Member ID not recognized.");
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }while(memberStatus == -1); // Will continue to have user enter ID until valid provider ID is entered

        return memberID;
    }
}
