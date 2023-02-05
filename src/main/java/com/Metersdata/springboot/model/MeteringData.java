package com.Metersdata.springboot.model;

import com.Metersdata.springboot.util.data.EnergyConsumptionMetrics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@Document("Meter_S01")
public class MeteringData {
    @Id
    private UUID id;
    private String smartMeterId;
    private long eventTime;
    private long processTime;
    private String timeZone;
    private double energyTotal;
    private double powerTotal;
    private double energyTotalNeg;
    private double energyTotalPos;
    private boolean retrieved;

    public MeteringData(){
        super();
    }

}
