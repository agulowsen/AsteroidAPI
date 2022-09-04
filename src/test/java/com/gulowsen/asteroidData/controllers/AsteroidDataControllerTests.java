package com.gulowsen.asteroidData.controllers;


import com.gulowsen.asteroidData.TestData;
import com.gulowsen.asteroidData.errorhandling.DateRangeTooBigException;
import com.gulowsen.asteroidData.repository.implementations.NeoWsRepository;
import com.gulowsen.asteroidData.repository.implementations.mysql.AsteroidDataRepositoryImpl;
import com.gulowsen.asteroidData.repository.implementations.mysql.CloseApproachDataRepositoryImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsteroidDataControllerTests {

    TestData testData = new TestData();

    @Mock
    AsteroidDataRepositoryImpl asteroidDataRepository;
    @Mock
    CloseApproachDataRepositoryImpl closeApproachDataRepository;
    @Mock
    NeoWsRepository neoWsRepository;

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


}
