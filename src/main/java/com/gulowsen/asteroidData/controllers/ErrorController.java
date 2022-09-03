package com.gulowsen.asteroidData.controllers;

import com.gulowsen.asteroidData.errorhandling.APIError;
import com.gulowsen.asteroidData.errorhandling.CustomParseException;
import com.gulowsen.asteroidData.errorhandling.DateRangeTooBigException;
import com.gulowsen.asteroidData.errorhandling.FailedFetchingDataException;
import com.gulowsen.asteroidData.errorhandling.YearNotYetIndexedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ErrorController {

    //INTERNAL ERROR CODES
    private final String SQL_ERROR_CODE = "500S";


    @ExceptionHandler({YearNotYetIndexedException.class})
    public ResponseEntity<APIError> handleYearNotYetIndexedException(YearNotYetIndexedException e) {
        return new ResponseEntity<>(
                new APIError(
                        HttpStatus.NOT_FOUND,
                        "The year " + e.getYear() + " has not yet been indexed. Please try again later"
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({DateRangeTooBigException.class})
    public ResponseEntity<APIError> handleDateRangeTooBigException(DateRangeTooBigException e) {
        return new ResponseEntity<>(
                new APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<APIError> handleSQLException(SQLException e) {
        e.printStackTrace();
        return new ResponseEntity<>(
                new APIError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Errorcode " + SQL_ERROR_CODE
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({FailedFetchingDataException.class})
    public ResponseEntity<APIError> handleFailedFetchingDataException(FailedFetchingDataException e) {
        e.printStackTrace();
        return new ResponseEntity<>(
                new APIError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({CustomParseException.class})
    public ResponseEntity<APIError> handleParseException(CustomParseException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
        return new ResponseEntity<>(
                new APIError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}
