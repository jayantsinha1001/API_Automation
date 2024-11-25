Feature: To send a GET Request

  Scenario Outline: To Send a GET Request
    Given user sets up the base uri
    When user sends a GET request to endpoint "<endpoint>"
    Then user receives response code as <Response_Code>
    And user validates the response body as JSON
    Examples:
      | endpoint | Response_Code |
      | /posts   | 200           |