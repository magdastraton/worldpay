package com.worldpay.rest.controller;

import com.worldpay.beans.Offer;
import com.worldpay.service.OfferService;
import com.worldpay.exceptions.OfferException;

import java.util.concurrent.ConcurrentMap;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@RequestMapping("rest/offers")

public class OfferRestController {

    private OfferService service = new OfferService();

    @RequestMapping(value="",method =  RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ConcurrentMap<String,Offer>  getOffers(@RequestParam(value = "query", required = false) String query){
        try {
            return service.queryOffers(query);
        } catch (OfferException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

    @RequestMapping(value="", method =  RequestMethod.POST)
    @ResponseBody
    public Offer createOffer(@RequestBody Offer offer){
        try {
            service.createOffer(offer);
        } catch (OfferException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
        return offer;
    }

    @RequestMapping(value="", method =  RequestMethod.DELETE)
    @ResponseBody
    public String deleteOffer(@RequestParam String productName){
        try {
            service.deleteOffer(productName);
            return "Offer for "+productName+" was deleted successfully.";
        } catch (OfferException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RequestMapping(value="", method =  RequestMethod.PUT)
    @ResponseBody
    public String cancelOffer(@RequestParam String productName){
        try
        {
          service.cancelOffer(productName);
        }
        catch (OfferException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_MODIFIED, e.getMessage(), e);
        }
        return "Offer for "+productName+" was cancelled";
    }

}
