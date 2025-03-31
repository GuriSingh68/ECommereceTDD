Feature: Payment Processing
  As a system user
  I want to process payments for orders
  So that customers can complete their purchases

  Background:
    Given the payment system is available
    And a user with ID "1" and role "USER" exists
    And a user with ID "2" and role "ADMIN" exists

  Scenario: Process payment for a valid order
    Given a valid order with ID "101" and total price "150.00"
    When the payment is processed for the order
    Then a payment record should be created with status "SUCCESS"
    And the payment amount should be "150.00"

  Scenario: Attempt to process payment for null order
    When the payment is processed for a null order
    Then a NullPointerException should be thrown with message "Received a null order in processPayment!"

  Scenario: Process credit card payment
    Given a credit card with number "4111111111111111" and CVV "123"
    When a payment of "200.00" is processed using the credit card
    Then a payment record should be created with status "SUCCESS"
    And the payment should have the last four digits "1111"
    And the payment method should be "CREDIT_CARD"

  Scenario: Fetch all payment details
    Given multiple payments exist in the system
    When all payment details are requested
    Then all existing payment records should be returned

  Scenario: Admin retrieves payment by ID
    Given a payment with ID "201" exists in the system
    When an admin with ID "2" requests the payment with ID "201"
    Then the payment details should be returned

  Scenario: Non-admin user attempts to retrieve payment by ID
    Given a payment with ID "201" exists in the system
    When a regular user with ID "1" requests the payment with ID "201"
    Then an IllegalArgumentException should be thrown with message "Only admin access"

  Scenario: Admin updates payment status
    Given a payment with ID "301" exists in the system
    When an admin with ID "2" updates the payment status to "FAILED"
    Then the payment status should be updated to "FAILED"

  Scenario: Non-admin user attempts to update payment status
    Given a payment with ID "301" exists in the system
    When a regular user with ID "1" attempts to update the payment status to "FAILED"
    Then an IllegalArgumentException should be thrown with message "Error occured while update"