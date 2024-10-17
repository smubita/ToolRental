package com.primaryredtools.models;

import lombok.Builder;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

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

    public static Map<String, Charge> readCharges(JSONObject configuration) {
        Map<String, Charge> chargeMap = new HashMap<>();
        JSONArray charges = getArray(configuration, "charges");
        StreamSupport.stream(charges.spliterator(), false)
            .forEach(jsonCharge -> {
                Charge thisCharge = readCharge((JSONObject) jsonCharge);
                chargeMap.put(thisCharge.getToolType(), thisCharge);
            });

        return chargeMap;
    }

    private static Charge readCharge(JSONObject charge) {
        return Charge.builder()
                .dailyCharge(getBigDecimalField(charge, "dailyCharge"))
                .toolType(getField(charge, "toolType"))
                .weekdayCharge(getBooleanField(charge, "weekdayCharge"))
                .weekendCharge(getBooleanField(charge, "weekendCharge"))
                .holidayCharge(getBooleanField(charge, "holidayCharge"))
                .build();
    }
}
