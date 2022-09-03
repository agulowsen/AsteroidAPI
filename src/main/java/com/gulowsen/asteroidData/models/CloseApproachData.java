package com.gulowsen.asteroidData.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloseApproachData implements Comparable<CloseApproachData> {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;
    private String asteroid_id;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;
    private BigDecimal missDistance;
    @JsonIgnore
    private LocalDateTime created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsteroid_id() {
        return asteroid_id;
    }

    public void setAsteroid_id(String asteroid_id) {
        this.asteroid_id = asteroid_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getMissDistance() {
        return missDistance;
    }

    public void setMissDistance(BigDecimal missDistance) {
        this.missDistance = missDistance;
    }

    @Override
    public int compareTo(CloseApproachData o) {
        if(getMissDistance() == null || o.getMissDistance() == null)
            return 0;
        return getMissDistance().compareTo(o.getMissDistance());
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
