package com.ICNH.chocan;

public class ChocAnMain {

    //Main loops through the login menu - the outermost scope of the software
    public static void main(String[] args) {
        Login login = new Login();
        while (true) {
            Utilities.clearConsole();
            System.out.println("Welcome to ChocAn!");
            login.loginMainMenu();
        }
    }
}
