Feature: User Login
  As a user (admin or customer)
  I should be able to log in to the e-commerce site
  So that I can continue my activity
  Scenario: User successfully logs in with valid credentials
    Given A user with email "abc@xyz.com" and password "password"
    When The user tries to log in with email "abc@xyz.com" and password "password"
    Then The login should be successful
  Scenario: User tries to log in with a non-existent email
    Given No user exists with email "notfound@xyz.com"
    When The user tries to log in with invalid email "notfound@xyz.com" and password "password"
    Then The login should fail with an error message "User not found"
  Scenario: Successfully update user role from USER to ADMIN
    Given an existing user with email "user@example.com" and role "USER"
    When the admin updates the role of "user@example.com" to "ADMIN"
    Then the system should return "Role updated successfully for user :user@example.com"





