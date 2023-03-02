package com.dto.mongodb;

import lombok.Data;

import java.util.UUID;
@Data
public class SmartMeterConcentratorDto {
    UUID concentratorId;
    UUID smartMeterId;
}