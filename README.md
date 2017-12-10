# ATMLocator
A web application to locate ATM machines in the Netherlands. Built for Backbase.

## Adopted Infrastructure

This project was developed using Eclipse IDE version Neon.2 Release Candidate 3 (4.6.2RC3).

The source code and all that is needed to compile and run this project is located at https://github.com/fabiolf/ATMLocator, including this document.

Maven version 3.3.9 was used to take care of package dependencies. It was also used to create the basic project archetype, including jUnit. It is good to speed-up project start.

For the *backend* the following technology stack was used:
- Springboot 1.5.8-RELEASE: good framework and starting point for web applications, encapsulating lots of complexity for REST services creation.
- Swagger 2.7.0: interesting tool to test services.
- Jackson 2.7.1: used to handle some JSON mapping and manipulation needed to clean the first bytes received from ING service.

For the *frontend* these are the modules/frameworks used:
- Angular.JS 1.6.7: lots of good features to produce modular dynamic web applications.
- jQuery 3.1.1: also needed by the web application.
- Bootstrap 3.3.7: Out of the box visual components to quickly create some nice layouts
- Bootstrap-UI 2.5.0: Additional visual components such as typeahead textedit (used in this project) and others.
- ng-map 1.18.4: angular module to encapsulate google maps api.

## Deployment

After cloning the repository from Github (or downloading and extracting the zip file), follow the steps below.

Create the WAR package
```
mvn clean package
```
Maven will produce the <code>atmlocator-0.1.0.war</code> file and will put it in the <code>target</code> directory. At this point, since it is a springboot war, it features an embedded Tomcat and it can be run in the command line:
```
java -jar target\atmlocator-0.1.0.war
```
Or you can use the war file and deploy it into a vanilla Tomcat server simply by pointing the browser to <code>http://localhost:8080/manager/html/</code> (if the used port is 8080), going to the section *WAR file to deploy*, clicking at *Chose file* button and then in the *Deploy* button. The application will be deployed at the <code>localhost:8080/atmlocator-0.1.0</code> route.

## Usage

Point the browser to <code>localhost:8080/atmlocator-0.1.0</code> and the main screen will appear. This is a simple application in which one can choose a Dutch city in the textedit box shown in the navbar at the top of the screen and then choose between two display modes: list view and map view.

The textedit box features typeahead suggestions based on the data provided by the ING service. You can use the suggestions or type the city name by your own. If the city name is invalid, an error message will appear.

Once you have chosen a valid city name and pressed the *Go* button, the default view (list) will appear and a table will show some information about the address of the ATM machines found in that city.

If you switch to the map view, a google maps map will appear with markers showing the locations of the ATM machines for the chosen city.

To change the city, just edit the textedit box in either view, and you will get updated information.

This application features Swagger, that is a nice tool to debug services and to simulate a frontend. It can be accessed at <code>localhost:8080/atmlocator-0.1.0/swagger-ui.html</code>. It presents a nice interface where you can call the services specifying parameters and verify the response.

There is also a way to monitor the application health (thanks to Springboot Actuator) by point to <code>localhost:8080/atmlocator-0.1.0/health</code>. Actuator will reply with a JSON response below if the application is ready and running:
```
{"status":"UP"}
```


## Implementation Details

This application was created using the aforementioned technology stack and in the following sections more details will be given for the application's backend and frontend.

### Backend

The Java application that represents the backend was divided in 6 main packages (located at <code>src/main/java</code>), found in the following sections.

#### <code>com.backbase.atmlocator</code>

This is the application main package and holds the <code>Application</code> class. It initialized the application, in its <code>main</code> method and overhides the <code>configure</code> method from the <code>SpringBootServletInitializer</code> to allow it to be served by the Tomcat server reading the WAR file. Without it, the application could only be served by the embedded Tomcat server in the JAR file (to generate the JAR file, you have to edit the <code>package</code> section of the <code>pom.xml</code> and choose *JAR* value for it).

#### <code>com.backbase.atmlocator.config</code>

This package holds two classes used to configure some features of the application:

- CORSConfig: used to include CORS compatibility headers in the service response to the frontend. This is needed for the case where the frontend is being served by another server, in another port. During the development phase, the backend was developed and hosted in the Springboot container and the frontend was being served by a node.js express server, just to speed-up tests. So this CORS configuration was needed.

- SwaggerConfig: this class was used to tell swagger the root package it should look for services. The found services are then exposed in a nice web layout and the developer can call it simulating the frontend calls. Very useful when developing the integration between the frontend and backend.

#### <code>com.backbase.atmlocator.controller</code>

This package holds the main application controller, the one that exposes the REST services that will be consumed by the frontend. It exposes two services:
- <code>/api/list</code> linked to the <code>listAllCities()</code> method, to provide a list of all cities available in the ING data. This data will then be used to feed the typeahead textedit at the frontend.
- <code>/api/list/{cityName}</code> linked to the <code>listATMByCity(cityName)</code> method, that returns the list of ATM addresses for that provided <code>cityName</code>.

Here, each service is annotated by the <code>RequestMapping</code> annotation that takes care of mapping the method to the specified path. Each method returns a <code>ResponseEntity</code> that takes care of generating a JSON object based on the type of the return class.

Also, the two class members <code>INGConsumerService</code> and <code>LocationService</code> are injected by springboot thanks to the <code>@Autowired</code> annotation.

#### <code>com.backbase.atmlocator.entity</code>

This package holds the classes that represent the model of the JSON objects returned by the ING service.

