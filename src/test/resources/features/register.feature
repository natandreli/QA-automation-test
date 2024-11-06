Feature:
  As a user of VAMPI
  I want to be able to register
  So that I can login in the platform

  Background:
    Given I am connected to capacities of the service

  @SmokeTest
  Scenario: Successful registration with all required fields
    When I enter the correct user data:
      | email                    | password                | username       |
      | usuarioprueba1@correo.com | micontraseñaSEGURA!2035 | usuarioprueba1 |
    Then I can see a 200 status code with successful registration message
