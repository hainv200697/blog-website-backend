package com.fu.aws.blogwebsite.component;

import com.fu.aws.blogwebsite.entity.Role;
import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.repository.AdminRepository;
import com.fu.aws.blogwebsite.repository.RoleRepository;
import com.fu.aws.blogwebsite.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapData implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        if (!adminService.isExistAdmin("admin@blogwebsite.com")) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            List listRole = new ArrayList();
            listRole.add(adminRole);
            User user = new User();
            user.setFullName("Hai Nguyen Van");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setEmail("admin@blogwebsite.com");
            user.setRoles(listRole);
            user.setEnabled(true);
            adminRepository.save(user);
        }
        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
        return role;
    }
}
