package com.deluxeviper.livestreambackend.Controllers;

import com.deluxeviper.livestreambackend.Models.User;
import com.deluxeviper.livestreambackend.Security.JWT.JWTUtils;
import com.deluxeviper.livestreambackend.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<User> fetchAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> fetchUserByEmail(@PathVariable String email, @RequestHeader(name = "Authorization") String token) {
        String jwtEmail = jwtUtils.getUsernameFromJwtToken(token.split("Bearer ")[1]);
        if (!email.equals(jwtEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Error requesting information from wrong user.\"}");
        }
        User user = userService.findUserByEmail(email);
        System.out.println(user);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}