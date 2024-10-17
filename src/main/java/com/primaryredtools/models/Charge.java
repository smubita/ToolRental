package com.primaryredtools.models;

import lombok.Builder;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

import java.util.HashMap;

import static com.primaryredtools.utilities.JSON.*;
import static com.primaryredtools.utilities.JSON.getBooleanField;

@Getter
@Builder
public class Charge {
    String toolType;
    BigDecimal dailyCharge;
    boolean weekdayCharge;
    boolean weekendCharge;
    boolean holidayCharge;

    public boolean hasWeekdayCharge() {
        return this.isWeekdayCharge();
    }
    public boolean hasWeekendCharge() {
        return this.isWeekendCharge();
    }
    public boolean hasHolidayCharge() {
        return this.isHolidayCharge();
    }

    public static HashMap<String, Charge> readCharges(JSONObject configuration) {
        HashMap<String, Charge> chargeMap = new HashMap<String, Charge>();
        JSONArray charges = getArray(configuration, "charges");
        for (int i = 0; i < charges.length(); i++) {
            Charge thisCharge = readCharge(charges, i);
            chargeMap.put(thisCharge.getToolType(), thisCharge);
        }
        return chargeMap;
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
}
