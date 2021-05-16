package com.writerxl.api.service;

import com.writerxl.api.model.Profile;

public interface ProfileService {

    Profile createProfile(Profile profile);

    Profile getProfileByKey(String key);

    Profile getProfileByEmail(String email);

}
