package com.repository;

import com.model.MeteringData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface MeteringDataRepository extends MongoRepository<MeteringData,String > {
    List<MeteringData> findBySmartMeterIdAndRetrievedIsFalse(String meterId);
    List<MeteringData> findAllByRetrievedIsFalseAndSmartMeterIdIn(Collection<String> smartMeterIds);

    //  @Query("{'retrieved': false ,'smartMeterId': {'$in': ?0}}")//same as the above
    // List<MeteringData> query(Iterable<String> smartMeterIds);



}
