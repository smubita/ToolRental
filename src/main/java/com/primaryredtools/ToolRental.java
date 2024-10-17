package com.primaryredtools;

import com.primaryredtools.models.Charge;
import com.primaryredtools.models.Holiday;
import com.primaryredtools.models.RentalAgreement;
import com.primaryredtools.models.Tool;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

import com.primaryredtools.utilities.JSON;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import static org.apache.logging.log4j.LogManager.getLogger;

@Getter
public class ToolRental {
    public static final String CONFIGURATION_JSON = "configuration.json";

    private final Map<String, Tool> tools;
    private final Map<String, Charge> charges;
    private final List<Holiday> holidays;
    private final JSONObject configuration;

    private static final Logger logger = getLogger(ToolRental.class);

    public ToolRental() throws FileNotFoundException {
        this.configuration = JSON.readJSON(CONFIGURATION_JSON);

        this.charges = Charge.readCharges(this.getConfiguration());
        this.tools = Tool.readTools(this.getConfiguration(), this.getCharges());
        this.holidays = Holiday.readHolidays(this.getConfiguration());
        logger.info("Checkout tool loaded.");
    }

    public RentalAgreement checkout(
            String toolCode,
            int rentalDayCount,
            int discountPercentage,
            LocalDate checkoutDate) {
        checkRental(toolCode, rentalDayCount, discountPercentage, checkoutDate);

        Tool selectedTool = this.getTools().get(toolCode);

        RentalAgreement agreement = RentalAgreement.builder()
                .tool(selectedTool)
                .rentalDays(rentalDayCount)
                .checkoutDate(checkoutDate)
                .holidays(this.getHolidays())
                .discountPercentage(discountPercentage)
                .build();

        String infoMessage = String.format("Checkout:%n %s", agreement);
        logger.info(infoMessage);
        return agreement;

    }

    private void checkRental(String toolCode, int rentalDayCount, int discountPercentage, LocalDate checkoutDate) {
        if(rentalDayCount < 1) {
            String errorMessage = "The rental day should be greater than or equal to one.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if(discountPercentage < 0 || discountPercentage > 100) {
            String errorMessage = "The discount percentage must be between 0 and 100.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if(toolCode == null) {
            String errorMessage = "The tool code cannot be null.";
            logger.error(errorMessage);
            throw new NullPointerException(errorMessage);
        }

        if(! this.getTools().containsKey(toolCode)) {
            String errorMessage = String.format("Tool %s is not in our current inventory.", toolCode);
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if(checkoutDate == null) {
            String errorMessage = "The checkout date cannot be null";
            logger.error(errorMessage);
            throw new NullPointerException(errorMessage);
        }
    }
}
