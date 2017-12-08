package com.backbase.atmlocator.service;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backbase.atmlocator.entity.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This service bean aims at encapsulating the logic and code to consume the ING
 * Service. At the date where this class was built, the ING service had a
 * problem where it was returning 6 bytes of unexpected chars before the JSON
 * response. So there is some code here to handle that. 
 * 
 * @author Fabio Fonseca
 *
 */
@Service
public class INGConsumerService {

    private static final String INGURL = "https://www.ing.nl/api/locator/atms/";
    private static final Logger logger = LoggerFactory.getLogger(INGConsumerService.class);

    /**
     * This method retrieves the data from the service mapped into the model classes.
     * @return an ArrayList of Location if all goes right or null otherwise.
     */
    public ArrayList<Location> getAllINGATMList() {
        RestTemplate restTemplate = new RestTemplate();
        // since the ING service is returning some bad chars at the
        // beginning, I have to map the result of the call into a String,
        // clean it and then perform the JSON map into may model class. If
        // the service was returning clean data, it would be much simpler,
        // like the example below:
        // Location[] loc = rest.getForObject("https://www.ing.nl/api/locator/atms/", Location[].class);
        ResponseEntity<String> response = restTemplate.getForEntity(INGURL, String.class);
        // verifying if we could call the service successfully
        if (response.getStatusCodeValue() != 200) {
            // we got some error
            logger.error(new StringBuilder().append("Failed to call service [").append(INGURL)
                    .append("]. Response code: ").append(response.getStatusCodeValue()).toString());

            return null;
        }

        // getting response body and cleaning up first 6 bytes of junk
        String jsonBody = response.getBody();
        if (jsonBody.length() == 0) {
            // we've got an empty response from the service
            logger.warn(new StringBuilder().append("Service [").append(INGURL).append("] returned an empty response")
                    .toString());
        } else {
            // if not empty then, let's clean it, if needed
            if (!jsonBody.startsWith("[") && !jsonBody.startsWith("{")) {
                jsonBody = jsonBody.substring(jsonBody.indexOf("[") - 1);
            }
        }

        // use Jackson ObjectMapper to translate from JSON to my model classes
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonBody, new TypeReference<ArrayList<Location>>() {
            });
        } catch (IOException e) {
            logger.error("Failed to map the returned json into a list of Locations. Exception message: "
                    .concat(e.getMessage()));
            return null;
        }
    }
}
