package com.fu.aws.blogwebsite.component;

import com.fu.aws.blogwebsite.entity.Role;
import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.service.RoleService;
import com.fu.aws.blogwebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private UserService userService;
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
        // Default User Admin
        if (!userService.isExistUser("admin@blogwebsite.com")) {
            User user = new User("admin@blogwebsite.com", "123456");
            userService.createUser(user);
        }
    }
}
