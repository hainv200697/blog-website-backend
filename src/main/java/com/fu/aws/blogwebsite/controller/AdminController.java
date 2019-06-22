package com.fu.aws.blogwebsite.controller;

import com.fu.aws.blogwebsite.entity.Admin;
import com.fu.aws.blogwebsite.model.AdminRequest;
import com.fu.aws.blogwebsite.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AdminController {

    @Autowired
    private AdminService adminService;

//    @Autowired
//    private AdminRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder encoder;

    //Get current user
    @RequestMapping(value = "/admin/me", method = RequestMethod.GET)
    public ResponseEntity<Admin> getMe(Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            Admin admin = adminService.getUserByEmail(name);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(admin);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/admin/sign-up")
    public ResponseEntity<Admin> signUp(@RequestBody Admin newAdmin) {
        Admin result = adminService.createUser(newAdmin);
        result.setPassword(null);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/admin/change-pass")
    @CrossOrigin
    public ResponseEntity<Admin> changePass(@RequestBody AdminRequest editAdmin) {
        Admin result = adminService.changePass(editAdmin);
        result.setPassword(null);
        return ResponseEntity.ok().body(result);
    }
}
