package com.Metersdata.springboot.util.generator;

import com.Metersdata.springboot.util.data.EnergyConsumptionData;
import com.Metersdata.springboot.util.data.EnergyConsumptionMetrics;

import java.util.Random;
import java.util.UUID;

public class EnergyConsumptionGenerator {
    private static final int MIN_VOLTAGE = 110;
    private static final int MAX_VOLTAGE = 120;
    private static final double MIN_CURRENT = 0.5;
    private static final double MAX_CURRENT = 2.0;
    private static final double MIN_POWER = 50;
    private static final double MAX_POWER = 200;
    private static final double MIN_FREQUENCY = 59;
    private static final double MAX_FREQUENCY = 61;
    private static final double MIN_ENERGY = 0;
    private static final double MAX_ENERGY = 500;

    private final Random random = new Random();

    public EnergyConsumptionData generateData() {
        EnergyConsumptionData data = new EnergyConsumptionData();
        data.setSmartMeterId(UUID.randomUUID().toString());//fix this one
        data.setEventTime(System.currentTimeMillis());
        data.setProcessTime(System.currentTimeMillis());
        data.setTimeZone("UTC");
        data.setMetrics(generateMetrics());
        return data;
    }

    private EnergyConsumptionMetrics generateMetrics() {
        EnergyConsumptionMetrics metrics = new EnergyConsumptionMetrics();
        metrics.setVoltageA(randomDouble(MIN_VOLTAGE, MAX_VOLTAGE));
        metrics.setVoltageB(randomDouble(MIN_VOLTAGE, MAX_VOLTAGE));
        metrics.setVoltageAB(randomDouble(MIN_VOLTAGE * 2, MAX_VOLTAGE * 2));
        metrics.setVoltageCB(randomDouble(MIN_VOLTAGE * 2, MAX_VOLTAGE * 2));
        metrics.setCurrentA(randomDouble(MIN_CURRENT, MAX_CURRENT));
        metrics.setCurrentB(randomDouble(MIN_CURRENT, MAX_CURRENT));
        metrics.setPowerTotal(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerA(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerB(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerReacA(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerReacB(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerReacSum(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerAppSum(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerAppA(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setPowerAppB(randomDouble(MIN_POWER, MAX_POWER));
        metrics.setFrequency(randomDouble(MIN_FREQUENCY, MAX_FREQUENCY));
        metrics.setEnergyTotalPos(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyAPos(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyBPos(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyReacSumImport(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyReacAImport(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyReacBImport(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyReacSumExport(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyReacAExport(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyReacBExport(randomDouble(MIN_ENERGY, MAX_ENERGY));
        metrics.setEnergyTotal(metrics.getEnergyTotalPos()- metrics.getEnergyTotalNeg());
        metrics.setEnergyTotalA(metrics.getEnergyAPos()-metrics.getEnergyANeg());
        metrics.setEnergyTotalB(metrics.getEnergyBPos()-metrics.getEnergyBNeg());
        return metrics;}
    public double randomDouble(double min, double max){
        return (Math.random() * ((max-min  + 1)) + min);

    }
}