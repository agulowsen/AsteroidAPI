package com.gulowsen.asteroidData;

import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.models.NearbyRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    public List<AsteroidData> getAsteroidTestData() {
        List<AsteroidData> asteroidDataList = new ArrayList<>();

        AsteroidData asteroidData = new AsteroidData();
        asteroidData.setName("Testoroid 1");
        asteroidData.setDiamMin(345.213);
        asteroidData.setDiamMin(200.400);
        asteroidData.setId("SU1223412");
        asteroidDataList.add(asteroidData);

        return asteroidDataList;

    }

    public List<NearbyRequest> getNearbyRequestList() {
        List<NearbyRequest> nearbyRequestList = new ArrayList<>();
        NearbyRequest nearbyRequest = new NearbyRequest();
        nearbyRequest.setFrom(LocalDate.of(2020,12,20));
        nearbyRequest.setUntil(LocalDate.of(2020,12,21));
        nearbyRequestList.add(nearbyRequest);

        NearbyRequest nearbyRequest1 = new NearbyRequest();
        nearbyRequest1.setFrom(LocalDate.of(2020,12,20));
        nearbyRequest1.setUntil(LocalDate.of(2020,12,21));
        nearbyRequestList.add(nearbyRequest1);

        NearbyRequest nearbyRequest2 = new NearbyRequest();
        nearbyRequest2.setFrom(LocalDate.of(2020,12,20));
        nearbyRequest2.setUntil(LocalDate.of(2020,12,21));
        nearbyRequestList.add(nearbyRequest2);
        return nearbyRequestList;
    }

    public NearbyRequest getNearbyRequest(LocalDate from, LocalDate until) {
        NearbyRequest nearbyRequest = new NearbyRequest();
        nearbyRequest.setFrom(from);
        nearbyRequest.setUntil(until);
        return nearbyRequest;
    }
}
