package com.fu.aws.blogwebsite.service;

import com.fu.aws.blogwebsite.entity.Post;
import com.fu.aws.blogwebsite.repository.PostRepository;
import com.fu.aws.blogwebsite.uploader.S3Uploader;
import com.fu.aws.blogwebsite.uploader.Uploader;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    private final String UPLOADED_FOLDER = "temp/";

    @Autowired
    private PostRepository postRepository;

    public String saveUploadedFile(MultipartFile file, String bucket) throws IOException {
        byte[] bytes = file.getBytes();
        String name = getFileName() + FilenameUtils.getExtension(file.getOriginalFilename());

        File folder = new File(UPLOADED_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }

        Path path = Paths.get(UPLOADED_FOLDER + name);
        Files.write(path, bytes);
        String url = upload(name, new File(UPLOADED_FOLDER + name), bucket);
        File fileTmp = new File(path.toString());
        fileTmp.delete();
        return url;
    }

    private String getFileName() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int random = (int) (Math.random() * Integer.MAX_VALUE);
        return "IMAGE_" + timeStamp + "_" + random + "_.";
    }

    private String upload(String fileName, File file, String bucket) {
        Uploader uploader = new S3Uploader("AKIATSKZRGCKKA5LEAJW", "RHsqHxy/qf6/mpi59Wc700VwrhBlpZqTUW1NAYA0", bucket);
        return uploader.upload(fileName, file);
    }

    public Post savePost(Post newPost) {
        return postRepository.save(newPost);
    }

    public List<Post> getApprove(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return postRepository.findPostApprove("APPROVE", pageable);
    }

    public List<Post> getAll(Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return postRepository.getAllAndPaging(pageable);
    }

    public Page<Post> getAllWithParam(Integer size, Integer page, String status, String title, String type, String fullName) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return postRepository.findPost(status, title, type, fullName, pageable);
    }

    public Post changeStatus(Long id, String status) {
        Post post = postRepository.findById(id).get();
        if (post != null) {
            post.setStatus(status);
            return postRepository.save(post);
        }
        return null;
    }

    public Post changeStatusDelete(Long id, String email) {
        Post post = postRepository.findById(id).get();
        if (post.getUser().getEmail().compareTo(email) != 0) {
            return null;
        }
        return changeStatus(id, "DELETE");
    }

    public Post getById(Long id) {
        return postRepository.findById(id).get();
    }

    public List<Post> getAllByMe(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return postRepository.findPostByEmail(email, pageable);
    }
}