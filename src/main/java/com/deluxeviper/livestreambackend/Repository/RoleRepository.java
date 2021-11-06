package com.deluxeviper.livestreambackend.Repository;

import com.deluxeviper.livestreambackend.Models.ERole;
import com.deluxeviper.livestreambackend.Models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
