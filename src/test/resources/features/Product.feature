Feature: Product feature

  Scenario: Product found
    Given a product with ID 1 exists
    When I request the product with ID 1
    Then the product details are returned

  Scenario: Product not found
    Given no product with ID 99 exists
    When I request the product with ID 99
    Then a not found error is returned

#Get all products
  Scenario: Products are available
    Given products are present in the store
    When I request all products
    Then the list of all products is returned

  Scenario: No products are available
    Given I request all products
    Then no products are present in the store
    Then an empty list is returned

  #Get products by IDs
  Scenario: Valid product IDs provided
    Given the following product IDs exist: "1, 2"
    When I request products with these IDs
    Then the corresponding products are returned

  Scenario: No product IDs provided
    Given no product IDs are provided
    When I request products with these IDs
    Then a product not found error is thrown

  Scenario: Non-existing product IDs provided
    Given the following product IDs do not exist: "999, 9999"
    When I request products with these IDs
    Then a product not found error is thrown

#Get total price by product IDs
  Scenario: Valid product IDs provided
    Given the following product IDs: "1, 2"
    When I request the total price for these products
    Then the total price is returned

  Scenario: No product IDs provided
    Given no product IDs are provided
    When I request the total price for these products
    Then a default price of "0" is returned

#Get products by category name
  Scenario: Category name exists
    Given the category with name "Bebidas" exists
    When I request products for the category "Bebidas"
    Then a list of products in the "Bebidas" category is returned

  Scenario: Category name does not exist
    Given the category with name "Furniture" does not exist
    When I request products for the category "Furniture"
    Then an empty list is returned