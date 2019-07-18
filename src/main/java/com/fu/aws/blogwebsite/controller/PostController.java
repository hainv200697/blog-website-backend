package com.fu.aws.blogwebsite.controller;

import com.fu.aws.blogwebsite.entity.ExternalUser;
import com.fu.aws.blogwebsite.entity.Post;
import com.fu.aws.blogwebsite.model.FormWrapper;
import com.fu.aws.blogwebsite.service.ExternalService;
import com.fu.aws.blogwebsite.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ExternalService externalService;

    @PostMapping("/create")
    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute FormWrapper model) {
        try {
            ExternalUser user = externalService.findByEmail(model.getEmail());
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            String link = postService.saveUploadedFile(model.getImage());
            Post newPost = new Post();
            newPost.setType(model.getType());
            newPost.setTitle(model.getTitle());
            newPost.setContent(model.getContent());
            newPost.setSource(model.getSource());
            newPost.setImage(link);
            newPost.setStatus("PENDING");
            newPost.setUser(user);
            postService.savePost(newPost);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
    }

    @GetMapping("/top3")
    public List<Post> getTop3() {
        return postService.getTop3();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Post> getAll(@RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        if (page == null) {
            page = 0;
        }
        return postService.getAll(size, page);
    }
}
