package com.ICNH.chocan;

import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.exit;


public class Login {

    private DatabaseInterface database;

    public Login() {
        this.database = new DatabaseInterface(/*database constructor args here, if any*/);
    }

    public void loginMainMenu() {
        Scanner loginInput = new Scanner(System.in);
        int loginKey;

        do {
            System.out.print("1. Provider login.\n" +
                    "2. Manager login.\n" +
                    "3. Exit Program\n" +
                    "Make a selection: ");
            while (!loginInput.hasNextInt()) {
                Utilities.clearConsole();
                System.out.println("Invalid selection.\n");
                loginInput.next();
                System.out.print("1. Provider login.\n" +
                        "2. Manager login.\n" +
                        "Make a selection: ");
            }
            loginKey = loginInput.nextInt();
            if (loginKey != 1 && loginKey != 2 && loginKey != 3) {
                Utilities.clearConsole();
                System.out.println("Invalid selection.\n");
            }
        } while (loginKey != 1 && loginKey != 2 && loginKey != 3);

        switch (loginKey) {
            case 1:
                providerLogin();
                break;
            case 2:
                managerLogin();
                break;
            case 3:
                exit(0);
            default:
                break;
        }
    }

    private void providerLogin() {
        Scanner loginID = new Scanner(System.in);
        int ID = 0;
        boolean found = false;

        do {
            System.out.print("Enter your provider ID to login, or \"x\" to return to main menu: ");
            while (!loginID.hasNextInt()) {
                if (loginID.hasNext("x"))
                    return;
                Utilities.clearConsole();
                System.out.println("Invalid ID, please enter a valid Provider ID.");
                System.out.print("Enter your provider ID to login, or \"x\" to return to main menu: ");
                loginID.next();
            }
            ID = loginID.nextInt();
            //check for ID in database here
            //placeholder check, always true
            try {
                if (database.validateProvider(ID))
                    found = true;
                else {
                    System.out.println("Invalid ID. Press enter to continue.");
                    loginID.nextLine();
                    loginID.nextLine();
                    return;
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        } while (!found);

        //open provider interface here
        ProviderInterface providerInterface = new ProviderInterface(database, ID/*other constructor args*/);
        providerInterface.menu();
    }

    private void managerLogin() {
        ManagerInterface managerInterface = new ManagerInterface(database/*constructor args*/);
        managerInterface.menu();
    }
}
