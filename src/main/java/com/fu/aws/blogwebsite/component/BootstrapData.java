package com.fu.aws.blogwebsite.component;

import com.fu.aws.blogwebsite.entity.Admin;
import com.fu.aws.blogwebsite.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    @Override
    public void run(String... args) {
        Admin admin = adminService.getUserByEmail("AdminRequest@blogwebsite.com");
        if (admin == null) {
            admin = new Admin();
            admin.setEmail("AdminRequest@blogwebsite.com");
            admin.setPassword("123456");
            adminService.createUser(admin);
        }
    }
}
