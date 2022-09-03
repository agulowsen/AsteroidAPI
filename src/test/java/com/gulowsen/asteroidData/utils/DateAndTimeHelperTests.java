package com.gulowsen.asteroidData.utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateAndTimeHelperTests {

    @Test
    public void getAlldatesReturnsCorrectAmountForRegularYear() {
        assertEquals(365, DateAndTimeHelper.getAllDaysInYear(2022).size());
    }

    @Test
    public void getAlldatesReturnsCorrectAmountForLeapYear() {
        assertEquals(366, DateAndTimeHelper.getAllDaysInYear(2020).size());
    }
    @Test
    public void getEverySevenDatesInRangeReturnsListOfEverySevenDays() {
        assertEquals(53, DateAndTimeHelper.getEveryStartOfWeekDateInYear(2023).size());
    }

    @Test
    public void calcEndateReturnsEnDateAsLastDayOfYearWhenDaysToAddGoesIntoNextYear() {
        LocalDate correctDate = LocalDate.of(2021, 12, 31);
        LocalDate generatedDate = DateAndTimeHelper.calculateEndDateWithinYear(LocalDate.of(2021, 12, 27), 6);
        assertEquals(correctDate, generatedDate);
    }


}
