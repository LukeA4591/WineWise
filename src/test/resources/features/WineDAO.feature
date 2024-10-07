Feature: Add Wine to the database
  Scenario: Admin adds a valid single wine to the database
    Given An admin is on the admin page and has filled in the add wine popup with inputs "Rose", "Ollies Wines", "Pinot Noir", 2010, 80, "Chch", "A lovely wine"
    When The admin clicks save new wine
    Then The wine would be saved to the database

  Scenario: Admin manually adds two valid wines to the database
    Given An admin is on the admin page and has filled in the add wine popup with inputs "Rose", "Ollies Wines", "Pinot Noir", 2010, 80, "Chch", "A lovely wine"
    And An admin is on the admin page and has filled in the add wine popup with inputs "White", "Ryan's Wines", "Pinot Noir", 1970, 85, "Auckland", "A great wine"
    When The admin clicks save new wine
    Then The wine would be saved to the database


  Scenario: Admin manually adds duplicate wine to the database
    Given An admin is on the admin page and has filled in the add wine popup with inputs "Rose", "Ollies Wines", "Pinot Noir", 2010, 80, "Chch", "A lovely wine"
    And An admin is on the admin page and has filled in the add wine popup with inputs "Rose", "Ollies Wines", "Pinot Noir", 2010, 80, "Chch", "A lovely wine"
    When The admin clicks save new wine
    Then The wine duplicate wine is not saved to the database

Feature: Delete wine from database
  Scenario: Admin deletes a wine successfully from the database
    Given A database with a wine with wine "Famous Wine", "White", "Famous Winery", 2020
    When The admin deletes a wine with details "Famous Wine", "Famous Winery", 2020
    Then The wine with details "Famous Wine", "Famous Winery", 2020 is deleted from the database
    And The database has one less entry

  Scenario: Admin
