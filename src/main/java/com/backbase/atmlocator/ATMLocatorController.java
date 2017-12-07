package com.backbase.atmlocator;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.atmlocator.entity.Location;
import com.backbase.atmlocator.entity.utils.LocationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ATMLocatorController {

    @Autowired
    private ApplicationContext context;

    @RequestMapping(value = "/list", method = { POST, GET })
    public String listAllCities() throws JsonProcessingException {
        // get the INGServiceConsumer bean from the ApplicationContext injected
        // in this controller
        INGServiceConsumer consumer = (INGServiceConsumer) context.getBean("INGServiceConsumer");
        // extract the unique cities names from the list of all locations
        ArrayList<String> cities = new ArrayList<String>(
                Arrays.asList(LocationUtils.getUniqueCityNames(consumer.getAllINGATMList())));
        // prepare the response as a JSON object
        System.out.println("Returned " + cities.size() + " cities!");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(cities);
//        return response;
    }

    @RequestMapping(value = "/list/{cityName}", method = { POST, GET })
    public String listATMByCity(@PathVariable String cityName) throws JsonProcessingException {
        // get the INGServiceConsumer bean from the ApplicationContext injected
        // in this controller
        INGServiceConsumer consumer = (INGServiceConsumer) context.getBean("INGServiceConsumer");
        // extract the unique cities names from the list of all locations
        ArrayList<Location> locations = LocationUtils.filterLocationByCity(consumer.getAllINGATMList(), cityName);
        System.out.println("Returned " + locations.size() + " locations!");
        ObjectMapper mapper = new ObjectMapper();
        // this required "jackson-databind" dependency to be set in the POM file
        return mapper.writeValueAsString(locations);
        
    }
}