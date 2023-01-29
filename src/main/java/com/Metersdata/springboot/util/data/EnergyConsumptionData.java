package com.Metersdata.springboot.util.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnergyConsumptionData {
    private String smartMeterId;
    private long eventTime;
    private long processTime;
    private String timeZone;
    private EnergyConsumptionMetrics metrics;
}
