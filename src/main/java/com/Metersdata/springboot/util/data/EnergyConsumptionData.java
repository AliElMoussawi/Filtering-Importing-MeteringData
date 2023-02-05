package com.Metersdata.springboot.util.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EnergyConsumptionData {
    private String smartMeterId;
    private long eventTime;
    private long processTime;
    private String timeZone;
    private EnergyConsumptionMetrics metrics;

    public String getSmartMeterId() {
        return smartMeterId;
    }

}
