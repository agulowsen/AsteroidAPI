package com.gulowsen.asteroidData.tasks;


import com.gulowsen.asteroidData.controllers.AsteroidDataController;
import com.gulowsen.asteroidData.errorhandling.CustomParseException;
import com.gulowsen.asteroidData.errorhandling.FailedFetchingDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@EnableAsync
@Component
public class AsteroidDataImporterTask {

    @Value("${import.startyear}")
    private int startYear;
    @Value("${import.endyear}")
    private int endYear;

    @Value("${import.startyear}")
    private int currentYear;


    AsteroidDataController asteroidDataController;

    public AsteroidDataImporterTask() {
        this.currentYear = startYear;
    }

    @Async
    @Scheduled(cron = "${import.cron.interval}")
    public void importAsteroidData() throws FailedFetchingDataException, SQLException, CustomParseException {
        if(currentYear <= endYear) {
            int yearToImport = currentYear;
            System.out.println("Importing asteroid data for " + yearToImport);
            currentYear++;
            asteroidDataController.saveAsteroidDataForYear(yearToImport);
            System.out.println("Done importing asteroid data for " + yearToImport);
        }else {
            currentYear = startYear;
        }

    }

    @Autowired
    public void setAsteroidDataController(AsteroidDataController asteroidDataController) {
        this.asteroidDataController = asteroidDataController;
    }
}
