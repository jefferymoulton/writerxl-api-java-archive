package com.writerxl.api.data;

import com.writerxl.api.UserNotFoundException;
import com.writerxl.api.data.mapper.MongoUserEntityMapper;
import com.writerxl.api.data.repository.MongoUserRepository;
import com.writerxl.api.model.User;
import com.writerxl.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MongoUserService implements UserService {

    private final MongoUserRepository userRepository;

    private final MongoUserEntityMapper mapper;

    @Override
    public User createUser(User user) {
        return mapper.toModel(userRepository.save(mapper.toEntity(user)));
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return mapper.toModel(userRepository.findOneByEmail(email).orElseThrow(
                () -> new UserNotFoundException("Unable to find specified user.")
        ));
    }

}
