package com.ICNH.chocan;

import java.util.Scanner;

public class Utilities {
    //clears the standard output device by printing a bunch of newlines
    public static void clearConsole() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public static boolean confirm() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("(Y/N): ");
            char yn = in.nextLine().toLowerCase().charAt(0);
            if (yn == 'y') {
                return true;
            }
            if (yn == 'n') {
                return false;
            }
        }
    }
}
