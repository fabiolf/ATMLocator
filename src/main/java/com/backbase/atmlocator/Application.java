package com.backbase.atmlocator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * The SpringBoot Application class. The entry point for the framework to start
 * the application.
 * 
 * @author Fabio Fonseca
 *
 */
@SpringBootApplication
public class Application  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
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

    // @Bean
    // public CommandLineRunner run() throws Exception {
    // return args -> {

    // RestTemplate rest = new RestTemplate();
    // // since the ING service is returning some bad chars at the
    // // beginning, I have to map the result of the call into a String,
    // // clean it and then perform the JSON map into may model class. If
    // // the service was returning clean data, it would be much simpler,
    // // like the example below:
    // // Location[] loc =
    // rest.getForObject("https://www.ing.nl/api/locator/atms/",
    // Location[].class);
    // ResponseEntity<String> response =
    // rest.getForEntity("https://www.ing.nl/api/locator/atms/", String.class);
    // // cleaning up first 6 bytes of junk
    // String dirtyBody = response.getBody();
    // String cleanBody = dirtyBody.substring(dirtyBody.indexOf("[") - 1);
    //
    // // using a Jackson ObjectMapper to translate from JSON to my model
    // // classes
    // ObjectMapper mapper = new ObjectMapper();
    // Location[] loc = mapper.readValue(cleanBody, Location[].class);

    // URL url = new URL("https://www.ing.nl/api/locator/atms/");
    // HttpURLConnection conn = (HttpURLConnection)
    // url.openConnection();
    // conn.setRequestMethod("GET");
    // conn.setRequestProperty("Accept", "application/json");
    //
    // if (conn.getResponseCode() != 200) {
    // throw new RuntimeException("Failed : HTTP error code : " +
    // conn.getResponseCode());
    // }
    //
    // BufferedReader br = new BufferedReader(new
    // InputStreamReader((conn.getInputStream())));
    //
    // // The service is returning 6 char (including EOL char) of junk
    // // in the beginning, so we need to clean before doing the mapping
    // // TODO need a more robust solution to this problem. parsing
    // // each line of the response may do the trick
    // br.skip(6);
    // // the next line is the JSON object
    // String jsonResponse = br.readLine();
    //
    // if (jsonResponse == null) {
    // throw new RuntimeException("Service returned no ATMs");
    // }
    //
    // // trying object mapper
    // ObjectMapper mapper = new ObjectMapper();
    // JsonParser parser =
    // mapper.getFactory().createParser(jsonResponse);
    //
    // Location[] loc = mapper.readValue(jsonResponse,
    // Location[].class);
    //
    // System.out.println(loc[0].toString());
    // System.out.println(loc.length);
    // conn.disconnect();

    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // };
    // }
}
