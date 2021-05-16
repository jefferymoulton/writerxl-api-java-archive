package com.writerxl.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private String key;
    private String firstName;
    private String lastName;
    private String email;
    private ProfileStatus status;

    private LocalDateTime memberSince;

    public Profile(String key,
                   String firstName,
                   String lastname,
                   String email,
                   ProfileStatus status) {
        this.key = key;
        this.firstName = firstName;
        this.lastName = lastname;
        this.email = email;
        this.status = status;
    }
}
