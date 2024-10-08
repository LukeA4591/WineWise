Feature: WineDAO
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

  Scenario: Admin deletes a wine successfully from the database
    Given A database with the following wines:
      | wineName    | wineType | winery | year |
      | Famous Wine  | White | Famous Winery | 2020 |
      | Second Wine  | Red   | Another Winery | 2018 |
    When The admin deletes a wine with details "Famous Wine", "Famous Winery", 2020
    Then The wine with details "Famous Wine", "Famous Winery", 2020 is deleted from the database
    And The database has a size of 1
    And There will be a wine with details "Second Wine", "Red", "Another Winery", 2018

  Scenario: Admin successfully updates wine
    Given A database with the following wines:
      | wineName    | wineType | winery | year |
      | Famous Wine  | White | Famous Winery | 2020 |
    When The admin updates the wine with details "Famous Wine", "White", "Famous Winery", 2020 to "Famous Wine 2", "White", "Famous Winery", 2019
    Then There will be a wine with details "Famous Wine 2", "White", "Famous Winery", 2019
    And The wine with details "Famous Wine", "Famous Winery", 2020 is deleted from the database

  Scenario: Admin unsuccessfully updates wine
    Given A database with the following wines:
      | wineName    | wineType | winery | year |
      | Famous Wine  | White | Famous Winery | 2020 |
      | Second Wine  | Red | Another Winery | 2020 |
    When The admin updates the wine with details "Famous Wine", "White", "Famous Winery", 2020 to "Second Wine", "Red", "Another Winery", 2020
    Then There will be a wine with details "Famous Wine", "White", "Famous Winery", 2020
    And There will be a wine with details "Second Wine", "Red", "Another Winery", 2020
    And The database has a size of 2

  Scenario: A user getting similar wines
    Given A database with the following wines:
      | wineName    | wineType | winery | year |
      | First Wine  | White | First Winery | 2020 |
      | Second Wine  | White | Second Winery | 2019 |
      | Third Wine  | Red | Third Winery | 2020 |
      | Fourth Wine  | Red | First Winery | 2017 |
      | Nothing in Common Wine  | Red | Niche Winery | 1882 |
    When A user queries for similar wines to the wine with details "First Wine", "White", "First Winery", 2020
    Then The suggested wines will contain the wine with details "Second Wine", "White", "Second Winery", 2019
    And The suggested wines will contain the wine with details "Third Wine", "Red", "Third Winery", 2020
    And The suggested wines will contain the wine with details "Fourth Wine", "Red", "First Winery", 2017
    And The suggested wines will have a size of 3
    And The database has a size of 5

  Scenario: A user getting similar wines when there is no similar wines
    Given A database with the following wines:
      | wineName    | wineType | winery | year |
      | First Wine  | White | First Winery | 2020 |
      | Second Wine  | Red | Second Winery | 2019 |
      | Third Wine  | Red | Third Winery | 2018 |
      | Fourth Wine  | Red | Fourth Winery | 2017 |
    When A user queries for similar wines to the wine with details "First Wine", "White", "First Winery", 2020
    Then The suggested wines will contain the wine with details "Second Wine", "Red", "Second Winery", 2019
    And The suggested wines will contain the wine with details "Third Wine", "Red", "Third Winery", 2018
    And The suggested wines will contain the wine with details "Fourth Wine", "Red", "Fourth Winery", 2017
    And The suggested wines will have a size of 3
    And The database has a size of 4