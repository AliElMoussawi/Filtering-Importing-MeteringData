package com.Metersdata.springboot.services.meteringdb;

import com.Metersdata.springboot.dao.SmartMeterConcentratorRepository;
import com.Metersdata.springboot.dao.SmartMeterRepository;
import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.model.SmartMeter;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.util.generator.EnergyConsumptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SmartMeterService {

    @Autowired(required = true)
    SmartMeterConcentratorRepository smartMeterConcentratorRepository;
    @Autowired(required = true)
    SmartMeterRepository smartMeterRepository;

    public SmartMeter createDummyData(UUID id){
        EnergyConsumptionGenerator energyConsumptionGenerator=new EnergyConsumptionGenerator();
        SmartMeter smartMeter=new SmartMeter(energyConsumptionGenerator.generateData(String.valueOf(id)));
        return smartMeterRepository.insert(smartMeter);
    }
    @Cacheable(value = "SmartMeterDCUCache")
    public List<SmartMeterConcentrator> findAll(){
        return smartMeterConcentratorRepository.findAll();
    }


    public void createMetersData(){
     //use the cached list and for each element create Dummy data by id + async to work in parrallel with schedule

    }


}
