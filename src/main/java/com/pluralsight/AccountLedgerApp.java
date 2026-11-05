package com.pluralsight;

import java.util.Scanner;

public class AccountLedgerApp {
    public static void main(String[] args) {
        // Create a Scanner so the program can read the user's menu choices.
        Scanner myScanner = new Scanner(System.in);

        // Keep the home menu running until the user chooses to exit.
        boolean running = true;

        while (running) {

            // Display the main menu options.
            System.out.println("\n===== ACCOUNTING LEDGER =====");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");

            // Read the user's choice and convert it to uppercase.
            // This allows the user to enter either d or D, for example.
            String choice = myScanner.nextLine().toUpperCase();
        }
        }
}

