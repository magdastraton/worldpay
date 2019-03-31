package com.worldpay.util;

public enum STATUS {

    ACTIVE("active"),
    CANCELLED("canceled"),
    EXPIRED("expired");

    String value;

    private STATUS(String value){
        this.value = value;
    }


}
