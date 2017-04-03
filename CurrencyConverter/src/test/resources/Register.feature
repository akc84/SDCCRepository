Feature: Register user in Currency Converter

Scenario: Successful Registration

Given the Registration page is open

When the following user data is entered
|username|password|confirmPassword|firstName|lastName|  emailId    |dateOfBirth|
|demo01  |demo1234|demo1234		  |demo01	|demo01	 |demo@demo.com|2002-01-01 |

And the following address data is entered
|addressLine1|     street    |    city    |zipCode| state |country|
|No. 15		 | Sonnenstrasse |   Munich   | 80331 |Bavaria|Germany|

And Register button is clicked

Then registration is successful and Login page is open



Scenario: Unsuccessful Registration

Given the Registration page is open

When the following user data is entered
|username|password|confirmPassword|firstName|lastName|  emailId    |dateOfBirth|
|demo02  |demo1234|demo1234		  |     	|demo01	 |demo@demo.com|2029-01-01 |

And the following address data is entered
|addressLine1|     street    |    city    |zipCode| state |country|
|No. 15		 | Sonnenstrasse |   Munich   | 80331 |Bavaria|Germany|

And Register button is clicked

Then registration is unsuccessful and Login page is not open