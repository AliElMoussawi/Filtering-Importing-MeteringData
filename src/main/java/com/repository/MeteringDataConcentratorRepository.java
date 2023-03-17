package com.repository;

import com.model.SmartMeterConcentrator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository

public interface MeteringDataConcentratorRepository extends MongoRepository<SmartMeterConcentrator,String > {
    boolean existsBySmartMeterId(UUID smartMeterId);
}
