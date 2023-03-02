package com.repository;

import com.model.Concentrator;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ConcentratorRepository extends MongoRepository<Concentrator,String > {
}
