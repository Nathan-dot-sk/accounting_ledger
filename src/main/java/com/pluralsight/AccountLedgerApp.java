package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
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

            switch (choice) {

                case "D":
                    // Open the method that collects and saves a deposit.
                    addDeposit(myScanner);
                    break;

                case "P":
                    // Open the method that collects and saves a payment.
                    makePayment(myScanner);
                    break;
            }

        }


    }

    // This method asks for deposit information and saves it as a transaction.
    public static void addDeposit(Scanner myScanner) {

        System.out.println("\n===== ADD DEPOSIT =====");

        // Ask the user what the deposit is for.
        System.out.print("Description: ");
        String description = myScanner.nextLine();

        // Ask who or where the money came from.
        System.out.print("Vendor: ");
        String vendor = myScanner.nextLine();

        // Ask for the deposit amount and convert the text to a decimal number.
        System.out.print("Amount: $");
        double amount = Double.parseDouble(myScanner.nextLine());

        // A deposit must be greater than zero.
        // return ends this method if the amount is not valid.
        if (amount <= 0) {
            System.out.println("Please enter an amount greater than zero.");
            return;
        }

        // Create a Transaction object using the current date and time.
        // The amount stays positive because this transaction is a deposit.
        Transactions transaction = new Transactions(
                LocalDate.now(),
                LocalTime.now().withNano(0),
                description,
                vendor,
                amount
        );
    }

    // This method collects payment information and saves the payment as a negative amount.
    public static void makePayment(Scanner myScanner) {

        System.out.println("\n===== MAKE PAYMENT =====");

        // Ask what the payment was for.
        System.out.print("Description: ");
        String description = myScanner.nextLine();

        // Ask for the name of the store, company, or person receiving the payment.
        System.out.print("Vendor: ");
        String vendor = myScanner.nextLine();

        // Ask for the payment amount and convert it to a number.
        System.out.print("Amount: $");
        double amount = Double.parseDouble(myScanner.nextLine());

        // Make sure the amount entered is greater than zero.
        if (amount <= 0) {
            System.out.println("Please enter an amount greater than zero.");
            return;
        }
        // Create a Transaction object with the current date and time.
        // The minus sign makes the amount negative to identify it as a payment.
        Transactions transaction = new Transactions(
                LocalDate.now(),
                LocalTime.now().withNano(0),
                description,
                vendor,
                -amount
        );
    }

}

