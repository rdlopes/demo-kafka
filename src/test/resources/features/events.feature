Feature: Event Processing

  As a system
  I want to process user and account events from Kafka
  So that I can maintain the state in the database

  Background: Database is empty
    Given the database is empty

  Scenario: Zero - Initial state is empty
    Then there should be 0 users in the database
    And there should be 0 accounts in the database

  Scenario: One - Processing a single user event
    When a user event is sent for id "user-1" with name "John" and email "john@example.com"
    Then the user "user-1" should eventually exist with name "John" and email "john@example.com"

  Scenario: Many - Processing user and account events
    When a user event is sent for id "user-2" with name "Jane" and email "jane@example.com"
    And an account event is sent for id "account-1" for user "user-2"
    Then the user "user-2" should eventually exist with name "Jane" and email "jane@example.com"
    And the account "account-1" for user "user-2" should eventually exist

  Scenario: Many - Bulk processing of events
    When the following user events are sent:
      | id     | name | email            |
      | user-3 | Bob  | bob@example.com  |
      | user-4 | Alice| alice@example.com|
    And the following account events are sent:
      | id        | userId |
      | account-2 | user-3 |
      | account-3 | user-4 |
    Then the user "user-3" should eventually exist with name "Bob" and email "bob@example.com"
    And the user "user-4" should eventually exist with name "Alice" and email "alice@example.com"
    And the account "account-2" for user "user-3" should eventually exist
    And the account "account-3" for user "user-4" should eventually exist

  Scenario Outline: Validating multiple event combinations
    When a user event is sent for id "<userId>" with name "<name>" and email "<email>"
    And an account event is sent for id "<accountId>" for user "<userId>"
    Then the user "<userId>" should eventually exist with name "<name>" and email "<email>"
    And the account "<accountId>" for user "<userId>" should eventually exist

    Examples:
      | userId | name  | email             | accountId |
      | user-x | Alice | alice@example.com | account-x |
      | user-y | Bob   | bob@example.com   | account-y |

  Scenario: Exceptional - Account event for non-existent user
    When an account event is sent for id "account-fail" for user "unknown-user"
    Then there should be 0 accounts in the database
