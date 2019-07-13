package com.fu.aws.blogwebsite.service;

import com.fu.aws.blogwebsite.entity.Role;
import com.fu.aws.blogwebsite.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
       if (isExistRole(role.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This Role exist!");
        } else {
            return roleRepository.save(role);
        }
    }

    public boolean isExistRole(String name) {
        Optional<Role> checkRole = roleRepository.findByName(name);
        if (checkRole.isPresent()) {
            return true;
        }
        return false;
    }
}
