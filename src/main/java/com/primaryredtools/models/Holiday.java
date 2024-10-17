package com.primaryredtools.models;

import lombok.Builder;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.primaryredtools.utilities.JSON.*;

@Builder
@Getter
public class Holiday {
    String name;
    Month month;
    Integer day = null;
    boolean allDays = false;
    List<DayOfWeek> dayList;
    String which = "all";

    public static Holiday readHoliday(JSONObject holidays) {
        JSONArray daysOfWeek = (JSONArray) holidays.get("daysOfWeek");
        List<DayOfWeek> dayList = new ArrayList<DayOfWeek>();
        StreamSupport.stream(daysOfWeek.spliterator(), false)
            .forEach(record -> {
                DayOfWeek day = DayOfWeek.valueOf( (String) record);
                dayList.add(day);
            });

        return Holiday.builder()
                .name(getField(holidays, "name"))
                .month(Month.of(getIntegerField(holidays, "month")))
                .day(getIntegerField(holidays, "day"))
                .dayList(dayList)
                .which(getField(holidays, "which"))
                .build();
    }

    public static List<Holiday> readHolidays(JSONObject configuration) {
        List<Holiday> holidayList = new ArrayList<Holiday>();
        JSONArray holidays = getArray(configuration, "holidays");
        StreamSupport.stream(holidays.spliterator(), false)
                .forEach(record -> {
                    Holiday holiday = readHoliday( (JSONObject) record);
                    holidayList.add(holiday);
                });

        return holidayList;
    }

    public boolean matchesDate(LocalDate thisDate) {
        // this function checks to see if the argument date is a holiday.
        // it only checks the two date scenarios listed in the spec: Labor Day and Independence Day

        // this is for ordinal holidays - last or first X of a month
        boolean matchesMonth = thisDate.getMonth() == this.getMonth();
        if(matchesMonth && this.getDay() < 1) {
            switch (this.getWhich()) {
                case "first" -> {
                    if (checkNthDayInMonth(thisDate, 1)) return true;
                }
                case "second" -> {
                    if (checkNthDayInMonth(thisDate, 2)) return true;
                }
                case "third" -> {
                    if (checkNthDayInMonth(thisDate, 3)) return true;
                }
                case "fourth" -> {
                    if (checkNthDayInMonth(thisDate, 4)) return true;
                }
                case "fifth" -> {
                    if (checkNthDayInMonth(thisDate, 5)) return true;
                }
                case "last" -> {
                    for (DayOfWeek day : this.getDayList()) {
                        if (thisDate.with(TemporalAdjusters.lastInMonth(day)).equals(thisDate)) {
                            return true;
                        }
                    }
                }
            }
        }

        // This matches holidays that are observed on their actual day
        boolean matchesDay = (thisDate.getMonth() == this.getMonth() && thisDate.getDayOfMonth() == this.getDay());
        if(matchesDay && this.getDayList().contains( thisDate.getDayOfWeek() ) ) {
            return true;
        }

        // This matches holidays that are observed the day before the actual day (if day of week
        // of the actual day is not on the allowed list)
        LocalDate nextDate = thisDate.plusDays(1L);
        matchesDay = (nextDate.getMonth() == this.getMonth() && nextDate.getDayOfMonth() == this.getDay());
        if(matchesDay && this.getDayList().contains(thisDate.getDayOfWeek())
            && ! this.getDayList().contains(nextDate.getDayOfWeek())) {
            return true;
        }

        // This matches holidays that are observed the day after the actual day (if day of week
        // of the actual day is not on the allowed list)
        LocalDate previousDate = thisDate.minusDays(1L);
        matchesDay = (previousDate.getMonth() == this.getMonth() && previousDate.getDayOfMonth() == this.getDay());
        if(matchesDay && this.getDayList().contains(nextDate.getDayOfWeek())
            && ! this.getDayList().contains(previousDate.getDayOfWeek())) {
            return true;
        }

        // not a holiday
        return false;
    }

    private boolean checkNthDayInMonth(LocalDate thisDate, int ordinal) {
        for (DayOfWeek day : this.getDayList()) {
            if (thisDate.with(TemporalAdjusters.dayOfWeekInMonth(ordinal, day)).equals(thisDate)) {
                return true;
            }
        }
        return false;
    }
}
