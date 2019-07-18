package com.fu.aws.blogwebsite.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
public class FormWrapper {
    private String type;
    private String title;
    private String content;
    private String source;
    private String email;
    private MultipartFile image;
}
