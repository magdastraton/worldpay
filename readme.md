Prerequisites:
- I've used Spring MVC Framework to take advantage of some provided facities, such as embedded Web Container and great support for integration tests
- another aproach I was initially thinking of using, just to keep this as lightweight as possible, whould have been Javax WS RS API but that would
 require to be packaged as war and deployed on a standalone web server

Command to run the application: mvn spring-boot:run

Assumptions:
- the user is logged in
- all the offers are storred in memory as a Concurrent Hash Map, just to make sure

The idea of this application is to have all the requirements covers within a base architecture that can be easily extented, for example adding a DB persistence layer.

Improvements:
A validator for currency.
To mark the offers are expired, normally there should be a scheduled process that runs once per day and set the status value as EXPIRED
if the current date is after the offer expiration date.
To keep the simplicity of the application, I wrote a method called updateStatus() from OfferStartService class which is called when a GET request is
sent.


