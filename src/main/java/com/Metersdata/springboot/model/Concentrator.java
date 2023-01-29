package com.Metersdata.springboot.model;


import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Document("DCU")
public class Concentrator {
     @Id
    private UUID concentratorId;

    private String localtion;

    private long serialNumber;

    private UUID product;

    private DateTime manufactureDate;



    public UUID getConcentratorId() {
        return concentratorId;
    }

    public void setConcentratorId(UUID concentratorId) {
        this.concentratorId = concentratorId;
    }

    public String getLocaltion() {
        return localtion;
    }

    public void setLocaltion(String localtion) {
        this.localtion = localtion;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(UUID product) {
        this.product = product;
    }

    public DateTime getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(DateTime manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    @Override
    public String toString() {
        return "ModelContentrator{" +
                "concentratorId=" + concentratorId +
                ", localtion='" + localtion + '\'' +
                ", serialNumber=" + serialNumber +
                ", product=" + product +
                ", manufactureDate=" + manufactureDate +
                '}';
    } public Concentrator( UUID concentratorId,String localtion, long serialNumber, UUID product, DateTime manufactureDate) {
        super();
        this.concentratorId=concentratorId;
        this.manufactureDate=manufactureDate;
        this.product=product;
        this.localtion=localtion;
        this.serialNumber= serialNumber;
    }

}
