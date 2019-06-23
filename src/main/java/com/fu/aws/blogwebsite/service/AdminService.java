package com.fu.aws.blogwebsite.service;

import com.fu.aws.blogwebsite.entity.Admin;
import com.fu.aws.blogwebsite.model.AdminRequest;
import com.fu.aws.blogwebsite.repository.AdminRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder encoder;

    public AdminService(AdminRepository adminRepository, BCryptPasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    public Page<Admin> findAllUser(String search, int page, int size) {
        Pageable sortedByCreatedDateDesc =
                PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Admin> result = adminRepository.findAll(where(AdminRepository.filterByName(search)), sortedByCreatedDateDesc);
        return result;
    }


    public Admin createUser(Admin admin) {
        Optional<Admin> duplicateUser = adminRepository.findByEmail(admin.getEmail());
        if (duplicateUser.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "This account exist!");
        } else {
            admin.setRole("ROLE_ADMIN");
            admin.setActive(true);
            admin.setPassword(encoder.encode(admin.getPassword()));
            return adminRepository.save(admin);
        }
    }

    public Admin changePass(AdminRequest editAdmin) {
        Optional<Admin> find = adminRepository.findByEmail(editAdmin.getEmail());
        if (!find.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cant found account!");
        } else {
            if (!encoder.matches(editAdmin.getPassword(), find.get().getPassword())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Password is wrong!");
            }
                Admin tmp = find.get();
                tmp.setPassword(encoder.encode(editAdmin.getNewPassword()));
                return adminRepository.save(tmp);
        }
    }

    public Admin getUserByEmail(String email) {
        Optional<Admin> existUser = adminRepository.findByEmail(email);
        if (existUser.isPresent()) {
            return adminRepository.findByEmail(email).get();
        }
        return null;
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }
}
