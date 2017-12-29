package com.backbase.atmlocator.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.atmlocator.entity.Location;
import com.backbase.atmlocator.service.INGConsumerService;
import com.backbase.atmlocator.service.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Controller class that exposes the REST services to be consumed by the
 * frontend. It uses the INGConsumerService bean (injected by the framework) to
 * get data from ING service and process it before sending to frontend.
 * 
 * @author Fabio Fonseca
 *
 */
@RestController
public class ATMLocatorController {

    /**
     * The INGConsumerService bean injected by spring
     */
    @Autowired
    private INGConsumerService consumer;
    
    @Autowired
    private LocationService locationService;

    private static final Logger logger = LoggerFactory.getLogger(ATMLocatorController.class);

    @RequestMapping(value = "/api/list", method = { GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> listAllCities() throws JsonProcessingException {
        // extract the unique cities names from the list of all locations
        ArrayList<Location> locationList = consumer.getAllINGATMList();
        if (locationList == null) {
            // some error (already logged) occurred while retrieving data from
            // ING service. So, return an empty response.
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        ArrayList<String> cities = new ArrayList<String>(Arrays.asList(locationService.getUniqueCityNames(locationList)));
        logger.info("Returned ".concat(Integer.toString(cities.size())).concat(" cities!"));
        // prepare the response as a JSON object
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/list/{cityName}", method = { GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> listATMByCity(@PathVariable String cityName) throws JsonProcessingException {
        // extract the unique cities names from the list of all locations
        ArrayList<Location> locationList = consumer.getAllINGATMList();
        if (locationList == null) {
            // some error (already logged) occurred while retrieving data from
            // ING service. So, return an empty response.
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        ArrayList<Location> locations = locationService.filterLocationByCity(locationList, cityName);
        logger.info("Returned ".concat(Integer.toString(locations.size())).concat(" locations!"));
        return new ResponseEntity<>(locations, HttpStatus.OK);

    }
}