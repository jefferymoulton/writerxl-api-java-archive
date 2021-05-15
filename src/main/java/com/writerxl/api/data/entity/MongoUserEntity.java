package com.writerxl.api.data.entity;

import com.writerxl.api.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class MongoUserEntity {

    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private UserStatus status;

    @DateTimeFormat(style = "M-")
    @CreatedDate
    private LocalDateTime memberSince;

    public MongoUserEntity(ObjectId id,
                           String firstName,
                           String lastName,
                           String email,
                           UserStatus status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
    }
}
