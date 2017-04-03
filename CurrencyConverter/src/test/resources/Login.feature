Feature: Login to the Currency Converter

Scenario: Successful Login

Given the login page is open
When the following correct credentials are entered and submit is clicked
|username|password|
|demo	 |demo    |
Then the Currency Converter Main page is open


Scenario: Unsuccessful Login

Given the login page is open
When following incorrect credentials are entered and submit is clicked
|username|password|
|demo007 |demo007 |
Then the Currency Converter Main page is not open