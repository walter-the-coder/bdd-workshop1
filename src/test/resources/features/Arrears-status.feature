#language:en
Ability: Display the arrears status

  Background:
    Given taxpayer with id 101001554433 is logged in
    And the taxpayer has selected certificate with name "TODO"
    And the taxpayer has selected certification type "Tax Clearance"

  @NotImplemented
  Scenario: Display the arrears status when there are unpaid taxes
    Given the user has the following unpaid arrears
      | taxType | Domestic |
      | periode | JAN_FEB  |
      | year    | 2024     |
      | total   | 10000.0  |
    When the user apply for the certificate
    Then the following arrears details are displayed
      | taxType | Domestic |
      | periode | JAN_FEB  |
      | year    | 2024     |
      | total   | 10000.0  |

  @NotImplemented
  Scenario: Take user directly to payment when there are no unpaid taxes
    Given the user has no unpaid arrears
    When the user apply for the certificate
    Then the user can proceed with payment
