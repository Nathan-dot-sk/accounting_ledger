package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountLedgerApp {

    public static void AccountLedgerApp(String[] args) {
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

            // Use the selected letter to decide which method should run.
            switch (choice) {

                case "D":
                    // Open the method that collects and saves a deposit.
                    addDeposit(myScanner);
                    break;

                case "P":
                    // Open the method that collects and saves a payment.
                    makePayment(myScanner);
                    break;

                case "L":
                    // Open the Ledger menu, which also contains the Reports option.
                    showLedger(myScanner);
                    break;

                case "X":
                    // Set running to false so the while loop ends.
                    running = false;
                    System.out.println("Thank you for using Accounting Ledger!");
                    break;

                default:
                    // Display a message if the user enters an option that is not listed.
                    System.out.println("Invalid option. Try again.");
            }
        }

        // Close the Scanner after the user exits the application.
        myScanner.close();
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

        // Send the new transaction to the method that saves it in the file.
        saveTransaction(transaction);
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

        // Save the payment to transactions.csv.
        saveTransaction(transaction);
    }

    // This method adds a transaction to transactions.csv.
    public static void saveTransaction(Transactions transaction) {

        // FileWriter's true setting means new transactions are added to the end
        // of the file instead of replacing the transactions already saved.
        // try-with-resources automatically closes the file when finished.
        try (FileWriter writer = new FileWriter("transactions.csv", true)) {

            // Write each transaction field with a | between fields.
            // Replace any | typed inside the description or vendor so the file
            // can still be separated into the correct five fields when read.
            writer.write(
                    transaction.getDate() + "|" +
                            transaction.getTime() + "|" +
                            transaction.getDescription().replace("|", " ") + "|" +
                            transaction.getVendor().replace("|", " ") + "|" +
                            transaction.getAmount() + "\n"
            );

            System.out.println("Transaction saved successfully!");

            // Catch file errors and show the reason to the user.
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    // This method displays the Ledger menu and responds to the user's selection.
    public static void showLedger(Scanner myScanner) {

        // Keep showing the Ledger menu until the user chooses Home.
        boolean inLedger = true;

        while (inLedger) {

            // Display the Ledger options, including the Reports submenu.
            System.out.println("\n===== LEDGER =====");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            // Read the selection and make it uppercase for easier comparison.
            String choice = myScanner.nextLine().toUpperCase();

            // Run the correct action based on the selected option.
            switch (choice) {

                case "A":
                    // Show all transactions saved in the file.
                    displayTransactions("ALL");
                    break;

                case "D":
                    // Show only deposits, which have positive amounts.
                    displayTransactions("DEPOSITS");
                    break;

                case "P":
                    // Show only payments, which have negative amounts.
                    displayTransactions("PAYMENTS");
                    break;

                case "R":
                    // Open the Reports submenu.
                    showReports(myScanner);
                    break;

                case "H":
                    // End the Ledger loop and return to the home menu.
                    inLedger = false;
                    break;

                default:
                    // Handle a letter that is not one of the menu choices.
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // This method reads the file and displays all transactions or a selected type.
    public static void displayTransactions(String filter) {

        // Create a list to hold the transactions that match the selected filter.
        List<String> transactions = new ArrayList<>();

        // Open transactions.csv and read it one line at a time.
        // The reader is automatically closed when the try block ends.
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("transactions.csv"))) {

            String line;

            // Continue reading until there are no more lines in the file.
            while ((line = reader.readLine()) != null) {

                // Skip the column-heading row if the file has a header.
                if (line.startsWith("date|")) {
                    continue;
                }

                // Split the line into date, time, description, vendor, and amount.
                String[] details = line.split("\\|");

                // Skip a line if it does not have all five expected fields.
                if (details.length != 5) {
                    continue;
                }

                // Convert the amount field from text into a decimal number.
                double amount = Double.parseDouble(details[4]);

                // Add the transaction to the list if it matches the selected filter.
                if (filter.equals("ALL")) {
                    transactions.add(line);
                } else if (filter.equals("DEPOSITS") && amount > 0) {
                    transactions.add(line);
                } else if (filter.equals("PAYMENTS") && amount < 0) {
                    transactions.add(line);
                }
            }

            System.out.println("\n===== TRANSACTIONS =====");

            // If the list is empty, tell the user no matching transactions were found.
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                // Print each transaction that matched the filter.
                for (String transaction : transactions) {
                    System.out.println(transaction);
                }
            }

            // Display an error if the file cannot be opened or read.
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
    }

    // This method displays report choices for date ranges and vendor searches.
    public static void showReports(Scanner myScanner) {

        // Keep the Reports menu open until the user chooses Back.
        boolean inReports = true;

        while (inReports) {

            // Display all available report options.
            System.out.println("\n===== REPORTS =====");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            // Read the user's report selection.
            String choice = myScanner.nextLine();

            // Get today's date so the report ranges update automatically.
            LocalDate today = LocalDate.now();

            // Choose which date range or vendor search to run.
            switch (choice) {

                case "1":
                    // Month to date starts on the first day of this month
                    // and ends on today's date.
                    displayDateReport(
                            today.withDayOfMonth(1),
                            today,
                            "MONTH TO DATE"
                    );
                    break;

                case "2":
                    // Find the previous month by moving back one month from today.
                    LocalDate previousMonth = today.minusMonths(1);

                    // Use the first and last days of that previous month.
                    displayDateReport(
                            previousMonth.withDayOfMonth(1),
                            previousMonth.withDayOfMonth(
                                    previousMonth.lengthOfMonth()
                            ),
                            "PREVIOUS MONTH"
                    );
                    break;

                case "3":
                    // Year to date starts on January 1 of the current year
                    // and ends on today's date.
                    displayDateReport(
                            today.withDayOfYear(1),
                            today,
                            "YEAR TO DATE"
                    );
                    break;

                case "4":
                    // Move back one year to get the previous calendar year.
                    LocalDate previousYear = today.minusYears(1);

                    // Use January 1 and the final day of that previous year.
                    displayDateReport(
                            previousYear.withDayOfYear(1),
                            previousYear.withDayOfYear(
                                    previousYear.lengthOfYear()
                            ),
                            "PREVIOUS YEAR"
                    );
                    break;

                case "5":
                    // Ask for the vendor name, then search for matching transactions.
                    System.out.print("Enter vendor name: ");
                    String vendor = myScanner.nextLine();

                    searchByVendor(vendor);
                    break;

                case "0":
                    // End the Reports loop and return to the Ledger menu.
                    inReports = false;
                    break;

                default:
                    // Tell the user if the selected report option is invalid.
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // This method displays transactions between a starting date and an ending date.
    public static void displayDateReport(
            LocalDate startDate,
            LocalDate endDate,
            String title) {

        // Print the name of the report being displayed.
        System.out.println("\n===== " + title + " =====");

        // Open the transaction file so its dates can be checked.
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("transactions.csv"))) {

            String line;

            // This variable tracks whether any transaction matched the date range.
            boolean found = false;

            // Read each transaction row from the file.
            while ((line = reader.readLine()) != null) {

                // Ignore the header row.
                if (line.startsWith("date|")) {
                    continue;
                }

                // Separate the row into its five transaction fields.
                String[] details = line.split("\\|");

                // Ignore rows that do not have the expected five fields.
                if (details.length != 5) {
                    continue;
                }

                try {
                    // Convert the date field from text into a LocalDate object.
                    LocalDate transactionDate = LocalDate.parse(details[0]);

                    // Check that the transaction date is not before the start
                    // and not after the end of the selected report period.
                    if (!transactionDate.isBefore(startDate)
                            && !transactionDate.isAfter(endDate)) {

                        // Display the matching transaction and record that one was found.
                        System.out.println(line);
                        found = true;
                    }

                    // If a row has an invalid date, skip it instead of stopping the program.
                } catch (java.time.format.DateTimeParseException e) {
                    // No action is needed because this row cannot be used in the report.
                }
            }

            // Let the user know if no transactions were in the selected date range.
            if (!found) {
                System.out.println("No transactions found.");
            }

            // Display an error if transactions.csv cannot be read.
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // This method searches transactions.csv for a vendor entered by the user.
    public static void searchByVendor(String vendor) {

        System.out.println("\n===== VENDOR SEARCH =====");

        // Open the file and read each transaction row.
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("transactions.csv"))) {

            String line;

            // Track whether at least one transaction matches the vendor.
            boolean found = false;

            // Read through the file until there are no more rows.
            while ((line = reader.readLine()) != null) {

                // Skip the header row.
                if (line.startsWith("date|")) {
                    continue;
                }

                // Split the transaction into its five fields.
                String[] details = line.split("\\|");

                // Ignore any row that does not have five fields.
                if (details.length != 5) {
                    continue;
                }

                // The fourth field (index 3) is the vendor name.
                // equalsIgnoreCase allows matches even if capitalization differs.
                if (details[3].equalsIgnoreCase(vendor)) {
                    System.out.println(line);
                    found = true;
                }
            }

            // Display a message if no transaction used the searched vendor.
            if (!found) {
                System.out.println("No transactions found for that vendor.");
            }

            // Display an error if the file cannot be opened or read.
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}


