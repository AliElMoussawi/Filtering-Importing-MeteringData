package com.Metersdata.springboot.model;


import com.Metersdata.springboot.util.data.EnergyConsumptionData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.UUID;
@Data
@AllArgsConstructor
@Document("Meter-S01")
public class SmartMeter {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

     private EnergyConsumptionData energyConsumptionData;

     public SmartMeter(EnergyConsumptionData energyConsumptionData){
         this.energyConsumptionData=energyConsumptionData;
     }


}
