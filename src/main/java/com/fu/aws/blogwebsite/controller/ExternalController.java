package com.fu.aws.blogwebsite.controller;

import com.fu.aws.blogwebsite.entity.ExternalUser;
import com.fu.aws.blogwebsite.service.ExternalService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/external")
public class ExternalController {

    @Autowired
    private ExternalService externalService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    List<ExternalUser> get(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "size", required = false) Integer size,
                           @RequestParam(value = "search", required = false) String search) {
        if (search == null) {
            search = "";
        }
        if (size == null) {
            size = 10;
        }
        if (page == null) {
            page = 0;
        }
        return externalService.searchLikeEmail(search, page, size);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    ResponseEntity register(@RequestBody ExternalUser externalUser) {
        JSONObject jsonObject = new JSONObject();
        ExternalUser save = externalService.register(externalUser);
        if (save == null) {
            jsonObject.put("message", "Email is banned");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        jsonObject = new JSONObject(save);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/ban", method = RequestMethod.PUT)
    ResponseEntity banAccount(@RequestParam String email) {
        JSONObject jsonObject = new JSONObject();
        ExternalUser save = externalService.editActive(email, false);
        if (save == null) {
            jsonObject.put("message", "Email is not found");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        jsonObject = new JSONObject(save);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/removeban", method = RequestMethod.PUT)
    ResponseEntity removeBanAccount(@RequestParam String email) {
        ExternalUser save = externalService.editActive(email, true);
        JSONObject jsonObject = new JSONObject();
        if (save == null) {
            jsonObject.put("message", "Email is not found");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        jsonObject = new JSONObject(save);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}
