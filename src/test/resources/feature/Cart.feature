Feature: Cart Service
  Scenario: Add items to cart successfully
    Given A user with ID 2
    And A product with name "Laptop", price 1200.0, and quantity 10
    When The user adds "Laptop" with quantity 2 to their cart
    Then The item should be added to the cart successfully
    And The product's quantity should be reduced by 2

  Scenario: Add items to cart with insufficient stock
    Given A user with ID 1
    And A product with name "Headphones", price 100.0, and quantity 5
    When The user adds "Headphones" with quantity 6 to their cart
    Then The product addition should fail with an error message "Insufficient stock available!"

  Scenario: Add items to cart with non-existent product
    Given A user with ID 1
    When The user adds "NonExistentProduct" with quantity 1 to their cart
    Then The product addition should fail with an error message "Product not found!"

  Scenario: View user cart successfully
    Given A user with ID 1
    And A product with name "Mouse", price 50.0, and quantity 10
    And The user adds "Mouse" with quantity 3 to their cart
    When The user views their cart with cart ID 1
    Then The cart should contain "Mouse" with quantity 3

  Scenario: View non-existent cart
    When The user views their cart with cart ID 999
    Then The cart view should fail with an error message "Cart not found with ID: 999"

  Scenario: Update cart quantity successfully
    Given A user with ID 1
    And A product with name "Keyboard", price 80.0, and quantity 10
    And The user adds "Keyboard" with quantity 2 to their cart
    When The user updates the cart item quantity with cart ID 1 to 4
    Then The cart item quantity should be updated to 4

  Scenario: Update cart quantity with insufficient product stock
    Given A user with ID 1
    And A product with name "Monitor", price 300.0, and quantity 5
    And The user adds "Monitor" with quantity 2 to their cart
    When The user updates the cart item quantity with cart ID 1 to 6
    Then The cart quantity update should fail with an error message "Not enough quantity"

  Scenario: Delete cart by admin successfully
    Given A user with ID 1 and role "ADMIN"
    And A product with name "Printer", price 200.0, and quantity 10
    And The user adds "Printer" with quantity 1 to their cart
    When The admin deletes the cart with cart ID 1
    Then The cart should be deleted successfully

  Scenario: Delete cart by non-admin user
    Given A user with ID 2 and role "USER"
    And A product with name "Scanner", price 150.0, and quantity 10
    And The user adds "Scanner" with quantity 1 to their cart
    When The user tries to delete the cart with cart ID 1
    Then The cart deletion should fail with an error message "Only admin can delete cart"

  Scenario: Update cart item details by admin successfully
    Given A user with ID 1 and role "ADMIN"
    And A product with name "Webcam", price 60.0, and quantity 10
    And The user adds "Webcam" with quantity 2 to their cart
    When The admin updates the cart item with cart ID 1 to quantity 3, product name "New Webcam", and price 70.0
    Then The cart item should be updated with quantity 3, product name "New Webcam", and price 70.0

  Scenario: Update cart item details by non-admin user
    Given A user with ID 2 and role "USER"
    And A product with name "Speaker", price 40.0, and quantity 10
    And The user adds "Speaker" with quantity 1 to their cart
    When The user tries to update the cart item with cart ID 1 to quantity 2, product name "New Speaker", and price 50.0
    Then The cart item update should fail with an error message "Only admin can change these fields"

  Scenario: Update quantity of cart item success.
    Given A user with ID 1
    And a product with name "charger", price 30.0, and quantity 10
    And The user adds "charger" with quantity 2 to their cart
    When the user update cart with cartId 1 to quantity 5
    Then the cart item quantity should update to 5

  Scenario: Update quantity with null cartId
    When The user tries to update a cart with a null cartId
    Then the cart update should fail with an error message "Cart id or Quantity cannot be null"