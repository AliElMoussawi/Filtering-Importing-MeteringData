package com.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@Document("Customer_Info")
public class CustomerInfo {
     @Id
    private UUID customerId;
     @Unique
     private String userEmail;
    @Unique
     private UUID customerAccountId;

}
