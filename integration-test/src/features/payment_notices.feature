Feature: Recover Payment Notices

  Scenario: Retrieve Payment Notices
    Given the payment notice "DEBT1" for the taxCode "CF1" with due date "2024/03/15" and pull recovery on "true"
    And the payment notice "DEBT2" for the taxCode "CF1" with due date "2024/03/15" and pull recovery on "false"
    And the payment notice "DEBT3" for the taxCode "CF1" with due date "2024/01/01" and pull recovery on "true"

    When an Http GET request is sent to recover notices for taxCode "CF1" with dueDate "2024/03/15"
    Then response has a 200 Http status
    AND response has size "1"
    And response contains notice "DEBT1"

    When an Http GET request is sent to recover notices for taxCode "CF2" with dueDate "2024/03/15"
    Then response has a 200 Http status
    And response has size "0"

    When an Http GET request is sent to recover notices for taxCode "CF1" with dueDate "2024/01/01"
    Then response has a 200 Http status
    AND response has size "2"
    And response contains notice "DEBT1"
    And response contains notice "DEBT3"


    
