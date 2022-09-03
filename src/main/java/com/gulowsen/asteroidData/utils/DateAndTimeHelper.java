package com.gulowsen.asteroidData.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateAndTimeHelper {

    public static List<LocalDate> getAllDatesInRange(LocalDate from, LocalDate until) {
        return from.datesUntil(until.plusDays(1)).collect(Collectors.toList());
    }

    public static List<LocalDate> getEveryStartOfWeekDateInYear(int year) {
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate until =  LocalDate.of(year, 12, 31);
        return Stream.iterate(from, d -> !d.isAfter(until), d -> d.plusWeeks(1)).collect(Collectors.toList());
    }

    public static List<LocalDate> getAllDaysInYear(int year) {
        return getAllDatesInRange(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
    }

    public static LocalDateTime parseTimestampToLocaleDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public static LocalDate calculateEndDateWithinYear(LocalDate date, int daysToAdd) {
        LocalDate lastDayOfYear = LocalDate.of(date.getYear(), 12, 31);
        if(!date.plusDays(daysToAdd).isAfter(lastDayOfYear)) {
            return date.plusDays(daysToAdd);
        }
        daysToAdd--;
        return calculateEndDateWithinYear(date, daysToAdd);
    }
}
