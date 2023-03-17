package com.dto.mongodb;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.UUID;

@Data
public class ConcentratorDto {
    UUID concentratorId;
    String localtion;
    long serialNumber;
    UUID product;
    DateTime manufactureDate;
}