package com.Metersdata.springboot.model;


import com.Metersdata.springboot.util.data.EnergyConsumptionData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Data
@AllArgsConstructor
@Document("Meter-S01")
public class SmartMeter {
     @Id
    private UUID id;

     private EnergyConsumptionData energyConsumptionData;




}
