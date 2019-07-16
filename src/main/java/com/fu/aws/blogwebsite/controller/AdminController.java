package com.fu.aws.blogwebsite.controller;

import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.model.AdminRequest;
import com.fu.aws.blogwebsite.service.AdminService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin/me", method = RequestMethod.GET)
    public ResponseEntity<User> getMe(Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            User user = adminService.getAdminByEmail(name);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User newUser) {
        boolean isExist = adminService.isExistAdmin(newUser.getEmail());
        if (isExist) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Message", "Email is exist!");
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }
        User result = adminService.createAdmin(newUser);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Message", "Email " + result.getEmail() + "is created");
        return ResponseEntity.ok().body(jsonObject.toString());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/change-pass")
    @CrossOrigin
    public ResponseEntity<User> changePass(@RequestBody AdminRequest editAdmin) {
        User result = adminService.changePass(editAdmin);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public List<User> getAll() {
        return adminService.getAllAdmin();
    }
}
