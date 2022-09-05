package com.gulowsen.asteroidData.constants.mysql;

public final class DatabaseConstants {

    // Schemas
    public static final String ASTEROID_DATA_SCHEMA = "AsteroidData";
    public static final String CLOSE_APPROACH_DATA_SCHEMA = "CloseApproachData";
    public static final String LARGEST_ASTEROID_SCHEMA = "LargestAsteroid";

    // Fields
    public static final String CREATED_DATETIME = "created";
    public static final String ASTEROID_DATA_FIELD_ID = "id";
    public static final String ASTEROID_DATA_FIELD_NAME = "name";
    public static final String ASTEROID_DATA_FIELD_MAX_DIAM = "diam_max";
    public static final String ASTEROID_DATA_FIELD_MIN_DIAM = "diam_min";
    public static final String CLOSE_APPROACH_DATA_FIELD_ID = "id";
    public static final String CLOSE_APPROACH_DATA_FIELD_ASTEROID_ID = "asteroid_id";
    public static final String CLOSE_APPROACH_DATA_FIELD_DATE = "date";
    public static final String CLOSE_APPROACH_DATA_FIELD_MISS_DISTANCE = "miss_distance";
    public static final String LARGEST_ASTEROID_YEAR = "year";
    public static final String LARGEST_ASTEROID_ASTEROID_ID = "asteroid_id";

}
