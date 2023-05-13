package com.repository;

import com.model.CustomerInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInfoRepository extends MongoRepository<CustomerInfo,String > {

    CustomerInfo findByUserEmail(String userEmail);

}
