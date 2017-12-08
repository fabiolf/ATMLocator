package com.backbase.atmlocator.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.backbase.atmlocator.entity.Location;

/**
 * This service perform some processing at the ING service data to feed the rest
 * services exposed by the ATMLocatorController
 * 
 * @author Fabio Fonseca
 *
 */
@Service
public class LocationService {
    // TODO it would be useful to store the results of ING service call into a
    // HashTable where the key is the city. But to keep this structure updated,
    // I would need some approach to recreate it from time to time or after n
    // calls to the service... or both! Think about it if I have time at the
    // end.

    /**
     * This method extracts the unique city names from all locations returned by
     * ING.
     * 
     * @param locations
     *            the complete list of locations
     * @return an ArrayList<String> with the unique city names
     */
    public String[] getUniqueCityNames(List<Location> locations) {
        // use a HashSet (constant time for add and contains) to hold the unique
        // city names
        HashSet<String> tree = new HashSet<String>();
        for (Location loc : locations) {
            // get the city name from the location
            String city = loc.getAddress().getCity();
            if (!tree.contains(city)) {
                tree.add(city);
            }
        }
        String[] result = new String[tree.size()];
        result = tree.toArray(result);
        return result;
    }

    /**
     * This method filters from the complete list the ATM that is located in the
     * city received as parameter
     * 
     * @param locations
     *            the complete list of locations
     * @param city
     *            the city name to filter the results
     * @return an ArrayList<Location> with the records that matches the search
     *         criteria (filtered by city name)
     */
    public ArrayList<Location> filterLocationByCity(List<Location> locations, String city) {
        ArrayList<Location> locList = new ArrayList<Location>();
        for (Location loc : locations) {
            // get the city name from the location
            String c = loc.getAddress().getCity();
            if (c.equals(city)) {
                locList.add(loc);
            }
        }
        return locList;
    }
}
