Feature: Product and Price Input
  As user, I want to input a new item with all the details (name, unit) and its price.

  Scenario Outline: Full input of a new item with the name
    Given I am at the price input screen
    When I enter a new Product called "<productName>" with Unit "<unitFormat>"
    And I enter a price of "<price>"
    And I enter the store as "<store>"
    And I press on the save button
    Then the fields go blank and I see a success message

    Examples:
      | productName             | unitFormat | price | store              |
      | Leche                   | 1L         | 1.20  | Mercamona          |
      | Huevos L                | 12         | 2.40  | Fruteria Las nenas |
      | Carne picada de ternera | 400g       | 3.54  | Carniceria Puri    |
      | Harina de Reposteria    | 1Kg        | 1.10  | Carreflus          |