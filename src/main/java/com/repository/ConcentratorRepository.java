package com.repository;

import com.model.Concentrator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ConcentratorRepository extends MongoRepository<Concentrator,String > {
}
