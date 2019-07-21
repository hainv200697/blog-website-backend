package com.fu.aws.blogwebsite.controller;

import com.fu.aws.blogwebsite.entity.ExternalUser;
import com.fu.aws.blogwebsite.entity.Post;
import com.fu.aws.blogwebsite.model.FormWrapper;
import com.fu.aws.blogwebsite.service.ExternalService;
import com.fu.aws.blogwebsite.service.PostService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            JSONObject jObj = new JSONObject();
            if (user == null) {
                jObj.put("message", "Email is not exist");
                jObj.put("status", 400);
                return new ResponseEntity<>(jObj.toString(), HttpStatus.BAD_REQUEST);
            }
            Post newPost = new Post();
            if (model.getType().compareTo("SLOGAN") != 0) {
                MultipartFile file = model.getImage();
                if (file == null || file.isEmpty()) {
                    jObj.put("message", "File is empty!");
                    jObj.put("status", 400);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jObj.toString());
                }

                //Check file is video
                if (!file.getContentType().contains("image/")) {
                    jObj.put("message", "File is not image!");
                    jObj.put("status", 400);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jObj.toString());
                }
                String link = postService.saveUploadedFile(file);
                newPost.setImage(link);
            }
            newPost.setType(model.getType());
            newPost.setTitle(model.getTitle());
            newPost.setContent(model.getContent());
            newPost.setSource(model.getSource());
            newPost.setStatus("PENDING");
            newPost.setUser(user);
            postService.savePost(newPost);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Post is success");
        return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
    }

    @GetMapping("/top3")
    public List<Post> getTop3() {
        return postService.getApprove(0, 3);
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @GetMapping("/admin/all")
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

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Post> getAllByStatus(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "fullName", required = false) String fullName) {
        if (size == null) {
            size = 10;
        }
        if (page == null) {
            page = 0;
        }
        if (status == null) {
            status = "";
        }
        if (title == null) {
            title = "";
        }
        if (type == null) {
            type = "";
        }
        if (fullName == null) {
            fullName = "";
        }
        Page<Post> result = postService.getAllWithParam(size, page, status, title, type, fullName);
        return result;
    }

    @GetMapping("/approve")
    public List<Post> getAllApprove(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        if (page == null) {
            page = 0;
        }
        return postService.getApprove(page, size);
    }


    @GetMapping("/admin/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeStatus(@RequestParam String status, @PathVariable Long id) {
        Post post = postService.changeStatus(id, status);
        JSONObject jsonObject = new JSONObject();
        if (post == null) {
            jsonObject.put("message", "Not found blogId " + id);
            return new ResponseEntity(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        jsonObject.put("message", "Change status success");
        return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @RequestParam String email) {
        Post post = postService.changeStatusDelete(id, email);
        JSONObject jsonObject = new JSONObject();
        if (post == null) {
            jsonObject.put("message", "Can't delete post");
            return new ResponseEntity(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        jsonObject.put("message", "Delete success");
        return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public List<Post> getAllByMe(@RequestParam String email,
                                 @RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        if (page == null) {
            page = 0;
        }
        return postService.getAllByMe(email, page, size);
    }
}
