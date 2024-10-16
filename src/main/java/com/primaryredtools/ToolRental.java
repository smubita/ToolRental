package com.primaryredtools;

import com.primaryredtools.models.Charge;
import com.primaryredtools.models.Tool;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
public class ToolRental {
    private HashMap<String, Tool> tools;
    private HashMap<String, Charge> charges;
    private JSONObject configuration;


    public ToolRental() {
        this.tools = new HashMap<String, Tool>();
        this.charges = new HashMap<String, Charge>();
        this.configuration = readConfiguration();

        this.readCharges();
        this.readTools();
    }

    public void readCharges() {
        JSONArray tools = (JSONArray) this.getConfiguration().get("charges");
        for (int i = 0; i < tools.length(); i++) {
            Charge thisCharge = ToolRental.readCharge(tools, i);
            this.getCharges().put(thisCharge.getToolType(), thisCharge);
        }
    }

    public void readTools( ) {
        JSONArray tools = (JSONArray) this.getConfiguration().get("tools");
        for (int i = 0; i < tools.length(); i++) {
            Tool thisTool = ToolRental.readTool(tools, i, this.getCharges());
            this.getTools().put(thisTool.getToolCode(), thisTool);
        }
    }

    private static Tool readTool(JSONArray tools, int index, HashMap<String, Charge> chargeMap) {
        String toolCode = getField(tools, index, "code");
        String toolType = getField(tools, index, "toolType");
        String brand = getField(tools, index, "brand");

        return Tool.builder()
                .toolCode(toolCode)
                .toolType(toolType)
                .brand(brand)
                .charge(chargeMap.get(toolType))
                .build();
    }

    private static String getField(JSONArray array, int index, String fieldName) {
        return ((JSONObject) array.get(index)).get(fieldName).toString();
    }

    private static Charge readCharge(JSONArray tools, int index) {
        String toolType = getField(tools, index, "toolType");
        BigDecimal dailyCharge = new BigDecimal(
                getField(tools, index, "dailyCharge")
        );
        boolean weekdayCharge = Boolean.getBoolean(getField(tools, index, "weekdayCharge"));
        boolean weekendCharge = Boolean.getBoolean(getField(tools, index, "weekendCharge"));
        boolean holidayCharge = Boolean.getBoolean(getField(tools, index, "holidayCharge"));

        return Charge.builder()
                .dailyCharge(dailyCharge)
                .toolType(toolType)
                .weekdayCharge(weekdayCharge)
                .weekendCharge(weekendCharge)
                .holidayCharge(holidayCharge)
                .build();
    }

    private static JSONObject readConfiguration() {
        InputStream is
                = ClassLoader.getSystemResourceAsStream("configuration.json");

        if(is == null) {
           System.out.println("Could not find the configuration file 'configuration.json'");
           System.exit(1);
        }
        Scanner readJson = new Scanner(is);

        StringBuilder jsonString = new StringBuilder();
        while(readJson.hasNextLine()) {
            jsonString.append(readJson.nextLine());
        }
        return new JSONObject(jsonString.toString());
    }

    public void displayTools() {
        System.out.printf("%-10s %-16s %-12s %12s  %-15s %-15s %-15s%n",
                "Tool Code",
                "Tool Type",
                "Brand",
                "Daily Charge",
                "Weekday Charge",
                "Weekend Charge",
                "Holiday Charge"
        );
        for(String toolCode: this.getTools().keySet()) {
            System.out.println(tools.get(toolCode));
        }
    }
}
