package com.gulowsen.asteroidData.controllers;


import com.gulowsen.asteroidData.TestData;
import com.gulowsen.asteroidData.errorhandling.CustomParseException;
import com.gulowsen.asteroidData.errorhandling.DateRangeTooBigException;
import com.gulowsen.asteroidData.errorhandling.FailedFetchingDataException;
import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.repository.implementations.mysql.AsteroidDataRepositoryImpl;
import com.gulowsen.asteroidData.repository.implementations.mysql.CloseApproachDataRepositoryImpl;
import com.gulowsen.asteroidData.repository.interfaces.LargestAsteroidRepository;
import com.gulowsen.asteroidData.services.NeoWsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsteroidDataControllerTests {

    TestData testData = new TestData();

    @Mock
    AsteroidDataRepositoryImpl asteroidDataRepository;
    @Mock
    CloseApproachDataRepositoryImpl closeApproachDataRepository;
    @Mock
    LargestAsteroidRepository largestAsteroidRepository;
    @Mock
    NeoWsService neoWsService;


    @InjectMocks
    AsteroidDataController asteroidDataController;


    @BeforeEach
    public void createMocks () {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAsteroidExistsReturnsTrueWhenCountIsOne() throws SQLException {
        when(asteroidDataRepository.queryCountOfElement(anyString(), anyString(), anyString(), isNull())).thenReturn(1);
        assertTrue(asteroidDataController.asteroidExists(testData.getAsteroidTestData().get(0)));
    }

    @Test
    public void testAsteroidExistsReturnsFalseWhenCountIsZero() throws SQLException {
        when(asteroidDataRepository.queryCountOfElement(anyString(), anyString(), anyString(), isNull())).thenReturn(0);
        assertFalse(asteroidDataController.asteroidExists(testData.getAsteroidTestData().get(0)));
    }

    @Test
    public void testValidateNearbyRequestThrowsErrorWhenGettingEightDates() {
        DateRangeTooBigException exception = assertThrows(
             DateRangeTooBigException.class,
                () -> asteroidDataController.validateNearbyRequest(testData.getNearbyRequest(LocalDate.of(2020,1,1), LocalDate.of(2020,1,8)))
        );
        assertEquals("Date range is too long. Only supports query for upto 7 days", exception.getMessage());
    }

    @Test
    public void testYearShouldBeUpdatedReturnsFalseWhenEntryExists() throws SQLException {
        when(largestAsteroidRepository.getLargestAsteroidByYear(anyInt())).thenReturn(testData.getAsteroidTestData().get(0));
        assertFalse(asteroidDataController.yearShouldBeUpdated(2002));
    }

    @Test
    public void testYearShouldBeUpdatedReturnsTrueWhenEntryDoesNotExists() throws SQLException {
        when(largestAsteroidRepository.getLargestAsteroidByYear(anyInt())).thenReturn(null);
        assertTrue(asteroidDataController.yearShouldBeUpdated(2002));
    }

    @Test
    public void testYearShouldBeUpdatedReturnsTrueWhenEntryHasNoId() throws SQLException {
        when(largestAsteroidRepository.getLargestAsteroidByYear(anyInt())).thenReturn(testData.getEmptyAsteroidData());
        assertTrue(asteroidDataController.yearShouldBeUpdated(2002));
    }

    @Test
    public void verifyLargestAsteroidRepositoryIsNotCalledWhenYearShouldBeUpdated() throws SQLException, FailedFetchingDataException, CustomParseException {
        final AsteroidData asteroidData = testData.getAsteroidTestData().get(0);
        when(largestAsteroidRepository.getLargestAsteroidByYear(2020)).thenReturn(asteroidData);
        asteroidDataController.saveAsteroidDataForYear(2020, false);
        verify(largestAsteroidRepository, times(0)).save(asteroidData, 2020);
        verify(neoWsService, times(0)).fetchNearbyAsteroidsForDateRange(any(), any());
    }

    @Test
    public void testForceRefreshOverridesYearShouldBeUpdatedCheck() throws SQLException, FailedFetchingDataException, CustomParseException {
        final AsteroidData asteroidData = testData.getAsteroidTestData().get(0);
        when(largestAsteroidRepository.getLargestAsteroidByYear(2020)).thenReturn(asteroidData);
        asteroidDataController.saveAsteroidDataForYear(2020, true);
        verify(largestAsteroidRepository).save(asteroidData, 2020);
        verify(neoWsService, atLeastOnce()).fetchNearbyAsteroidsForDateRange(any(), any());
    }


}
