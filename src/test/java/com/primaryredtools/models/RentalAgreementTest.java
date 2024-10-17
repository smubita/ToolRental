package com.primaryredtools.models;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class RentalAgreementTest {
    Holiday christmas;
    Holiday laborDay;
    Holiday independenceDay;
    List<Holiday> holidayList;
    Tool tool;
    RentalAgreement rentalAgreement;

    @Before
    public void setup() {
        christmas = Holiday.builder()
                .name("Christmas Day")
                .month(Month.DECEMBER)
                .day(25)
                .dayList(List.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY,
                        DayOfWeek.SUNDAY))
                .which("all")
                .build();
        laborDay = Holiday.builder()
                .name("Labor Day")
                .month(Month.SEPTEMBER)
                .day(-1)
                .dayList(List.of(
                        DayOfWeek.MONDAY))
                .which("first")
                .build();
        independenceDay = Holiday.builder()
                .name("Independance Day")
                .month(Month.JULY)
                .day(4)
                .dayList(List.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY))
                .which("all")
                .build();
        holidayList = List.of(christmas, laborDay, independenceDay);
        tool = Tool.builder()
                .toolCode("SCDV")
                .toolType("Screwdriver")
                .brand("Smirnoff")
                .charge(Charge.builder()
                        .toolType("Screwdriver")
                        .dailyCharge(BigDecimal.valueOf( .99))
                        .weekdayCharge(false)
                        .weekendCharge(true)
                        .holidayCharge(true)
                        .build())
                .build();
        rentalAgreement = RentalAgreement.builder()
                .tool(tool)
                .rentalDays(184)
                .discountPercentage(50)
                .checkoutDate(LocalDate.of(2024, 6, 29))
                .holidays(holidayList)
                .build();
    }

    @Test
    public void checkDueDate() {
        assertEquals(LocalDate.of(2024, 12, 30), rentalAgreement.getDueDate());
    }

    @Test
    public void checkToolCode() {
        assertEquals("SCDV", rentalAgreement.getToolCode());
    }

    @Test
    public void checkBrand() {
        assertEquals("Smirnoff", rentalAgreement.getBrand());
    }

    @Test
    public void checkToolType() {
        assertEquals("Screwdriver", rentalAgreement.getToolType());
    }

    @Test
    public void checkRentalCharge() {
        assertEquals(BigDecimal.valueOf(.99), rentalAgreement.getDailyRentalCharge());
    }

    @Test
    public void checkChargeDays() {
        assertEquals(56, rentalAgreement.calculateChargeDays());
    }

    @Test
    public void checkPreDiscountCharge() {
        assertEquals(BigDecimal.valueOf(55.44), rentalAgreement.getPreDiscountCharge());
    }

    @Test
    public void getDiscountAmount() {
        assertEquals(BigDecimal.valueOf(27.72), rentalAgreement.getDiscountAmount());
    }

    @Test
    public void checkFinalAmount() {
        assertEquals(BigDecimal.valueOf(27.72), rentalAgreement.getFinalCharge());
    }

    @Test
    public void checkRentalDayList() {
        List<LocalDate> list = rentalAgreement.getRentalDayList();
        assertNotNull(list);
        assertEquals(184, list.size());
        assertFalse(list.contains(LocalDate.of(2024, 6, 29)));
        assertTrue(list.contains(LocalDate.of(2024, 6, 30)));
        assertEquals(LocalDate.of(2024, 6, 30),
                list.stream().min(Comparator.naturalOrder()).get());
        assertTrue(list.contains(LocalDate.of(2024, 12, 30)));
        assertEquals(LocalDate.of(2024, 12, 30),
                list.stream().max(Comparator.naturalOrder()).get());
        assertFalse(list.contains(LocalDate.of(2025, 1, 1)));
    }

    @Test
    public void checkToString() {
        String result = rentalAgreement.toString();
        assertEquals(12, result.lines().count());
        String rentalString = result.lines()
                .filter(line -> line.startsWith("Rental days"))
                .findFirst()
                .get();

        assertEquals("Rental days: 184", rentalString);
    }

}
