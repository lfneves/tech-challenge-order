Feature: Get user by username

  Scenario: User found by username
    Given a user with username "99999999999"
    When I request to get the user by username "99999999999"
    Then the user should be returned

  Scenario: User not found by username
    Given no user with username "test"
    When I request to get the user by username "test"
    Then a NotFoundException should be thrown
