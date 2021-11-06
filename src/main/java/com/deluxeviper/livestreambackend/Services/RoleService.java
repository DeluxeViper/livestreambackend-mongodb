package com.deluxeviper.livestreambackend.Services;

import com.deluxeviper.livestreambackend.Models.ERole;
import com.deluxeviper.livestreambackend.Models.Role;
import com.deluxeviper.livestreambackend.Repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Role findByName(ERole eRole) {
        return roleRepository.findByName(eRole).orElseThrow(()->
                new RuntimeException("Error: Role is not found."));
    }
}
