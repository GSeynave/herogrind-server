Feature: Hero API regression tests

  Scenario:  Get heroes returns succesfully
    Given url 'http://localhost:8080/heroes/550e8400-e29b-41d4-a716-446655440000'
    When method GET
    Then status 404
