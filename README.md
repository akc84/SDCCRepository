# Currency Converter

The Currency Converter application is built using Spring Boot. It uses an in-memory relational H2 database to store User and Address information. The application also uses an external MongoDB database to maintain a list of last 10 currency conversion queries and results for each username.

## Running the application

The application can be started up by entering the following command from within the CurrencyConverter directory

`mvn spring-boot:run`

Alternately, the following command may be executed to build and execute an executable WAR

`mvn clean package`

`java -jar target/CurrencyConverter-0.0.1-SNAPSHOT.war --server.port=8080`

Once running, the application may be accessed at

[http://localhost:8080](http://localhost:8080/) or [http://localhost:8080/currencyConverter](http://localhost:8080/currencyConverter)

The application may be accessed with the following seeded credentials –

Username: demo

Password: demo

Alternately, a new user may be registered.

A running version of the Currency Converter Application may be found at

[https://currency-converter-sdcc.herokuapp.com/](https://currency-converter-sdcc.herokuapp.com/)

## Application Details

The Currency Converter application has 3 screens -

- A registration screen where users are required to register their details along with their address details.
- A login screen that controls access to the application
- The main page where users may perform currency conversion and see an audit history of the last 10 queries performed by them

## Configuration

In addition to the Spring setup properties, the src/main/resources/application.properties file also allows the following configurable-

`country.list` – Maintains a configurable list of countries that users may select from while registering their address.

`currency.list` – Maintains a configurable list of currencies supported by the Currency Converter application.

## Additional Details

**Registration Form-**

The Currency Converter implements a Spring based registration form where users must register an RFC-compliant email address, a date of birth in the past, as well as a postal address with street, zip code, city, and a country selectable from a list

**Acceptance Tests-**

Acceptance Tests have been implemented using Cucumber. The Web-based tests are dependent on the Firefox driver and may be executed as –

`mvn -f pom-includeCucumberTests.xml clean test`

The source may be found in the com.zp.sdcc.it.cucumber package under src/test/java.

**Continuous Integration-**

Continuous Integration has been setup on Travis –CI at

[https://travis-ci.org/akc84/SDCCRepository/](https://travis-ci.org/akc84/SDCCRepository/)

**Monitoring and Management -**

Spring Boot actuator has been setup for Currency Converter. The following urls may be accessed to perform monitoring and management of the application –

/metrics

/trace

/heapdump

/beans

/dump

/env

/loggers

/configprops

/mappings

/health

/autoconfig

/auditevents

**Caching –**

Currency Converter uses a cache named exchangeRate to cache requests to external service.

The TTL and Cache size may be configured from the application.properties with the following properties –

`spring.cache.cache-names=exchangeRate`

`spring.cache.caffeine.spec=maximumSize=100,expireAfterAccess=300s`



## Known Limitation

As the User/Address information is store in an in-memory H2 database, the tables are dropped when the server is restarted. The audit details, however, are persisted in an external MongoDB database. Therefore, if the user were to register again using a username that was previously used but was purged due to server restart, the user will still find the audit details preserved. This issue will be addressed when an external database is used to store the User/Address information.
