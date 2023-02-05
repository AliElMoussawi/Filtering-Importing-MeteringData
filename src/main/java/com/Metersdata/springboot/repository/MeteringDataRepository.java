package com.Metersdata.springboot.repository;

import com.Metersdata.springboot.model.MeteringData;
import com.Metersdata.springboot.model.SmartMeter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MeteringDataRepository extends MongoRepository<MeteringData,String > {
    List<MeteringData> findBySmartMeterId(String meterId);
    List<MeteringData> findBySmartMeterIdAndRetrievedIsFalse(String meterId);
}
