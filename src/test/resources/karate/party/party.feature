# TODO: Waiting for database init.
Feature: Party API regression tests

  Scenario: Active Party workflow

    # requierments :  hero unlock.
    * print 'Step 1 - get active party - 404'
    * print 'Step 2 - add member to active party - 200 + not null'
    * print 'Step 3 - get active party - 200 + not null'
    * print 'Step 4 - remove from active party - 204'
    * print 'Step 5 - get active party - 404'
    * match true == true


  Scenario: Area Party workflow

    # requierments : area seed + hero unlock.
    * print 'Step 1 - get area party - 404'
    * print 'Step 2 - add member to area party - 200 + not null'
    * print 'Step 3 - get area party - 200 + not null'
    * print 'Step 4 - remove from area party - 204'
    * print 'Step 5 - get area party - 404'
    * match true == true

  Scenario: All Parties workflow

    # requierments : area seed + hero unlock (2heroes).
    * print 'Step 1 - get all parties - empty list'
    * print 'Step 2 - add member to actie party - 200 + not null'
    * print 'Step 3 - add member to area party - 200 + not null'
    * print 'Step 4 - get all parties - size = 2'
    * print 'Step 5 - Verify one active and one area party'
    * match true == true
