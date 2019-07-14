package com.fu.aws.blogwebsite.service;

import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@Transactional
public class AdminDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository adminRepository;

    public AdminDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = adminRepository.findByEmail(email);
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), emptyList());
    }
}