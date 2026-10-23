Feature: TS01 Authentication endpoints
  As a developer
  I want authentication endpoints
  So that the web and mobile apps can sign users in securely

  Scenario: Valid sign-in
    Given a farmer account registered with "diego.ramos@example.com"
    When the developer sends POST "/api/v1/auth/signin" with valid credentials
    Then the response status is 200
    And the body contains a token and its expiration time

  Scenario: Invalid credentials
    Given a farmer account registered with "diego.ramos@example.com"
    When the developer sends POST "/api/v1/auth/signin" with a wrong password
    Then the response status is 401
    And the body contains an error message without internal details
