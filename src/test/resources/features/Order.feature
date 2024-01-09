Feature: Order BDD test
  @smoke
  Scenario: Creating a valid order
    Given a user named "99999999999"
    And products with IDs "1, 2, 3"
    When I create an order for "99999999999" with products "1, 2, 3"
    Then the order should be successfully created
    And the total price should be calculated correctly

#Feature: Get order by ID
  Scenario: Getting an existing order by ID
    Given an order exists with ID 1
    When I retrieve the order with ID 1
    Then the order should be successfully returned

  Scenario: Trying to get an order with a non-existing ID
    When I retrieve the order not exists with ID 9999
    Then a not found error should be returned

#Feature: Retrieve order products by order ID
  Scenario: Retrieving products for an existing order
    Given an order with ID 1 has products
    When I retrieve products for order with ID 1
    Then the products should be successfully returned

  Scenario: Retrieving products for a non-existing order
    When I retrieve products for order with ID 99
    Then a not found error should be returned

#Feature: Retrieve order by external ID
  Scenario: Retrieving an existing order by external ID
    Given a user named "99999999999"
    And products with IDs "1, 2, 3"
    When I create an order for "99999999999" with products "1, 2, 3"
    Then the order should be successfully created
    When I retrieve the order with external ID
    Then the order should be successfully returned

  Scenario: Trying to retrieve an order with a non-existing external ID
    When I retrieve not exist the order with external ID "00000000-0000-0000-0000-000000000000"
    Then no order should be found

#Feature: Update order product
  Scenario: Updating an existing unpaid order
    Given a user named "99999999999"
    And products with IDs "1, 2, 3"
    When I create an order for "99999999999" with products "1, 2, 3"
    Then the order should be successfully created
    When I update the order for user "99999999999"
    Then the order should be successfully updated


#Feature: Save order products
  Scenario: Saving a list of order products
    Given a user named "99999999999"
    And products with IDs "1, 2, 3"
    When I create an order for "99999999999" with products "1, 2, 3"
    Given a list of order products
    When I save the order products
    Then the order products should be successfully saved

#Feature: Retrieve all order products by order ID
  Scenario: Retrieving products for an existing order
    When I create an order for "99999999999" with products "1"
    When I retrieve all products for order
    Then the products should be successfully returned

  Scenario: Trying to retrieve products for a non-existing order
    When I retrieve all products not exists for order with ID 9999
    Then a not found error should be returned


#Feature: Delete order products by IDs
#  Scenario: Deleting existing order products
#    When I create an order for "99999999998" with products "1, 2, 3"
#    When I delete the order for "99999999998" products with IDs "1, 2, 3"
#    Then the order products should be successfully deleted

#Feature: Delete order by ID
  Scenario: Deleting an existing order
    When I create an order for "99999999999" with products "1"
    When Delete the order
    Then the order should be successfully deleted for username "99999999999"