Feature: Perform Currency Conversion

Scenario: Currency Conversion

Given the Login page is open
And the following login credentials are entered
|username|password|
|demo	 |demo    |
And the submit button is clicked

When the amount is entered as 42
And the source currency is selected as EUR
And the target currency is selected as USD
And the date is entered as 2010-01-01
And convert button is clicked

Then currency conversion is performed


