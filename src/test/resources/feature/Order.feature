Feature: Create Order

  Scenario: Successfully create order with successful payment
    Given a user with ID 1
    And products with sufficient stock
    And the payment is successful
    When I create an order with items
      | productId | quantity |
      | 1         | 2        |
      | 2         | 1        |
    Then the order should be created with status "COMPLETED"
    And the product quantities should be updated

  Scenario: Create order with insufficient stock
    Given a user with ID 1
    And products with insufficient stock
    When I create an order with items
      | productId | quantity |
      | 1         | 100      |
    Then I should receive a stock error "Insufficient Stock" error

  Scenario: Create order with product not found
    Given a user with ID 1
    And a product with ID 999 does not exist
    When I create an order with items
      | productId | quantity |
      | 999       | 2        |
    Then I should receive a "Product not found" error

  Scenario: Create order with failed payment
    Given a user with ID 1
    And products with sufficient stock
    And the payment is failed
    When I create an order with items
      | productId | quantity |
      | 1         | 2        |
    Then the order should be created with status a "CANCELLED"
    And I should receive an "Checkout failed...retry again" error
