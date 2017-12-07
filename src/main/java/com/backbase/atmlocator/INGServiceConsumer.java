package com.backbase.atmlocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backbase.atmlocator.entity.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class INGServiceConsumer {

    private static final String INGURL = "https://www.ing.nl/api/locator/atms/";

    public ArrayList<Location> getAllINGATMList() {
        try {
            URL url = new URL(INGURL);
            // using standard Http Connection to consume the service because it
            // returns some junk chars at the beginning of the response and that
            // breaks the nice springboot way of consuming services
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                // TODO manage this problem better. We don't want our
                // application to explode in the backend without the user even
                // get a warn in the frontend
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            // The service is returning 6 char (including EOL char) of junk
            // in the beginning, so we need to clean before doing the mapping
            // TODO need a more robust solution to this problem. parsing
            // each line of the response may do the trick
            br.skip(6);
            // the next line is the JSON object we want
            String jsonResponse = br.readLine();

            if (jsonResponse == null) {
                // TODO again, process this error in a better way
                throw new RuntimeException("Service returned no ATMs");
            }

            conn.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonResponse, new TypeReference<ArrayList<Location>>(){});
        } catch (MalformedURLException e) {
            //TODO
            e.printStackTrace();
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
        return null;
    }
}
