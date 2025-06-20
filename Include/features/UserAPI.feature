#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
#@tag
#
Feature: Reqres API Tests

  Scenario: Register user with valid credentials
    Given The API endpoint is "https://reqres.in/api/register"
    And The request body contains valid email and password
    When I send a POST request to register
    Then The response status code should be 200
    And The response should contain an "id" and a "token"

  Scenario: Get a list of users from page 2
    Given The API endpoint is "https://reqres.in/api/users?page=2"
    When I send a GET request to retrieve user list
    Then The response status code should be 200
    And The response should contain a list of users

  Scenario: Get details of a specific user
    Given The API endpoint is "https://reqres.in/api/users/2"
    When I send a GET request to retrieve the user
    Then The response status code should be 200
    And The response should contain the user's first name and email
    