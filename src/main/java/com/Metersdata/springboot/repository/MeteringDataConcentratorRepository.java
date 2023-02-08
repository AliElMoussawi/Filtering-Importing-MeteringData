package com.Metersdata.springboot.repository;

import com.Metersdata.springboot.model.SmartMeterConcentrator;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MeteringDataConcentratorRepository extends MongoRepository<SmartMeterConcentrator,String > {
    boolean existsBySmartMeterId(UUID smartMeterId);
}
