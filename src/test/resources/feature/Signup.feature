Feature: User Signup
  As a website visitor
  I want to register as a user or admin
  So that I can access the e-commerce platform

  Scenario: User successfully logs in with valid credentials
    Given A user with email "abc@xyz.com" and password "password"
    When The user tries to log in with email "abc@xyz.com" and password "password"
    Then The login should be successful

  Scenario: User tries to sign up with existing email
    Given A user with email "existing@example.com" exists
    When The user submits signup request with firstName "Jane", lastName "Smith", email "existing@example.com", phoneNumber "1122334455", password "Pass123", and role "USER"
    Then The signup should fail with error "Email already registered"

  Scenario: User tries to sign up with blank fields
    Given No user exists with email "test@example.com"
    When The user submits signup request with firstName "", lastName "Doe", email "test@example.com", phoneNumber "1234567890", password "Pass123", and role "USER"
    Then The signup should fail with error "Enter valid input"