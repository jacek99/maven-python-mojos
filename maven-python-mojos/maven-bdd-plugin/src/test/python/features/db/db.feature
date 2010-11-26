Feature: Bill students alphabetically
  In order to bill students properly
  As a financial specialist
  I want to bill those which name starts with some letter

  Scenario: Bill students whose name starts with "G"
    Given I have the following students in my database:
      | name     | monthly_due | billed |
      | Anton    | $ 500       | no     |
      | Jack     | $ 400       | no     |
      | Gabriel  | $ 300       | no     |
      | Gloria   | $ 442.65    | no     |
      | Ken      | $ 907.86    | no     |
      | Leonard  | $ 742.84    | no     |
    When I bill names starting with "G"
    Then I see those billed students:
      | Gabriel  | $ 300       | yes     |
      | Gloria   | $ 442.65    | yes     |
    And those that weren't:
      | Anton    | $ 500       | no     |
      | Jack     | $ 400       | no     |
      | Ken      | $ 907.86    | no     |
      | Leonard  | $ 742.84    | no     |