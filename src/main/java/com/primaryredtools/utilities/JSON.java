package com.primaryredtools.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

public class JSON {
    public static JSONObject readJSON(String resourceFileName) throws FileNotFoundException {
        InputStream is
                = ClassLoader.getSystemResourceAsStream(resourceFileName);

        if(is == null) {
            throw new FileNotFoundException(
                    String.format("Could not find the configuration file '%s'%n", resourceFileName));
        }
        Scanner readJson = new Scanner(is);

        StringBuilder jsonString = new StringBuilder();
        while(readJson.hasNextLine()) {
            jsonString.append(readJson.nextLine());
        }
        return new JSONObject(jsonString.toString());
    }

    public static String getField(JSONObject record, String fieldname) {
        return record.getString(fieldname);
    }

    public static Boolean getBooleanField(JSONObject record, String fieldname) {
        return record.getBoolean(fieldname);
    }

    public static BigDecimal getBigDecimalField(JSONObject record, String fieldname) {
        return record.getBigDecimal(fieldname);
    }

    public static String getField(JSONArray array, int index, String fieldName) {
        return ((JSONObject) array.get(index)).get(fieldName).toString();
    }

    public static Boolean getBooleanField(JSONArray array, int index, String fieldName) {
        return ((JSONObject) array.get(index)).getBoolean(fieldName);
    }

    public static BigDecimal getBigDecimalField(JSONArray array, int index, String fieldName) {
        return ((JSONObject) array.get(index)).getBigDecimal(fieldName);
    }

    public static Integer getIntegerField(JSONObject record, String fieldName) {
        return record.getInt(fieldName);
    }

    public static Integer getIntegerField(JSONArray array, int index, String fieldName) {
        return ((JSONObject) array.get(index)).getInt(fieldName);
    }

    public static JSONArray getArray(JSONObject json, String arrayName) {
        return (JSONArray) json.get(arrayName);
    }
}
