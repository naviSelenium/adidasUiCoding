Feature: Search by keyword

  Scenario: Searching for a Apple in wiki Page
    Given User is on the wiki home page
    When User searches for "apple"
    Then all the result titles should contain the word "A common, round fruit produced by the tree Malus domestica, cultivated in temperate climates."

  Scenario: Searching for a Pear in wiki Page
    Given User is on the wiki home page
    When User searches for "pear"
    Then all the result titles should contain the word "An edible fruit produced by the pear tree, similar to an apple but elongated towards the stem."
