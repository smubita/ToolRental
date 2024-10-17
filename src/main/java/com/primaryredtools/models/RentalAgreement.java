package com.primaryredtools.models;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@Builder
public class RentalAgreement {
    private Tool tool;
    private int rentalDays;
    private int discountPercentage;
    private LocalDate checkoutDate;
    private List<Holiday> holidays;
    private Integer chargeDays = null;

    public LocalDate getDueDate() {
        return this.getCheckoutDate().plusDays(this.getRentalDays());
    }

    public String getToolCode() {
        return this.getTool().getToolCode();
    }

    public String getBrand() {
        return this.getTool().getBrand();
    }

    public String getToolType() {
        return this.getTool().getToolType();
    }

    public BigDecimal getDailyRentalCharge() {
        return this.getTool().getCharge().getDailyCharge();
    }

    public void print() {
        System.out.println(this);
    }

    public BigDecimal getPreDiscountCharge() {
        return this.getDailyRentalCharge().multiply(BigDecimal.valueOf(this.calculateChargeDays()));
    }

    public BigDecimal getDiscountAmount() {
        return this.getPreDiscountCharge()
                .multiply( BigDecimal.valueOf(this.getDiscountPercentage()))
                .divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFinalCharge() {
        return this.getPreDiscountCharge().subtract(this.getDiscountAmount());
    }

    public int calculateChargeDays() {
        if(this.chargeDays != null) {
            return this.chargeDays;
        }

        List<LocalDate> rentalPeriod = getRentalDayList();

        int chargeDays = 0;

        per_day:
        for(LocalDate thisDate: rentalPeriod) {
            // First check for a holiday
            for(Holiday holiday: this.getHolidays()) {
                if(holiday.matchesDate(thisDate)) {
                    if(this.getTool().getCharge().hasHolidayCharge()) {
                        chargeDays += 1;
                        continue per_day;
                    }
                }
            }

            // then check for a weekend day
            if(thisDate.getDayOfWeek() == DayOfWeek.SATURDAY || thisDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                if(this.getTool().getCharge().hasWeekendCharge()) {
                    chargeDays += 1;
                }
            } else {
                // then treat like a "working" week day.
                if(this.getTool().getCharge().hasWeekdayCharge()) {
                    chargeDays += 1;
                }
            }

        }
        this.chargeDays = chargeDays;
        return chargeDays;
    }

    public List<LocalDate> getRentalDayList() {
        return this.getCheckoutDate().plusDays(1L).datesUntil
                (this.getDueDate().plusDays(1L)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        StringBuilder agreement = new StringBuilder();

        agreement.append(String.format("Tool code: %s%n", this.getToolCode()));
        agreement.append(String.format("Tool type: %s%n", this.getToolType()));
        agreement.append(String.format("Tool brand: %s%n", this.getBrand()));
        agreement.append(String.format("Rental days: %d%n",  this.getRentalDays()));
        agreement.append(String.format("Check out date: %s%n",
                this.getCheckoutDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))));
        agreement.append(String.format("Due date: %s%n",
                this.getDueDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))));
        agreement.append(String.format("Daily rental charge: %s%n",
                moneyFormatter.format(this.getDailyRentalCharge())));
        agreement.append(String.format("Charge days: %d%n", this.calculateChargeDays()));
        agreement.append(String.format("Pre-discount charge: %s%n",
                moneyFormatter.format(this.getPreDiscountCharge())));
        agreement.append(String.format("Discount percentage: %d%%%n", this.getDiscountPercentage()));
        agreement.append(String.format("Discount amounte: %s%n",
                moneyFormatter.format(this.getDiscountAmount())));
        agreement.append(String.format("Final Charge: %s%n",
                moneyFormatter.format(this.getFinalCharge())));

        return agreement.toString();
    }
}
