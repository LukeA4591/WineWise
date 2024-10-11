Feature: WineService
  Scenario: Inputting valid data
    Given A user inputting the text "This is a winery", "This is a wine", "2020", "80", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be ""

  Scenario: Inputting blank winery name
    Given A user inputting the text "", "This is a wine", "2020", "80", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be "Winery Name field is empty."

  Scenario: Inputting blank wine name
    Given A user inputting the text "This is a winery", "", "2020", "80", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be "Wine Name field is empty."

  Scenario: Inputting blank vintage field
    Given A user inputting the text "This is a winery", "This is a wine", "", "80", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be "Wine Vintage field is empty."

  Scenario: Inputting a long winery name
    Given A user inputting the text "Omg this is so insanely long why would anyone call a winery this its just gonna clog our database up with crazy strings please come taste our wine", "This is a wine", "2020", "80", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be "Text fields are too long."

  Scenario: Inputting characters as strings
    Given A user inputting the text "This is a winery", "This is a wine", "3030 Deltron 0", "80", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be "Wine Vintage and Score should be a number."

  Scenario: Vintage is in the future
    Given A user inputting the text "This is a winery", "This is a wine", "3030", "80", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be "Vintage should be between 0 and the current year."

  Scenario: Score is too high
    Given A user inputting the text "This is a winery", "This is a wine", "2020", "110", "Canterbury", "This is a wine from canterbury"
    When The user saves the wine
    Then The error message will be "Score should be between 0-100."