package com.fu.aws.blogwebsite.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
public class FormUploadImage {
    private MultipartFile image;
}