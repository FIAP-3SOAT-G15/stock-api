Feature: Product

  @database
  Scenario: Creation of product
    Given valid data for product
    When request to create product
    Then product should be created
