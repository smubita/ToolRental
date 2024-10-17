package models;

import com.primaryredtools.models.Holiday;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HolidayTest {

    Holiday christmas;
    Holiday laborDay;
    Holiday independenceDay;
    Holiday memorialDay;

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
        memorialDay = Holiday.builder()
                .name("Memorial Day")
                .month(Month.MAY)
                .day(-1)
                .dayList(List.of(
                        DayOfWeek.MONDAY))
                .which("last")
                .build();
    }

    @Test
    public void matchesMemorialDay() {
        assertTrue(memorialDay.matchesDate(LocalDate.of(2024, 5, 27)));
        assertTrue(memorialDay.matchesDate(LocalDate.of(2025, 5, 26)));
    }

    @Test
    public void matchesNonMemorialDay() {
        assertFalse(memorialDay.matchesDate(LocalDate.of(2024, 5, 20)));
        assertFalse(memorialDay.matchesDate(LocalDate.of(2024, 5, 28)));
        assertFalse(memorialDay.matchesDate(LocalDate.of(2025, 5, 19)));
        assertFalse(memorialDay.matchesDate(LocalDate.of(2025, 5, 25)));
    }

    @Test
    public void matchesNonLaborDay() {
        assertFalse(laborDay.matchesDate(LocalDate.of(2024, 9, 9)));
        assertFalse(laborDay.matchesDate(LocalDate.of(2024, 9, 1)));
        assertFalse(laborDay.matchesDate(LocalDate.of(2023, 9, 11)));
        assertFalse(laborDay.matchesDate(LocalDate.of(2023, 9, 3)));
    }

    @Test
    public void matchesLaborDay() {
        assertTrue(laborDay.matchesDate(LocalDate.of(2024, 9, 2)));
        assertTrue(laborDay.matchesDate(LocalDate.of(2023, 9, 4)));
    }

    @Test
    public void matchesNonChristmas() {
        assertFalse(christmas.matchesDate(LocalDate.of(2002, 12, 1)));
        assertFalse(christmas.matchesDate(LocalDate.of(2023, 11, 25)));
    }

    @Test
    public void matchesChristmas() {
        assertTrue(christmas.matchesDate(LocalDate.of(2003, 12, 25)));
        assertTrue(christmas.matchesDate(LocalDate.of(2024, 12, 25)));
        assertTrue(christmas.matchesDate(LocalDate.of(1962, 12, 25)));
    }

    @Test
    public void matchesNonIndependenceDay() {
        assertFalse(independenceDay.matchesDate(LocalDate.of(2024, 6, 4)));
        assertFalse(independenceDay.matchesDate(LocalDate.of(2024, 6, 3)));
        assertFalse(independenceDay.matchesDate(LocalDate.of(2023, 7, 8)));
        assertFalse(independenceDay.matchesDate(LocalDate.of(2027, 7, 4)));
        assertFalse(independenceDay.matchesDate(LocalDate.of(2026, 7, 4)));
    }

    @Test
    public void matchesIndependenceDays() {
        assertTrue(independenceDay.matchesDate(LocalDate.of(2024, 7, 4)));
        assertTrue(independenceDay.matchesDate(LocalDate.of(2026, 7, 3)));
        assertTrue(independenceDay.matchesDate(LocalDate.of(2027, 7, 5)));

    }
}
