package com.gulowsen.asteroidData.utils;

import com.gulowsen.asteroidData.errorhandling.FailedFetchingDataException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ExternalAPIHelper {


    public String fetchData(String urlString) throws FailedFetchingDataException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if(responseCode != 200) {
                throw new FailedFetchingDataException("Got back non 200 responseCode: " + responseCode);
            }

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            scanner.close();
            return inline;

        } catch (IOException e) {
            throw new FailedFetchingDataException("Failed fetching data from " + urlString);
        }
    }

}
