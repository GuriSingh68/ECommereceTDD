Feature: Update Products

  Scenario: Admin updates a product successfully
    Given A user with email "admin@example.com" and role "ADMIN"
    And An existing product with name "Old Product" and price 100.0
    When The user tries to update the product with name "Updated Product", price 150.0, and quantity 20
    Then The product should be updated successfully

  Scenario: Non-admin tries to update a product
    Given A user with email "user@example.com" and role "USER"
    And An existing product with name "Old Product" and price 100.0
    When The user tries to update the product with name "Updated Product", price 150.0, and quantity 20
    Then The product update should fail with an error message "Only admin can update products"

  Scenario: Admin tries to update a product with invalid data
    Given A user with email "admin@example.com" and role "ADMIN"
    And An existing product with name "Old Product" and price 100.0
    When The user tries to update the product with name null, price 150.0, and quantity 20
    Then The product update should fail with an error message "Product name cannot be null or empty"

  Scenario: Admin tries to update a product with negative price
    Given A user with email "admin@example.com" and role "ADMIN"
    And An existing product with name "Old Product" and price 100.0
    When The user tries to update the product with name "Updated Product", price -50.0, and quantity 20
    Then The product update should fail with an error message "Price must be greater than zero"

  Scenario: Admin tries to update a product with negative quantity
    Given A user with email "admin@example.com" and role "ADMIN"
    And An existing product with name "Old Product" and price 100.0
    When The user tries to update the product with name "Updated Product", price 150.0, and quantity -5
    Then The product update should fail with an error message "Quantity cannot be negative"