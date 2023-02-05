package com.Metersdata.springboot.model;


import com.Metersdata.springboot.util.data.EnergyConsumptionData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class SmartMeter {
    private UUID id;

     private EnergyConsumptionData energyConsumptionData;
}
