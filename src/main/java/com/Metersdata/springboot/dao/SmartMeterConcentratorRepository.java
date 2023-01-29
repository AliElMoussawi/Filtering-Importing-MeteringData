package com.Metersdata.springboot.dao;

import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmartMeterConcentratorRepository extends MongoRepository<SmartMeterConcentrator,String > {
}
