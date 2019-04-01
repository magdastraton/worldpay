package com.worldpay.exceptions;

public class OfferException extends Exception{

    public static final String OFFER_NOT_FOUND = "Offer not found";
    public static final String OFFER_ALREADY_EXISTS = "There is already an existing offer";
    public static final String OFFER_NOT_VALID = "Offer does not exist or is not valid";
    public static final String OFFER_CANNOT_BE_DELETED = "Offer is still Active and cannot be deleted";

    public OfferException(String message){
        super(message);
    }

}
