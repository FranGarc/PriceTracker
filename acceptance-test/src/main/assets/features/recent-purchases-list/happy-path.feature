Feature: Recent Purchases List
  As a user
  I want to see my last 5 purchases
  So that I can verify they were saved correctly

  Scenario: Show empty state when no purchases exist
    Given I am at the price input screen
    And the database has no records
    Then I should see a message "No recent purchases found"

  Scenario Outline: Show purchases in real-time
    Given I am at the price input screen
    When I register the product details with "<productName>", "<amount>", "<unitFormat>", "<price>" and "<store>" and I press the button
    Then the purchase "<productName>" should appear at the top of the list
    And the price shown for "<productName>" should be "<productName>"

    Examples:
      | productName | amount | unitFormat | price | store  |
      | Bread       | 1      | UNIT       | 0.80  | Bakery |