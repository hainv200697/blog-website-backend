package com.fu.aws.blogwebsite.component;

import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) {
        User user = userService.getUserByEmail("admin@blogwebsite.com");
        if (user == null) {
            user = new User();
            user.setEmail("admin@blogwebsite.com");
            user.setPassword("123");
            userService.createUser(user);
        }
    }
}
