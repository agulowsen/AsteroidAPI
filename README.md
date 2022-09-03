# Asteroid API
A small Spring boot application that exposes computed data from NASAs NeoWS API.

```
Health can be verified on /  Should return 200 and "OK"
Version can be checked on /version
```

## Swagger
#### Swagger UI: /swagger-ui/

## Installation


### Environment variables
The application uses the following environment variables

|Variable | Value  | Note |
| ------------- | ------------- | ------------- |
|DB_ENDPOINT| Endpoint for database  | Only supports MySQL
|DB_USERNAME| Username for database  | 
|DB_PASSWORD| Password for database  | 
|NEO_API_TOKEN| API Token for NeoWS  | https://api.nasa.gov


### Create database schemas (MySQL)

```
create table AsteroidData (
    id varchar(30) PRIMARY KEY UNIQUE,
    name varchar(100),
    diam_min double(14,3),
    diam_max double(14,3),
    created datetime
);

create table CloseApproachData (
    id int  PRIMARY KEY AUTO_INCREMENT,
    asteroid_id varchar(30),
    FOREIGN KEY (asteroid_id) references AsteroidData(id),
    miss_distance varchar(30),
    date DATE,
    created datetime
);

create table LargestAsteroid (
    year int PRIMARY KEY,
    asteroid_id varchar(30),
    FOREIGN KEY (asteroid_id) references AsteroidData(id),
    created datetime
);
```

### Build application
Application is built on Java 11
```
mvn clean install
```

### Run application
```
mvn spring-boot:run
```

### Scheduled task
There is a scheduled task that runs and updates asteroid data and largest asteroid table. 
This job can also be triggered manually by sending a POST request to /asteroids/import with the year as parameter.
