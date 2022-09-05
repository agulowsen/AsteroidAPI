package com.gulowsen.asteroidData.controllers;

import com.gulowsen.asteroidData.constants.mysql.DatabaseConstants;
import com.gulowsen.asteroidData.errorhandling.CustomParseException;
import com.gulowsen.asteroidData.errorhandling.DateRangeTooBigException;
import com.gulowsen.asteroidData.errorhandling.FailedFetchingDataException;
import com.gulowsen.asteroidData.errorhandling.YearNotYetIndexedException;
import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.models.CloseApproachData;
import com.gulowsen.asteroidData.models.NearbyRequest;
import com.gulowsen.asteroidData.models.NearbyResponse;
import com.gulowsen.asteroidData.repository.implementations.mysql.AsteroidDataRepositoryImpl;
import com.gulowsen.asteroidData.repository.implementations.mysql.LargestDataRepositoryImpl;
import com.gulowsen.asteroidData.repository.interfaces.AsteroidDataRepository;
import com.gulowsen.asteroidData.repository.interfaces.CloseApproachDataMySQLRepositoryImpl;
import com.gulowsen.asteroidData.repository.interfaces.LargestAsteroidRepository;
import com.gulowsen.asteroidData.services.NeoWsService;
import com.gulowsen.asteroidData.utils.DateAndTimeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AsteroidDataController {

    @Value("${nearby.max.hits}")
    private int maxHits;


    private AsteroidDataRepository asteroidDataRepository;
    private CloseApproachDataMySQLRepositoryImpl closeApproachDataRepository;
    private NeoWsService neoWsService;
    private LargestAsteroidRepository largestAsteroidRepository;

    public NearbyResponse findNearbyInTimeFrame(NearbyRequest nearbyRequest) throws SQLException, FailedFetchingDataException, CustomParseException, DateRangeTooBigException {
        validateNearbyRequest(nearbyRequest);
        if(!eachDateHasDatabaseEntry(DateAndTimeHelper.getAllDatesInRange(nearbyRequest.getFrom(), nearbyRequest.getUntil()))) {
            List<AsteroidData> asteroidData = neoWsService.fetchNearbyAsteroidsForDateRange(nearbyRequest.getFrom(), nearbyRequest.getUntil());
            persistAsteroidList(asteroidData);
            persistCloseApproachDataList(asteroidData);
        }

        List<CloseApproachData> closeApproachesInDateRange = closeApproachDataRepository.findInDateRange(nearbyRequest.getFrom(), nearbyRequest.getUntil());
        Collections.sort(closeApproachesInDateRange);
        return new NearbyResponse(getAsteroidDataForCloseApproaches(closeApproachesInDateRange.stream().limit(maxHits).collect(Collectors.toList())));
    }

    public void saveAsteroidDataForYear(int year, boolean forceUpdate) throws FailedFetchingDataException, CustomParseException, SQLException {
        if(forceUpdate || yearShouldBeUpdated(year)) {
            AsteroidData currentLargest = largestAsteroidRepository.getLargestAsteroidByYear(year);
            List<LocalDate> allStartOfWeekDaysInYear = DateAndTimeHelper.getEveryStartOfWeekDateInYear(year);
            for (LocalDate date : allStartOfWeekDaysInYear) {
                List<AsteroidData> asteroidDataList = neoWsService.fetchNearbyAsteroidsForDateRange(date, DateAndTimeHelper.calculateEndDateWithinYear(date,6));
                for (AsteroidData asteroidData : asteroidDataList) {
                    if(asteroidData.getDiamMax() > currentLargest.getDiamMax())
                        currentLargest = asteroidData;
                }
                persistAsteroidList(asteroidDataList);
            }
            largestAsteroidRepository.save(currentLargest, year);
        }
    }

    public AsteroidData findLargestAsteroidByYear(int year) throws SQLException, YearNotYetIndexedException {
        AsteroidData asteroidData = largestAsteroidRepository.getLargestAsteroidByYear(year);
        if(ObjectUtils.isEmpty(asteroidData.getId())) {
            throw new YearNotYetIndexedException(year);
        }
        return asteroidDataRepository.findById(asteroidData.getId());
    }

    // TODO: Also refresh old data
    protected boolean yearShouldBeUpdated(int year) throws SQLException {
        final AsteroidData largestAsteroidByYear = largestAsteroidRepository.getLargestAsteroidByYear(year);
        return largestAsteroidByYear == null || ObjectUtils.isEmpty(largestAsteroidByYear.getId());
    }

    protected void validateNearbyRequest(NearbyRequest nearbyRequest) throws DateRangeTooBigException {
        if(nearbyRequest.getAllDatesInRange().size() > 7) {
            throw new DateRangeTooBigException();
        }
    }

    private List<AsteroidData> getAsteroidDataForCloseApproaches(List<CloseApproachData> closeApproachDataList) throws SQLException {
        List<AsteroidData> asteroidDataList = new ArrayList<>();
        for (CloseApproachData closeApproachData : closeApproachDataList) {
            AsteroidData asteroidData = asteroidDataRepository.findById(closeApproachData.getAsteroid_id());
            asteroidData.addCloseApproachData(closeApproachData);
            asteroidDataList.add(asteroidData);
        }
        return asteroidDataList;
    }

    /**
     *  Returns <b>true</b> if each entry in dates has at least one database entry. Else returns <b>false</b>
     */
    private boolean eachDateHasDatabaseEntry(List<LocalDate> dates) throws SQLException {
        for (LocalDate date : dates) {
            if(closeApproachDataRepository.findAmountForDate(date) == 0) {
                return false;
            }
        }
        return true;
    }

    protected void persistAsteroidList(List<AsteroidData> asteroidData) throws SQLException {
        for (AsteroidData asteroid : asteroidData) {
            if(!asteroidExists(asteroid)) {
                asteroidDataRepository.save(asteroid);
            }
        }
    }

    protected void persistCloseApproachDataList(List<AsteroidData> asteroidData) throws SQLException {
        for (AsteroidData asteroid : asteroidData) {
            for(CloseApproachData closeApproachData : asteroid.getCloseApproachDataList()) {
                if(!closeApproachDataExists(closeApproachData)) {
                    closeApproachDataRepository.save(closeApproachData);
                }
            }
        }
    }

    public boolean asteroidExists(AsteroidData asteroid) throws SQLException {
        return asteroidDataRepository.queryCountOfElement(DatabaseConstants.ASTEROID_DATA_SCHEMA, DatabaseConstants.ASTEROID_DATA_FIELD_NAME, asteroid.getName(), null) != 0;
    }

    protected boolean closeApproachDataExists(CloseApproachData closeApproachData) throws SQLException {
        return closeApproachDataRepository.findAmountForDateAndAsteroidId(closeApproachData.getDate(), closeApproachData.getAsteroid_id()) != 0;
    }

    @Autowired
    public void setAsteroidDataRepository(AsteroidDataRepositoryImpl asteroidDataRepository) {
        this.asteroidDataRepository = asteroidDataRepository;
    }

    @Autowired
    public void setCloseApproachDataRepository(CloseApproachDataMySQLRepositoryImpl closeApproachDataRepository) {
        this.closeApproachDataRepository = closeApproachDataRepository;
    }

    @Autowired
    public void setLargestAsteroidRepository(LargestDataRepositoryImpl largestAsteroidRepository){
        this.largestAsteroidRepository = largestAsteroidRepository;
    }

    @Autowired
    public void setNeoWsRepository(NeoWsService neoWsService) {
        this.neoWsService = neoWsService;
    }

}
