package com.Metersdata.springboot.dao;

import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.model.UsageRecordKillBill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsageRecordKillBillRepository extends MongoRepository<UsageRecordKillBill,String > {
}
