package com.writerxl.api.data.repository;

import com.writerxl.api.data.entity.MongoUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoUserRepository extends MongoRepository<MongoUserEntity, Integer> {

    Optional<MongoUserEntity> findOneByEmail(final String email);
}
