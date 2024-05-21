Feature: Component

  @database
  Scenario: Creation of product component
    Given valid data for product component
    When request to create product component
    Then product component should be created
