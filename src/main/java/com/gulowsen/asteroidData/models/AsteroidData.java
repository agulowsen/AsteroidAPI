package com.gulowsen.asteroidData.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsteroidData {

    private String id;
    private String name;
    private double diamMax;
    private double diamMin;

    @JsonIgnore
    private LocalDateTime created;

    private List<CloseApproachData> closeApproachDataList;

    public AsteroidData() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiamMax() {
        return diamMax;
    }

    public void setDiamMax(double diamMax) {
        this.diamMax = diamMax;
    }

    public double getDiamMin() {
        return diamMin;
    }

    public void setDiamMin(double diamMin) {
        this.diamMin = diamMin;
    }

    public List<CloseApproachData> getCloseApproachDataList() {
        return closeApproachDataList;
    }

    public void setCloseApproachDataList(List<CloseApproachData> closeApproachDataList) {
        this.closeApproachDataList = closeApproachDataList;
    }

    public void addCloseApproachData(CloseApproachData closeApproachData) {
        if(this.closeApproachDataList == null)
            this.closeApproachDataList = new ArrayList<>();
        this.closeApproachDataList.add(closeApproachData);
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
