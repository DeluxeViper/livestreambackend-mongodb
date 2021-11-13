package com.deluxeviper.livestreambackend.Services;

import com.deluxeviper.livestreambackend.Models.User;
import com.deluxeviper.livestreambackend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        System.out.println(user);
        return user.orElse(null);
    }
    public void addUser(User user) {
        userRepository.insert(user);
    }

    public void setUserLoggedIn(String email, Boolean isLoggedIn) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            System.out.println(user.get());
            user.get().setIsLoggedIn(isLoggedIn);
            userRepository.save(user.get());
        }
    }
}
