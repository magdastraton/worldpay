package com.worldpay.service;

import com.worldpay.beans.Offer;
import com.worldpay.exceptions.OfferException;
import com.worldpay.util.STATUS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class TestOfferService {

    private OfferService offerService;

    ConcurrentMap<String,Offer> offers;

    @Before
    public void setUp() throws OfferException {

        offerService =new OfferService();
        offers = new ConcurrentHashMap<String, Offer>();
        Offer apples = addNewOffer("apples","4 apples for 2 £",new Date());

    }

    @Test
    public void testQueryAllOffers() throws OfferException {
        Offer banannas = addNewOffer("banannas","4 bannanas for 1 £", new Date());
        offers = offerService.queryOffers("");
        assertEquals(2,offers.size());
        assertEquals(true,offers.containsValue(offers.get("apples")));
        assertEquals(true,offers.containsValue(banannas));
    }

    @Test
    public void testQueryOffersByProductName() throws OfferException {
        String productName = "apples";
        Offer offer = offers.get(productName);
        offerService.setOffersMap(offers);
        offers = offerService.queryOffers(offer.getProductName());
        assertTrue(offers.containsKey(offer.getProductName()));
    }

    @Test(expected = OfferException.class)
    public void testQueryOffersByProductNameNotFound() throws OfferException {
        String dummyValue = "dummyValue";
        offers = offerService.queryOffers(dummyValue);
        assertEquals(offers.get(dummyValue),null);
    }

    @Test(expected = OfferException.class)
    public void testCreateOfferWhileExist() throws OfferException {
        Offer newOffer = addNewOffer("kiwi","1 kilo for 1 £",new Date());
        offerService.createOffer(newOffer);
        offers = offerService.getOffersMap();
        assertTrue(offers.containsValue(newOffer));
    }

    @Test
    public void testCreateOffer() throws OfferException {
        Offer newOffer = addNewOffer("kiwi","1 kilo for 1 £",new Date());
        offers.remove("kiwi");
        offerService.createOffer(newOffer);
        offers = offerService.getOffersMap();
        assertTrue(offers.containsValue(newOffer));
    }

    @Test
    public void testCancelOffer() throws OfferException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expiredDate = dateFormat.parse("31-05-2019");
        Offer newOffer = addNewOffer("tomatoes","1 kilo for 1 £",expiredDate);
        offerService.cancelOffer(newOffer.getProductName());
        offers = offerService.getOffersMap();
        assertTrue(offers.containsValue(newOffer));
        assertTrue(newOffer.getStatus().equals(STATUS.CANCELLED));
    }

    @Test (expected = OfferException.class)
    public void testCancelExpiredOffer() throws OfferException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expiredDate = dateFormat.parse("31-01-2019");
        Offer newOffer = addNewOffer("tomatoes","1 kilo for 1 £",expiredDate);
        offerService.cancelOffer(newOffer.getProductName());
        offers = offerService.getOffersMap();
        assertTrue(offers.containsValue(newOffer));
    }

    @Test(expected = OfferException.class)
    public void testDeleteOfferStillActive() throws OfferException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expiredDate = dateFormat.parse("31-05-2019");
        Offer newOffer = addNewOffer("tomatoes","1 kilo for 1 £",expiredDate);
        newOffer.setStatus(STATUS.ACTIVE);
        offerService.deleteOffer("tomatoes");
        offers = offerService.getOffersMap();
        assertTrue(offers.containsKey("tomatoes"));
    }

    @Test
    public void testDeleteOffer() throws OfferException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expiredDate = dateFormat.parse("31-01-2019");
        Offer newOffer = addNewOffer("tomatoes","1 kilo for 1 £",expiredDate);

        offerService.deleteOffer("tomatoes");
        offers = offerService.getOffersMap();
        assertFalse(offers.containsKey("tomatoes"));
    }

    private Offer addNewOffer(String productName, String description, Date date){
        Offer testOffer = new Offer();
        testOffer.setProductName(productName);
        testOffer.setDescription(description);
        testOffer.setCurency("£");
        testOffer.setRegularPrice(5);
        testOffer.setExpirationDate(date);
        offers.put(productName,testOffer);
        offerService.setOffersMap(offers);
        return testOffer;
    }
}
