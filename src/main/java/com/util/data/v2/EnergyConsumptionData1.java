package com.util.data.v2;
import com.util.data.EnergyConsumptionMetrics;
import lombok.Data;

@Data
public class EnergyConsumptionData1 {
    private String smartMeterId;
    private long eventTime;
    private long processTime;
    private String timeZone;
    private EnergyConsumptionMetrics metrics;

    public String getSmartMeterId() {
        return smartMeterId;
    }

}
