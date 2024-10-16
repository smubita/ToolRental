package com.primaryredtools.models;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Builder
@Getter
public class Holiday {
    String name;
    Month month;
    Integer day = null;
    boolean allDays = false;
    List<DayOfWeek> dayList;
    String which = "all";

    public boolean matchesDate(LocalDate thisDate) {
        // this function checks to see if the argument date is a holiday.
        // it only checks the two date scenarios listed in the spec: Labor Day and Independence Day

        if(this.getWhich().equals("first")) {
            for(DayOfWeek day: this.getDayList()) {
                if(thisDate.with(TemporalAdjusters.firstInMonth(day)).equals(thisDate)) {
                    return true;
                }
            }
        }
        // TODO: implement for second, third, fourth, fifth and last days


        boolean matchesDay = (thisDate.getMonth() == this.getMonth() && thisDate.getDayOfMonth() == this.getDay());
        if(matchesDay && this.getDayList().contains( thisDate.getDayOfWeek() ) ) {
            return true;
        }

        LocalDate previousDate = thisDate.minusDays(1L);
        if(matchesDay && this.getDayList().contains(previousDate.getDayOfWeek())) {
            return true;
        }

        LocalDate nextDate = thisDate.plusDays(1L);
        if(matchesDay && this.getDayList().contains(nextDate.getDayOfWeek())) {
            return true;
        }

        return false;
    }
}
