Feature: Edit Wines

  Scenario: Admin edits a wine's score and region
    Given The admin has added the dataset "dataset_cucumber.csv"
    And The admin is on the view table page
    When The admin selects the wine: 2017 "Plume Pinot Noir" from "Lake Chalice"
    And The admin edits the wine details with a score of 86 and region "Auckland"
    And The admin clicks Save Wine
    Then The wine should be updated in both the WineWise system and the wines database table
