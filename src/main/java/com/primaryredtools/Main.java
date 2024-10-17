package com.primaryredtools;

import com.primaryredtools.models.RentalAgreement;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ToolRental toolRental = new ToolRental();
        RentalAgreement rentalAgreement
                = toolRental.checkout("JAKR", 5, 0, LocalDate.now());
        System.out.println(rentalAgreement);
    }
}