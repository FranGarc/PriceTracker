Feature: Product registration
  As user, I want to input a new item with all the details (name, unit) and its price.

  Scenario Outline: Full input of a new item
    Given I am at the price input screen
    When I fill the product details with "<productName>", "<amount>", "<unitFormat>", "<price>" and "<store>" and I press the button
    Then the fields go blank and I see a success message

    Examples:
      | productName             | amount | unitFormat | price | store              |
      | Leche                   | 1      | LITER      | 1.20  | Mercamona          |
      | Huevos L                | 12     | UNIT       | 2.40  | Fruteria Las nenas |
      | Carne picada de ternera | 400    | GRAM       | 3.54  | Carniceria Puri    |
      | Harina de Reposteria    | 1      | KILOGRAM   | 1.10  | Carreflus          |