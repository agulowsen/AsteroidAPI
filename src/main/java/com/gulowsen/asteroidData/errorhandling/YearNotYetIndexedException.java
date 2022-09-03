package com.gulowsen.asteroidData.errorhandling;

public class YearNotYetIndexedException extends Throwable {

    private int year;
    public YearNotYetIndexedException(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }
}
