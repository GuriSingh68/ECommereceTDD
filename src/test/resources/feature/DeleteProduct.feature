Feature: Delete Products

  Scenario: Admin deletes a product successfully
    Given A user with email "admin@example.com" and role "ADMIN"
    And An existing product with name "Test Product"
    When The user tries to delete the product
    Then The product should be deleted successfully

  Scenario: Non-admin tries to delete a product
    Given A user with email "user@example.com" and role "USER"
    And An existing product with name "Test Product"
    When The user tries to delete the product
    Then The product deletion should fail with an error message "Only admin can delete products"

  Scenario: Admin tries to delete a non-existing product
    Given A user with email "admin@example.com" and role "ADMIN"
    When The user tries to delete a product with ID 999
    Then The product deletion should fail with an error message "Product not found"