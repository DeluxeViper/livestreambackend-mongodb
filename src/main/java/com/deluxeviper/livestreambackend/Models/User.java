package com.deluxeviper.livestreambackend.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private LocationInfo locationInfo;
    private String streamUrl;

    public User(String firstName, String lastName, String email, LocationInfo locationInfo, String streamUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.locationInfo = locationInfo;
        this.streamUrl = streamUrl;
    }
}
