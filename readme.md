Prerequisites:
- I've used Spring MVC Framework to take advantage of some provided facities, such as embedded Web Container and support for unit tests
- another aproach I was initially thinking of using, just to keep this as lightweight as possible, whould have been Javax WS RS API but that would
 require to be packaged as war and deployed on a standalone web server

Command to run the application: mvn spring-boot:run
To test the application, use any REST CLIENT. The application runs on default port 8080

Example: Method GET (all Offers) -> http://localhost:8080/rest/offers
                    (with filter by product name) -> http://localhost:8080/rest/offers?query=apples
         Method POST (create Offer) -> http://localhost:8080/rest/offers
         JSON Body {
                     "productName":"grapes",
                      "description":"4 grapes for 2 £",
                      "regularPrice":"3",
                      "curency":"£",
                      "expirationDate":"31-05-2019"
                   }
        Method PUT (cancel Offer) ->http://localhost:8080/offers?productName=tomatoes
        Method DELETE (delete expired Offer) -> http://localhost:8080/offers?productName=tomatoes

Assumptions:
- no authorization required (all mechanism is for merchant use). Otherwise another level of mapping by user identification will be easy to implement
- an Offer is identified by product name, which is assumpted as unique (eg. "golden apple" is not "red apple" or "apple")
- an Offer can be in ACTIVE, CANCEL and EXPIRED state.
    An Offer is ACTIVE if its expiration date is after the current date.
    An offer can be DELETED only if it is CANCELED or EXPIRED.
    An offer can be CANCELED only if it is ACTIVE.
    An offer can be created if there is no other ACTIVE offer with the same product name. If there is an EXPIRED offer, creating a new one with the same product name is successful.
- for simplicity, there is just one Offer per product, no matter the status
- all Offers have the same currency in £

For exercise convenience, all the offers are stored in memory as a Concurrent Hash Map (that will assure concurrent access data integrity)
For exercise need I would have loved to offer full test coverage but limitations have applied
The idea of this application is to have all the requirements covers within a base architecture that can be easily extented, for example adding user differentiation by type (merchant, customer) and identification, authentification, DB persistence layers etc.

Improvements:
- a validator for currency.
- multiple query filters for offers
- to mark the offers are expired, normally there should be a scheduled process that runs once per day and set the status value as EXPIRED
 if the current date is after the offer expiration date. To keep the simplicity of the application, I wrote a method called updateStatus() from OfferStartService class which is called when a GET request is sent.


