Feature: ReviewDAO
  Scenario: Add a valid review to a wine - AT_3
    Given A user leaves review "I liked it", and rating 60 on a wine
    When The user saves the review
    Then The review is store in the database

  Scenario: Unflag Review - AT_14
    Given A user flags the following reviews on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
      | 80     | "I thought this was yummy" | 1 |
      | 90     | "One of the best wines ever" | 2 |
    When The admin unflags the following reviews:
      | rating | description | id |
      | 80     | "I thought this was yummy" | 1 |
    Then The following reviews are unflagged on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
      | 80     | "I thought this was yummy" | 1 |
    And The flagged reviews contain the following reviews on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
      | 90     | "One of the best wines ever" | 2 |

  Scenario: Delete Review - AT_4
    Given A user flags the following reviews on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
      | 80     | "I thought this was yummy" | 1 |
      | 90     | "One of the best wines ever" | 2 |
    When The admin deletes the following reviews:
      | rating | description | id |
      | 80     | "I thought this was yummy" | 1 |
    Then The following reviews are unflagged on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
    And The flagged reviews contain the following reviews on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
      | 90     | "One of the best wines ever" | 2 |

  Scenario: Delete all reviews - AT_4
    Given A user flags the following reviews on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
      | 80     | "I thought this was yummy" | 1 |
      | 90     | "One of the best wines ever" | 2 |
    When The admin deletes the following reviews:
      | rating | description | id |
      | 80     | "I thought this was yummy" | 1 |
      | 90     | "One of the best wines ever" | 2 |
    Then The following reviews are unflagged on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |
    And The flagged reviews contain the following reviews on a wine with details "Famous Wine", "Famous Winery", 2011:
      | rating | description | id |