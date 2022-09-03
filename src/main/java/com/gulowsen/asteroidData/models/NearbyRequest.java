package com.gulowsen.asteroidData.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gulowsen.asteroidData.utils.DateAndTimeHelper;
import io.swagger.annotations.ApiParam;

import java.time.LocalDate;
import java.util.List;

public class NearbyRequest {

    // TODO: Fix format of dates displayed wrong in Swagger

    @ApiParam(example = "14.04.1990")
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate from;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate until;

    public LocalDate getFrom() {
        return this.from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getUntil() {
        return this.until;
    }

    public void setUntil(LocalDate until) {
        this.until = until;
    }

    public List<LocalDate> getAllDatesInRange() {
        return DateAndTimeHelper.getAllDatesInRange(from, until);
    }
}
