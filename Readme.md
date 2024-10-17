# Tool Checkout - Primary Red Tools

This project contains a library that runs a checkout process for the fictional store Primary Red Tools. It was written and tested on Windows (sorry) but should work on any platform that supports Maven and Java.

## Requirements
- Maven
- Java 17
- Lombok 1.18
- json (org.json) 20240303
- junit 4.13

I had meant to use jupiter, but accidentally used junit 4 and just went with it. sorry!

For more details on the requirements, check the pom!

## Configuration
The tools, charges and holidays are defined in the json file `configuration.json`, duplicated in the resource directories of `main` and `test`.

## Sample run
The class `Main` contains a `main()` routine that runs an example run. The Rental itself is run from the class `ToolRental`; instantiating the class reads in the configuration, and running the `checkout()` method checks out one tool.

The function that can print out a `RentalAgreement` is `print()`. It is used in the `main()` method.

## Tests

The Test Suite is under `src/tests`. It uses its own configuration file that is a clone of the one the main program runs on.

There is a test class for each of four of the java classes. The tests required by the assignment are in the class `DemonstationSecenariosTest`.

Thank you! Contact me with comments at `stephen.mubita@gmail.com`.




