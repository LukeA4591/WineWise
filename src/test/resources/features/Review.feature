Feature: Add a review to a wine
  Scenario: Add a valid review to a wine
    Given A user leaves review "I liked it", and rating 60 on a wine
    When The user saves the review
    Then The review is store in the database