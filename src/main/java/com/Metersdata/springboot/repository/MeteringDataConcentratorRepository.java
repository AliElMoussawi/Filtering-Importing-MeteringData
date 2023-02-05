package com.Metersdata.springboot.repository;

import com.Metersdata.springboot.model.SmartMeterConcentrator;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeteringDataConcentratorRepository extends MongoRepository<SmartMeterConcentrator,String > {
}
