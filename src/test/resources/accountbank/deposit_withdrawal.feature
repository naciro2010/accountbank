Feature: Bank Account

  Scenario: Deposit 100 euros and ask the statement balance
    Given I deposit 100 euros
    When I ask for my balance
    Then My balance should be 100

  Scenario: Multiple Deposit / Withdraw and ask the statement balance
    Given I deposit 100 euros
    And I withdraw 50 euros
    When I ask for my balance
    Then My balance should be 50

  Scenario: I make withdrawal from my account
    Given I withdraw 20 Euros
    When I ask for my balance
    Then My balance should be 80 Euros
