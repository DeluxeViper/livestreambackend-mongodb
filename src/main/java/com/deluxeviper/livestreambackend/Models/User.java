package com.deluxeviper.livestreambackend.Models;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Document
@ToString
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    @Indexed(unique = true)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;
    private LocationInfo locationInfo;
    private String streamUrl;
    private Boolean isStreaming;
    private Boolean isLoggedIn;

//    private Set<Role> roles = new HashSet<>();

    public User(String email, LocationInfo locationInfo, String password, String streamUrl, Boolean isStreaming, Boolean isLoggedIn) {
        this.email = email;
        this.locationInfo = locationInfo;
        this.password = password;
        this.streamUrl = streamUrl;
        this.isStreaming = isStreaming;
        this.isLoggedIn = isLoggedIn;
    }
}
