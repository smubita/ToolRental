package com.primaryredtools.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
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
}
