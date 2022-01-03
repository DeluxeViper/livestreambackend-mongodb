package com.deluxeviper.livestreambackend.Controllers;

import com.deluxeviper.livestreambackend.Models.*;
import com.deluxeviper.livestreambackend.Payload.Request.LoginRequest;
import com.deluxeviper.livestreambackend.Payload.Request.SignupRequest;
import com.deluxeviper.livestreambackend.Payload.Response.JwtResponse;
import com.deluxeviper.livestreambackend.Payload.Response.MessageResponse;
import com.deluxeviper.livestreambackend.Security.JWT.JWTUtils;
import com.deluxeviper.livestreambackend.Services.RoleService;
import com.deluxeviper.livestreambackend.Services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException authenticationException) {
            System.out.println("authenticateUser: " + authenticationException);

            return ResponseEntity.status(401).body("Invalid Credentials. Please try again.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Set user logged in
        userService.setUserLoggedIn(loginRequest.getEmail(), true);

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.userExistsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error. Email is already registered.");
        }

        User user = new User(signupRequest.getEmail(),
                new LocationInfo(-200.0, -200.0),
                encoder.encode(signupRequest.getPassword()),
                "",
                false,
                false);

//        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

//        if (strRoles == null) {
//            Role userRole = roleService.findByName(ERole.ROLE_USER);
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleService.findByName(ERole.ROLE_ADMIN);
//                        roles.add(adminRole);
//                        break;
//                    default:
//                        Role userRole = roleService.findByName(ERole.ROLE_USER);
//                        roles.add(userRole);
//                }
//            });
//        }

//        user.setRoles(roles);
        userService.addUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(@Valid @RequestParam(name = "email") String email) {
        if (!userService.userExistsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        userService.setUserLoggedIn(email, false);

        return ResponseEntity.ok(new MessageResponse("User signed out sucessfully."));
    }

    @GetMapping("/stream")
    public ResponseEntity<?> authenticateStream(
            @RequestParam(name="name", required = false) String name) {
        if (name == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean userExists = userService.userExistsByEmail(name);

        if (userExists) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(401).build();
    }
}
