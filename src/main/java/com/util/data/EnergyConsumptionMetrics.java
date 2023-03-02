package com.util.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnergyConsumptionMetrics {
    private double voltageA;
    private double voltageB;
    private double voltageAB;
    private double voltageCB;
    private double currentA;
    private double currentB;
    private double powerTotal;
    private double powerA;
    private double powerB;
    private double powerReacSum;
    private double powerReacA;
    private double powerReacB;
    private double powerAppSum;
    private double powerAppA;
    private double powerAppB;
    private double frequency;
    private double energyTotalPos;
    private double energyAPos;
    private double energyBPos;
    private double energyTotalNeg;
    private double energyANeg;
    private double energyBNeg;
    private double energyReacSumImport;
    private double energyReacAImport;
    private double energyReacBImport;
    private double energyReacSumExport;
    private double energyReacAExport;
    private double energyReacBExport;
    private double energyTotal;
    private double energyTotalA;
    private double energyTotalB;
}
