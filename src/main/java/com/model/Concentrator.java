package com.model;


import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Data
@Document("DCU")
public class Concentrator {
     @Id
    private UUID concentratorId;

    private String localtion;

    private long serialNumber;

    private UUID product;

    private DateTime manufactureDate;

    public Concentrator( UUID concentratorId,String localtion, long serialNumber, UUID product, DateTime manufactureDate) {
        super();
        this.concentratorId=concentratorId;
        this.manufactureDate=manufactureDate;
        this.product=product;
        this.localtion=localtion;
        this.serialNumber= serialNumber;
    }

}
