Feature: Recover Payment Notices

  Background:
    Given the payment notice "DEBT1" for the taxCode "CF10000000000000" with due date "2034/03/15" and pull recovery on true
    And the payment notice "DEBT2" for the taxCode "CF10000000000000" with due date "2034/03/15" and pull recovery on false
    And the payment notice "DEBT3" for the taxCode "CF10000000000000" with due date "2034/01/01" and pull recovery on true

  Scenario: Retrieve Payment Notices filtered by existing CF
    When an Http GET request is sent to recover notices for taxCode "CF10000000000000" with dueDate "2034/03/15"
    Then response has a 200 Http status
    And response has size 1
    And response contains notice "DEBT1"

  Scenario: Retrieve Payment Notices filtered by NOT existing CF
    When an Http GET request is sent to recover notices for taxCode "CF20000000000000" with dueDate "2034/03/15"
    Then response has a 200 Http status
    And response has size 0

  Scenario: Retrieve all Payment Notices filtered by dueDate
    When an Http GET request is sent to recover notices for taxCode "CF10000000000000" with dueDate "2034/01/01"
    Then response has a 200 Http status
    And response has size 2
    And response contains notice "DEBT1"
    And response contains notice "DEBT3"


  Scenario: Debt Position with one payment option
    Given the payment notice "ONE_OPTION" with one option for org "77777777777" and debtor "STCCST83A15L0001"
    When an Http GET request is sent to recover notices for taxCode "STCCST83A15L0001"
    Then response has a 200 Http status
    And response has size 1
    And payments options has size 1
    And payments option n 1 has 1 installments


  Scenario: Debt Position with one payment option with Installments
    Given the payment notice "INSTALLMENT" with one option with installments for org "77777777777" and debtor "STCCST83A15L0002"
    When an Http GET request is sent to recover notices for taxCode "STCCST83A15L0002"
    Then response has a 200 Http status
    And response has size 1
    And payments options has size 1
    And payments option n 1 has 4 installments

  Scenario: Debt Position with two payment options and installments
    Given the payment notice "COMPLEX" with complex options and installments for org "77777777777" and debtor "STCCST83A15L0003"
    When an Http GET request is sent to recover notices for taxCode "STCCST83A15L0003"
    Then response has a 200 Http status
    And response has size 1
    And payments options has size 2
    And payments option n 1 has 1 installments
    And payments option n 2 has 3 installments
