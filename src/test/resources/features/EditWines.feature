Feature: Edit Wines

  Scenario: Admin edits a wine's score and region - AT_11
    Given The admin has added the dataset "dataset_cucumber.csv"
    And The admin is on the view table page
    When The admin selects the wine: 2017 "Plume Pinot Noir" from "Lake Chalice"
    And The admin edits the wine details with a score of 86 and region "Auckland"
    And The admin clicks Save Wine
    Then The wine should be updated in both the WineWise system and the wines database table

  Scenario: Admin does not edit any of the wine's details. - AT_11
    Given The admin has added the dataset "dataset_cucumber.csv"
    And The admin is on the view table page
    When The admin selects the wine: 2017 "Plume Pinot Noir" from "Lake Chalice"
    And The admin does not edit any of the wines details.
    And The admin clicks Save Wine
    Then The wine should be updated in both the WineWise system and the wines database table

  Scenario: Admin edits the wine's score to a value that exceeds allowed range. - AT_11
    Given The admin has added the dataset "dataset_cucumber.csv"
    And The admin is on the view table page
    When The admin selects the wine: 2017 "Plume Pinot Noir" from "Lake Chalice"
    And The admin edits the wine details with a score of 101 and region "Marlborough"
    And The admin clicks Save Wine
    Then The wine is not updated in the WineWise system or the wines database table
