package com.writerxl.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private UserStatus status;

    private LocalDateTime memberSince;

    public User(String firstName,
                String lastname,
                String email,
                UserStatus status) {
        this.firstName = firstName;
        this.lastName = lastname;
        this.email = email;
        this.status = status;
    }
}
