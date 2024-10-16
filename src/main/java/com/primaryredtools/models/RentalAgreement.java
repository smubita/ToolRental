package com.primaryredtools.models;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
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

    public LocalDate getDueDate() {
        return this.getCheckoutDate().plusDays(this.getRentalDays());
    }

    private String getToolCode() {
        return this.getTool().getToolCode();
    }

    private String getBrand() {
        return this.getTool().getBrand();
    }

    private String getToolType() {
        return this.getTool().getToolType();
    }

    public BigDecimal getDailyRentalCharge() {
        return this.getTool().getCharge().getDailyCharge();
    }

    public void print() {
        System.out.println(this);
    }


   public int getChargeDays() {
        List<LocalDate> rentalPeriod = getRentalDayList();

        int chargeDays = 0;

        for(LocalDate thisDate: rentalPeriod) {

            for(Holiday holiday: this.getHolidays()) {
                if(holiday.matchesDate(thisDate)) {
                    if(this.getTool().getCharge().hasHolidayCharge()) {
                        chargeDays += 1;
                        break;
                    }
                }
            }

            if(thisDate.getDayOfWeek() == DayOfWeek.SATURDAY || thisDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                if(this.getTool().getCharge().hasWeekendCharge()) {
                    chargeDays += 1;
                }
            } else {
                if(this.getTool().getCharge().hasWeekdayCharge()) {
                    chargeDays += 1;
                }
            }

        }
        return chargeDays;
    }

    private List<LocalDate> getRentalDayList() {
        return this.getCheckoutDate().datesUntil(this.getDueDate().plusDays(1L)).collect(Collectors.toList());
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
        agreement.append(String.format("Charge days: %d%n", this.getChargeDays()));

        return agreement.toString();
    }



}
