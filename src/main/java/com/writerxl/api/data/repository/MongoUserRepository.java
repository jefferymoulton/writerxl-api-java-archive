package com.writerxl.api.data.repository;

import com.writerxl.api.data.entity.MongoUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoUserRepository extends MongoRepository<MongoUserEntity, Integer> {
}
