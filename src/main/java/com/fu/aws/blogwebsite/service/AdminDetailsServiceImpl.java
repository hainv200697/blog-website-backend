package com.fu.aws.blogwebsite.service;

import com.fu.aws.blogwebsite.entity.Admin;
import com.fu.aws.blogwebsite.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository adminRepository;

    public AdminDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> optionalUser = adminRepository.findByEmail(email);
        Admin admin = null;
        if (optionalUser.isPresent()) {
            admin = optionalUser.get();
        }

        if (admin == null) {
            throw new UsernameNotFoundException(email);
        }
        return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(), emptyList());
    }
}
