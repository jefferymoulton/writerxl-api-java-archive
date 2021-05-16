package com.writerxl.api.data;

import com.writerxl.api.data.entity.MongoUserEntity;
import com.writerxl.api.data.mapper.MongoUserEntityMapper;
import com.writerxl.api.data.mapper.MongoUserEntityMapperImpl;
import com.writerxl.api.data.repository.MongoUserRepository;
import com.writerxl.api.model.User;
import com.writerxl.api.model.UserStatus;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MongoUserEntityMapperImpl.class)
class MongoUserServiceTest {

    private final ObjectId MOCK_USER_ID = new ObjectId("54651022bffebc03098b4567");
    private final String MOCK_VALID_FIRST_NAME = "Valid";
    private final String MOCK_VALID_LAST_NAME = "User";
    private final String MOCK_VALID_EMAIL = "valid@email.com";
    private final LocalDateTime MOCK_DATE_TIME = LocalDateTime.now();

    private MongoUserService userService;

    @Autowired
    private MongoUserEntityMapper mapper;

    @Mock
    private MongoUserRepository mockUserRepository;

    private final User validUserRequest = new User(
            MOCK_VALID_FIRST_NAME,
            MOCK_VALID_LAST_NAME,
            MOCK_VALID_EMAIL,
            UserStatus.ACTIVE
    );

    private final MongoUserEntity validUserEntity = new MongoUserEntity(
            MOCK_USER_ID,
            MOCK_VALID_FIRST_NAME,
            MOCK_VALID_LAST_NAME,
            MOCK_VALID_EMAIL,
            UserStatus.ACTIVE,
            MOCK_DATE_TIME
    );

    @BeforeEach
    void setUp() {
        userService = new MongoUserService(mockUserRepository, mapper);
    }

    @Test
    void testValidUserCreation() {
        when(mockUserRepository.save(any(MongoUserEntity.class)))
                .thenReturn(validUserEntity);

        User createdUser = userService.createUser(validUserRequest);

        assertEquals(MOCK_VALID_FIRST_NAME, createdUser.getFirstName());
        assertEquals(MOCK_VALID_LAST_NAME, createdUser.getLastName());
        assertEquals(MOCK_VALID_EMAIL, createdUser.getEmail());
        assertEquals(UserStatus.ACTIVE, createdUser.getStatus());
        assertEquals(MOCK_DATE_TIME, createdUser.getMemberSince());
    }

    @Test
    void testDuplicateUserCreation() {
        String duplicateErrorMsg = "The user account already exists.";

        when(mockUserRepository.save(any(MongoUserEntity.class)))
                .thenThrow(new DuplicateKeyException(duplicateErrorMsg));

        Exception ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(validUserRequest));

        assertEquals(duplicateErrorMsg, ex.getMessage());
    }
}