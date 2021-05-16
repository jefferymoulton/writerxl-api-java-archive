package com.writerxl.api.data;

import com.writerxl.api.UserNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private final String MOCK_INVALID_EMAIL = "invalid@email.com";

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
        testValidUser(createdUser);
    }

    @Test
    void testDuplicateUserCreation() {
        String duplicateErrorMsg = "The user account already exists.";

        when(mockUserRepository.save(any(MongoUserEntity.class)))
                .thenThrow(new DuplicateKeyException(duplicateErrorMsg));

        Exception ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(validUserRequest));

        assertEquals(duplicateErrorMsg, ex.getMessage());
    }

    @Test
    void testGetUserByValidEmail() {
        when(mockUserRepository.findOneByEmail(MOCK_VALID_EMAIL)).thenReturn(Optional.of(validUserEntity));

        User foundUser = userService.getUserByEmail(MOCK_VALID_EMAIL);
        testValidUser(foundUser);
    }

    @Test
    void testGetUserByInvalidEmail() {
        String userAccountNotFound = "Unable to find specified user.";

        when(mockUserRepository.findOneByEmail(MOCK_INVALID_EMAIL))
                .thenThrow(new UserNotFoundException(userAccountNotFound));

        Exception ex = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(MOCK_INVALID_EMAIL));

        assertEquals(userAccountNotFound, ex.getMessage());
    }

    void testValidUser(User userToTest) {
        assertEquals(MOCK_VALID_FIRST_NAME, userToTest.getFirstName());
        assertEquals(MOCK_VALID_LAST_NAME, userToTest.getLastName());
        assertEquals(MOCK_VALID_EMAIL, userToTest.getEmail());
        assertEquals(UserStatus.ACTIVE, userToTest.getStatus());
        assertEquals(MOCK_DATE_TIME, userToTest.getMemberSince());
    }
}