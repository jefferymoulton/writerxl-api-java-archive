package com.writerxl.api.data;

import com.writerxl.api.ProfileNotFoundException;
import com.writerxl.api.data.entity.MongoProfileEntity;
import com.writerxl.api.data.mapper.MongoProfileEntityMapper;
import com.writerxl.api.data.mapper.MongoProfileEntityMapperImpl;
import com.writerxl.api.data.repository.MongoProfileRepository;
import com.writerxl.api.model.Profile;
import com.writerxl.api.model.ProfileStatus;
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
@SpringBootTest(classes = MongoProfileEntityMapperImpl.class)
class MongoProfileServiceTest {

    private final String MOCK_PROFILE_KEY = "523157ec-1436-4390-b38d-3b5c5a26acea";
    private final ObjectId MOCK_PROFILE_ID = new ObjectId("54651022bffebc03098b4567");
    private final String MOCK_VALID_FIRST_NAME = "Valid";
    private final String MOCK_VALID_LAST_NAME = "User";
    private final String MOCK_VALID_EMAIL = "valid@email.com";
    private final LocalDateTime MOCK_DATE_TIME = LocalDateTime.now();

    private final String MOCK_INVALID_EMAIL = "invalid@email.com";

    private MongoProfileService userService;

    @Autowired
    private MongoProfileEntityMapper mapper;

    @Mock
    private MongoProfileRepository mockProfileRepository;

    private final Profile validProfileRequest = new Profile(
            MOCK_PROFILE_KEY,
            MOCK_VALID_FIRST_NAME,
            MOCK_VALID_LAST_NAME,
            MOCK_VALID_EMAIL,
            ProfileStatus.ACTIVE
    );

    private final MongoProfileEntity validUserEntity = new MongoProfileEntity(
            MOCK_PROFILE_ID,
            MOCK_PROFILE_KEY,
            MOCK_VALID_FIRST_NAME,
            MOCK_VALID_LAST_NAME,
            MOCK_VALID_EMAIL,
            com.writerxl.api.model.ProfileStatus.ACTIVE,
            MOCK_DATE_TIME
    );

    @BeforeEach
    void setUp() {
        userService = new MongoProfileService(mockProfileRepository, mapper);
    }

    @Test
    void testValidUserCreation() {
        when(mockProfileRepository.save(any(MongoProfileEntity.class)))
                .thenReturn(validUserEntity);

        Profile createdProfile = userService.createProfile(validProfileRequest);
        testValidUser(createdProfile);
    }

    @Test
    void testDuplicateProfileCreation() {
        String duplicateErrorMsg = "The profile already exists.";

        when(mockProfileRepository.save(any(MongoProfileEntity.class)))
                .thenThrow(new DuplicateKeyException(duplicateErrorMsg));

        Exception ex = assertThrows(DuplicateKeyException.class, () -> userService.createProfile(validProfileRequest));

        assertEquals(duplicateErrorMsg, ex.getMessage());
    }

    // TODO: Add tests for profile key

    @Test
    void testGetUserByValidEmail() {
        when(mockProfileRepository.findOneByEmail(MOCK_VALID_EMAIL)).thenReturn(Optional.of(validUserEntity));

        Profile foundProfile = userService.getProfileByEmail(MOCK_VALID_EMAIL);
        testValidUser(foundProfile);
    }

    @Test
    void testGetUserByInvalidEmail() {
        String userAccountNotFound = "Unable to find specified user.";

        when(mockProfileRepository.findOneByEmail(MOCK_INVALID_EMAIL))
                .thenThrow(new ProfileNotFoundException(userAccountNotFound));

        Exception ex = assertThrows(ProfileNotFoundException.class, () -> userService.getProfileByEmail(MOCK_INVALID_EMAIL));

        assertEquals(userAccountNotFound, ex.getMessage());
    }

    void testValidUser(Profile testProfile) {
        assertEquals(MOCK_VALID_FIRST_NAME, testProfile.getFirstName());
        assertEquals(MOCK_VALID_LAST_NAME, testProfile.getLastName());
        assertEquals(MOCK_VALID_EMAIL, testProfile.getEmail());
        assertEquals(com.writerxl.api.model.ProfileStatus.ACTIVE, testProfile.getStatus());
        assertEquals(MOCK_DATE_TIME, testProfile.getMemberSince());
    }
}