package com.fu.aws.blogwebsite.component;

import com.fu.aws.blogwebsite.entity.Admin;
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
            Role roleAdmin = new Role("ROLE_ADMIN");
            roleService.createRole(roleAdmin);
        }
        if (!roleService.isExistRole("ROLE_USER")) {
            Role roleAdmin = new Role("ROLE_USER");
            roleService.createRole(roleAdmin);
        }
        // Default Admin Admin
        if (!adminService.isExistAdmin("admin@blogwebsite.com")) {
            Admin admin = new Admin("admin@blogwebsite.com", "123456");
            adminService.createAdmin(admin);
        }
    }
}
