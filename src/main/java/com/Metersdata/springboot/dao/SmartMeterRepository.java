package com.Metersdata.springboot.dao;

import com.Metersdata.springboot.model.SmartMeter;
import com.mongodb.client.model.Collation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmartMeterRepository extends MongoRepository<SmartMeter,String > {

    SmartMeter findByEnergyConsumptionDataSmartMeterId(String meterId);

    SmartMeter findByEnergyConsumptionDataSmartMeterIdNotIn(String meterId, Collation collection);
}
