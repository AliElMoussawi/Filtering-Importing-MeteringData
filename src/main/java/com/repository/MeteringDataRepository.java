package com.repository;

import com.model.MeteringData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository

public interface MeteringDataRepository extends MongoRepository<MeteringData,String > {

    List<MeteringData> findBySmartMeterIdAndRetrievedIsFalse(String meterId);

    /**
     * This function calls this query: @Query("{'retrieved': false ,'smartMeterId': {'$in': ?0}}")
     * retrieves the MeteringData that didn't pushed to KillBill
     * @param smartMeterIds
     * @return MeteringData
     */
    List<MeteringData> findAllByRetrievedIsFalseAndSmartMeterIdIn(Collection<String> smartMeterIds);


}
