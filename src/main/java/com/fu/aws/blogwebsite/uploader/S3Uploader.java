package com.fu.aws.blogwebsite.uploader;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class S3Uploader implements Uploader {
    private AmazonS3 s3;
    private String bucket;

    public S3Uploader(String accessKey, String secretKey, String bucket) {
        this.bucket = bucket;
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        this.s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    @Override
    public String upload(String path, File file) {
        path = path + "/" + file.getName();
        s3.putObject(new PutObjectRequest(bucket, path, file).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3.getUrl(bucket, path).toString();
    }
}
