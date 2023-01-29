package com.Metersdata.springboot.dao;

import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.model.SmartMeter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmartMeterRepository extends MongoRepository<SmartMeter,String > {
}
