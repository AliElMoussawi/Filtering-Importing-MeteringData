package com.Metersdata.springboot.repository;

import com.Metersdata.springboot.model.Concentrator;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ConcentratorRepository extends MongoRepository<Concentrator,String > {
}
