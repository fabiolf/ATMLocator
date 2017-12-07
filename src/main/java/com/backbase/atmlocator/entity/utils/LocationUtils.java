package com.backbase.atmlocator.entity.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.backbase.atmlocator.entity.Location;

/**
 * Some static util methods to handle the Locations returned by ING.
 * 
 * @author Fabio Fonseca
 *
 */
public class LocationUtils {

    /**
     * This static method extracts the unique city names from all locations
     * returned by ING.
     * 
     * @param locations
     *            a list of Location(s)
     * @return an ArrayList<String> with the unique city names
     */
    static public String[] getUniqueCityNames(List<Location> locations) {
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
    
    static public ArrayList<Location> filterLocationByCity(List<Location> locations, String city) {
        ArrayList<Location> locList = new ArrayList<Location>();
        for (Location loc : locations) {
            // get the city name from the location
            String c = loc.getAddress().getCity();
            if (c.equals(city)) { 
                locList.add(loc);
            }
        }
        return locList;
//        Location[] result = new Location[locList.size()];
//        result = locList.toArray(result);
//        return result;
    }
}
