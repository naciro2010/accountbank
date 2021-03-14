Feature: deposit money to account
  In order to save money
  As a bank client
  I want to make a deposit in my account


  Background: I have a new empty account
    Given I have an empty account
    And I deposit 100 Euros


  Scenario Outline: I make deposit in euro on my account
    Given I deposit "<amount>"
    When I ask for the statement
    Then My balance should be "<credit>" Euros


    Examples:
      | amount         | credit   |
      | 200.00         | 300.00   |
      | 234.40         | 334.40   |
      | 200.01         | 300.01   |
