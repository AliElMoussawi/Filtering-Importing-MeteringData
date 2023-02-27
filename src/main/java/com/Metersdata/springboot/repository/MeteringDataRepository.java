package com.Metersdata.springboot.repository;

import com.Metersdata.springboot.model.MeteringData;
import com.Metersdata.springboot.model.SmartMeter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MeteringDataRepository extends MongoRepository<MeteringData,String > {
    List<MeteringData> findBySmartMeterIdAndRetrievedIsFalse(String meterId);
    List<MeteringData> findAllByRetrievedIsFalseAndSmartMeterIdIn(Collection<String> smartMeterIds);

    //  @Query("{'retrieved': false ,'smartMeterId': {'$in': ?0}}")//same as the above
    // List<MeteringData> query(Iterable<String> smartMeterIds);



}
