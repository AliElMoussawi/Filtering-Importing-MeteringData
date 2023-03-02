package com.service.meteringdb;

import com.model.MeteringData;
import com.repository.MeteringDataRepository;
import com.repository.MeteringDataConcentratorRepository;
import com.model.SmartMeter;
import com.model.SmartMeterConcentrator;
import com.util.generator.EnergyConsumptionGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
public class MeteringDataService {
    private static final Logger log =  LogManager.getLogger(MeteringDataService.class);
    final long RETRY_INTERVAL_SECONDS=5;
    final long MAX_RETRIES=6;
    @Autowired
    MeteringDataConcentratorRepository smartMeterConcentratorRepository;

    @Autowired
    MeteringDataRepository meteringDataRepository;
    private final Executor executor;

    public MeteringDataService(Executor executor) {
        this.executor = executor;
    }


    /**
     * Simulate metering data for a given smart meter ID.
     *
     * @param smartMeterId the ID of the smart meter to generate data for
     * @return the metering data for the smart meter
     */
    @Async
    public MeteringData simulateMeteringData(String smartMeterId){
        EnergyConsumptionGenerator energyConsumptionGenerator=new EnergyConsumptionGenerator();
        SmartMeter smartMeter=new SmartMeter(UUID.randomUUID(),energyConsumptionGenerator.generateData(smartMeterId));
        return insertMeteringData(smartMeter);
        }    //@Cacheable("addresses")

    //cache the smart meter ids that exist in the database
    /**
     * Retrieves a list of all smart meter IDs from the that exist in the database
     * This method is cached using the SmartMeterDCUCache cache manager.
     * @return A list of all smart meter IDs.
     */
    @Cacheable(value = "SmartMeterDCUCache", cacheManager = "cacheSmartMeterDCUManager")
    public List<String> findAllSmartMeterIds(){
        System.out.println("findAllSmartMeterIds Method is called ");
        List<SmartMeterConcentrator> smartMeterConcentrators =smartMeterConcentratorRepository.findAll();
        List<String> smartMeterIds = new ArrayList<>();
        for (SmartMeterConcentrator concentrator : smartMeterConcentrators) {
            smartMeterIds.add(String.valueOf(concentrator.getSmartMeterId()));
        }
        return smartMeterIds;
    }
    /**
     *Creates metering data for all smart meters at a fixed rate concurrently
     *The data generation for each smart meter is retried up to MAX_RETRIES times with a fixed interval between each attempt.
     *The method is periodically executed
     */
    @Scheduled(fixedRate = 120000)
    @Async
    public void createMeteringData(){
        List<String> smartMeterIds = findAllSmartMeterIds();
        Map<String, Integer> retryAttempts = new HashMap<>();
        for (String id : smartMeterIds) {
            executor.execute(() -> {
                int retries = 0;
                retryAttempts.getOrDefault(id, 0);
                while (retries < MAX_RETRIES) {
                    try {
                        retryAttempts.put(id, retries + 1);
                        simulateMeteringData(id);
                        break;
                    } catch (Exception e) {
                        retries++;
                        log.error("Error generating dummy data for smart meter with ID: " + id + ". Retrying...", e);
                        try {
                            TimeUnit.SECONDS.sleep(RETRY_INTERVAL_SECONDS);
                        } catch (InterruptedException ie) {
                            log.error("Interrupted while sleeping between retries.", ie);
                        }
                    }
                }
                if (retries == MAX_RETRIES) {
                    log.error("Failed to generate dummy data for smart meter with ID: " + id + " after " + MAX_RETRIES + " retries.");
                }});}}
    public List<MeteringData> returnMeteringData(List<String> smartMeterIds){
        return meteringDataRepository.findAllByRetrievedIsFalseAndSmartMeterIdIn(smartMeterIds);
    }
    /**
     * Insert Metering Data in Database
     * This method map the simulated data into MeteringData and then store the data
     * @param smartMeter the simulated metering data of a smart meter
     * @return MeteringData
     *
     */
    @Async
    public MeteringData insertMeteringData(SmartMeter smartMeter){
        MeteringData meteringData=new MeteringData();
        meteringData.setId(UUID.randomUUID());
        meteringData.setSmartMeterId(smartMeter.getEnergyConsumptionData().getSmartMeterId());
        meteringData.setEnergyTotal(smartMeter.getEnergyConsumptionData().getMetrics().getEnergyTotal());
        meteringData.setTimeZone(smartMeter.getEnergyConsumptionData().getTimeZone());
        meteringData.setPowerTotal(smartMeter.getEnergyConsumptionData().getMetrics().getPowerTotal());
        meteringData.setEnergyTotalPos(smartMeter.getEnergyConsumptionData().getMetrics().getEnergyTotalPos());
        meteringData.setEnergyTotalNeg(smartMeter.getEnergyConsumptionData().getMetrics().getEnergyTotalNeg());
        meteringData.setEventTime(new DateTime(smartMeter.getEnergyConsumptionData().getEventTime()));
        meteringData.setProcessTime(new DateTime(smartMeter.getEnergyConsumptionData().getProcessTime()));
        meteringData.setRetrieved(false);
        return meteringDataRepository.insert(meteringData);
    }
    /**
     * Mark the records that pushed to KillBill
     * @param retrievedMeteringData the ID of the smart meter to generate data for
     */
    @Async
    public void updateMeteringData(List<MeteringData> retrievedMeteringData){
        meteringDataRepository.saveAll(retrievedMeteringData);
        System.out.println("Update database" );}


}

