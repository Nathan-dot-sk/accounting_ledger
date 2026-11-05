package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transactions {
    // These variables store information about each transaction.
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // Constructor: receives the transaction information when an object is created.
    public Transactions(LocalDate date, LocalTime time,
                       String description, String vendor, double amount) {

        // Store the values passed into the constructor.
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Returns the date of the transaction.
    public LocalDate getDate() {
        return date;
    }

    // Returns the time of the transaction.
    public LocalTime getTime() {
        return time;
    }

    // Returns the transaction description.
    public String getDescription() {
        return description;
    }

    // Returns the vendor name.
    public String getVendor() {
        return vendor;
    }

    // Returns the transaction amount.
    public double getAmount() {
        return amount;
    }

}

