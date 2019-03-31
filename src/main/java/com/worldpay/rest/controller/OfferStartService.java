package com.worldpay.rest.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class OfferStartService extends SpringBootServletInitializer {

    public static void main(String[] args) {
       // SpringApplication.run(OfferRestController.class, args);
        new OfferStartService().configure(new SpringApplicationBuilder(OfferStartService.class)).run(args);
    }

}
