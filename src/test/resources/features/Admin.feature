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

  Scenario: Trying to login to admin with incorrect password
    Given An admin registers with username "admin", password "password", and confirm password "password"
    When The admin creates the account
    And A user tries to log in with username "admin" and password "wrongpassord"
    Then The user is not able to login to the admin account

  Scenario: An admin wants to change their password
    Given An admin registers with username "admin", password "password", and confirm password "password"
    When The admin changes their password from "password" to "newPassword"
    Then The password of the account is "newPassword"