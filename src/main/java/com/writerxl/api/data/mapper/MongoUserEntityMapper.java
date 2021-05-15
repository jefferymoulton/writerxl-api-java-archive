package com.writerxl.api.data.mapper;

import com.writerxl.api.data.entity.MongoUserEntity;
import com.writerxl.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface MongoUserEntityMapper {
    User toModel(MongoUserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    MongoUserEntity toEntity(User user);
}
