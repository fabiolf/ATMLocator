package com.backbase.atmlocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.backbase.atmlocator.entity.Location;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // @Bean
    // public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // return builder.build();
    // }
    //
    // @Bean
    // public RestTemplate restTeamplate(RestTemplateBuilder
    // restTemplateBuilder) {
    // return restTemplateBuilder.build();
    // }
    //
    // @Bean
    // public CommandLineRunner run(RestTemplate restTemplate) throws Exception
    // {
    // return args -> {
    // Location loc =
    // restTemplate.getForObject("https://www.ing.nl/api/locator/atms/",
    // Location.class);
    // System.out.println(loc.toString());
    // };
    // }

//    @Bean
    // ApplicationContext ctx
//    public CommandLineRunner run() throws Exception {
//        return args -> {
//            try {
//
//                URL url = new URL("https://www.ing.nl/api/locator/atms/");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Accept", "application/json");
//
//                if (conn.getResponseCode() != 200) {
//                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//                }
//
//                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//
//                // The service is returning 6 char (including EOL char) of junk
//                // in the beginning, so we need to clean before doing the mapping
//                // TODO need a more robust solution to this problem. parsing
//                // each line of the response may do the trick
//                br.skip(6);
//                // the next line is the JSON object
//                String jsonResponse = br.readLine();
//
//                if (jsonResponse == null) {
//                    throw new RuntimeException("Service returned no ATMs");
//                }
//
//                // trying object mapper
//                ObjectMapper mapper = new ObjectMapper();
//                JsonParser parser = mapper.getFactory().createParser(jsonResponse);
//
//                Location[] loc = mapper.readValue(jsonResponse, Location[].class);
//
//                System.out.println(loc[0].toString());
//                System.out.println(loc.length);
//                conn.disconnect();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//    }
}
