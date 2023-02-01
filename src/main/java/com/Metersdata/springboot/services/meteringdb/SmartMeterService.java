package com.Metersdata.springboot.services.meteringdb;

import com.Metersdata.springboot.dao.SmartMeterConcentratorRepository;
import com.Metersdata.springboot.dao.SmartMeterRepository;
import com.Metersdata.springboot.model.SmartMeter;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.services.killbill.UsageService;
import com.Metersdata.springboot.util.generator.EnergyConsumptionGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.model.gen.UsageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
public class SmartMeterService {
    private static final Logger log =  LogManager.getLogger(SmartMeterService.class);

    final long RETRY_INTERVAL_SECONDS=5;
    final long MAX_RETRIES=6;
    @Autowired(required = true)
    SmartMeterConcentratorRepository smartMeterConcentratorRepository;
    @Autowired(required = true)
    SmartMeterRepository smartMeterRepository;

    private final Executor executor;

    public SmartMeterService(Executor executor) {
        this.executor = executor;
    }

     // Create dummy data for a smart meter with the given ID.
    public SmartMeter createDummyDataForSmartMeter(String id)throws Exception{
        EnergyConsumptionGenerator energyConsumptionGenerator=new EnergyConsumptionGenerator();
        SmartMeter smartMeter=new SmartMeter(energyConsumptionGenerator.generateData(id));
        return smartMeterRepository.insert(smartMeter);
    }
    //cache the smart meter ids that exist in the database
    @Cacheable(value = "SmartMeterDCUCache")
    public List<String> findAllSmartMeterIds(){
        List<SmartMeterConcentrator> smartMeterConcentrators =smartMeterConcentratorRepository.findAll();
        List<String> smartMeterIds = new ArrayList<>();
        for (SmartMeterConcentrator concentrator : smartMeterConcentrators) {
            smartMeterIds.add(String.valueOf(concentrator.getSmartMeterId()));
        }
        return smartMeterIds;
    }

    // will generate dummy data for each smart meter in parallel with handling error and retries for the failed requests;
    @Scheduled(fixedRate = 120000)
    @Async
    public void createMetersData() throws KillBillClientException {
        List<String> smartMeterIds = findAllSmartMeterIds();
        Map<String, Integer> retryAttempts = new HashMap<>();

        for (String id : smartMeterIds) {
            executor.execute(() -> {
                int retries = 0;
                retryAttempts.getOrDefault(id, 0);
                while (retries < MAX_RETRIES) {

                    try {
                        retryAttempts.put(id, retries + 1);
                        createDummyDataForSmartMeter(id);
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
                }
            });
        }

    }

    public SmartMeter returnByMeterId(String meterId){
        return smartMeterRepository.findByEnergyConsumptionDataSmartMeterIdNotIn(meterId, );
    }

}
