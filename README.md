# Positivity

## Dependencies

| Dependency Name | Version |
|-----------------|---------|
| `spring-boot-starter-parent` | `2.5.4` |
| `spring-boot-starter-jpa` | `2.5.4` |
| `spring-boot-starter-thymeleaf` | `2.5.4` |
| `spring-boot-starter-validation` | `2.5.4` |
| `spring-boot-starter-web` | `2.5.4` |
| `mysql-connector-java` | `8.0.26` |
| `spring-boot-starter-test` | `2.5.4` |
| `hsqldb` | `2.5.2` |
| `junit` | `4.13.12` |
| `h2` | `1.4.200` |
| `hibernate-core` | `5.4.32` |
| `okhttp` | `4.9.1` |
| `gson` | `2.8.8` |
| `sortable.js` | `1.2.0` |
| `jquery` | `3.6.0` |
| `spring-session-jdbc` | `2.5.2` |
| `android-json` | `0.0.20131108.vaadin1` |
| `stanford-corenlp` | `4.2.2` |
| `bootstrap` | `5.1.0` |
| `bootstrap-icons` | `1.6.1` |
| `leaflet` | `1.7.1` |
| `mapbox` | `v1` |
| `newscatcher` | `v2` |
| `GCP Cloud Natural Language API` | `-` |


## Working Functionality

* Sign up / Sign in / Logout
* Updating user details
* Region Map preference selection, interactive map
* Topic preference selection
* Outlet preference selection
* Positive news filtering using NLP
* Like story, unlike story
* Search functionality, filtering using keywords, topic, outlet, region, language
* Results ordering (newest & most relevant)
* Home page and positive news feed
* Story viewing history
* View story

## Guide to run the application.
This application requires the use of environment variables in order to run. Namely:
```shell
MYSQL_DB
MYSQL_USERNAME
MYSQL_PASSWORD
NEWS_API_KEY
SENTIMENT_APIKEY
```

The first 3 are to allow for the application to connect to the MySQL database, and the last two are API keys from the `newscatcher` API and Google's Cloud Natural Language API key. 

Once those are set as environment variables, run the `main` method in `src/main/java/com/elec5619/positivity/PositivityApplication.java`

## Code:

Most of backend code is stored in Positivity-News/Positivity-ELEC5619/src/main/java/com/elec5619/positivity/. This application follow the MVC architecture. 

Model; 
* domain folder that create and define objects
* repository folder: contain interfaces of model
Controller:
* controller folder contain logic
Other folders:
* utils: Other package may be used
* forms folder: contain forms 

Most of frontend code (View) is stored in Positivity-News/Positivity-ELEC561/src/resources. 


