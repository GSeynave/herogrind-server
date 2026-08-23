Feature: Hero API regression tests

  Scenario: Hero workflow

    * print 'Step 1 - Get unknown hero - expect 404'

    Given url 'http://localhost:8080'
    And path 'heroes', '550e8400-e29b-41d4-a716-446655440000'
    When method GET
    Then status 404

    * print 'Step 2 - Get heroes - expect empty list'

    Given url 'http://localhost:8080'
    And path 'heroes'
    When method GET
    Then status 200
    And match response == '#[0]'

    * print 'Step 3 - Unlock hero - expect 201'

    Given url 'http://localhost:8080'
    And path 'heroes'
    And request
      """
      {
        "name": "Cleric"
      }
      """
    When method POST
    Then status 201
    * def heroId = response.id

    * print 'Step 4 - Get unlocked hero - expect 200'

    Given url 'http://localhost:8080'
    And path 'heroes', heroId
    When method GET
    Then status 200
    And match response contains
      """
      {
        id: '#string',
        name: '#string',
        role: '#string',
        level: '#number',
        health: '#number',
        attack: '#number',
        defense: '#number'
      }
      """

    * print 'Step 5 - Get heroes list - expect one hero'

    Given url 'http://localhost:8080'
    And path 'heroes'
    When method GET
    Then status 200
    And match response == '#[1]'