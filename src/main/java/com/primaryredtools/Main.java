package com.primaryredtools;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ToolRental toolRental = new ToolRental();
        toolRental.checkout("JAKR", 5, 0, LocalDate.now());
    }
}