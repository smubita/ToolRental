package com.primaryredtools;

import com.primaryredtools.models.Charge;
import com.primaryredtools.models.Holiday;
import com.primaryredtools.models.RentalAgreement;
import com.primaryredtools.models.Tool;

import java.time.LocalDate;
import java.util.*;

import com.primaryredtools.utilities.JSON;
import lombok.Getter;
import org.json.JSONObject;

@Getter
public class ToolRental {
    private HashMap<String, Tool> tools;
    private HashMap<String, Charge> charges;
    private List<Holiday> holidays;
    private JSONObject configuration;

    public ToolRental() {
        this.configuration = JSON.readJSON("configuration.json");

        this.charges = Charge.readCharges(this.getConfiguration());
        this.tools = Tool.readTools(this.getConfiguration(), this.getCharges());
        this.holidays = Holiday.readHolidays(this.getConfiguration());
    }

    public RentalAgreement checkout(
            String toolCode,
            int rentalDayCount,
            int discountPercentage,
            LocalDate checkoutDate) {
        checkRental(toolCode, rentalDayCount, discountPercentage, checkoutDate);

        Tool selectedTool = this.getTools().get(toolCode);

        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .tool(selectedTool)
                .rentalDays(rentalDayCount)
                .checkoutDate(checkoutDate)
                .holidays(this.getHolidays())
                .discountPercentage(discountPercentage)
                .build();

        return rentalAgreement;
    }

    private void checkRental(String toolCode, int rentalDayCount, int discountPercentage, LocalDate checkoutDate) {
        if(rentalDayCount < 1) {
            throw new IllegalArgumentException("The rental day should be greater than or equal to one.");
        }

        if(discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("The discount percentage must be between 0 and 100.");
        }

        if(toolCode == null) {
            throw new NullPointerException("The tool code cannot be null");
        }

        if(! this.getTools().containsKey(toolCode)) {
            throw new IllegalArgumentException(String.format("Tool %s is not in our current inventory.", toolCode));
        }

        if(checkoutDate == null) {
            throw new NullPointerException("The checkout date cannot be null.");
        }

        // Realized this wasn't in the requirements
//        if(checkoutDate.isBefore(LocalDate.now())) {
//            throw new IllegalArgumentException("The check out date cannot be in the past.");
//        }
    }
}
