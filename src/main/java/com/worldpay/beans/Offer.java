package com.worldpay.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.worldpay.handler.DateDeserializer;
import com.worldpay.handler.DateSerializer;
import com.worldpay.util.STATUS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer {

    @JsonProperty
    String productName;
    @JsonProperty
    String description;
    @JsonProperty
    double regularPrice;
    @JsonProperty
    String curency;
    @JsonProperty
    Date expirationDate;

    STATUS status;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getCurency() {
        return curency;
    }

    public void setCurency(String curency) {
        this.curency = curency;
    }

    @JsonSerialize(using=DateSerializer.class)
    public Date getExpirationDate() {
        return expirationDate;
    }

    @JsonDeserialize(using = DateDeserializer.class)
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public boolean isExpired(){
        Date current = Calendar.getInstance().getTime();
        return this.expirationDate.before(current);
    }

    public boolean equals(Offer obj) {
        return productName.equalsIgnoreCase(obj.getProductName());
    }

    public int hashCode(){
        return Objects.hash(productName,description, regularPrice,curency,expirationDate,status);
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        sb.append(productName);
        if(isExpired())
            sb.append(" ").append("EXPIRED").append(" ");
        sb.append(description).append(" ").append(regularPrice).append(" ").append(curency).append(" ").append(dateFormat.format(expirationDate)).append(" ").append(status);
        //sb.append("\t").append(res.toString()).append("\n");
        return sb.toString();
    }
}
