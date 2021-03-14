Feature: Statement printing
  In order to check my operations
  As a bank client
  I want to see the history (operation, date, amount, balance) of my operations

  Background: I have a provisioned account
    Given I have an empty account

  Scenario: make multiple deposits on my account and print statement
    Given make the following operations
      | operation  | date       | amount |
      | DEPOSIT    | 2010-10-01 | 50     |
      | DEPOSIT    | 2010-10-02 | 30     |
      | WITHDRAWAL | 2010-10-03 | 20     |
    When I show my history
    Then I should see
      | operation  | date       | amount | balance |
      | WITHDRAWAL | 2010-10-03 | -20    | 60      |
      | DEPOSIT    | 2010-10-02 | 30     | 80      |
      | DEPOSIT    | 2010-10-01 | 50     | 50      |
