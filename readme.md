Prerequisites:
- I've used Spring MVC Framework to take advantage of some provided facities, such as embedded Web Container and support for unit tests
- another aproach I was initially thinking of using, just to keep this as lightweight as possible, whould have been Javax WS RS API but that would
 require to be packaged as war and deployed on a standalone web server

Command to run the application: mvn spring-boot:run

Assumptions:
- no authorization required (all mechanism is for merchant use). Otherwise another level of mapping by user identification will be easy to implement
- an Offer is identified by product name, which is assumpted as unique (eg. "golden apple" is not "red apple" or "apple")
- all Offers have the same currency in £

For exercise convenience, all the offers are stored in memory as a Concurrent Hash Map (that will assure concurrent access data integrity)
For exercise need I would have loved to offer full test coverage but limitations have applied
The idea of this application is to have all the requirements covers within a base architecture that can be easily extented, for example adding user differentiation by type (merchant, customer) and identification, authentification, DB persistence layers etc.

Improvements:
- a validator for currency.
- to mark the offers are expired, normally there should be a scheduled process that runs once per day and set the status value as EXPIRED
 if the current date is after the offer expiration date. To keep the simplicity of the application, I wrote a method called updateStatus() from OfferStartService class which is called when a GET request is sent.


