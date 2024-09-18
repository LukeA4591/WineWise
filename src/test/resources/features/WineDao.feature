Feature: Add Wine to the database
  Scenario: Admin adds a valid single wine to the database
    Given An admin is on the admin page and has filled in the add wine popup with a valid "winery", "name", and "vintage"
    When The admin clicks save new wine
    Then the wine would be saved to the database

Feature: Get Wine from the database
  Scenario: User goes to the search page to see all wines
    Given The database has wine in it
    When the user goes to the search page
    Then the search page table is initialized, showing all wine in the database
