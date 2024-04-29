ThunderBuddy weather app
========================

The purpose of the app is to provide a simple presentation of weather data for given locations.
In order to retrieve the relevant weather data, we make calls to the openweathermap.org api.

The app structure follows an MVVM approach with some elements of clean architecture.
Dependency injection is provided by Hilt, network calls is orchestrated using Ktor and Room is used
for data persistence.

The current device location is procured using FusedLocationClient.

Standard layouts and views are used instead of Composables.

Navigation and safe args are implemented for easier navigation.

The main features of the app are getting and displaying the weather information for the current
location, as well as saving favourite locations and viewing their weather information in the main
screen.

The database holds the last updated weather information for offline viewing.

Reaching the maps screen is done via the navigation drawer menu. The app settings such as theme and
units are also available via the navigation drawer menu.

After adding some favourite locations and returning to the main screen, one can change the current
location via the arrow buttons on each side of the location name.

Third party libraries
=====================
Google play services location - used to acquire the devices current coordinates  
Google play services maps - used to display a map and show favourite locations using markers  
Hilt - Dependency injection  
Ktor - Unified network calls manager  
Kotlin serialization - Used to encode and decode JSON objects  
Room - Database for offline and caching of weather responses  
Navigation - Used to ease fragment navigation and manage backstack  
Preference - Used to generate preference screens and manage their associated data
