package com.fu.aws.blogwebsite.service;

import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.entity.Role;
import com.fu.aws.blogwebsite.model.AdminRequest;
import com.fu.aws.blogwebsite.repository.AdminRepository;
import com.fu.aws.blogwebsite.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    public AdminService(AdminRepository adminRepository, BCryptPasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    public Page<User> findAllAdmin(String search, int page, int size) {
        Pageable sortedByCreatedDateDesc =
                PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<User> result = adminRepository.findAll(where(AdminRepository.filterByName(search)), sortedByCreatedDateDesc);
        return result;
    }


    public User createAdmin(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        user.setEnabled(true);
        user.setPassword(encoder.encode(user.getPassword()));
        return adminRepository.save(user);
    }

    public boolean isExistAdmin(String email) {
        User checkAdmin = adminRepository.findByEmail(email);
        if (checkAdmin != null) {
            return true;
        }
        return false;
    }

    public User getAdminByEmail(String email) {
        User existAdmin = adminRepository.findByEmail(email);
        if (existAdmin != null) {
            return adminRepository.findByEmail(email);
        } else {
            System.out.println("Not Found");
        }
        return null;
    }

    public User changePass(AdminRequest editAdmin) {
        User find = adminRepository.findByEmail(editAdmin.getEmail());
        if (find != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cant found account!");
        } else {
            if (!encoder.matches(editAdmin.getPassword(), find.getPassword())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Password is wrong!");
            }
            User tmp = find;
            tmp.setPassword(encoder.encode(editAdmin.getNewPassword()));
            return adminRepository.save(tmp);
        }
    }

    public List<User> getAllAdmin() {
        return adminRepository.findAll();
    }
}
