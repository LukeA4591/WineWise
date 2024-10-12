Feature: WineryDao
  Scenario: Admin adds a location to a winery using the map feature - AT_23
    Given An admin is on the place winery page and they place are winery either by address or clicking on the map at Latitude: 10 and Longitude: 5
    When The admin selects the winery to place
    Then The Winery is updated to have that location

  Scenario: Admin want to remove the location of a wine - AT_24
    Given An admin selects winery "Winery 2" to remove location
    When They confirm to remove the winery
    Then The wineries location is set to null, null