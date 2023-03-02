package com.Metersdata.springboot.service.meteringdb;

import com.Metersdata.springboot.model.MeteringData;
import com.Metersdata.springboot.repository.MeteringDataRepository;
import com.Metersdata.springboot.repository.MeteringDataConcentratorRepository;
import com.Metersdata.springboot.model.SmartMeter;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.util.generator.EnergyConsumptionGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

     // simulate metering data for the given Id.
    @Async
    public MeteringData createDummyDataForSmartMeter(String id)throws Exception{
        EnergyConsumptionGenerator energyConsumptionGenerator=new EnergyConsumptionGenerator();
        SmartMeter smartMeter=new SmartMeter(UUID.randomUUID(),energyConsumptionGenerator.generateData(id));
        return insertMeteringData(smartMeter);
    }
    //cache the smart meter ids that exist in the database
    //@Cacheable(value = "SmartMeterDCUCache", cacheManager = "cacheSmartMeterDCUManager")
    @Cacheable("addresses")
    public List<String> findAllSmartMeterIds(){
        System.out.println("findAllSmartMeterIds Method is called ");
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

    public List<MeteringData> returnMeteringData(List<String> smartMeterIds){
        return meteringDataRepository.findAllByRetrievedIsFalseAndSmartMeterIdIn(smartMeterIds);
    }
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
    @Async
    public void updateMeteringData(List<MeteringData> retrievedMeteringData){
        meteringDataRepository.saveAll(retrievedMeteringData);
        System.out.println("Update database" );}
    }


