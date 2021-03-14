Feature: Statement printing
  In order to check my operations
  As a bank client
  I want to see the history (operation, date, amount, balance) of my operations

  Background: I have a provisioned account
    Given I have an empty account

  Scenario: make multiple deposits on my account and print statement
    Given I deposit 100 euros
    And I withdraw 50 euros
    When I show my history
    Then I should see
      | type       | date             | amount | balance | currency |
      | WITHDRAWAL | 2021-02-02T17:09 | -50    | 50      | EUR      |
      | DEPOSIT    | 2021-02-02T17:09 | 100    | 100     | EUR      |