- A <code>Location</code> contains some attributes and a <code>Address</code> instance.
- A <code>Address</code> contains some more attributes and a <code>GeoLocation<code> instance.
- Finally, the <code>GeoLocation</code> class has two attributes representing the latitude and longitude of the ATM machine.

These classes are used by the deserializer to translate the JSON response of the ING service to objects that we can handle in the code. On the other way around, they are also used by the serializer (in the aforementioned <code>ResponseEntity</code>) to produce the JSON strings sent to the frontend.

These classes were generated by the <code>http://www.jsonschema2pojo.org/</code> online tool. We provide the JSON and the tool generates the classes. Very handy!

#### <code>com.backbase.atmlocator.frontend.controller</code>

This package only holds the web frontend controlled. It just returns the <code>index.html</code> file when the root path of the web application is accessed. It is a simple web server.

#### <code>com.backbase.atmlocator.service</code>

Here there are two Beans, injected directly at the controller classes. This approach was used to separate the code that is responsible for handling the inbound frontend calls and the code that is responsible to handle the ING service consumption, the outbound calls. Since this is a rather simple application, there was no point in creating yet a third layer in between, but for big applications, that is a good practice.

The <code>INGConsumerService</code> is responsible for calling the ING service and returning a clean set of data. It uses a <code>RestTemplate</code> to call the service and get the response mapped into a String. Then, the first 6 bytes of junk received is cleaned, and the Jackson <code>ObjectMapper</code> can map the remaining good JSON string into a List of Locations. If the ING service was more well behaved, this method would have just two lines and the <code>RestTemplate</code> would be able to map the service response directly to an array of Locations.

The other service is the <code>LocationService</code> that has two methods:

- <code>getUniqueCityNames</code>: responsible for extracting a list of unique city names from the ING returned set of data. It uses a HashSet (which has constant time for the <code>add</code> and <code>contains</code> operations) to filter the data and get the unique city names. The use of this data structure makes this a very efficient operation.

- <code>filterLocationByCity</code>: extract the ATM records from the ING data given a specific city. It uses an <code>ArrayList</code> to store the filtered records.

These methods implement a conservative approach regarding the accuracy of the generated data. They just process the data that is provided to them by the controller that calls it. The controller, in turn, reads the ING service whenever it is called. Since it is unknown how often the ING ATM machines are installed/decomissioned, this approach guarantees the lastest data will be provided to the end user, but imposes some performance restrictions since the ING service is the bottleneck of the operation.

If the user provided a non functional requirement telling that the information of the frontend could have some delay, then a good approach would be to create a cache of the ING data, periodically updated. Then, we could have used a HashMap to store the information where the cities could be the key. This would give a very good performance (O(k), where k is the amount of addresses) when reading the addresses data. Since the whole ING data is not that big, the used approach does not represent a big performance problem at the end.

### Frontend

The frontend is located at the <code>src/main/resources</code> path. As already mentioned, it was used Angular.JS + bootstrap to create a nice looking dynamic layout for the web application.

The web application files are located at the <code>static/</code> folder with some few files the its parent folder:

- <code>application.properties</code>: this file sets the context path for the application. This was needed because Tomcat serves the application in a path that is the maven name of the application (in this case <code>atmlocator-0.1.0</code>). So this file sets the application context, meaning, the path of the URL served by tomcat. If there were no requirement of being able to deploy the war file in the tomcat server, this would not be needed.

- <code>server.js</code>: a simple Express server executed by Node.js to provide a simple web server, usefull for the web application development. This avoids, the time consuming task of recompiling and regenerating the package (unsing maven) and then redeploying it to test for any modifications in the web application. This is file is not required by the final WAR file, thou.

Examining the <code>static/</code> folder, some modules will be found, explained in the following sections.

#### Main HTML file

This is where all starts. It includes all the scripts required, the CSSs and created the navbar, with the typeahead textedit and the toogle buttons, to switch views.

The typeahead textedit is provided by bootstrap-ui module and relies on the city names returned by the backend corresponding REST service.

The toogle buttons, uses a <code>btn-toolbar</code> to group the which are really labels acting as buttons (since we give them the bootstrap <code>btn</code> classes) with a embedded radio button, to give the toogle behavior to the "label-buttons". As the label text, some glyphicons were used. The first one represents the list view and the second the map view.

The loading of the js files are at the end of the file to enhance loading performance by giving the user the impression that everything was loaded while they are still being loaded.

#### <code>js/</code> folder

This folder holds the JavaScript files that composes the Angular solution. They are:

- <code>ATMLocatorApp.js</code>: it just declares the angular application module including the dependencies. In this case, the solution depends on the <code>ngRoute</code> modeule (to provide the routes to the views), <code>ngResource</code> (to consume backend REST services), <code>ngMap</code> (an angular wrap module for the google maps api) and <code>ui.bootstrap</code> (for the typeahead textedit component).

- <code>ATMLocatorRoutes.js</code>: This module provides the routes for the views. This application has two vies, one for the list representation and other for the map representation.

- <code>MainCtrl.js</code>: this is the Angular controller for the application. It handles the backend service calls and all the dynamicity of the application.

#### <code>view/</code> folder

It has both views of the application, the list and the map view.

Theh list view just creates a table populated using the <code>ng-repeat</code> directive.

The map view instantiates the google maps api using the ngMap module and create markers at the locations of the ATM machines, given by their latitude and longitude attributes. When a marker is clicked, it displays the address related to it.

#### Other folders

The <code>lib/</code> folder holds the frontend libs used in this project. For the sake of performance they were downloaded and included in the war file.

The <code>img/</code> holds some images used in the project.

The <code>fonts/</code> folder holds the glyphicons used in the project.

Finally, the <code>css/</code> holds the style sheets used in the project.
