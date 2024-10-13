Feature: Scanning a dataset
  Scenario: Process a small wine dataset - AT_8
    Given A small wine dataset "dataset_cucumber.csv"
    When The wine dataset is imported
    Then A list of wines should be returned
    And The length of the list should be 11
