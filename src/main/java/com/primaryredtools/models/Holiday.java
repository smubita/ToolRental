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

    public static Holiday readHoliday(JSONArray holidays, int index) {
        JSONArray daysOfWeek = (JSONArray) ((JSONObject) holidays.get(index)).get("daysOfWeek");
        List<DayOfWeek> dayList = new ArrayList<DayOfWeek>();
        for(int i = 0; i < daysOfWeek.length(); i++) {
            DayOfWeek day = DayOfWeek.valueOf( daysOfWeek.getString(i) );
            dayList.add(day);
        }

        return Holiday.builder()
                .name(getField(holidays, index, "name"))
                .month(Month.of(getIntegerField(holidays, index, "month")))
                .day(getIntegerField(holidays, index, "day"))
                .dayList(dayList)
                .which(getField(holidays, index, "which"))
                .build();
    }

    public static List<Holiday> readHolidays(JSONObject configuration) {
        List<Holiday> holidayList = new ArrayList<Holiday>();
        JSONArray holidays = getArray(configuration, "holidays");
        for(int i = 0; i < holidays.length(); i++) {
            Holiday holiday = readHoliday(holidays, i);
            holidayList.add(holiday);
        }
        return holidayList;
    }

    public boolean matchesDate(LocalDate thisDate) {
        // this function checks to see if the argument date is a holiday.
        // it only checks the two date scenarios listed in the spec: Labor Day and Independence Day

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

        boolean matchesDay = (thisDate.getMonth() == this.getMonth() && thisDate.getDayOfMonth() == this.getDay());
        if(matchesDay && this.getDayList().contains( thisDate.getDayOfWeek() ) ) {
            return true;
        }

        LocalDate nextDate = thisDate.plusDays(1L);
        matchesDay = (nextDate.getMonth() == this.getMonth() && nextDate.getDayOfMonth() == this.getDay());
        if(matchesDay && this.getDayList().contains(thisDate.getDayOfWeek())
            && ! this.getDayList().contains(nextDate.getDayOfWeek())) {
            return true;
        }

        LocalDate previousDate = thisDate.minusDays(1L);
        matchesDay = (previousDate.getMonth() == this.getMonth() && previousDate.getDayOfMonth() == this.getDay());
        if(matchesDay && this.getDayList().contains(nextDate.getDayOfWeek())
            && ! this.getDayList().contains(previousDate.getDayOfWeek())) {
            return true;
        }

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
