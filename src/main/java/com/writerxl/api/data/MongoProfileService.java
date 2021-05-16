package com.writerxl.api.data;

import com.writerxl.api.ProfileNotFoundException;
import com.writerxl.api.data.mapper.MongoProfileEntityMapper;
import com.writerxl.api.data.repository.MongoProfileRepository;
import com.writerxl.api.model.Profile;
import com.writerxl.api.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MongoProfileService implements ProfileService {

    private final MongoProfileRepository userRepository;

    private final MongoProfileEntityMapper mapper;

    @Override
    public Profile createProfile(Profile profile) {
        // TODO: Deal with empty keys
        return mapper.toModel(userRepository.save(mapper.toEntity(profile)));
    }

    @Override
    public Profile getProfileByKey(String key) {
        return mapper.toModel(userRepository.findOneByKey(key).orElseThrow(
                () -> new ProfileNotFoundException("Unable to find specified user.")
        ));
    }

    @Override
    public Profile getProfileByEmail(String email) throws ProfileNotFoundException {
        return mapper.toModel(userRepository.findOneByEmail(email).orElseThrow(
                () -> new ProfileNotFoundException("Unable to find specified user.")
        ));
    }

}
