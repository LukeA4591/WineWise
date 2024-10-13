# WineWise App Overview
WineWise is a kiosk-based online application designed to simplify wine discovery and reviewing. It allows its users to browse and filter wines that are provided by a winery, liquor store, or iSite location. The kiosk owners are able to manage user's reviews and import their own wines in the form of CSV files. WineWise is powered by Java and JavaFX, and it uses a local SQLite database but needs Internet Connectivity for Interactive Maps.

## Key Features
For Kiosk Admins:
- Create a secure account
- Change credentials
- Adding wines and wineries (manually or imported CSV Files)
- Deleting wines and wineries
- Bulk viewing wines and wineries
- Placing wineries on a map
- Viewing all reported reviews with options to un-flag (un-report) or delete them

For Kiosk Users:
- Browsing, filtering and searching through wines
- More in-depth views of wine details
- Recommended wines for a wine you view
- Leaving reviews for wines
- Viewing all reviews for a wine
- Reporting invalid reviews
- Viewing wineries on a map
- Interactive home page to view the highest rated wines by the critics and users


Other Features:
- The GUI is designed to be run on the linux OS, functionality will not be effected on other OS however the GUI may not line up
- SQLite Injection Protection to prevent malicious users from deleting or editing the data in the SQL Database


## Authors
- SENG202 Group 7 (Luke Armstrong, Oliver Barclay, Felix Blanchard, Ryan Hamilton, Alex Wilson)

## Prerequisites
- JDK >= 21 [click here to get the latest stable OpenJDK release (as of writing this README)](https://jdk.java.net/18/) The JDK contains the Java Cryptography Architecture which is needed for PBEKeySpec for password hashing.
- Gradle [Download](https://gradle.org/releases/) and [Install](https://gradle.org/install/)

## Dependencies
This project comes with some basic examples of the following (including dependencies in the build.gradle file):
- JavaFX
- Logging (with Log4J)
- Junit 5 (testing logic / service classes)
- Cucumber (for acceptance testing)
- OpenCSV (for CSVImporter)
- Leaflet (for maps)

## Build Project 
- Open a command line interface inside the project directory (folder team-7) and run `./gradlew jar` to build a .jar file. 
- The file is located at `build/libs/WineWise-1.0-SNAPSHOT.jar`

## Run App (First time)
- If the jar is not provided, build the project (see above). 
- Open a command line interface and navigate to the directory containing `WineWise-1.0-SNAPSHOT.jar`
- Run the command `java -jar WineWise-1.0-SNAPSHOT.jar` to open the application.

## Run Tests
- Run the command `./gradlew test` in the terminal to run the JUnit Tests
- Run the command `./gradlew cucumber` in the terminal to run the Cucumber Tests

## Reset App
- If WineWise has already been run once - and therefore the credentials have been set, and database created - then to hard reset the app these steps will need to be taken:
- Delete the `credentials.txt` file
- Delete the `database.db` file
- Rerun the command: `java -jar WineWise-1.0-SNAPSHOT.jar` in the terminal.
-**** In order to create a new account without losing data you can just delete `credentials.txt` and rerun the jar.

## Credits
- Geolocator.java and most methods in map.html, JavaScriptBridge.java were provided by Morgan English
- Shipped with decanter.csv as an example for a dataset, credit for csv to Decanter