Feature: Register and login admin
  Scenario: Register admin with valid username and password
    Given An admin registers with username "admin", password "password", and confirm password "password"
    When The admin creates a valid account
    Then The account is created and the credentials.txt file is made
    And The username and password match

  Scenario: Register admin with invalid password
    Given An admin registers with username "admin", incorrect password "a", and confirm password "a"
    When The admin creates an invalid account
    Then The account is not created