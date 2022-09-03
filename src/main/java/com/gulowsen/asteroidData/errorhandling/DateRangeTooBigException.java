package com.gulowsen.asteroidData.errorhandling;

public class DateRangeTooBigException extends Throwable{
    public DateRangeTooBigException() {
        super("Date range is too long. Only supports query for upto 7 days");
    }
}
