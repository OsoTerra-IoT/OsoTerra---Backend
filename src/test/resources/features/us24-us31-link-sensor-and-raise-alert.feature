Feature: US24 and US31 Link a sensor and raise a salinity alert
  As a farmer
  I want to link my sensor to a plot and be alerted when salinity exceeds my crop threshold
  So that I can act before the harvest is affected

  Scenario: Sensor linked by its activation code
    Given a farmer with a plot of table grape
    When the farmer registers the device with activation code "OSO-3H8D-5T2W"
    And attaches it to the plot
    Then the device status is "ACTIVE" and it belongs to the plot

  Scenario: Reading above the crop threshold
    Given a farmer with a plot of table grape
    And the plot has an active device
    When the Edge Service sends a batch with a compensated conductivity of 2.4 dS/m
    And the request carries a valid "X-Edge-Api-Key"
    Then the batch is synchronized
    And an open salinity alert with severity "CRITICAL" exists for the plot

  Scenario: Batch without the Edge key
    Given a farmer with a plot of table grape
    And the plot has an active device
    When the Edge Service sends a batch with a compensated conductivity of 2.4 dS/m
    And the request carries no "X-Edge-Api-Key"
    Then the response status is 401
