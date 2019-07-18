package com.fu.aws.blogwebsite.uploader;

import java.io.File;

public interface Uploader {
    String upload(String path, File file);
}
