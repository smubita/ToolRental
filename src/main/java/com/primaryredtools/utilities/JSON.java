package com.primaryredtools.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

public class JSON {
    public static JSONObject readJSON(String resourceFileName) {
        InputStream is
                = ClassLoader.getSystemResourceAsStream(resourceFileName);

        if(is == null) {
            System.err.printf("Could not find the configuration file '%s'%n", resourceFileName);
            System.exit(1);
        }
        Scanner readJson = new Scanner(is);

        StringBuilder jsonString = new StringBuilder();
        while(readJson.hasNextLine()) {
            jsonString.append(readJson.nextLine());
        }
        return new JSONObject(jsonString.toString());
    }

    public static String getField(JSONArray array, int index, String fieldName) {
        return ((JSONObject) array.get(index)).get(fieldName).toString();
    }

    public static Boolean getBooleanField(JSONArray array, int index, String fieldName) {
        return Boolean.valueOf(getField(array, index, fieldName));

    }

    public static BigDecimal getBigDecimalField(JSONArray array, int index, String fieldName) {
        return new BigDecimal(getField(array, index, fieldName));
    }

    public static Integer getIntegerField(JSONArray array, int index, String fieldName) {
        return Integer.parseInt(getField(array, index, fieldName));
    }

    public static JSONArray getArray(JSONObject json, String arrayName) {
        return (JSONArray) json.get(arrayName);
    }
}
