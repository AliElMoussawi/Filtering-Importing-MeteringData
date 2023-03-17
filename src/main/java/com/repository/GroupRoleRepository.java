package com.repository;

;

import com.model.GroupRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface GroupRoleRepository extends MongoRepository<GroupRole, Long> {

    Optional<GroupRole> findByCode(String code);
}