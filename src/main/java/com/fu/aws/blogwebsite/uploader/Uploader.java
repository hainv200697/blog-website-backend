package com.fu.aws.blogwebsite.uploader;

import java.io.File;

public interface Uploader {
    String upload(String fileName, File file);
}
