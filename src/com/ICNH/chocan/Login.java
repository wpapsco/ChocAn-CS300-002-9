package com.ICNH.chocan;

import java.util.Scanner;


public class Login {
    private static DatabaseInterface database;
    Login(){this.database = new DatabaseInterface(/*database constructor args here, if any*/);}

    //clears the standard output device by printing a bunch of newlines
    private static void clearConsole(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    //Main loops through the login menu - the outermost scope of the software
    public static void main(String[] args){
        while(true){
            clearConsole();
            System.out.println("Welcome to ChocAn!");
            loginMainMenu();
        }
    }

    private static void loginMainMenu(){
        Scanner loginInput = new Scanner(System.in);
        int loginKey;

        do {
            System.out.print("1. Provider login.\n" +
                    "2. Manager login.\n" +
                    "Make a selection: ");
            while(!loginInput.hasNextInt()){
                clearConsole();
                System.out.println("Invalid selection.\n");
                loginInput.next();
                System.out.print("1. Provider login.\n" +
                        "2. Manager login.\n" +
                        "Make a selection: ");
            }
            loginKey = loginInput.nextInt();
            if(loginKey != 1 && loginKey != 2){
                clearConsole();
                System.out.println("Invalid selection.\n");
            }
        } while (loginKey != 1 && loginKey != 2);

        switch(loginKey){
            case 1: ProviderLogin();
                break;
            case 2: ManagerLogin();
                break;
            default: break;
        }
    }

    private static void ProviderLogin(){
        Scanner loginID = new Scanner(System.in);
        int ID = 0;
        boolean found = false;

        do{
            System.out.print("Enter your provider ID to login, or \"x\" to return to main menu: ");
            while(!loginID.hasNextInt()){
                if(loginID.hasNext("x"))
                    return;
                clearConsole();
                System.out.println("Invalid ID, please enter a valid Provider ID.");
                System.out.print("Enter your provider ID to login, or \"x\" to return to main menu: ");
                loginID.next();
            }
            ID = loginID.nextInt();
            //check for ID in database here
            //placeholder check, always true
            if(ID == ID)
                found = true;
        } while (!found);

        //open provider interface here
        ProviderInterface account = new ProviderInterface(database, ID/*other constructor args*/);
        account.main(null);
    }

    private static void ManagerLogin(){
        ManagerInterface account = new ManagerInterface(database/*constructor args*/);
        account.main(null);
    }
}
