package com.worldpay.service;

import com.worldpay.beans.Offer;
import com.worldpay.rest.controller.OfferRestController;
import com.worldpay.rest.controller.OfferStartService;
import com.worldpay.util.AbstractTest;
import com.worldpay.util.STATUS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={OfferStartService.class})
@WebMvcTest(OfferRestController.class)
public class TestOfferRestController  extends AbstractTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    @Test
    public void createOffer() throws Exception {
        Offer offer = addApplesOffer();
        String inputJson = super.mapToJson(offer);
        System.out.println("offer "+inputJson);
        MvcResult mvcResult = mockMvc.perform(post("/rest/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "{\"productName\":\"apples\"," +
                "\"description\":\"4 apples for 2 £\"," +
                "\"regularPrice\":3.0," +
                "\"curency\":\"£\"," +
                "\"expirationDate\":\"31-05-2019\"," +
                "\"status\":\"ACTIVE\"," +
                "\"expired\":false}");

    }


    @Test
    public void queryAllOffers() throws Exception {
        Offer offer = addApplesOffer();
        ConcurrentMap<String,Offer> map = new ConcurrentHashMap<>();
        map.put(offer.getProductName(),addApplesOffer());
        offerService.setOffersMap(map);

        MvcResult mvcResult = this.mockMvc.perform(get("/rest/offers")).andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        System.out.println("content == "+content);

        assertEquals(content, "{\"productName\":\"apples\"," +
                "\"description\":\"4 apples for 2 £\"," +
                "\"regularPrice\":3.0," +
                "\"curency\":\"£\"," +
                "\"expirationDate\":\"31-05-2019\"," +
                "\"status\":\"ACTIVE\"," +
                "\"expired\":false}");
    }


    @Test
    public void queryOffersByProductName() throws Exception {
        Offer offer = addApplesOffer();
        ConcurrentMap<String,Offer> map = new ConcurrentHashMap<>();
        map.put(offer.getProductName(),offer);
        offerService.setOffersMap(map);
        given(offerService.queryOffers("apples")).willReturn(map);

        MvcResult mvcResult = this.mockMvc.perform(get("/rest/offers").param("productName","apples"))
                      .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        System.out.println("content == "+content);

        assertEquals(content, "{\"productName\":\"apples\"," +
                "\"description\":\"4 apples for 2 £\"," +
                "\"regularPrice\":3.0," +
                "\"curency\":\"£\"," +
                "\"expirationDate\":\"31-05-2019\"," +
                "\"status\":\"ACTIVE\"," +
                "\"expired\":false}");
    }

    @Test
    public void cancelOffer() throws Exception {
        Offer offer = addApplesOffer();
        ConcurrentMap<String,Offer> map = new ConcurrentHashMap<>();
        map.put(offer.getProductName(),offer);
        given(offerService.queryOffers(null)).willReturn(map);

        MvcResult mvcResult = this.mockMvc.perform(put("/rest/offers").param("productName","apples"))
                        .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        System.out.println("content == "+content);

        assertEquals(content, "{\"productName\":\"apples\"," +
                "\"description\":\"4 apples for 2 £\"," +
                "\"regularPrice\":3.0," +
                "\"curency\":\"£\"," +
                "\"expirationDate\":\"31-05-2019\"," +
                "\"status\":\"CANCELED\"," +
                "\"expired\":false}");
    }

    @Test
    public void cancelInvalidOffer() throws Exception {
        Offer offer = addApplesOffer();
        ConcurrentMap<String,Offer> map = new ConcurrentHashMap<>();
        map.put(offer.getProductName(),offer);
        given(offerService.queryOffers(null)).willReturn(map);

        this.mockMvc.perform(put("/rest/offers").param("productName","apples"))
                .andExpect(status().isNotModified()).andReturn();
    }

    @Test
    public void deleteOffer() throws Exception {
        Offer offer = addApplesOffer();
        ConcurrentMap<String,Offer> map = new ConcurrentHashMap<>();
        map.put(offer.getProductName(),offer);
        given(offerService.queryOffers(null)).willReturn(map);

        this.mockMvc.perform(delete("/rest/offers").param("productName","apples"))
                .andExpect(status().isOk()).andReturn();
    }


    private Date getFormattedDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }

    private Offer addApplesOffer() throws ParseException {
        Offer apples = new Offer();
        apples.setProductName("apples");
        apples.setDescription("4 apples for 2 £");
        apples.setCurency("£");
        apples.setRegularPrice(3);
        apples.setExpirationDate(getFormattedDate("31-05-2019"));
        apples.setStatus(STATUS.ACTIVE);
        //ConcurrentMap offers = new ConcurrentHashMap<String, Offer>();
        //offers.put("apples", apples);
        return apples;
    }

}
