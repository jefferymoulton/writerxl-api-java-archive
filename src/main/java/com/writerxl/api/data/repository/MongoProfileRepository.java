package com.writerxl.api.data.repository;

import com.writerxl.api.data.entity.MongoProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoProfileRepository extends MongoRepository<MongoProfileEntity, Integer> {

    Optional<MongoProfileEntity> findOneByKey(final String key);

    Optional<MongoProfileEntity> findOneByEmail(final String email);
}
