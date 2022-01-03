package com.deluxeviper.livestreambackend.Controllers;

import com.deluxeviper.livestreambackend.Models.LocationInfo;
import com.deluxeviper.livestreambackend.Models.User;
import com.deluxeviper.livestreambackend.Security.JWT.JWTUtils;
import com.deluxeviper.livestreambackend.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private final UserService userService;

    @GetMapping
    public List<User> fetchAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/loggedin")
    public List<User> fetchAllLoggedInUsers() {
        return userService.getAllLoggedInUsers();
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> fetchUserByEmail(@PathVariable String email) {

        // Verify that the JWT token provided is from a specific user (described by the email)

        // Might not need this logic due to users being fetched by different accounts
//        String jwtEmail = jwtUtils.getUsernameFromJwtToken(token.split("Bearer ")[1]);
//        if (!email.equals(jwtEmail)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(new ErrorResponse("Error. Requesting information from wrong user."));
//        }

        User user = userService.findUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/subscribe", produces = "application/stream+json")
    public Flux<Object> subscribeToUsers() {

        return this.userService.subscribeToUsersCollection();
    }

    @PutMapping(path = "/user", produces = "application/json")
    public ResponseEntity<?> setUser(@NotNull @RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error. User not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(user);
    }

    @PutMapping(path = "{userId}/updateUserLocation", produces = "application/json")
    public ResponseEntity<?> updateUserLocation(@NotNull @PathVariable(name = "userId") String userId, @NotNull @RequestBody LocationInfo locationInfo) {
        User user = userService.updateLocation(userId, locationInfo);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error. User not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(user);
    }

    @PutMapping(path = "{userId}/setStreaming", produces = "application/json")
    public ResponseEntity<?> setIsStreaming(@NotNull @PathVariable(name = "userId") String userId, @NotNull @RequestParam(name="isStreaming") Boolean isStreaming) {
        User user = userService.isStreaming(userId, isStreaming);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error. User not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(user);
    }
}