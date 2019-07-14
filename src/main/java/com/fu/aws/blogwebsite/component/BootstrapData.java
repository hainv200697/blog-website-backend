package com.fu.aws.blogwebsite.component;

import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.service.AdminService;
import com.fu.aws.blogwebsite.entity.Role;
import com.fu.aws.blogwebsite.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) {
        // Default Role

        if (!roleService.isExistRole("ROLE_ADMIN")) {
            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleService.createRole(roleAdmin);
        }
        if (!roleService.isExistRole("ROLE_USER")) {
            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");
            roleService.createRole(roleUser);
        }
        // Default User User
        if (!adminService.isExistAdmin("admin@blogwebsite.com")) {
            User user = new User();
            user.setEmail("admin@blogwebsite.com");
            user.setPassword("123456");
            adminService.createAdmin(user);
        }
    }
}
