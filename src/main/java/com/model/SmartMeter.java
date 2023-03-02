package com.model;


import com.util.data.EnergyConsumptionData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class SmartMeter {
    private UUID id;
     private EnergyConsumptionData energyConsumptionData;
}
