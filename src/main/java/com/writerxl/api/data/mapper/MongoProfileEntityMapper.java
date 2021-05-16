package com.writerxl.api.data.mapper;

import com.writerxl.api.data.entity.MongoProfileEntity;
import com.writerxl.api.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface MongoProfileEntityMapper {
    Profile toModel(MongoProfileEntity userEntity);

    @Mapping(target = "id", ignore = true)
    MongoProfileEntity toEntity(Profile profile);
}
