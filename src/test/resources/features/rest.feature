Feature: User REST Service

  Background:
    Given the database is empty

  Scenario: Query all users when empty
    Then the REST endpoint "/users" should return an empty list

  Scenario: Query all users after creation
    Given a user exists with id "user-rest-1", name "Rest User", email "rest@example.com"
    And an account exists with id "account-rest-1" for user "user-rest-1"
    When I query the REST endpoint "/users"
    Then the response should contain user "user-rest-1" with 1 account

  Scenario: Query a single user
    Given a user exists with id "user-single", name "Single User", email "single@example.com"
    And an account exists with id "account-s1" for user "user-single"
    And an account exists with id "account-s2" for user "user-single"
    When I query the REST endpoint "/users/user-single"
    Then the response should be user "user-single" with 2 accounts
