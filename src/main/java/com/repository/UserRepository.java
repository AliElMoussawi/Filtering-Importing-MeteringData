package com.repository;

import com.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findById(UUID id);
    User findByUsername(String username);

   Boolean existsByCustomerAccountId(UUID id);
}
