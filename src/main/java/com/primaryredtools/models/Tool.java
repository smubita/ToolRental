package com.primaryredtools.models;

import lombok.Builder;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.primaryredtools.utilities.JSON.*;
import static com.primaryredtools.utilities.JSON.getField;

@Data
@Builder
public class Tool {
    String toolCode;
    String toolType;
    String brand;
    Charge charge;

    @Override
    public String toString() {
        return String.format("%-10s %-16s %-12s %12s  %-15s %-15s %-15s",
                this.getToolCode(),
                this.getToolType(),
                this.getBrand(),
                this.getCharge().getDailyCharge(),
                this.getCharge().hasWeekdayCharge() ? "Yes" : "No",
                this.getCharge().hasWeekendCharge() ? "Yes" : "No",
                this.getCharge().hasHolidayCharge() ? "Yes" : "No"
        );
    }

    public static Map<String, Tool> readTools(JSONObject configuration, Map<String, Charge> chargeMap ) {
        Map<String, Tool> toolMap = new HashMap<>();
        JSONArray tools = getArray(configuration, "tools");

        StreamSupport.stream(tools.spliterator(), false)
            .forEach(jsonTool -> {
                Tool thisTool = readTool((JSONObject) jsonTool, chargeMap);
                toolMap.put(thisTool.getToolCode(), thisTool);
            });

        return toolMap;
    }

    private static Tool readTool(JSONObject tools, Map<String, Charge> chargeMap) {
        String toolType = getField(tools, "toolType");

        return Tool.builder()
                .toolCode(getField(tools, "code"))
                .toolType(toolType)
                .brand(getField(tools, "brand"))
                .charge(chargeMap.get(toolType))
                .build();
    }
}
