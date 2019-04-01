package com.worldpay.service;

import com.worldpay.beans.Offer;
import com.worldpay.exceptions.OfferException;
import com.worldpay.util.STATUS;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class OfferService {

    private ConcurrentMap<String, Offer> offersMap;

    public OfferService(){
        offersMap = new ConcurrentHashMap<String, Offer>();
    }

    public ConcurrentMap<String,Offer> queryOffers(String query) throws OfferException {
        updateStatus();
        if(StringUtils.isEmpty(query)) {
            return offersMap;
        }
       ConcurrentMap<String,Offer>  resultMap =offersMap.entrySet().stream()
                .filter(x -> x.getKey().equals(query))
                .collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        if(resultMap==null || resultMap.isEmpty()) {
            throw new OfferException(OfferException.OFFER_NOT_FOUND);
        }
        return resultMap;
    }

    public void createOffer(Offer offer) throws OfferException
    {
        Offer exitingOffer = offersMap.get(offer.getProductName());
        if(!needToRejectOffer(exitingOffer))
            throw new OfferException(OfferException.OFFER_ALREADY_EXISTS);
        STATUS status = offer.isExpired()?STATUS.EXPIRED:STATUS.ACTIVE;
        offer.setStatus(status);
        offersMap.put(offer.getProductName(),offer);
    }

    public void cancelOffer(String productName) throws OfferException
    {
        Offer offer = offersMap.get(productName);
        if(needToRejectOffer(offer))
            throw new OfferException(OfferException.OFFER_NOT_VALID);
        offer.setStatus(STATUS.CANCELLED);
        offersMap.replace(productName,offer);
    }

    public void deleteOffer(String productName) throws OfferException {
        //delete an offer only if it has been canceled or expired
        Offer toBeDeleted = offersMap.get(productName);
        if(!canBeDeleted(toBeDeleted))
            throw new OfferException(OfferException.OFFER_CANNOT_BE_DELETED);
        offersMap.remove(productName);
    }

    private void updateStatus(){
        for(Offer offer:offersMap.values()){
            if(offer.getExpirationDate().before(new Date()))
                offer.setStatus(STATUS.EXPIRED);
        }
    }

    private boolean needToRejectOffer(Offer offer)
    {
        return (offer==null ||offer.isExpired());
    }

    private boolean canBeDeleted(Offer offer){
        return offer==null || offer.getStatus()!=STATUS.ACTIVE;
    }

    public ConcurrentMap<String, Offer> getOffersMap() {
        return offersMap;
    }

    public void setOffersMap(ConcurrentMap<String, Offer> offersMap) {
        this.offersMap = offersMap;
    }
}
