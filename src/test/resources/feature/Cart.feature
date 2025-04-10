Feature: Cart Management

  Scenario: Create a new cart for a customer
    Given a customer with ID "123" exists
    And the following products exist:
      | productId | name   |
      | 456       | Apple  |
      | 789       | Banana |
    When the client creates a cart for customer "123" with products:
      | productId | quantity |
      | 456       | 2        |
      | 789       | 3        |
    Then the cart should be created

  Scenario: Retrieve all carts
    Given some carts exist
    When the client retrieves all carts
    Then all carts should be returned

  Scenario: Retrieve a cart by ID
    Given a cart with ID "1" exists
    When the client retrieves cart "1"
    Then cart "1" should be returned


  Scenario: Delete a cart
    Given a cart with ID "4" exists
    When the client deletes cart "4"
    Then the cart should be removed
