package com.primaryredtools.models;

import lombok.Builder;
import lombok.Data;

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
                this.getCharge().hasWeekdayCharge() ? "Yes" : "No",
                this.getCharge().hasHolidayCharge() ? "Yes" : "No"
        );
    }

}
