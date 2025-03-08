Feature: Add Products

  Scenario: Admin adds a product successfully
    Given A user with email "admin@example.com" and role "ADMIN"
    When The user tries to add a product with name "Test Product", price 100.0, and quantity 10
    Then The product should be added successfully

  Scenario: Non-admin tries to add a product
    Given A user with email "user@example.com" and role "USER"
    When The user tries to add a product with name "Test Product", price 100.0, and quantity 10
    Then The product addition should fail with an error message "Only admin can add products"

  Scenario: Admin tries to add a product with invalid data
    Given A user with email "admin@example.com" and role "ADMIN"
    When The user tries to add a product with name null, price 100.0, and quantity 10
    Then The product addition should fail with an error message "Product name cannot be null or empty"

  Scenario: Admin tries to add a product with negative price
    Given A user with email "admin@example.com" and role "ADMIN"
    When The user tries to add a product with name "Test Product", price -50.0, and quantity 10
    Then The product addition should fail with an error message "Price must be greater than zero"

  Scenario: Admin tries to add a product with negative quantity
    Given A user with email "admin@example.com" and role "ADMIN"
    When The user tries to add a product with name "Test Product", price 100.0, and quantity -5
    Then The product addition should fail with an error message "Quantity must be greater than zero"