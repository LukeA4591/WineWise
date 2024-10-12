Feature: Register and login admin
  Scenario: Register admin with valid username and password - AT_5
    Given An admin registers with username "admin", password "password", and confirm password "password"
    When The admin creates a valid account
    Then The account is created and the credentials.txt file is made
    And The username matches

  Scenario: Register admin with invalid password - AT_6
    Given An admin registers with username "admin", password "a", and confirm password "a"
    When The admin creates an invalid account
    Then The account is not created