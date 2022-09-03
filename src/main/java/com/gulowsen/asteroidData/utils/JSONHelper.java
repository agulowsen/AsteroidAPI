package com.gulowsen.asteroidData.utils;

import com.gulowsen.asteroidData.errorhandling.CustomParseException;
import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.models.CloseApproachData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// TODO: Generify and make less fragile
public class JSONHelper {

    public List<AsteroidData> parseStringToAsteroidDataList(String jsonString, LocalDate from, LocalDate until) throws CustomParseException {
        List<AsteroidData> asteroidDataList = new ArrayList<>();
        JSONParser parse = new JSONParser();
        JSONObject data_obj = null;
        try {
            data_obj = (JSONObject) parse.parse(jsonString);
        } catch (ParseException e) {
            throw new CustomParseException("Failed parsing " + jsonString, e);
        }
        JSONObject listOfDates = (JSONObject) data_obj.get("near_earth_objects");
        for (LocalDate date : DateAndTimeHelper.getAllDatesInRange(from, until)) {
            JSONArray listOfAsteroidForDate = (JSONArray) listOfDates.get(date.toString());
            if(listOfAsteroidForDate == null) {
                return new ArrayList<>();
            }
            int asteroidAmount = listOfAsteroidForDate.size();

            for(int i = 0; i < asteroidAmount; i++) {
                JSONObject asteroidDataObject = (JSONObject) listOfAsteroidForDate.get(i);
                AsteroidData asteroidData = new AsteroidData();
                asteroidData.setId((String) asteroidDataObject.get("id"));
                asteroidData.setName((String) asteroidDataObject.get("name"));

                asteroidData.setDiamMax(getDoubleField(asteroidDataObject, "estimated_diameter_max"));
                asteroidData.setDiamMin(getDoubleField(asteroidDataObject, "estimated_diameter_min"));

                asteroidData.setCloseApproachDataList(parseCloseApproachData(asteroidData.getId(), (JSONArray)asteroidDataObject.get("close_approach_data")));
                asteroidDataList.add(asteroidData);
            }
        }
        return asteroidDataList;
    }

    private double getDoubleField(JSONObject asteroidDataObject, String field) {
        if(asteroidDataObject.get("estimated_diameter") != null &&
                ((JSONObject)asteroidDataObject.get("estimated_diameter")).get("meters") != null &&
                ((JSONObject)((JSONObject)asteroidDataObject.get("estimated_diameter")).get("meters")).get(field) != null) {
            return (double) ((JSONObject)((JSONObject)asteroidDataObject.get("estimated_diameter")).get("meters")).get(field);
        } else {
            return 0;
        }
    }

    private List<CloseApproachData> parseCloseApproachData(String id, JSONArray close_approach_data_array) {
        List<CloseApproachData> closeApproachDataList = new ArrayList<>();
        if(close_approach_data_array == null) return closeApproachDataList;

        for(int i = 0; i< close_approach_data_array.size(); i++) {
            JSONObject closeApproachData = (JSONObject) close_approach_data_array.get(i);
            CloseApproachData caData = new CloseApproachData();
            String dateAsString = (String) closeApproachData.get("close_approach_date");
            caData.setDate(LocalDate.parse(dateAsString));
            caData.setAsteroid_id(id);
            JSONObject miss_distance_object = (JSONObject) closeApproachData.get("miss_distance");
            caData.setMissDistance(new BigDecimal((String) miss_distance_object.get("kilometers")));
            closeApproachDataList.add(caData);
        }

        return closeApproachDataList;
    }

}
