Feature: User Signup
  As a website visitor
  I want to register as a user or admin
  So that I can access the e-commerce platform

  Scenario: User successfully signs up with valid credentials
    Given No user exists with email "user@example.com"
    When The user submits signup request with firstName "John", lastName "Doe", email "user@example.com", phoneNumber "1234567890", password "Pass123", and role "USER"
    Then The signup should be successful with message "User signed up successfully!"
    And The user "user@example.com" should exist with role "USER"

  Scenario: Admin successfully signs up with valid credentials
    Given No user exists with email "admin@example.com"
    When The user submits signup request with firstName "Admin", lastName "User", email "admin@example.com", phoneNumber "9876543210", password "AdminPass", and role "ADMIN"
    Then The signup should be successful with message "User signed up successfully!"
    And The user "admin@example.com" should exist with role "ADMIN"

  Scenario: User tries to sign up with existing email
    Given A user with email "existing@example.com" exists
    When The user submits signup request with firstName "Jane", lastName "Smith", email "existing@example.com", phoneNumber "1122334455", password "Pass123", and role "USER"
    Then The signup should fail with error "Email already registered"

  Scenario: User tries to sign up with blank fields
    Given No user exists with email "test@example.com"
    When The user submits signup request with firstName "", lastName "Doe", email "test@example.com", phoneNumber "1234567890", password "Pass123", and role "USER"
    Then The signup should fail with error "Enter valid input"