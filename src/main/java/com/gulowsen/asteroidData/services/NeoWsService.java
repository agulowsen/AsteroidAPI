package com.gulowsen.asteroidData.services;

import com.gulowsen.asteroidData.errorhandling.CustomParseException;
import com.gulowsen.asteroidData.errorhandling.FailedFetchingDataException;
import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.utils.ExternalAPIHelper;
import com.gulowsen.asteroidData.utils.JSONHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NeoWsService {

    private final String NEO_TOKEN = System.getenv("NEO_API_TOKEN");
    @Value("${neows.endpoint}")
    private String NEO_API_ENDPOINT;
    private final String NEO_API_START_PARAM = "start_date";
    private final String NEO_API_END_PARAM = "end_date";
    private final String NEO_API_TOKEN_PARAM = "api_key";

    public List<AsteroidData> fetchNearbyAsteroidsForDateRange(LocalDate from, LocalDate until) throws CustomParseException, FailedFetchingDataException {
        String response = new ExternalAPIHelper().fetchData(buildUrl(from, until));
        return new JSONHelper().parseStringToAsteroidDataList(response, from, until);
    }

    private String buildUrl(LocalDate from, LocalDate until) {
        return NEO_API_ENDPOINT + "?" + NEO_API_START_PARAM + "=" + from + "&" + NEO_API_END_PARAM + "=" + until + "&" + NEO_API_TOKEN_PARAM + "=" + NEO_TOKEN;
    }



}
