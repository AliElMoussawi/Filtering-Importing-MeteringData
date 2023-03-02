package com.util.data;

import lombok.Data;

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
