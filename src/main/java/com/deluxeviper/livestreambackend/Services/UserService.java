package com.deluxeviper.livestreambackend.Services;

import com.deluxeviper.livestreambackend.Models.User;
import com.deluxeviper.livestreambackend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean userExistsByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    public void addUser(User user) {
        userRepository.insert(user);
    }
}
