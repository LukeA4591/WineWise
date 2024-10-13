Feature: WineryDao
  Scenario: Admin adds a location to a winery using the map feature - AT_22
    Given An admin is on the place winery page and they place are winery either by address or clicking on the map at Latitude: 10 and Longitude: 5
    When The admin selects the winery to place
    Then The Winery is updated to have that location

  Scenario: Admin want to remove the location of a wine - AT_23
    Given An admin selects winery "Winery 2" to remove location
    When They confirm to remove the winery
    Then The wineries location is set to null, null

  Scenario: An admin adds a new winery and wants to select location
    Given An admin enters a new winery "newWinery" with latitude "null" and longitude "null"
    When They enter a location for "newWinery" by address or clicking on the map at lat: 70, lng: 50
    Then The winery "newWinery" is added to the database with the correct latitude and longitude