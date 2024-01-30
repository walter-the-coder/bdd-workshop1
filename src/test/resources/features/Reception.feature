#language:en
Ability: Handle revieved data by checking if end-user is authorized and then store the data in the database

  Scenario: Store data in database if user has authorization
    Given user with person id "18126220886" is authorized for the following organisations
      | 999888777 |
      | 666555444 |
      | 333222111 |
    When the following data is submitted
      | organisationNumber | 999888777               |
      | submitterId        | 18126220886             |
      | taxCategory        | NORMAL                  |
      | year               | 2024                    |
      | taxationPeriod     | JAN_FEB                 |
      | timeOfSubmission   | 2024-02-25T09:44:39.123 |
    Then the response should return with status code OK
    And the following record should exist in the database as unprocessed
      | organisationNumber | 999888777               |
      | submitterId        | 18126220886             |
      | taxCategory        | NORMAL                  |
      | year               | 2024                    |
      | taxationPeriod     | JAN_FEB                 |
      | timeOfSubmission   | 2024-02-25T09:44:39.123 |

  Scenario: Fail on missing data
    Given user with person id "18126220886" is authorized for the following organisations
      | 999888777 |
      | 666555444 |
      | 333222111 |
    When the following data is submitted
      | organisationNumber | 999888777               |
      | submitterId        | 18126220886             |
      | taxCategory        | NORMAL                  |
      | year               | 2024                    |
      | taxationPeriod     | MAR_APR                 |
      | timeOfSubmission   | 2024-02-25T09:44:39.123 |
    Then the response should be the following error
      | statusCode   | 400                                                                                                                     |
      | errorCode    | BAD_REQUEST                                                                                                             |
      | errorMessage | Invalid taxation period type: MAR_APR. We only allow submissions of the taxation period january-february at the moment. |

  Scenario: Return error if user does not has authorization
    Given user with person id "18126220886" is authorized for the following organisations
      | 333222111 |
    When the following data is submitted
      | organisationNumber | 999888777               |
      | submitterId        | 18126220886             |
      | taxCategory        | NORMAL                  |
      | year               | 2024                    |
      | taxationPeriod     | JAN_FEB                 |
      | timeOfSubmission   | 2024-02-25T09:44:39.123 |
    Then the response should be the following error
      | statusCode   | 401                                                                         |
      | errorCode    | NOT_AUTHORIZED                                                              |
      | errorMessage | User with id 18126220886 is not authorized to access organisation 999888777 |
