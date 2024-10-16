package com.primaryredtools;

import com.primaryredtools.models.Charge;
import com.primaryredtools.models.Holiday;
import com.primaryredtools.models.RentalAgreement;
import com.primaryredtools.models.Tool;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import com.primaryredtools.utilities.JSON;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.primaryredtools.utilities.JSON.*;

@Getter
public class ToolRental {
    private HashMap<String, Tool> tools;
    private HashMap<String, Charge> charges;
    private List<Holiday> holidays;
    private JSONObject configuration;

    public ToolRental() {
        this.tools = new HashMap<String, Tool>();
        this.charges = new HashMap<String, Charge>();
        this.configuration = JSON.readJSON("configuration.json");

        this.readCharges();
        this.readTools();
    }

    public void readHolidays() {
        JSONArray holidays = getArray(this.getConfiguration(), "holidays");
        for(int i = 0; i < holidays.length(); i++) {
            Holiday holiday = readHoliday(holidays, i);
            this.getHolidays().add(holiday);
        }
    }

    public void readCharges() {
        JSONArray charges = getArray(this.getConfiguration(), "charges");
        for (int i = 0; i < charges.length(); i++) {
            Charge thisCharge = ToolRental.readCharge(charges, i);
            this.getCharges().put(thisCharge.getToolType(), thisCharge);
        }
    }

    public void readTools( ) {
        JSONArray tools = getArray(this.getConfiguration(), "tools");
        for (int i = 0; i < tools.length(); i++) {
            Tool thisTool = ToolRental.readTool(tools, i, this.getCharges());
            this.getTools().put(thisTool.getToolCode(), thisTool);
        }
    }

    private static Holiday readHoliday(JSONArray holidays, int index) {
        JSONArray daysOfWeek = (JSONArray) ((JSONObject) holidays.get(index)).get("daysOfWeek");
        List<DayOfWeek> dayList = new ArrayList<DayOfWeek>();
        for(int i = 0; i < daysOfWeek.length(); i++) {
            DayOfWeek day = DayOfWeek.valueOf( daysOfWeek.getString(i) );
            dayList.add(day);
        }

        return Holiday.builder()
                .name(getField(holidays, index, "name"))
                .month(Month.of(getIntegerField(holidays, index, "month")))
                .day(getIntegerField(holidays, index, "day:"))
                .dayList(
                       dayList
                )
                .build();
    }

    private static Tool readTool(JSONArray tools, int index, HashMap<String, Charge> chargeMap) {
        String toolType = getField(tools, index, "toolType");

        return Tool.builder()
                .toolCode(getField(tools, index, "code"))
                .toolType(toolType)
                .brand(getField(tools, index, "brand"))
                .charge(chargeMap.get(toolType))
                .build();
    }

    private static Charge readCharge(JSONArray tools, int index) {
        return Charge.builder()
                .dailyCharge(getBigDecimalField(tools, index, "dailyCharge"))
                .toolType(getField(tools, index, "toolType"))
                .weekdayCharge(getBooleanField(tools, index, "weekdayCharge"))
                .weekendCharge(getBooleanField(tools, index, "weekendCharge"))
                .holidayCharge(getBooleanField(tools, index, "holidayCharge"))
                .build();
    }

    public void checkout(String toolCode, int rentalDayCount, int discountPercentage, LocalDate checkoutDate) {
        checkRental(toolCode, rentalDayCount, discountPercentage, checkoutDate);

        Tool selectedTool = this.getTools().get(toolCode);

        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .tool(selectedTool)
                .rentalDays(rentalDayCount)
                .checkoutDate(checkoutDate)
                .build();

        rentalAgreement.print();

    }

    private void checkRental(String toolCode, int rentalDayCount, int discountPercentage, LocalDate checkoutDate) {
        if(rentalDayCount < 1) {
            throw new IllegalArgumentException("The rental day could should be greater than or equal to one.");
        }

        if(discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("THe discount percentage must be between 0 and 100.");
        }

        if(! this.getTools().containsKey(toolCode)) {
            throw new IllegalArgumentException(String.format("Tool %s is not in our current inventory.", toolCode));
        }

        if(checkoutDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The check out date cannot be in the past.");
        }
    }
}
