Feature: Product registration validation errors

  Scenario Outline: Display validation errors for invalid input
    Given I am at the price input screen
    Then I fill the form with "<productName>", "<amount>", "<unitFormat>", "<price>", "<store>" and I should see error "<errorMessage>"

    Examples:
      | productName | amount | unitFormat | price | store | errorMessage                                   |
      |             | 1      | LITER      | 1.20  | Shop  | Product name must not be empty.                |
      | Milk        | 0      | LITER      | 1.20  | Shop  | Quantity must be greater than 0.               |
      | Milk        | abc    | LITER      | 1.20  | Shop  | Quantity must be a valid number (e.g., 1, 1.5).|
      | Milk        | 1      | LITER      | -1.0  | Shop  | Price must be greater than 0.                  |
      | Milk        | 1      | LITER      | abc   | Shop  | Price must be a valid number (e.g., 2.99, 10). |