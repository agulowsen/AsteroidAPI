package com.gulowsen.asteroidData.controllers.endpoints;


import com.gulowsen.asteroidData.controllers.AsteroidDataController;
import com.gulowsen.asteroidData.errorhandling.CustomParseException;
import com.gulowsen.asteroidData.errorhandling.DateRangeTooBigException;
import com.gulowsen.asteroidData.errorhandling.FailedFetchingDataException;
import com.gulowsen.asteroidData.errorhandling.YearNotYetIndexedException;
import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.models.NearbyRequest;
import com.gulowsen.asteroidData.models.NearbyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.LocalDate;

@Api(tags = "Asteroid Data API")
@RestController
@RequestMapping("/asteroids")
public class AsteroidRestController {

    private final String NEAREST_ENDPOINT = "/nearby";
    private final String LARGEST_ENDPOINT = "/largest";
    private final String IMPORT_ASTEROID_DATA_BY_YEAR_ENDPOINT = "/import";

    AsteroidDataController asteroidDataController;


    @ApiOperation(value = "Get data on nearby asteroids",
            notes = "Returns a list of the ten asteroids that was nearest to earth in the given date range")
    @GetMapping(NEAREST_ENDPOINT)
    public NearbyResponse findNearbyInTimePeriod(@RequestParam("from") @DateTimeFormat(pattern="dd-MM-yyyy") @ApiParam(example="22-12-2022") LocalDate fromDate,
                                                 @RequestParam("until") @DateTimeFormat(pattern="dd-MM-yyyy") @ApiParam(example="27-12-2022") LocalDate untilDate)
            throws DateRangeTooBigException, FailedFetchingDataException, SQLException, CustomParseException {
        NearbyRequest nearbyRequest = new NearbyRequest();
        nearbyRequest.setFrom(fromDate);
        nearbyRequest.setUntil(untilDate);
        return asteroidDataController.findNearbyInTimeFrame(nearbyRequest);
    }

    @ApiOperation(value = "Get data on largest asteroid",
            notes = "Returns data on the largest asteroid that passed by earth in the given year")
    @GetMapping(LARGEST_ENDPOINT)
    public AsteroidData findLargestAsteroid(@RequestParam("year") @ApiParam(example="2022") int year) throws SQLException, YearNotYetIndexedException {
        return asteroidDataController.findLargestAsteroidByYear(year);
    }

    @ApiOperation(value = "Import asteroid data",
            notes = "Imports asteroid data for given year")
    @PostMapping(IMPORT_ASTEROID_DATA_BY_YEAR_ENDPOINT)
    public void importAsteroidData(@RequestParam("year") @ApiParam(example="2022") int year,
                                   @RequestParam(value="forceUpdate", required=false) Boolean forceUpdate) throws CustomParseException, FailedFetchingDataException, SQLException {
        if(forceUpdate == null) forceUpdate = false;
        asteroidDataController.saveAsteroidDataForYear(year, forceUpdate);
    }

    @Autowired
    public void setAsteroidDataController(AsteroidDataController asteroidDataController) {
        this.asteroidDataController = asteroidDataController;
    }

    /*
    @ApiOperation(value = "Get nearby asteroids",
            notes = "Return a list of asteroids that was near to earth in the given date range")
    @GetMapping("")
    public NearbyResponse findNearbyInTimePeriod(@RequestBody NearbyRequest nearbyRequest) throws SQLException, FailedFetchingDataException, ParseException, DateRangeTooBigException {
        if(nearbyRequest.getAllDatesInRange().size() > 7) {
            throw new DateRangeTooBigException();
        }
        return repositoryManager.findNearbyInTimeFrame(nearbyRequest);
    }
     */

}
