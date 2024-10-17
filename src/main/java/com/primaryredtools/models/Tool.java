package com.primaryredtools.models;

import lombok.Builder;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.HashMap;

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

    public static HashMap<String, Tool> readTools(JSONObject configuration, HashMap<String, Charge> chargeMap ) {
        HashMap<String, Tool> toolMap = new HashMap<String, Tool>();
        JSONArray tools = getArray(configuration, "tools");
        for (int i = 0; i < tools.length(); i++) {
            Tool thisTool = readTool(tools, i, chargeMap);
            toolMap.put(thisTool.getToolCode(), thisTool);
        }
        return toolMap;
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
}
